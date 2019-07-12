package com.philips.cdp.di.ecs.network;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

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
    public JSONObject getJSONRequest() {
        return null;
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }

    @Override
    public Response.ErrorListener getJSONFailureResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return this;
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

    @Override
    public Token getToken() {
        return null;
    }
}
