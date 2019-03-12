package com.philips.platform.pim.rest;

import com.philips.platform.appinfra.rest.request.StringRequest;
import com.android.volley.Response;

import java.util.Map;

public class PimRequest extends StringRequest {
    public PimRequest(int methodType, String url, Response.Listener<String> successListener, Response.ErrorListener errorListener, Map<String, String> header) {
        super(methodType, url, successListener, errorListener, header, null, null);
    }



}
