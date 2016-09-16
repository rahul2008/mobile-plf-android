package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetMomentRequest extends PlatformRequest {

    @Override
    public void executeRequest(Context context) {

    }


    @Override
    public int getRequestType() {

        return 0;
    }

    @Override
    public String getUrl() {
        return null;
    }


    @Override
    public Response.Listener<JSONObject> getResponseListener() {
        return null;
    }

    @Override
    public Response.ErrorListener getErrorResponseListener() {
        return null;
    }
}
