package com.philips.platform.pim.rest;

import com.android.volley.Response;

public class PIMRestClient {

    public void invokeRequest(PIMRequestInterface pimRequestInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {

        PIMRequest request = makePimRequest(pimRequestInterface, successListener, errorListener);

//        PIMConfiguration.getInstance().getComponent().getRestClientInterface().getRequestQueue().add(request);
    }

    private PIMRequest makePimRequest(PIMRequestInterface pimRequestInterface, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        return new PIMRequest(pimRequestInterface.getMethodType(), pimRequestInterface.getUrl(), successListener, errorListener, pimRequestInterface.getHeader());
    }
}
