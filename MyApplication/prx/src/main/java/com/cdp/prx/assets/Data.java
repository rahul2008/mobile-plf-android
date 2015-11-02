package com.cdp.prx.assets;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("assets")
    @Expose
    private Assets assets;

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param assets
     */
    public Data(Assets assets) {
        this.assets = assets;
    }

    /**
     *
     * @return
     * The assets
     */
    public Assets getAssets() {
        return assets;
    }

    /**
     *
     * @param assets
     * The assets
     */
    public void setAssets(Assets assets) {
        this.assets = assets;
    }

}