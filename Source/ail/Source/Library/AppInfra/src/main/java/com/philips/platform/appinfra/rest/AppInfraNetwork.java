package com.philips.platform.appinfra.rest;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ByteArrayPool;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class AppInfraNetwork extends BasicNetwork {

    private static final String SSL_PUBLIC_KEY_PIN_LOG_MESSAGE = "Public-key pins Mismatch";
    private static final String SSL_PUBLIC_KEY_NOT_FOUND_LOG_MESSAGE = "Could not find Public-Key-Pins in network response";
    private static final String SSL_EMPTY_HOSTNAME_LOG_MESSAGE = "Could not find hostname for the request";
    private static final String SSL_STORAGE_ERROR_LOG_MESSAGE = "Could not update Public-Key-Pins in Secure Storage";
    private static final String SSL_RESPONSE_PUBLIC_KEY = "Public-Key-Pins";

    private SecureStorageInterface secureStorageInterface;

    private LoggingInterface loggingInterface;

    public AppInfraNetwork(BaseHttpStack httpStack) {
        super(httpStack);
    }

    public AppInfraNetwork(BaseHttpStack httpStack, ByteArrayPool pool) {
        super(httpStack, pool);
    }

    public AppInfraNetwork(BaseHttpStack httpStack, AppInfraInterface appInfraInterface) {
        this(httpStack);
        this.secureStorageInterface = appInfraInterface.getSecureStorage();
        this.loggingInterface = appInfraInterface.getLogging();
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        NetworkResponse networkResponse = super.performRequest(request);
        String publicKeyDetails = getPublicKeyDetailsFromHeader(networkResponse);
        String[] networkKeys = extractPublicKeys(publicKeyDetails);
        String hostName = getHostname(request);
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
        return networkResponse;
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

    private String getPublicKeyDetailsFromHeader(NetworkResponse networkResponse) {
        Map<String, String> headers = networkResponse.headers;
        if (headers.containsKey(SSL_RESPONSE_PUBLIC_KEY)) {
            return headers.get(SSL_RESPONSE_PUBLIC_KEY);
        }
        return "";
    }

    private String getHostname(Request<?> request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            log(e.getMessage(), LoggingInterface.LogLevel.ERROR);
            return "";
        }
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
