package com.philips.cdp.prodreg.model;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegistrationResponse extends ResponseData {
    private RegistrationResponseData data;

    private String success;

    public RegistrationResponseData getData() {
        return data;
    }

    public void setData(RegistrationResponseData data) {
        this.data = data;
    }

    public String isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ProdRegResponse [data = " + data + ", success = " + success + "]";
    }

    public ResponseData parseJsonResponseData(JSONObject response) {
        RegistrationResponse registrationResponse;
        registrationResponse = new Gson().fromJson(response.toString(), RegistrationResponse.class);
        return registrationResponse;
    }
}
