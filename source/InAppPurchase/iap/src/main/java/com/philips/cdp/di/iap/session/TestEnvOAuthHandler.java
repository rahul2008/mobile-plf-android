/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TestEnvOAuthHandler implements OAuthHandler {
    static Certificate testCertificate;
    String access_token;

    @Override
    public String generateToken(final Context context, final String janRainID, final String userID) {
        sendOAuthRequest(context);
        return access_token;
    }

    // HTTP GET request
    private void sendOAuthRequest(final Context context) {

        String url = NetworkConstants.HOST_URL + NetworkConstants.WEB_ROOT +
                "oauth/token?username=inapp@3&password=philips@123&grant_type=password&client_id=mobile_android&client_secret=secret";

        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setHostnameVerifier(hostnameVerifier);

            con.setRequestProperty("Authorization", "Basic bW9iaWxlX2FuZHJvaWQ6c2VjcmV0");
            con.setSSLSocketFactory(buildSslSocketFactory(context));
            con.setRequestProperty("Content-type", "application/json");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            assignTokenFromResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignTokenFromResponse(final StringBuffer response) {
        Gson gson = new Gson();
        TestEnvOAuthHandler result = gson.fromJson(response.toString(), TestEnvOAuthHandler.class);
        access_token = result.access_token;
    }

    public SSLSocketFactory buildSslSocketFactory(Context context) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = context.getResources().getAssets().open("test.crt");
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                testCertificate = ca;
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] mngrs = new TrustManager[]{new TestTrustManager()};//tmf.getTrustManagers();
            sslContext.init(null, mngrs, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TestTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{(X509Certificate) testCertificate};
        }
    }

    public HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            boolean result = false;
            try {
                result = session.getPeerCertificates()[0].equals(testCertificate);
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
            return result;
        }
    };
}