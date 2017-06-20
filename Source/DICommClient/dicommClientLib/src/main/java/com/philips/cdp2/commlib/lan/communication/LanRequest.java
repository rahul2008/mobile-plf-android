/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class LanRequest extends Request {

    private static final int CONNECTION_TIMEOUT = 10 * 1000; // 10secs
    private static final int GETWIFI_TIMEOUT = 3 * 1000; // 3secs
    private static final String BASEURL_PORTS = "http://%s/di/v%d/products/%d/%s";
    private static final String BASEURL_PORTS_HTTPS = "https://%s/di/v%d/products/%d/%s";
    @VisibleForTesting
    final String mUrl;
    private final LanRequestType mRequestType;
    private final DISecurity mDISecurity;
    private boolean mHttps = false;
    private static SSLContext sslContext;
    private static final Object LOCK = new Object();

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true; //Just accept everything
        }
    };

    private static void initializeSslFactory() throws NoSuchAlgorithmException, KeyManagementException {
        if (sslContext != null) return;
        sslContext = SSLContext.getInstance("TLS");
        // Accept all certificates, DO NOT DO THIS FOR PRODUCTION CODE
        sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
    }

    public LanRequest(String applianceIpAddress, int protocolVersion, boolean isHttps, String portName, int productId, LanRequestType requestType, Map<String, Object> dataMap,
                      ResponseHandler responseHandler, DISecurity diSecurity) {
        super(dataMap, responseHandler);
        mHttps = isHttps;
        mUrl = createPortUrl(applianceIpAddress, protocolVersion, portName, productId);
        mRequestType = requestType;
        mDISecurity = diSecurity;
    }

    private String createPortUrl(String ipAddress, int dicommProtocolVersion, String portName, int productId) {
        if (mHttps) {
            return String.format(Locale.US, BASEURL_PORTS_HTTPS, ipAddress, dicommProtocolVersion, productId, portName);
        }
        return String.format(Locale.US, BASEURL_PORTS, ipAddress, dicommProtocolVersion, productId, portName);
    }

    private String createDataToSend(Map<String, Object> dataMap) {
        if (dataMap == null || dataMap.size() <= 0) return null;

        String data = GsonProvider.get().toJson(dataMap);
        DICommLog.i(DICommLog.LOCALREQUEST, "Data to send: " + data);

        if (!mHttps && mDISecurity != null) {
            return mDISecurity.encryptData(data);
        }
        DICommLog.i(DICommLog.LOCALREQUEST, "Not encrypting data");
        return data;
    }

    @Override
    public Response execute() {
        DICommLog.d(DICommLog.LOCALREQUEST, "Start request LOCAL");
        DICommLog.i(DICommLog.LOCALREQUEST, "Url: " + mUrl + ", Requesttype: " + mRequestType);

        String result;
        InputStream inputStream = null;
        OutputStreamWriter out = null;
        HttpURLConnection conn = null;
        int responseCode = -1;

        try {
            URL urlConn = new URL(mUrl);
            conn = LanRequest.createConnection(urlConn, mRequestType.getMethod(), CONNECTION_TIMEOUT, GETWIFI_TIMEOUT);
            if (conn == null) {
                DICommLog.e(DICommLog.LOCALREQUEST, "Request failed - no wificonnection available");
                return new Response(null, Error.NO_TRANSPORT_AVAILABLE, mResponseHandler);
            }

            if (mRequestType == LanRequestType.PUT || mRequestType == LanRequestType.POST) {
                if (mDataMap == null || mDataMap.isEmpty()) {
                    DICommLog.e(DICommLog.LOCALREQUEST, "Request failed - no data for Put or Post");
                    return new Response(null, Error.NO_REQUEST_DATA, mResponseHandler);
                }
                out = appendDataToRequestIfAvailable(conn);
            } else if (mRequestType == LanRequestType.DELETE) {
                appendDataToRequestIfAvailable(conn);
            }
            conn.connect();

            try {
                responseCode = conn.getResponseCode();
            } catch (Exception e) {
                responseCode = HttpURLConnection.HTTP_BAD_GATEWAY;
                DICommLog.e(DICommLog.LOCALREQUEST, "Failed to get responsecode");
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                return handleHttpOk(inputStream);
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getErrorStream();
                return handleBadRequest(inputStream);
            } else if (responseCode == HttpURLConnection.HTTP_BAD_GATEWAY) {
                return new Response(null, Error.CANNOT_CONNECT, mResponseHandler);
            } else {
                inputStream = conn.getErrorStream();
                result = convertInputStreamToString(inputStream);
                DICommLog.e(DICommLog.LOCALREQUEST, "REQUEST_FAILED - " + result);
                return new Response(result, Error.REQUEST_FAILED, mResponseHandler);
            }
        } catch (IOException e) {
            DICommLog.e(DICommLog.LOCALREQUEST, e.getMessage() != null ? e.getMessage() : "IOException");
            return new Response(null, Error.IOEXCEPTION, mResponseHandler);
        } finally {
            closeAllConnections(inputStream, out, conn);
            DICommLog.d(DICommLog.LOCALREQUEST, "Stop request LOCAL - responsecode: " + responseCode);
        }
    }

    private Response handleHttpOk(InputStream inputStream) throws IOException {
        String cypher = convertInputStreamToString(inputStream);

        if (TextUtils.isEmpty(cypher)) {
            DICommLog.e(DICommLog.LOCALREQUEST, "Request failed - null response");
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        }

        String data = decryptData(cypher);
        if (data == null) {
            DICommLog.e(DICommLog.LOCALREQUEST, "Request failed - failed to decrypt");
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        }

        DICommLog.i(DICommLog.LOCALREQUEST, "Received data: " + data);
        return new Response(data, null, mResponseHandler);
    }

    private Response handleBadRequest(InputStream inputStream) throws IOException {
        String errorMessage = convertInputStreamToString(inputStream);
        DICommLog.e(DICommLog.LOCALREQUEST, "BAD REQUEST - " + errorMessage);

        if (!mHttps && mDISecurity != null) {
            DICommLog.e(DICommLog.LOCALREQUEST, "Request not properly encrypted - notifying listener");
            mDISecurity.notifyEncryptionFailedListener();
        }

        return new Response(errorMessage, Error.NOT_UNDERSTOOD, mResponseHandler);
    }

    private String decryptData(String cypher) {
        if (!mHttps && mDISecurity != null) {
            return mDISecurity.decryptData(cypher);
        }
        return cypher;
    }

    private OutputStreamWriter appendDataToRequestIfAvailable(HttpURLConnection conn) throws IOException {
        String data = createDataToSend(mDataMap);
        if (data == null) return null;

        OutputStreamWriter out;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            conn.setDoOutput(true);
        }
        out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
        out.write(data);
        out.flush();
        return out;
    }

    @SuppressLint("NewApi")
    private static HttpURLConnection createConnection(URL url, String requestMethod, int connectionTimeout, int lockTimeout) throws IOException {
        HttpURLConnection conn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network wifiNetworkForSocket = LanRequest.getWifiNetworkForSocket(DICommClientWrapper.getContext(), lockTimeout);

            if (wifiNetworkForSocket == null) {
                return null;
            }
            conn = (HttpURLConnection) wifiNetworkForSocket.openConnection(url);
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        if (url.toString().startsWith("https://")) {
            try {
                initializeSslFactory();
            } catch (final NoSuchAlgorithmException e) {
                Log.e(ConnectionLibContants.LOG_TAG, "NoSuchAlgorithmException: " + e.getMessage());
            } catch (final KeyManagementException e) {
                Log.e(ConnectionLibContants.LOG_TAG, "KeyManagementException: " + e.getMessage());
            }

            ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
        }
        conn.setRequestProperty("content-type", "application/json");
        conn.setRequestProperty("connection", "close");
        conn.setRequestMethod(requestMethod);
        if (connectionTimeout != -1) {
            conn.setConnectTimeout(connectionTimeout);
        }
        return conn;
    }

    @SuppressLint("NewApi")
    private static Network getWifiNetworkForSocket(Context context, int lockTimeout) {
        ConnectivityManager connectionManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder request = new NetworkRequest.Builder();
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        //request.addCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P);

        WifiNetworkCallback networkCallback = new WifiNetworkCallback(LOCK, connectionManager);

        synchronized (LOCK) {
            connectionManager.registerNetworkCallback(request.build(), networkCallback);
            try {
                LOCK.wait(lockTimeout);
                Log.e(DICommLog.WIFI, "Timeout error occurred");
            } catch (InterruptedException ignored) {
            }
        }
        connectionManager.unregisterNetworkCallback(networkCallback);
        return networkCallback.getNetwork();
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param inputStream Input stream to convert to string
     * @return Returns converted string
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        if (inputStream == null) return "";
        Reader reader = new InputStreamReader(inputStream, "UTF-8");

        int len = 1024;
        char[] buffer = new char[len];
        StringBuilder sb = new StringBuilder(len);
        int count;

        while ((count = reader.read(buffer)) > 0) {
            sb.append(buffer, 0, count);
        }

        return sb.toString();
    }

    private static void closeAllConnections(InputStream is, OutputStreamWriter out, HttpURLConnection conn) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }
}
