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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinningManager implements PublicKeyPinInterface {

    private static final String PUBLIC_KEY_MISMATCH_LOG_MESSAGE = "Public-key pins Mismatch";
    private static final String PUBLIC_KEY_NOT_FOUND_LOG_MESSAGE = "Could not find Public-Key-Pins in network response";
    private static final String STORAGE_ERROR_LOG_MESSAGE = "Could not update Public-Key-Pins in Secure Storage";

    private HashMap<String, String> publicKeyPinCache;
    private SecureStorageInterface secureStorageInterface;
    private LoggingInterface loggingInterface;

    public PinningManager(AppInfraInterface appInfraInterface) {
        this.secureStorageInterface = appInfraInterface.getSecureStorage();
        this.loggingInterface = appInfraInterface.getLogging();
        publicKeyPinCache = new HashMap<>();
    }

    @Override
    public void updatePublicPins(String hostName, String publicKeyDetails) {
        Matcher networkKeyMatcher = extractPublicKeys(publicKeyDetails);
        String storedKeyDetails = getPublicKeyFromStorage(hostName);

        if (!hostName.isEmpty()) {
            if (networkKeyMatcher == null && storedKeyDetails != null) {
                log(PUBLIC_KEY_NOT_FOUND_LOG_MESSAGE, LoggingInterface.LogLevel.ERROR);
            } else if (networkKeyMatcher != null && storedKeyDetails == null) {
                updatePublicKeyInStorage(publicKeyDetails, hostName);
                publicKeyPinCache.put(hostName, publicKeyDetails);
            } else if (networkKeyMatcher != null) {
                    for (int i = 1; i <= networkKeyMatcher.groupCount(); i++){
                        if(storedKeyDetails.contains(networkKeyMatcher.group(i)))
                            return;
                    }
                    updatePublicKeyInStorage(publicKeyDetails, hostName);
                    publicKeyPinCache.put(hostName, publicKeyDetails);
            }
        }
    }

    private String getPublicKeyFromStorage(String hostName) {
        String storedKeyDetails;
        if(publicKeyPinCache.containsKey(hostName)){
            storedKeyDetails = publicKeyPinCache.get(hostName);
        }else {
            storedKeyDetails = secureStorageInterface.fetchValueForKey(hostName, getSecureStorageError());
            publicKeyPinCache.put(hostName, storedKeyDetails);
        }
        return storedKeyDetails;
    }

    @Override
    public void validatePublicPins(String hostName, X509Certificate[] chain) {
        String storedKeyDetails = getPublicKeyFromStorage(hostName);
        for (X509Certificate certificate : chain) {
            String certificatePin = getSHA256Value(certificate);
            if (certificatePin != null && storedKeyDetails.contains(certificatePin))
                return;
        }
        log(PUBLIC_KEY_MISMATCH_LOG_MESSAGE, LoggingInterface.LogLevel.ERROR);
    }

    private String getSHA256Value(X509Certificate certificate){
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

    private void updatePublicKeyInStorage(String publicKeyDetails, String hostName) {
        boolean isUpdated = secureStorageInterface.storeValueForKey(hostName, publicKeyDetails, getSecureStorageError());
        if (isUpdated) {
            log(publicKeyDetails, LoggingInterface.LogLevel.INFO);
        } else {
            log(STORAGE_ERROR_LOG_MESSAGE, LoggingInterface.LogLevel.DEBUG);
        }
    }

    private Matcher extractPublicKeys(String publicKeyInfo) {
        Pattern pattern = Pattern.compile("pin-sha256=\"*\";");
        Matcher matcher = pattern.matcher(publicKeyInfo);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    private void log(String message, LoggingInterface.LogLevel logLevel) {
        loggingInterface.log(logLevel, AppInfraNetwork.class.getSimpleName(), PUBLIC_KEY_MISMATCH_LOG_MESSAGE + ":" + message);
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

}
