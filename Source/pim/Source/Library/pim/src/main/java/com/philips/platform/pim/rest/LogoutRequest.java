package com.philips.platform.pim.rest;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.Map;

public class LogoutRequest implements PIMRequestInterface {
    private AuthorizationServiceConfiguration mAuthorizationServiceConfiguration;

    public LogoutRequest(AuthorizationServiceConfiguration pAuthorizationServiceConfiguration) {
        mAuthorizationServiceConfiguration = pAuthorizationServiceConfiguration;
    }

    @Override
    public String getUrl() {
        return mAuthorizationServiceConfiguration.discoveryDoc.getIssuer() + "/token/revoke";
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
