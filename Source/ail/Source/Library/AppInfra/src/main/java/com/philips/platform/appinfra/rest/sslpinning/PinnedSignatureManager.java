package com.philips.platform.appinfra.rest.sslpinning;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinnedSignatureManager implements PublicKeyPinInterface {

    private static final String LOG_MESSAGE_BASE = "Public-key pins Mismatch";
    private static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE = "Mismatch of stored pinned Public-key with certificate signature";
    private static final String LOG_MESSAGE_PUBLIC_KEY_PIN_EXPIRED = "Stored pinned Public-key matching header pinned Public-key is expired";
    private static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER = "Mismatch of stored pinned Public-key with response header pinned Public-key";
    private static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK = "Could not find Public-Key-Pins in network response";
    private static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE = "Could not find Public-Key-Pins in storage";
    private static final String LOG_MESSAGE_STORAGE_ERROR = "Could not update Public-Key-Pins in Secure Storage";

    private static final String PUBLIC_KEY_REGEX = "pin-sha256=\"(.+?)\";";
    private static final String EXPIRY_DATE_REGEX = "expiry-date=\"(.+?)\";";
    private static final String MAX_AGE_REGEX = "max-age=([0-9]+)";
    private static final String EXPIRY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String STORAGE_KEY_PREFIX = "SSL_HPKP_";
    private static final int PIN_MISMATCH_LOG_MAX_COUNT = 3;
    private static final int BUFFER_TIME_IN_SECONDS = 86400;

    private HashMap<String, String> publicKeyPinCache;
    private HashMap<String, Integer> pinMismatchLogCount;
    private SecureStorageInterface secureStorageInterface;
    private LoggingInterface loggingInterface;

    public PinnedSignatureManager(AppInfraInterface appInfraInterface) {
        this.secureStorageInterface = appInfraInterface.getSecureStorage();
        this.loggingInterface = appInfraInterface.getLogging();
        publicKeyPinCache = new HashMap<>();
        pinMismatchLogCount = new HashMap<>();
    }

    @Override
    public void updatePinnedPublicKey(String hostName, String networkPublicKeyDetails) {
        String storedPublicKeyDetails = getStoredPublicKeyDetails(hostName);
        boolean isKeyFound = networkPublicKeyDetails.contains("pin-sha256");

        if (!hostName.isEmpty()) {
            if (!isKeyFound && !storedPublicKeyDetails.isEmpty()) {
                logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK);
            } else if (isKeyFound) {
                int maxAge = getMaxAgeValue(networkPublicKeyDetails);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, maxAge);
                String currentPinsExpiryDate = getDateStringFromDate(calendar.getTime());

                if (storedPublicKeyDetails.isEmpty()) {
                    updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails.concat(" " + "expiry-date=\"" + currentPinsExpiryDate + "\";"));
                    logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE);
                } else {
                    List<String> publicKeys = getPinnedPublicKeysList(networkPublicKeyDetails);
                    for (String publicKey : publicKeys) {
                        if (storedPublicKeyDetails.contains(publicKey)) {
                            String storedPinsExpiryDate = getExpiryDateString(storedPublicKeyDetails);
                            checkPinnedPublicKeyValidity(hostName, storedPublicKeyDetails, currentPinsExpiryDate, storedPinsExpiryDate);
                            return;
                        }
                    }
                    updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
                    logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER);
                }
            }
        }
    }

    @Override
    public boolean isPinnedCertificateMatching(String hostName, List<X509Certificate> chain) {
        String storedPublicKeyDetails = getStoredPublicKeyDetails(hostName);
        for (X509Certificate certificate : chain) {
            String certificatePin = getSHA256Value(certificate);
            if (certificatePin != null && storedPublicKeyDetails.contains(certificatePin))
                return true;
        }
        logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE);
        return false;
    }

    private String getSHA256Value(X509Certificate certificate) {
        // Generate the certificate's spki pin
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        digest.reset();

        byte[] spki = certificate.getPublicKey().getEncoded();
        byte[] spkiHash = digest.digest(spki);
        return Base64.encodeToString(spkiHash, Base64.DEFAULT).trim();
    }

    private String getStoredPublicKeyDetails(String hostName) {
        String storedPublicKeyDetails;
        if (publicKeyPinCache.containsKey(hostName)) {
            storedPublicKeyDetails = publicKeyPinCache.get(hostName);
        } else {
            storedPublicKeyDetails = secureStorageInterface.fetchValueForKey(STORAGE_KEY_PREFIX + hostName, getSecureStorageError());
            if (storedPublicKeyDetails != null) {
                publicKeyPinCache.put(hostName, storedPublicKeyDetails);
            }
        }
        return storedPublicKeyDetails == null ? "" : storedPublicKeyDetails;
    }

    private void updateStoredPublicKeyDetails(String hostName, String publicKeyDetails) {
        boolean success = secureStorageInterface.storeValueForKey(STORAGE_KEY_PREFIX + hostName, publicKeyDetails, getSecureStorageError());
        if (!success) {
            log(hostName, LOG_MESSAGE_STORAGE_ERROR, LoggingInterface.LogLevel.DEBUG);
        }
        publicKeyPinCache.put(hostName, publicKeyDetails);
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    private void logError(String hostname, String message) {
        if (pinMismatchLogCount.containsKey(hostname)) {
            int count = pinMismatchLogCount.get(hostname);
            if (count <= PIN_MISMATCH_LOG_MAX_COUNT) {
                log(hostname, message, LoggingInterface.LogLevel.ERROR);
                pinMismatchLogCount.put(hostname, count + 1);
            }
        } else {
            log(hostname, message, LoggingInterface.LogLevel.ERROR);
            pinMismatchLogCount.put(hostname, 1);
        }
    }

    private void log(String hostname, String message, LoggingInterface.LogLevel logLevel) {
        loggingInterface.log(logLevel, PinnedSignatureManager.class.getSimpleName(), LOG_MESSAGE_BASE + ":" + hostname + ":" + message);
    }

    private void checkPinnedPublicKeyValidity(String hostName, String storedPublicKeyDetails, String currentPinsExpiryDate, String storedPinsExpiryDate) {
        Date currentPinsExpiry = getDateFromDateString(currentPinsExpiryDate);
        Date storedPinsExpiry = getDateFromDateString(storedPinsExpiryDate);
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        if(storedPinsExpiry != null && currentTime.compareTo(storedPinsExpiry) > 0){
            logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_EXPIRED);
        }else {
            calendar.setTime(storedPinsExpiry);
            calendar.add(Calendar.SECOND, BUFFER_TIME_IN_SECONDS);
            if(currentPinsExpiry!= null && currentPinsExpiry.compareTo(calendar.getTime()) > 0){
                updateExpiryInStoredPublicKeyDetails(hostName, storedPublicKeyDetails, currentPinsExpiryDate);
            }
        }
    }

    private void updateExpiryInStoredPublicKeyDetails(String hostName, String storedPublicKeyDetails, String currentPinsExpiryDate) {
        String updatedStoredKeyDetails = storedPublicKeyDetails.split(EXPIRY_DATE_REGEX)[0];
        updateStoredPublicKeyDetails(hostName, updatedStoredKeyDetails.concat(" " + "expiry-date=\"" + currentPinsExpiryDate + "\";"));
    }

    private Date getDateFromDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT, Locale.ENGLISH);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private String getDateStringFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXPIRY_DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    private List<String> getPinnedPublicKeysList(String publicKeyDetails) {
        Pattern pattern = Pattern.compile(PUBLIC_KEY_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        List<String> pinnedKeysList = new ArrayList<>();
        while (matcher.find()) {
            pinnedKeysList.add(matcher.group(1));
        }
        return pinnedKeysList;
    }

    private int getMaxAgeValue(String publicKeyDetails) {
        Pattern pattern = Pattern.compile(MAX_AGE_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    private String getExpiryDateString(String publicKeyDetails) {
        Pattern pattern = Pattern.compile(EXPIRY_DATE_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        return matcher.find() ? matcher.group(1) : "";
    }
}
