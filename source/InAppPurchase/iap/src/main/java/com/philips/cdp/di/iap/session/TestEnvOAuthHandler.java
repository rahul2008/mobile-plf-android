/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.widget.Toast;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.response.error.ServerError;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class TestEnvOAuthHandler implements OAuthHandler {
    String access_token;
    private String mOauthUrl;

    public TestEnvOAuthHandler(String url) {
        mOauthUrl = url;
    }

    @Override
    public String generateToken() {
        sendOAuthRequest();
        return access_token;
    }

    // HTTP GET request
    private void sendOAuthRequest() {
        try {
            URL obj = new URL(mOauthUrl);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setHostnameVerifier(hostnameVerifier);

            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                InputStreamReader errorStream = new InputStreamReader(con.getErrorStream());
                BufferedReader in = new BufferedReader(errorStream);
                String inputLine;
                StringBuffer errorString = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    errorString.append(inputLine);
                }
                errorStream.close();

                Gson gson = new Gson();
                Object result = gson.fromJson(errorString.toString(), ServerError.class);

                ServerError iapNetworkError = (ServerError) result;
                com.philips.cdp.di.iap.response.error.Error error = iapNetworkError.getErrors().get(0);
                String type = error.getType();
                String message = error.getMessage();
                if (type.equalsIgnoreCase("InvalidGrantError")) {
                    // TODO: 3/16/2016 - add refresh Logic
                }
            } else {
                InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                BufferedReader in = new BufferedReader(inputStreamReader);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                assignTokenFromResponse(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignTokenFromResponse(final StringBuffer response) {
        Gson gson = new Gson();
        Object result = gson.fromJson(response.toString(), TestEnvOAuthHandler.class);
        TestEnvOAuthHandler successResult = (TestEnvOAuthHandler) result;
        access_token = successResult.access_token;
    }

    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname.contains("philips.com");
        }
    };
}