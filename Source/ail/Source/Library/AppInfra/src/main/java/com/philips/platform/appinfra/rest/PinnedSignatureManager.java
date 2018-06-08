package com.philips.platform.appinfra.rest;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PinnedSignatureManager implements PublicKeyPinInterface {

    private static final String LOG_MESSAGE_BASE = "Public-key pins Mismatch";
    private static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH = "Mismatch of stored pinned Public-key with certificate signature";
    private static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK = "Could not find Public-Key-Pins in network response";
    private static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE = "Could not find Public-Key-Pins in storage";
    private static final String LOG_MESSAGE_STORAGE_ERROR = "Could not update Public-Key-Pins in Secure Storage";

    private static final String PUBLIC_KEY_REGEX = "pin-sha256=\".+?\";";
    private static final String STORAGE_KEY_PREFIX = "SSL_HPKP_";
    private static final int PIN_MISMATCH_LOG_MAX_COUNT = 3;

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
    public void updatePinnedPublicKey(String hostName, String publicKeyDetails) {
        String storedKeyDetails = getStoredPublicKey(hostName);
        boolean isKeyFound = publicKeyDetails.contains("pin-sha256");

        if (!hostName.isEmpty()) {
            if (!isKeyFound && storedKeyDetails != null) {
                logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK);
            } else if (isKeyFound) {
                if (storedKeyDetails == null) {
                    updateStoredPublicKey(hostName, publicKeyDetails);
                    logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE);
                } else {
                    Pattern pattern = Pattern.compile(PUBLIC_KEY_REGEX);
                    Matcher networkKeyMatcher = pattern.matcher(publicKeyDetails);

                    while (networkKeyMatcher.find()) {
                        if (storedKeyDetails.contains(networkKeyMatcher.group()))
                            return;
                    }
                    updateStoredPublicKey(hostName, publicKeyDetails);
                }
            }
        }
    }

    private String getStoredPublicKey(String hostName) {
        String storedKeyDetails;
        if (publicKeyPinCache.containsKey(hostName)) {
            storedKeyDetails = publicKeyPinCache.get(hostName);
        } else {
            storedKeyDetails = secureStorageInterface.fetchValueForKey(STORAGE_KEY_PREFIX + hostName, getSecureStorageError());
            if (storedKeyDetails != null) {
                publicKeyPinCache.put(hostName, storedKeyDetails);
            }
        }
        return storedKeyDetails == null ? "" : storedKeyDetails;
    }

    @Override
    public boolean isPinnedCertificateMatching(String hostName, List<X509Certificate> chain) {
        String storedKeyDetails = getStoredPublicKey(hostName);
        for (X509Certificate certificate : chain) {
            String certificatePin = getSHA256Value(certificate);
            if (certificatePin != null && storedKeyDetails.contains(certificatePin))
                return true;
        }
        logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH);
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

    private void updateStoredPublicKey(String hostName, String publicKeyDetails) {
        boolean success = secureStorageInterface.storeValueForKey(STORAGE_KEY_PREFIX + hostName, publicKeyDetails, getSecureStorageError());
        if (!success) {
            log(hostName, LOG_MESSAGE_STORAGE_ERROR, LoggingInterface.LogLevel.DEBUG);
        }
        publicKeyPinCache.put(hostName, publicKeyDetails);
    }

    private void log(String hostname, String message, LoggingInterface.LogLevel logLevel) {
        loggingInterface.log(logLevel, PinnedSignatureManager.class.getSimpleName(), LOG_MESSAGE_BASE + ":" + hostname + ":" + message);
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

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

}
