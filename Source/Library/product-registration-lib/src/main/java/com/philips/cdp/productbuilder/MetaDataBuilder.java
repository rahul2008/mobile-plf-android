package com.philips.cdp.productbuilder;

import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class MetaDataBuilder extends PrxDataBuilder {

    protected String accessToken;
    private String mServerInfo = "https://acc.philips.co.uk/prx/registration";

    public abstract String getAccessToken();

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public abstract ResponseData getResponseData(JSONObject jsonObject);

    public abstract String getRequestUrl();

    public String getServerInfo() {
        return mServerInfo;
    }
}
