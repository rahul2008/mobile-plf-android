/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.model.AbstractModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class TestEnvOAuthHandler implements OAuthHandler {
    private String access_token;
    private Context mContext;
    private AbstractModel mModel;

    public TestEnvOAuthHandler(Context context) {
        mContext = context;
    }

    @Override
    public String generateToken() {
        sendOAuthRequest();
        return access_token;
    }

    public void setModel(AbstractModel model) {
        mModel = model;
    }
    // HTTP GET request
    private void sendOAuthRequest() {
        try {
            URL obj = new URL(HybrisDelegate.getInstance(mContext).getStore().getOauthUrl());
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setHostnameVerifier(hostnameVerifier);

            int responseCode = con.getResponseCode();

//            if(responseCode != 200) {
//                new TokenErrorHandler(mModel,null);
//            }

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            BufferedReader in = new BufferedReader(inputStreamReader);
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

    private HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname.contains("philips.com");
        }
    };
}