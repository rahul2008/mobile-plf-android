package com.philips.platform.pim.rest;

import com.philips.platform.pim.models.OIDCConfig;

import java.util.Map;

public class UserProfileRequest implements PimRestClientInterface {

    public UserProfileRequest(OIDCConfig oidcConfig) {
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
