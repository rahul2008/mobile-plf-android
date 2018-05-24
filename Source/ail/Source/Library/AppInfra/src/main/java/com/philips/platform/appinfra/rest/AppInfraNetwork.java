package com.philips.platform.appinfra.rest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.ByteArrayPool;
import com.philips.platform.appinfra.AppInfraInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class AppInfraNetwork extends BasicNetwork {

    private static final String SSL_RESPONSE_PUBLIC_KEY = "Public-Key-Pins";

    private PublicKeyPinInterface pinInterface;

    public AppInfraNetwork(BaseHttpStack httpStack) {
        super(httpStack);
    }

    public AppInfraNetwork(BaseHttpStack httpStack, ByteArrayPool pool) {
        super(httpStack, pool);
    }

    public AppInfraNetwork(PublicKeyPinInterface pinInterface, BaseHttpStack httpStack, AppInfraInterface appInfraInterface) {
        this(httpStack);
        this.pinInterface = pinInterface;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        NetworkResponse networkResponse = super.performRequest(request);
        String publicKeyDetails = getPublicKeyDetailsFromHeader(networkResponse);
        String hostName = getHostname(request);
        pinInterface.updatePublicPins(hostName, publicKeyDetails);
        return networkResponse;
    }


    private String getPublicKeyDetailsFromHeader(NetworkResponse networkResponse) {
        Map<String, String> headers = networkResponse.headers;
        if (headers.containsKey(SSL_RESPONSE_PUBLIC_KEY)) {
            return headers.get(SSL_RESPONSE_PUBLIC_KEY);
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
