package com.philips.cdp.prodreg.model.metadata;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataResponse extends ResponseData {

    private ProductMetadataResponseData data;

    private String success;

    public ProductMetadataResponseData getData() {
        return data;
    }

    public void setData(ProductMetadataResponseData data) {
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
        ProductMetadataResponse productMetaData;
        productMetaData = new Gson().fromJson(response.toString(), ProductMetadataResponse.class);
        return productMetaData;
    }
}
