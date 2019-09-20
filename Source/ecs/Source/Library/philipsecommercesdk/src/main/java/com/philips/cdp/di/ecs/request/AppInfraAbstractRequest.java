package com.philips.cdp.di.ecs.request;

import com.android.volley.Response;
import com.philips.cdp.di.ecs.network.NetworkController;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.Map;

public abstract class AppInfraAbstractRequest implements APPInfraRequest {

    public void executeRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new NetworkController(AppInfraAbstractRequest.this).executeRequest();
            }
        }).start();

    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return null;
    }

    @Override
    public Response.ErrorListener getJSONFailureResponseListener() {
        return this;
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return null;
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