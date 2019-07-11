/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;

import com.google.gson.Gson;
import com.philips.cdp.di.ecs.model.response.OAuthResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;

import java.util.HashMap;
import java.util.Map;

public class OAuthRequest extends AbstractRequestModel  {
    OAuthResponse mOAuthResponse;


    @Override
    public Object parseResponse(final Object response) {
        mOAuthResponse = new Gson().fromJson(response.toString(), OAuthResponse.class);
        return mOAuthResponse;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        return getJanrainDetail();
    }

    @Override
    public String getUrl() {

        return new ECSURLBuilder().getOauthUrl();
    }

    /*
    * Janrain detail has to be send in request body
    * Note: These janrain details should not be passed in request url as query string
    *
    * */
    private Map getJanrainDetail(){
        Map map = new HashMap<String,String>();
       /* if(store.getUser().getJanRainID()!=null)
        map.put("janrain",store.getUser().getJanRainID());
        map.put("grant_type","janrain");
        map.put("client_id","mobile_android");
        map.put("client_secret","secret");*/
        return  map;
    }
}
