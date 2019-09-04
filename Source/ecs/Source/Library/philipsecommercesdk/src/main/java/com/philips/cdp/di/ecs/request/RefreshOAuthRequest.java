/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefreshOAuthRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject> {

    private final ECSCallback<OAuthResponse, Exception> ecsCallback;
    private final OAuthInput oAuthInput;


    public RefreshOAuthRequest(OAuthInput oAuthInput,ECSCallback<OAuthResponse, Exception> ecsListener) {
        this.ecsCallback = ecsListener;
        this.oAuthInput = oAuthInput;
    }

    /*
     * Janrain detail has to be send in request body
     * Note: These janrain details should not be passed in request url as query string
     *
     * */
    private Map getJanrainDetail() {
        Map map = new HashMap<String, String>();
        map.put("grant_type", oAuthInput.getGrantType().getType());
        map.put("refresh_token", oAuthInput.getOAuthID());
        map.put("client_id", oAuthInput.getClientID());
        map.put("client_secret", oAuthInput.getClientSecret());
        return map;
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
        return new ECSURLBuilder().getOauthUrl(oAuthInput);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            OAuthResponse oAuthResponse = new Gson().fromJson(response.toString(),
                    OAuthResponse.class);
            ECSConfig.INSTANCE.setAuthToken(oAuthResponse.getAccessToken());
            ECSConfig.INSTANCE.setAuthResponse(oAuthResponse);
            ecsCallback.onResponse(oAuthResponse);
        }else {
            ecsCallback.onFailure(new Exception(ECSErrorEnum.something_went_wrong.getLocalizedErrorString()),ECSErrorEnum.something_went_wrong.getErrorCode());
        }
    }
    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
