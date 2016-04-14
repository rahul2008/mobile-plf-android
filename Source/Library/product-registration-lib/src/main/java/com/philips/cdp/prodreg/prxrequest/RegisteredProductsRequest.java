package com.philips.cdp.prodreg.prxrequest;

import android.net.Uri;
import android.util.Log;

import com.philips.cdp.prodreg.handler.ProdRegConstants;
import com.philips.cdp.prodreg.model.RegisteredResponse;
import com.philips.cdp.prxclient.RequestType;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProductsRequest extends PrxRequest {

    private String accessToken;
    private String mServerInfo="https://acc.philips.com/prx/registration.registeredProducts";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ResponseData getResponseData(JSONObject jsonObject) {
        return new RegisteredResponse().parseJsonResponseData(jsonObject);
    }

    @Override
    public String getServerInfo() {
        String mConfiguration =  RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://acc.philips.com/prx/registration.registeredProducts";
        } else if (mConfiguration.equalsIgnoreCase("development")) {
            mServerInfo = "https://dev.philips.com/prx/registration.registeredProducts";
        }
        return mServerInfo;
    }

    @Override
    public String getRequestUrl() {
        return generateUrl();
    }

    @Override
    public int getRequestType() {
        return RequestType.GET.getValue();
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ProdRegConstants.ACCESS_TOKEN_TAG, getAccessToken());
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    private String generateUrl() {
        Uri builtUri = Uri.parse(getServerInfo())
                .buildUpon()
                .build();
        Log.d(getClass() + "", builtUri.toString());
        return builtUri.toString();
    }
}
