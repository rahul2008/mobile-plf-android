package com.philips.cdp.di.ecs.test;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

import org.json.JSONObject;

public class FetchConfiguration {


    public void fetchConfiguration(ECSCallback<HybrisConfigResponse, Exception> eCSCallback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, new ECSURLBuilder().getRawConfigUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response!=null){
                    HybrisConfigResponse resp = new Gson().fromJson(response.toString(),
                            HybrisConfigResponse.class);
                    eCSCallback.onResponse(resp);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                eCSCallback.onFailure(error,9000);

            }
        }, null, null, null);

        ECSConfig.INSTANCE.getAppInfra().getRestClient().getRequestQueue().add(jsonObjectRequest);

    }
}
