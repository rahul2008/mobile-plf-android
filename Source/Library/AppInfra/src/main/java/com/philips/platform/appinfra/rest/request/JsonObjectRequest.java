/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;

import org.json.JSONObject;


public class JsonObjectRequest extends com.android.volley.toolbox.JsonObjectRequest {

    public JsonObjectRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener)  {
        super(method, url, jsonRequest, listener, errorListener);
    }


    public JsonObjectRequest(int method, String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref, String urlExtension, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)  {
        super(method, ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), jsonRequest, listener, errorListener);
    }
}
