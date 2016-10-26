/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.response.oauth;

public class OAuthResponse {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }
}