package com.philips.platform.pim.rest;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.Map;

public class UserProfileRequest implements PIMRequestInterface {
    private AuthorizationServiceConfiguration mAuthorizationServiceConfiguration;

    public UserProfileRequest(AuthorizationServiceConfiguration pAuthorizationServiceConfiguration) {
        mAuthorizationServiceConfiguration = pAuthorizationServiceConfiguration;
    }

    @Override
    public String getUrl() {
        return mAuthorizationServiceConfiguration.discoveryDoc.getUserinfoEndpoint().toString();
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
