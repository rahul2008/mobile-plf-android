package com.philips.cdp.registration.restclient;

import com.android.volley.Response;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

public class URRequest {

    private final String header;
    private final String url;
    private final String body;
    private final Response.Listener<String> successListener;
    private final Response.ErrorListener errorListener;

    public URRequest(String url, String body, String header, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        this.url = url;
        this.body = body;
        this.successListener = successListener;
        this.errorListener = errorListener;
        this.header = header;
    }

    private URRestClientStringRequest getUrRestClientStringRequest() {
        return new URRestClientStringRequest(url, body, header, successListener, errorListener);
    }

    public void makeRequest() {
        RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue().add(getUrRestClientStringRequest());
    }
}
