/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.oauth.OAuthResponse;
import com.ecs.demouapp.ui.session.OAuthListener;
import com.ecs.demouapp.ui.session.RequestListener;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.IAPLog;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class OAuthRequest extends AbstractModel implements OAuthListener {
    OAuthResponse mOAuthResponse;

    public OAuthRequest(final StoreListener store, final Map<String, String> query) {
        super(store, query);
    }

    @Override
    public Object parseResponse(final Object response) {
        mOAuthResponse = new Gson().fromJson(response.toString(), OAuthResponse.class);
        return mOAuthResponse;
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "POST");
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        return getJanrainDetail();
    }

    @Override
    public String getUrl() {
        IAPLog.d(IAPLog.LOG, "Request URL = " + store.getOauthUrl());
        return store.getOauthUrl();
    }

    @Override
    public String getAccessToken() {
        if (mOAuthResponse == null) {
            return null;
        }
        return mOAuthResponse.getAccessToken();
    }

    @Override
    public void refreshToken(final RequestListener listener) {

    }

    @Override
    public void resetAccessToken() {
        mOAuthResponse = null;
    }

    public String getrefreshToken() {
        if (mOAuthResponse == null) {
            return ""; //Avoid NPE in Volley
        }
        return mOAuthResponse.getRefreshToken();
    }

    /*
    * Janrain detail has to be send in request body
    * Note: These janrain details should not be passed in request url as query string
    *
    * */
    private Map getJanrainDetail(){
        Map map = new HashMap<String,String>();
        if(store.getUser().getJanRainID()!=null)
        map.put("janrain",store.getUser().getJanRainID());
        map.put("grant_type","janrain");
        map.put("client_id","mobile_android");
        map.put("client_secret","secret");
        return  map;
    }
}
