/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.support.multidex.MultiDex;
import android.support.v4.util.Pair;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpClientTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    public HttpClientTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

    }

    public void testHttpClientcallGet(){
        HttpClient httpClient = new HttpClient();
     //   String s = httpClient.callGet("https://www.google.com","Ase345689fhf!@");
        //assertEquals("",s);
     //   assertNotNull(s);
    }

    public void testHttpClientPost(){
      /*  HttpClient httpClient = new HttpClient();
        DefaultHttpClient defaultHttpClient = httpClient.getHttpClient();
        List<org.apache.http.NameValuePair> nameValuePair = new ArrayList<org.apache.http.NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("productSerialNumber","rt234556778777"));
        nameValuePair
                .add(new BasicNameValuePair("purchaseDate", "2015-12-02"));

        nameValuePair
                .add(new BasicNameValuePair("registrationChannel","MS81376"));

        httpClient.postData("https://acc.philips.co.uk/prx/registration/B2C/ru%5FRU/CONSUMER/products/HD8969/09.register.type.product?",nameValuePair,"4ttk7eznansrzdvd");*/
        HttpClient httpClient = new HttpClient();
        Pair p1 = new Pair("productSerialNumber","rt234556778777");
        Pair p2 = new Pair("purchaseDate","2015-12-02");
        Pair p3 = new Pair("registrationChannel","MS81376");
        List<Pair<String,String>> al = new ArrayList<Pair<String,String>>();
        al.add(p1);
        al.add(p2);
        al.add(p3);
        httpClient.callPost("https://acc.philips.co.uk/prx/registration/B2C/de_DE/CONSUMER/products/HD8978/01.register.type.product?",al,"swa7ud5vjx75cqq2");
    }

    public void testHttpClientGet(){
        HttpClient httpClient = new HttpClient();
        httpClient.callGet("https://acc.philips.co.uk/prx/registration/B2C/de_DE/CONSUMER/products/HD8978/01.register.type.product?","swa7ud5vjx75cqq2");
        URL url = null;
        BufferedReader bufferedReader = null;
        StringBuilder inputResponse = new StringBuilder();
        try {
            url = new URL("https://acc.philips.co.uk/prx/registration/B2C/de_DE/CONSUMER/products/HD8978/01.register.type.product?");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("x-accessToken", "swa7ud5vjx75cqq2");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("GET");
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
}
