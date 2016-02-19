package com.philips.cdp.productbuilder;

import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class RegisteredDataBuilder extends RegistrationDataBuilder {

    protected String accessToken;
    private String mServerInfo = "www.philips.com/prx/registration";

    public abstract String getAccessToken();

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract int getMethod();

    public abstract Map<String, String> getParams();

    public abstract Map<String, String> getHeaders();

    public String getServerInfo() {
        return mServerInfo;
    }
}
