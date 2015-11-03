package com.cdp.prx.assets;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import horizontal.cdp.prxcomponent.ResponseData;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class AssetModel extends ResponseData {
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     *
     */
    public AssetModel() {
    }

    /**
     *
     * @param data
     * @param success
     */
    public AssetModel(boolean success, Data data) {
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
    public ResponseData parseJsonResponseData(JSONObject jsonResponse) {
        AssetModel assetModel = new AssetModel();

        assetModel = new Gson().fromJson(jsonResponse.toString(), AssetModel.class);
        return assetModel;
    }
}