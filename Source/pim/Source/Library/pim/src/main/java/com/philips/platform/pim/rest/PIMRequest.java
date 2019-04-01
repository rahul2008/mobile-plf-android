package com.philips.platform.pim.rest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.rest.request.StringRequest;

import java.util.Map;

public class PIMRequest extends StringRequest {
    public PIMRequest(int methodType, String url, Response.Listener<String> successListener, Response.ErrorListener errorListener, Map<String, String> header) {
        super(methodType, url, successListener, errorListener, header, null, null);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }
}
