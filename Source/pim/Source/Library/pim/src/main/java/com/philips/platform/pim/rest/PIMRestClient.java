package com.philips.platform.pim.rest;

import com.android.volley.Response;
import com.philips.platform.pim.configration.PIMConfiguration;

public class PIMRestClient {

    public void invokeRequest(PIMRestClientInterface pimRestClientInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {

        PIMRequest request = makePimRequest(pimRestClientInterface, successListener, errorListener);

        PIMConfiguration.getInstance().getComponent().getRestClientInterface().getRequestQueue().add(request);
    }

    private PIMRequest makePimRequest(PIMRestClientInterface pimRestClientInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        return new PIMRequest(pimRestClientInterface.getMethodType(), pimRestClientInterface.getUrl(), successListener, errorListener, pimRestClientInterface.getHeader());
    }
}
