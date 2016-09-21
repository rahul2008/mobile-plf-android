package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class PlatformRequest {

    public abstract void executeRequest(Context context);


    public abstract int getRequestType();

    public abstract String getUrl();


    public abstract Response.Listener<JSONObject> getResponseListener();

    public abstract Response.ErrorListener getErrorResponseListener();
}
