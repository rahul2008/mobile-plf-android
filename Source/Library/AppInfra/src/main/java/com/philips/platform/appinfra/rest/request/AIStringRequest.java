package com.philips.platform.appinfra.rest.request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by 310243577 on 10/26/2016.
 */

public class AIStringRequest extends StringRequest {

    public AIStringRequest(int method, String url, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(method, url, listener, errorListener);
        if (!url.contains("https")) {
            throw new HttpForbiddenException();
        }
    }

    public AIStringRequest(String url, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(url, listener, errorListener);
        if (!url.contains("https")) {
            throw new HttpForbiddenException();
        }
    }
}
