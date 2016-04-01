package com.philips.cdp.prodreg.model;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegResponse extends ResponseData {
    private ProdRegData data;

    private String success;

    public ProdRegData getData() {
        return data;
    }

    public void setData(ProdRegData data) {
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
        ProdRegResponse prodRegResponse;
        prodRegResponse = new Gson().fromJson(response.toString(), ProdRegResponse.class);
        return prodRegResponse;
    }
}
