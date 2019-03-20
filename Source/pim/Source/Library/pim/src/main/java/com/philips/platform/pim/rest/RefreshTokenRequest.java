package com.philips.platform.pim.rest;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.Map;

public class RefreshTokenRequest implements PIMRequestInterface {
    private AuthorizationServiceConfiguration mAuthorizationServiceConfiguration;

    public RefreshTokenRequest(AuthorizationServiceConfiguration pAuthorizationServiceConfiguration) {
        mAuthorizationServiceConfiguration = pAuthorizationServiceConfiguration;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Map<String, String> getHeader() {
        return null;
    }

    @Override
    public byte[] getBody() {
        return null;
    }

    @Override
    public int getMethodType() {
        return 0;
    }

}
