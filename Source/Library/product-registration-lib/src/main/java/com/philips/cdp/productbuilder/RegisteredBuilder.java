package com.philips.cdp.productbuilder;

import android.net.Uri;
import android.util.Log;

import com.philips.cdp.model.RegisteredDataResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredBuilder extends RegistrationDataBuilder {

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
    public String getServerInfo() {
        return "https://dev.philips.com/prx/registration.registeredProducts";
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .build();
        Log.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }
}
