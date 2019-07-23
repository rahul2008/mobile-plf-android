package com.philips.cdp.di.ecs.request;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class GetCartsRequest extends AppInfraAbstractRequest {


    @Override
    public int getMethod() {
        return 0;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
