package com.philips.platform.appinfra.rest;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

class AppInfraHurlStack extends HurlStack {

    public AppInfraHurlStack(UrlRewriter urlRewriter) {
        super(urlRewriter);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = super.createConnection(url);
        try {
            if (connection != null && "https".equals(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory(url.getHost()));
            }
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
