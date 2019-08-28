/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

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
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        Exception exception = new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR);
        RegionsList regionsList=null;
        String ErrorMessage="";
        try {
            if(null!=response && null!=response.toString() ){
                ErrorMessage=response.toString();
            }
            regionsList = new Gson().fromJson(response.toString(), RegionsList.class);
        }catch(Exception e){
            exception=e;
        }
        if(null!=regionsList && null!=regionsList.getRegions() && regionsList.getRegions().size()>0) {
            ecsCallback.onResponse(regionsList);
        }else{
            ecsCallback.onFailure(exception, 9000);
        }
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
