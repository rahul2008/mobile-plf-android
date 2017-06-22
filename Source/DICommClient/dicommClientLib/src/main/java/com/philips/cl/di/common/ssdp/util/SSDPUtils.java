package com.philips.cl.di.common.ssdp.util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class SSDPUtils {
    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true; //Just accept everything
        }
    };

    private static SSLContext sslContext;

    /**
     * HTTP GETs icon from selected url.
     *
     * @param url        to bitmap
     * @param pResources Resources
     * @return Icon's Drawable if successful, null otherwise
     */
    public static Drawable getBitmap(final URL url, final Resources pResources) {
        Drawable icon = null;

        if (null != url) {
            final String protocol = url.getProtocol();
            if ((protocol != null) && !protocol.isEmpty()) {
                URLConnection connection = null;
                InputStream is = null;
                BufferedInputStream bis = null;
                try {
                    connection = url.openConnection();
                    if (null != connection) {
                        connection.setConnectTimeout(1000);
                        connection.connect();
                        is = connection.getInputStream();
                        bis = new BufferedInputStream(is);
                        icon = new BitmapDrawable(pResources, BitmapFactory.decodeStream(bis));
                    }
                } catch (final IOException e) {
                    Log.e(ConnectionLibContants.LOG_TAG, "IOException " + e.getMessage());
                } finally {
                    if (null != bis) {
                        try {
                            bis.close();
                        } catch (final IOException e) {
                            bis = null;
                            Log.e(ConnectionLibContants.LOG_TAG, "IOException " + e.getMessage());
                        }
                    }
                    if (null != is) {
                        try {
                            is.close();
                        } catch (final IOException e) {
                            is = null;
                            Log.e(ConnectionLibContants.LOG_TAG, "IOException " + e.getMessage());
                        }
                    }
                }
            } else {
                Log.e(ConnectionLibContants.LOG_TAG, "Inavalid URL " + MalformedURLException.class.getName());
            }
        } else {
            Log.e(ConnectionLibContants.LOG_TAG, "Inavalid URL " + MalformedURLException.class.getName());
        }
        return icon;
    }

    /**
     * HTTP GETs String from selected location
     *
     * @param url url to bitmap
     * @return String containing response if successful, empty string otherwise
     */
    public static String getHTTP(final URL url) {
        String response = "";
        if (url != null) {
            /**
             * This was added because sometimes
             * it was not able to download file if we don't add above line of code
             */
            String line = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URLConnection connection = null;
            InputStreamReader is = null;
            BufferedReader br = null;
            try {
                connection = url.openConnection();
                if (null != connection) {
                    if (url.toString().startsWith("https://"))
                    {
                        try {
                            initializeSslFactory();
                        } catch (final NoSuchAlgorithmException e) {
                            Log.e(ConnectionLibContants.LOG_TAG, "NoSuchAlgorithmException: " + e.getMessage());
                        } catch (final KeyManagementException e) {
                            Log.e(ConnectionLibContants.LOG_TAG, "KeyManagementException: " + e.getMessage());
                        }

                        ((HttpsURLConnection)connection).setHostnameVerifier(hostnameVerifier);
                        ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
                    }
                    connection.setConnectTimeout(3000);
                    connection.connect();
                    is = new InputStreamReader(connection.getInputStream());
                    br = new BufferedReader(is);
                    final StringBuffer sb = new StringBuffer("");
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    response = sb.toString();
                }
            } catch (final IOException e) {
                Log.e(ConnectionLibContants.LOG_TAG, "IOException: " + e.getMessage() + ", url:" + url);
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e(ConnectionLibContants.LOG_TAG, "IOException: " + e.getMessage());
                    }
                }
                if (null != is) {
                    try {
                        is.close();
                    } catch (final IOException e) {
                        Log.e(ConnectionLibContants.LOG_TAG, "IOException: " + e.getMessage() + ", url:" + url);
                    }
                }
                if (null != connection) {
                    try {
                        ((HttpURLConnection)connection).disconnect();
                    } catch (Exception e) {
                        Log.e(ConnectionLibContants.LOG_TAG, "IOException: " + e.getMessage());
                    }
                }
            }
        }
        return response;
    }

    private static void initializeSslFactory() throws NoSuchAlgorithmException, KeyManagementException {
        if (sslContext != null) return;
        sslContext = SSLContext.getInstance("TLS");
        // Accept all certificates, DO NOT DO THIS FOR PRODUCTION CODE
        sslContext.init(null, new X509TrustManager[]{new X509TrustManager(){
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }}}, new SecureRandom());
    }
}
