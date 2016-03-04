package com.philips.cdp.model;

import com.google.gson.Gson;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredDataResponse extends ResponseData {

    private Results[] results;

    private String result_count;

    private String stat;

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
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
        RegisteredDataResponse registeredData;
        registeredData = new Gson().fromJson(response.toString(), RegisteredDataResponse.class);
        return registeredData;
    }
}
