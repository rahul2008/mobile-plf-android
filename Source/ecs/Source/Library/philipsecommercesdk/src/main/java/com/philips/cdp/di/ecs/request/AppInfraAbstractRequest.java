package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.philips.cdp.di.ecs.network.NetworkController;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.Map;

public abstract class AppInfraAbstractRequest implements APPInfraJSONRequest {

    public void executeRequest(){
        new NetworkController(this).executeRequest();
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
    public JSONObject getJSONRequest() {
        return null;
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
    public Token getToken() {
        return null;
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return null;
    }


}
