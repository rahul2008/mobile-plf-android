package com.philips.cdp.di.ecs.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import java.util.Map;

public class GetConfigurationRequest extends AppInfraAbstractRequest {

    private final ECSCallback<HybrisConfigResponse, Exception> eCSCallback;

    public GetConfigurationRequest(ECSCallback<HybrisConfigResponse, Exception> eCSCallback) {
        this.eCSCallback = eCSCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getRawConfigUrl();
    }




    @Override
    public void onErrorResponse(VolleyError error) {
        eCSCallback.onFailure(new Exception(ECSErrorReason.UNKNOWN_ERROR),3999);
    }

    @Override
    public void onResponse(JSONObject response) {

        if(response!=null){
            if(response.has("catalogId") && response.has("rootCategory") && response.has("siteId")){
                HybrisConfigResponse resp = new Gson().fromJson(response.toString(),
                        HybrisConfigResponse.class);
                eCSCallback.onResponse(resp);
            }else if(response.has("errors")) {
                eCSCallback.onFailure(new Exception(ECSErrorReason.UNSUPPORTED_LOCALE),3001);
            }else if(response.has("net")) {
                eCSCallback.onFailure(new Exception(ECSErrorReason.UNKNOWN_ERROR),3999);
            }
        }else{
            eCSCallback.onFailure(new Exception(ECSErrorReason.UNKNOWN_ERROR),3999);
        }
    }

}
