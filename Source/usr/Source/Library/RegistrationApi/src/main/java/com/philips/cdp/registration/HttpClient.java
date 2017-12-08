
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.support.v4.util.Pair;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.*;
import java.util.List;

import javax.net.ssl.*;

/**
 * @{code HttpClient} class wraps the GET and POST REST Calls to webservice by exposing simple APIs
 * like {@code callGet} and {@code callPost}
 */
public class HttpClient {

    final private String ACCESS_TOKEN_HEADER = "x-accessToken";

    final private String CONTENT_TYPE_HEADER = "Content-Type";

    final private String REQUEST_METHOD_POST = "POST";

    final  private String REQUEST_METHOD_GET = "GET";

    final private String CONTENT_TYPE = "application/x-www-form-urlencoded";

    final private String LOG_TAG = "HttpClient";

    /**
     * {@code callPost} method makes a POST call to a webservice.
     *
     * @param urlString
     * @param nameValuePairs
     * @param accessToken
     * @return
     */
    public String callPost(String urlString, List<Pair<String, String>> nameValuePairs,
                           String accessToken) {
        URL url = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder inputResponse = new StringBuilder();
        try {
            url = new URL(urlString);
            final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
            connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
            connection.setRequestMethod(REQUEST_METHOD_POST);
            final javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
            connection.setSSLSocketFactory(sf);
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            outputStream = connection.getOutputStream();
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(getPostString(nameValuePairs));
            wr.flush();
            wr.close();
            final int responseCode = connection.getResponseCode();
            inputResponse = new StringBuilder();
            bufferedReader = getBufferedReader(inputResponse, responseCode,
                    new InputStreamReader(connection.getInputStream()),
                    new InputStreamReader(connection.getErrorStream()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return inputResponse.toString();

    }

    private BufferedReader getBufferedReader(StringBuilder inputResponse,
                                             int responseCode, InputStreamReader in,
                                             InputStreamReader in2) throws IOException {
        BufferedReader bufferedReader = null;
        String input;
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(in);
            while ((input = bufferedReader.readLine()) != null) {
                inputResponse.append(input);
            }
        } else {
            bufferedReader = new BufferedReader(in2);
            while ((input = bufferedReader.readLine()) != null) {
                inputResponse.append(input);
            }
        }
        return bufferedReader;
    }

    private String getPostString(List<Pair<String, String>> nameValuePairs) {
        StringBuilder postString = new StringBuilder();
        boolean firstItem = true;

        for (Pair<String, String> pair : nameValuePairs) {
            if (firstItem)
                firstItem = false;
            else {
                postString.append("&");
            }
            try {
                postString.append(URLEncoder.encode( pair.first, "UTF-8"));
                postString.append("=");
                postString.append(URLEncoder.encode( pair.second, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return postString.toString();
    }

    /**
     * {@code callGet} method makes a GET call to a webservice
     *
     * @param urlString
     * @param accessToken
     * @return
     */
    public String callGet(String urlString, String accessToken) {
        URL url = null;
        BufferedReader bufferedReader = null;
        StringBuilder inputResponse = new StringBuilder();
        try {
            url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
            connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
            connection.setRequestMethod(REQUEST_METHOD_GET);
            javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
            connection.setSSLSocketFactory(sf);
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });


            int responseCode = connection.getResponseCode();
            bufferedReader = getBufferedReader(inputResponse, responseCode,
                    new InputStreamReader(connection.getInputStream()),
                    new InputStreamReader(connection.getErrorStream()));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return inputResponse.toString();

    }

    private javax.net.ssl.SSLSocketFactory createSslSocketFactory() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(new KeyManager[0], new TrustManager[]{tm}, new SecureRandom());
        return sslContext.getSocketFactory();
    }


}
