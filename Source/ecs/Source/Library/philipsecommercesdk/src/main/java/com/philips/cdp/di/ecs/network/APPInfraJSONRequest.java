package com.philips.cdp.di.ecs.network;

import com.android.volley.Response;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.Map;

public interface APPInfraJSONRequest extends Response.Listener<JSONObject>,Response.ErrorListener,TokenProviderInterface{

    int getMethod();

    String getURL();

    JSONObject getJSONRequest();

    com.android.volley.Response.Listener<JSONObject> getJSONSuccessResponseListener();

    Response.ErrorListener getJSONFailureResponseListener();

    Map<String, String> getHeader();

    Map<String, String> getParams();

    com.philips.platform.appinfra.rest.TokenProviderInterface getTokenProviderInterface();

}
