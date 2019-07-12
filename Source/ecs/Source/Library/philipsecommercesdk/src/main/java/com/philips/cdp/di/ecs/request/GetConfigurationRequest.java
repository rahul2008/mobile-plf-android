package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;

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
        eCSCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            HybrisConfigResponse resp = new Gson().fromJson(response.toString(),
                    HybrisConfigResponse.class);
            eCSCallback.onResponse(resp);
        }
    }

}
