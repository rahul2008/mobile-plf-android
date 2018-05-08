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
import java.util.Date;
import java.util.Map;

public class AppInfraNetwork extends BasicNetwork {

    private static final String SSL_PUBLIC_KEY_PIN_LOG_MESSAGE = "Public-key pins Mismatch!";
    private static final String SSL_RESPONSE_PUBLIC_KEY = "Public-Key-Pins";

    private AppInfraInterface appInfraInterface;

    public AppInfraNetwork(BaseHttpStack httpStack) {
        super(httpStack);
    }

    public AppInfraNetwork(BaseHttpStack httpStack, ByteArrayPool pool) {
        super(httpStack, pool);
    }

    public AppInfraNetwork(BaseHttpStack httpStack, AppInfraInterface appInfraInterface){
        this(httpStack);
        this.appInfraInterface = appInfraInterface;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        NetworkResponse networkResponse = super.performRequest(request);
        String publicKey = getPublicKey(networkResponse);
        String storedKey = appInfraInterface.getSecureStorage().fetchValueForKey(getHostname(request), getSecureStorageError());
        

        if(false){
            appInfraInterface.getSecureStorage().storeValueForKey(getHostname(request), publicKey, getSecureStorageError());
            log(publicKey);
        }

        return networkResponse;
    }

    private String getPublicKey(NetworkResponse networkResponse) {
        Map<String, String> headers = networkResponse.headers;
        if(headers.containsKey(SSL_RESPONSE_PUBLIC_KEY)){
            return headers.get(SSL_RESPONSE_PUBLIC_KEY);
        }
        return "";
    }

    private String getHostname(Request<?> request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            log(e.getMessage());
            return null;
        }
    }

    private void log(String message) {
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, AppInfraNetwork.class.getSimpleName(), SSL_PUBLIC_KEY_PIN_LOG_MESSAGE);
        appInfraInterface.getLogging().log(LoggingInterface.LogLevel.ERROR, AppInfraNetwork.class.getSimpleName(), message);
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }
}
