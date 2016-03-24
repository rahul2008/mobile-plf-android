package com.philips.cdp.model;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegMetaData extends ResponseData {

    private ProdRegMetaDataResponse data;

    private String success;

    public ProdRegMetaDataResponse getData() {
        return data;
    }

    public void setData(ProdRegMetaDataResponse data) {
        this.data = data;
    }

    public String isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ClassPojo [data = " + data + ", success = " + success + "]";
    }

    public ResponseData parseJsonResponseData(JSONObject response) {
        ProdRegMetaData productMetaData;
        productMetaData = new Gson().fromJson(response.toString(), ProdRegMetaData.class);
        return productMetaData;
    }
}
