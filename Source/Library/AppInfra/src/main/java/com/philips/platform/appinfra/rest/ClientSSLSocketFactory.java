package com.philips.platform.appinfra.rest;

import android.app.Application;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;

import com.philips.platform.appinfra.AppInfra;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 310238655 on 8/31/2016.
 */
public class ClientSSLSocketFactory extends SSLCertificateSocketFactory {
    private static SSLContext sslContext;
    AppInfra mAppInfra;

    /**
     * @param handshakeTimeoutMillis
     * @deprecated Use {@link #getDefault(int)} instead.
     */
    public ClientSSLSocketFactory(int handshakeTimeoutMillis) {
        super(handshakeTimeoutMillis);
    }


    public static SSLSocketFactory getSocketFactory(AppInfra appInfra) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslcontext = SSLContext.getInstance("TLSv1");

            sslcontext.init(null,
                    null,
                    null);
            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
//            l_connection = (HttpsURLConnection) l_url.openConnection();
//            l_connection.connect();
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{tm}, null);
//
//            SSLSocketFactory ssf = ClientSSLSocketFactory.getDefault(10000, new SSLSessionCache(appInfra.getAppInfraContext()));

            return NoSSLv3Factory;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}