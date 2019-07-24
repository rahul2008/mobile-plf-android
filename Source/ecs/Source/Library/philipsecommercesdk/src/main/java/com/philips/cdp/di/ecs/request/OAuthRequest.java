/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequest extends AppInfraAbstractRequest  {

    private final ECSCallback<OAuthResponse,Exception> ecsCallback;
    private final  OAuthInput oAuthInput;
    String janrainID ;

    public OAuthRequest(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsListener) {
        this.ecsCallback = ecsListener;
        this.oAuthInput = oAuthInput;
        janrainID = oAuthInput.getJanRainID();
    }

    /*
    * Janrain detail has to be send in request body
    * Note: These janrain details should not be passed in request url as query string
    *
    * */
    private Map getJanrainDetail(){
        Map map = new HashMap<String,String>();
        if(janrainID!=null)
        map.put("janrain",janrainID);
        map.put("grant_type",oAuthInput.getGrantType());
        map.put("client_id",oAuthInput.getClientID());
        map.put("client_secret",oAuthInput.getClientSecret());
        return  map;
    }

    @Override
    public Map<String, String> getParams() {
        return getJanrainDetail();
    }

    @Override
    public Map<String, String> getHeader() {
        return getJanrainDetail();
    }

    @Override
    public JSONObject getJSONRequest() {
        return new JSONObject(getJanrainDetail());
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getOauthUrl(oAuthInput.getJanRainID());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            OAuthResponse oAuthResponse = new Gson().fromJson(response.toString(),
                    OAuthResponse.class);
            ECSConfig.INSTANCE.setAuthToken( oAuthResponse.getAccessToken());
            ecsCallback.onResponse(oAuthResponse);
        }
    }
}
