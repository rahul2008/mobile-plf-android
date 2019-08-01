/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;

import org.json.JSONObject;


public class GetRegionsRequest extends OAuthAppInfraAbstractRequest  implements Response.Listener<JSONObject>{

    private final ECSCallback<RegionsList,Exception> ecsCallback;

    public GetRegionsRequest(ECSCallback<RegionsList, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }


    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getRegionsUrl();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error,"Error Fetching region",9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        RegionsList regionsList = new Gson().fromJson(response.toString(), RegionsList.class);
        ecsCallback.onResponse(regionsList);
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
