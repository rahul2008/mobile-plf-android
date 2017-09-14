/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.philips.platform.appinfra.rest.RestManager;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.Map;

/**
 * A wrapper class of request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public class JsonObjectRequest extends com.android.volley.toolbox.JsonObjectRequest {

    private Map<String, String> mHeader;
    private TokenProviderInterface mProvider;
    private Map<String, String> mParams;


    public JsonObjectRequest(int method, String url, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener, Map<String, String> header,
                             Map<String, String> params,
                             TokenProviderInterface tokenProviderInterface) {
        super(method, url, jsonRequest, listener, errorListener);
        this.mProvider = tokenProviderInterface;
        this.mHeader = header;
        this.mParams = params;
        VolleyLog.DEBUG = false;
//        Log.v(AppInfraLogEventID.AI_REST, "Json Object Request");
    }


    public JsonObjectRequest(int method, String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref,
                             String urlExtension, JSONObject jsonRequest,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), jsonRequest, listener, errorListener);
        VolleyLog.DEBUG = false;
//        Log.v(AppInfraLogEventID.AI_REST, "Json Object Request");
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeader != null) {
            if (mProvider != null) {
                final Map<String, String> tokenHeader = RestManager.setTokenProvider(mProvider);
                mHeader.putAll(tokenHeader);
            }
//            Log.v(AppInfraLogEventID.AI_REST, "Json Object Request get Headers"+mHeader);
            return mHeader;
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams()
            throws AuthFailureError {
        if (mParams != null) {
            return mParams;
        }
        return super.getParams();
    }

}
