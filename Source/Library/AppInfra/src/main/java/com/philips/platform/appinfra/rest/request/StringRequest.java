package com.philips.platform.appinfra.rest.request;

import com.android.volley.Response;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;

/**
 * Created by 310243577 on 11/3/2016.
 */

public class StringRequest extends com.android.volley.toolbox.StringRequest {
    public StringRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

    }

    public StringRequest(int method, String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref,
                         String urlExtension, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), listener, errorListener);
    }

}
