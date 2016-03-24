package com.philips.cdp.model;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegisteredDataResponse extends ResponseData {

    private ProdRegRegisteredResults[] results;

    private String result_count;

    private String stat;

    public ProdRegRegisteredResults[] getResults() {
        return results;
    }

    public void setResults(ProdRegRegisteredResults[] results) {
        this.results = results;
    }

    public String getResult_count() {
        return result_count;
    }

    public void setResult_count(String result_count) {
        this.result_count = result_count;
    }

    public String isSuccess() {
        return stat;
    }

    @Override
    public String toString() {
        return "ClassPojo [results = " + results + ", result_count = " + result_count + ", stat = " + stat + "]";
    }

    public ResponseData parseJsonResponseData(JSONObject response) {
        ProdRegRegisteredDataResponse registeredData;
        registeredData = new Gson().fromJson(response.toString(), ProdRegRegisteredDataResponse.class);
        return registeredData;
    }
}
