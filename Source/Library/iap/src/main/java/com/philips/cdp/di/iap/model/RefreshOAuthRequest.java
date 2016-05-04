/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;


public class RefreshOAuthRequest extends AbstractModel {

    public RefreshOAuthRequest(final Store store, final Map<String, String> query) {
        super(store, query);
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        HashMap<String, String> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", params.get(ModelConstants.REFRESH_TOKEN));
        body.put("client_id", "mobile_android");
        body.put("client_secret", "secret");
        return body;
    }

    @Override
    public String getUrl() {
        return store.getOauthRefreshUrl();
    }
}
