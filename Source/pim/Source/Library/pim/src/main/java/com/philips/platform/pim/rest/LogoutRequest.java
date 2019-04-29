package com.philips.platform.pim.rest;

import com.android.volley.Request;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthState;

import java.util.HashMap;
import java.util.Map;

public class LogoutRequest implements PIMRequestInterface {
    private AuthState mAuthState;

    public LogoutRequest(AuthState authState) {
        mAuthState = authState;
    }

    @Override
    public String getUrl() {
        return mAuthState.getLastAuthorizationResponse().request.configuration.discoveryDoc.getIssuer() + "/token/revoke";
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", mAuthState.getAccessToken());
        headers.put("token_type_hint", "access_token");
        headers.put("client_id", PIMSettingManager.getInstance().getPimOidcConfigration().getClientId());
        headers.put("client_secret", mAuthState.getClientSecret());
        return headers;
    }

    @Override
    public byte[] getBody() {
        return null;
    }

    @Override
    public int getMethodType() {
        return Request.Method.POST;
    }

}
