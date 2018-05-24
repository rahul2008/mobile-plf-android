package com.philips.platform.appinfra.rest;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.cert.X509Certificate;
import java.util.HashMap;

public class PinningManager implements PublicKeyPinInterface {

    private static final String SSL_PUBLIC_KEY_PIN_LOG_MESSAGE = "Public-key pins Mismatch";
    private static final String SSL_PUBLIC_KEY_NOT_FOUND_LOG_MESSAGE = "Could not find Public-Key-Pins in network response";
    private static final String SSL_STORAGE_ERROR_LOG_MESSAGE = "Could not update Public-Key-Pins in Secure Storage";

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
        String[] networkKeys = extractPublicKeys(publicKeyDetails);
        String[] storedKeys = fetchStoredKeys(hostName);

        if (!hostName.isEmpty()) {
            if (networkKeys == null && storedKeys != null) {
                log(SSL_PUBLIC_KEY_NOT_FOUND_LOG_MESSAGE, LoggingInterface.LogLevel.ERROR);
            } else if (networkKeys != null && storedKeys == null) {
                updateStoredKeys(publicKeyDetails, hostName);
            } else if (networkKeys != null) {
                if (!(networkKeys[1].equals(storedKeys[1])) || !(networkKeys[3].equals(storedKeys[3]))) {
                    updateStoredKeys(publicKeyDetails, hostName);
                }
            }
        }
    }

    @Override
    public void validatePublicPins(String hostName, X509Certificate[] chain) {

    }

    private String[] fetchStoredKeys(String hostName) {
        String storedInfo = secureStorageInterface.fetchValueForKey(hostName, getSecureStorageError());
        if (storedInfo != null)
            return extractPublicKeys(storedInfo);
        return null;
    }

    private void updateStoredKeys(String publicKeyDetails, String hostName) {
        boolean isUpdated = secureStorageInterface.storeValueForKey(hostName, publicKeyDetails, getSecureStorageError());
        if (isUpdated) {
            log(publicKeyDetails, LoggingInterface.LogLevel.INFO);
        } else {
            log(SSL_STORAGE_ERROR_LOG_MESSAGE, LoggingInterface.LogLevel.DEBUG);
        }
    }

    private String[] extractPublicKeys(String publicKeyInfo) {
        if (publicKeyInfo.contains("=\"")) {
            return publicKeyInfo.split("=\"");
        }
        return null;
    }


    private void log(String message, LoggingInterface.LogLevel logLevel) {
        loggingInterface.log(logLevel, AppInfraNetwork.class.getSimpleName(), SSL_PUBLIC_KEY_PIN_LOG_MESSAGE + ":" + message);
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

}
