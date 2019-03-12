package com.philips.platform.pim.rest;

import com.android.volley.Response;
import com.philips.platform.pim.configration.PimConfiguration;

public class PimRestClient {

    public void invokeRequest(PimRestClientInterface pimRestClientInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {

        PimRequest request = makePimRequest(pimRestClientInterface, successListener, errorListener);

        PimConfiguration.getInstance().getComponent().getRestClientInterface().getRequestQueue().add(request);
    }

    private PimRequest makePimRequest(PimRestClientInterface pimRestClientInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        return new PimRequest(pimRestClientInterface.getMethodType(), pimRestClientInterface.getUrl(), successListener, errorListener, pimRestClientInterface.getHeader());
    }
}
