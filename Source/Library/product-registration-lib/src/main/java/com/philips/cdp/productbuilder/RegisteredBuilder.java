package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.philips.cdp.model.RegisteredDataResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredBuilder extends RegisteredDataBuilder {

    public RegisteredBuilder(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new RegisteredDataResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo() + ".registeredProducts")
                .buildUpon()
                .build();
        Log.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }
}
