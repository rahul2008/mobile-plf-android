/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.session;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MECHurlStack {
    private final OAuthListener mOAuthListener;
    private static final String PHILIPS_HOST = "philips.com";

    public MECHurlStack(OAuthListener oAuthListener) {
        mOAuthListener = oAuthListener;
    }

    public HurlStack getHurlStack() {
        return new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(final URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                connection.setInstanceFollowRedirects(true);
                if (connection instanceof HttpsURLConnection) {
                    if (mOAuthListener != null && mOAuthListener.getAccessToken()!=null) {
                        connection.setRequestProperty("Authorization", "Bearer " + mOAuthListener.getAccessToken());
                    }
                }
                return connection;
            }
        };
    }
}