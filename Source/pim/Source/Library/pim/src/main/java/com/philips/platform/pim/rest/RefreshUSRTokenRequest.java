package com.philips.platform.pim.rest;

import java.util.HashMap;
import java.util.Map;

public class RefreshUSRTokenRequest implements PIMRequestInterface {


    private String refreshUrl;
    private String body;

    public RefreshUSRTokenRequest(String refreshUrl, String body) {
        this.refreshUrl = refreshUrl;
        this.body = body;
    }

    @Override
    public String getUrl() {
        return refreshUrl;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String getBody() {
        return body;
    }


    @Override
    public int getMethodType() {
        return PIMRequest.Method.POST;
    }
}
