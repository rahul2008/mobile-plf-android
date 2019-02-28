package com.philips.cdp.prxclient.datamodels.summary;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONObject;

/**
 * Created by philips on 2/28/19.
 */

public class PRXSummaryListResponse extends ResponseData {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     *
     */
    public PRXSummaryListResponse() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public PRXSummaryListResponse(boolean success, Data data) {
        this.success = success;
        this.data = data;
    }

    /**
     *
     * @return
     * The success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public ResponseData parseJsonResponseData(JSONObject response) {
        if (response != null) {
            return new Gson().fromJson(response.toString(), PRXSummaryListResponse.class);
        }
        return null;
    }
}