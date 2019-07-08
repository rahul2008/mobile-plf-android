/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.oauth.OAuthResponse;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class RefreshOAuthRequest extends AbstractModel {

    public RefreshOAuthRequest(final StoreListener store, final Map<String, String> query) {
        super(store, query);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), OAuthResponse.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put("grant_type", "refresh_token");
        bodyParams.put("refresh_token", params.get(ModelConstants.REFRESH_TOKEN));
        bodyParams.put("client_id", "mobile_android");
        bodyParams.put("client_secret", "secret");
        return bodyParams;
    }

    @Override
    public String getUrl() {
        return store.getOauthRefreshUrl();
    }
}
