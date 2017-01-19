/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivity.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.philips.cdp.registration.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class PlatformRequest {

    public abstract void executeRequest(Context context);


    public abstract int getRequestType();

    public abstract String getUrl();


    public abstract Response.Listener<JSONObject> getResponseListener();

    public abstract Response.ErrorListener getErrorResponseListener();

    protected Map<String, String> getMomentHeader(final User user) throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("authorization", "bearer " + user.getHsdpAccessToken());
        headers.put("performerid", user.getHsdpUUID());
        headers.put("api-version", "7");
        headers.put("appagent", "PlatformInfra Postman");
        headers.put("cache-control", "no-cache");
        headers.put("postman-token", "ba0eff76-322f-9fa3-effe-97e8b0e09e93");
        return headers;
    }
}
