package com.philips.platform.pim.rest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.philips.platform.appinfra.rest.request.StringRequest;

import java.util.Map;

public class PIMRequest extends StringRequest {
    private String mBody;
    public PIMRequest(int methodType, String url,String body, Response.Listener<String> successListener, Response.ErrorListener errorListener, Map<String, String> header) {
        super(methodType, url, successListener, errorListener, header, null, null);
        mBody = body;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody.getBytes();
    }
}
