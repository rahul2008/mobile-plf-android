/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Network;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.DICommLog.Verbosity;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.cdp2.commlib.core.util.GsonProvider;
import com.philips.cdp2.commlib.lan.util.WifiNetworkProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;

import static com.philips.cdp.dicommclient.util.DICommLog.LOCALREQUEST;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.DEBUG;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.ERROR;
import static com.philips.cdp.dicommclient.util.DICommLog.Verbosity.INFO;

public class LanRequest extends Request {

    private static final int CONNECTION_TIMEOUT = 10 * 1000; // 10secs

    private static final String BASEURL_PORTS = "http://%s/di/v%d/products/%d/%s";
    private static final String BASEURL_PORTS_HTTPS = "https://%s/di/v%d/products/%d/%s";

    @VisibleForTesting
    final String url;

    @NonNull
    private final NetworkNode networkNode;
    @Nullable
    private final SSLContext sslContext;
    private final LanRequestType requestType;
    private final DISecurity diSecurity;

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true; // Just accept everything
        }
    };

    LanRequest(final @NonNull NetworkNode networkNode, @Nullable SSLContext sslContext, String portName, int productId, LanRequestType requestType, Map<String, Object> dataMap, ResponseHandler responseHandler, DISecurity diSecurity) {
        super(dataMap, responseHandler);

        this.networkNode = networkNode;
        this.sslContext = sslContext;
        this.requestType = requestType;
        this.diSecurity = diSecurity;
        this.url = String.format(Locale.US, networkNode.isHttps() ? BASEURL_PORTS_HTTPS : BASEURL_PORTS, networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), productId, portName);
    }

    private String createDataToSend(final @NonNull Map<String, Object> dataMap) {
        if (dataMap.isEmpty())
            return null;

        String data = GsonProvider.get().toJson(dataMap, Map.class);
        log(INFO, LOCALREQUEST, "Data to send: " + data);

        if (!networkNode.isHttps() && diSecurity != null) {
            return diSecurity.encryptData(data);
        }
        log(INFO, LOCALREQUEST, "Not encrypting data");

        return data;
    }

    @Override
    public Response execute() {
        log(DEBUG, LOCALREQUEST, "Start request LOCAL");
        log(INFO, LOCALREQUEST, "Url: " + url + ", request type: " + requestType);

        String result;
        InputStream inputStream = null;
        OutputStreamWriter out = null;
        HttpURLConnection conn = null;
        int responseCode = -1;

        try {
            URL urlConn = new URL(url);
            conn = createConnection(urlConn, requestType.getMethod());

            if (requestType == LanRequestType.PUT || requestType == LanRequestType.POST) {
                if (mDataMap == null || mDataMap.isEmpty()) {
                    log(ERROR, LOCALREQUEST, "Request failed - no data for PUT or POST");
                    return new Response(null, Error.NO_REQUEST_DATA, mResponseHandler);
                }
                out = appendDataToRequestIfAvailable(conn);
            } else if (requestType == LanRequestType.DELETE) {
                appendDataToRequestIfAvailable(conn);
            }

            try {
                conn.connect();
            } catch (SocketTimeoutException e) {
                log(ERROR, LOCALREQUEST, e.getMessage() == null ? "SocketTimeoutException" : e.getMessage());
                return new Response(null, Error.TIMED_OUT, mResponseHandler);
            }

            try {
                responseCode = conn.getResponseCode();
            } catch (SocketTimeoutException e) {
                responseCode = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
            } catch (Exception e) {
                responseCode = HttpURLConnection.HTTP_BAD_GATEWAY;
                log(ERROR, LOCALREQUEST, "Failed to get response code");
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
                log(ERROR, LOCALREQUEST, "REQUEST_FAILED - " + result);
                return new Response(result, Error.REQUEST_FAILED, mResponseHandler);
            }
        } catch (SSLHandshakeException e) {
            log(ERROR, LOCALREQUEST, e.getMessage() == null ? "SSLHandshakeException" : e.getMessage());
            return new Response(null, Error.INSECURE_CONNECTION, mResponseHandler);
        } catch (TransportUnavailableException e) {
            log(ERROR, LOCALREQUEST, "Request failed - no wifi connection available");
            return new Response(null, Error.NO_TRANSPORT_AVAILABLE, mResponseHandler);
        } catch (IOException e) {
            log(ERROR, LOCALREQUEST, e.getMessage() == null ? "IOException" : e.getMessage());
            return new Response(null, Error.IOEXCEPTION, mResponseHandler);
        } finally {
            closeAllConnections(inputStream, out, conn);
            log(DEBUG, LOCALREQUEST, "Stop request LOCAL - response code: " + responseCode);
        }
    }

    protected void log(final Verbosity verbosity, final @NonNull String tag, final @NonNull String message) {
        DICommLog.log(verbosity, tag, message);
    }

    private Response handleHttpOk(InputStream inputStream) throws IOException {
        final String cypher = convertInputStreamToString(inputStream);

        if (TextUtils.isEmpty(cypher)) {
            log(ERROR, LOCALREQUEST, "Request failed - null response");
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        }

        final String data = decryptData(cypher);
        if (data == null) {
            log(ERROR, LOCALREQUEST, "Request failed - failed to decrypt");
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        }

        log(INFO, LOCALREQUEST, "Received data: " + data);
        return new Response(data, null, mResponseHandler);
    }

    private Response handleBadRequest(InputStream inputStream) throws IOException {
        final String errorMessage = convertInputStreamToString(inputStream);
        log(ERROR, LOCALREQUEST, "BAD REQUEST - " + errorMessage);

        if (!networkNode.isHttps() && diSecurity != null) {
            log(ERROR, LOCALREQUEST, "Request not properly encrypted - notifying listener");
            diSecurity.notifyEncryptionFailedListener();
        }
        return new Response(errorMessage, Error.NOT_UNDERSTOOD, mResponseHandler);
    }

    private String decryptData(final @NonNull String cypher) {
        if (!networkNode.isHttps() && diSecurity != null) {
            return diSecurity.decryptData(cypher);
        }
        return cypher;
    }

    private OutputStreamWriter appendDataToRequestIfAvailable(HttpURLConnection conn) throws IOException {
        final String data = createDataToSend(mDataMap);
        if (data == null) {
            return null;
        }

        OutputStreamWriter out;
        out = new OutputStreamWriter(conn.getOutputStream(), Charset.defaultCharset());
        out.write(data);
        out.flush();

        return out;
    }

    @NonNull
    @VisibleForTesting
    HttpURLConnection createConnection(final @NonNull URL url, final @NonNull String requestMethod) throws IOException, TransportUnavailableException {
        HttpURLConnection conn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Network network = getWifiNetwork();

            if (network == null) {
                throw new TransportUnavailableException("Network unavailable.");
            }
            conn = (HttpURLConnection) network.openConnection(url);
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
            if (sslContext != null) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(sslContext.getSocketFactory());
            }
        }
        conn.setRequestProperty("content-type", "application/json");
        conn.setRequestProperty("connection", "close");
        conn.setRequestMethod(requestMethod);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(CONNECTION_TIMEOUT);

        return conn;
    }

    private Network getWifiNetwork() {
        final Context context = ContextProvider.get();

        if (context == null) {
            throw new IllegalStateException("Context is null.");
        }
        return WifiNetworkProvider.get(context).getNetwork();
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param inputStream Input stream to convert to string
     * @return Returns converted string
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
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
