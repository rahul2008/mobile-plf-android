package com.philips.platform.pim.rest;

import com.android.volley.Request;

import net.openid.appauth.AuthState;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to create user profile request
 */
public class UserProfileRequest implements PIMRequestInterface {
    private AuthState mAuthState;

    public UserProfileRequest(AuthState mAuthState) {
        this.mAuthState = mAuthState;
    }

    @Override
    public String getUrl() {
        return mAuthState.getLastAuthorizationResponse().request.configuration.discoveryDoc.getUserinfoEndpoint().toString();
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + mAuthState.getAccessToken());
        return headers;
    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public int getMethodType() {
        return Request.Method.GET;
    }
}
