package com.philips.platform.appinfra.rest;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class AppInfraHurlStack extends HurlStack {

    private static final String SSL_RESPONSE_PUBLIC_KEY = "Public-Key-Pins";
    private PublicKeyPinInterface pinInterface;

    public AppInfraHurlStack(PublicKeyPinInterface pinInterface, UrlRewriter urlRewriter) {
        super(urlRewriter);
        this.pinInterface = pinInterface;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = super.createConnection(url);
        try {
            if (connection != null && "https".equals(url.getProtocol())) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory(pinInterface, url.getHost()));
            }
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        HttpResponse httpResponse = super.executeRequest(request, additionalHeaders);
        String publicKeyDetails = getPublicKeyDetailsFromHeader(httpResponse);
        String hostName = getHostname(request);
        pinInterface.updatePublicPins(hostName, publicKeyDetails);

        
        return httpResponse;
    }

    private String getPublicKeyDetailsFromHeader(HttpResponse httpResponse) {
        List<Header> headers = httpResponse.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals(SSL_RESPONSE_PUBLIC_KEY)) {
                return header.getValue();
            }
        }
        return "";
    }

    private String getHostname(Request<?> request) {
        try {
            URL url = new URL(request.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            return "";
        }
    }
}
