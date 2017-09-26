/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.util;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class HTTP {

    public static final String PROTOCOL_HTTPS = "https";

    private static HTTP instance;

    private SSLContext sslContext;
    private final ExecutorService executor = newCachedThreadPool();
    private URLConnection urlConnection;

    public interface RequestCallback {
        void onResponse(String response);

        void onError(String message, Throwable reason);
    }

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // FIXME This accepts everything
            return true;
        }
    };

    private HTTP() {
        // Utility class
    }

    public static HTTP getInstance() {
        if (instance == null) {
            instance = new HTTP();
        }
        return instance;
    }

    public void get(@NonNull final URL url, final int timeout, final RequestCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;

                try {
                    urlConnection = url.openConnection();

                    if (url.getProtocol().equals(PROTOCOL_HTTPS)) {
                        try {
                            initializeSslFactory();
                        } catch (final NoSuchAlgorithmException | KeyManagementException e) {
                            throw new IOException("Error creating a secure connection.", e);
                        }
                        ((HttpsURLConnection) urlConnection).setHostnameVerifier(hostnameVerifier);
                        ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
                    }

                    if (timeout > 0) {
                        urlConnection.setConnectTimeout(timeout);
                    }

                    if (((HttpURLConnection) urlConnection).getResponseCode() == HttpURLConnection.HTTP_OK) {
                        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                        final StringBuffer buffer = new StringBuffer();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        callback.onResponse(buffer.toString());
                    }
                } catch (final IOException e) {
                    callback.onError("Error connecting to: " + url.toString(), e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        });
    }

    private void initializeSslFactory() throws NoSuchAlgorithmException, KeyManagementException {
        if (sslContext == null) {
            sslContext = SSLContext.getInstance("TLS");

            // FIXME Accept all certificates, DO NOT DO THIS FOR PRODUCTION CODE
            sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        }
    }
}
