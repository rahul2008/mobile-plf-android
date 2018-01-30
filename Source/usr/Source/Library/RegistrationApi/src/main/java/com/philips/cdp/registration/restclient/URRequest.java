package com.philips.cdp.registration.restclient;

import com.android.volley.Response;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

/**
 * Created by philips on 29/01/18.
 */

public class URRequest {

    private final int method;
    private final String url;
    private final String body;
    private final Response.Listener<String> successListener;
    private final Response.ErrorListener errorListener;

    public URRequest(int method, String url, String body, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.successListener = successListener;
        this.errorListener = errorListener;
    }

    private URRestClientStringRequest getUrRestClientStringRequest() {
        return new URRestClientStringRequest(method, url, body, successListener, errorListener, null, null, null);
    }

    public void makeRequest() {
        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(getUrRestClientStringRequest());
    }
}
