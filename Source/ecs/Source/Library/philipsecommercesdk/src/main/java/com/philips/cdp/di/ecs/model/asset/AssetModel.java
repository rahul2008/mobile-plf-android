package com.philips.cdp.di.ecs.model.asset;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.di.ecs.prx.response.ResponseData;

import org.json.JSONObject;


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
        JSONObject assetResponse = jsonResponse;
        if (assetResponse != null) {
            return new Gson().fromJson(jsonResponse.toString(), AssetModel.class);

        }
        return null;




    }
}