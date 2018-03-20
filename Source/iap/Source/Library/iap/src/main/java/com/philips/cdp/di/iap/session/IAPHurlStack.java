/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class IAPHurlStack {
    private final OAuthListener mOAuthListener;
    private static final String PHILIPS_HOST = "philips.com";

    public IAPHurlStack(OAuthListener oAuthListener) {
        mOAuthListener = oAuthListener;
    }

    public HurlStack getHurlStack() {
        return new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(final URL url) throws IOException {
                HttpURLConnection connection = super.createConnection(url);
                connection.setInstanceFollowRedirects(true);
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(final String hostname, final SSLSession session) {
                            return hostname.contains(PHILIPS_HOST);
                        }
                    });
                    if (mOAuthListener != null) {
                        connection.setRequestProperty("Authorization", "Bearer " + mOAuthListener.getAccessToken());
                    }
                }
                return connection;
            }
        };
    }
}