package com.philips.cdp.di.ecs.model.asset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class Assets {

    @SerializedName("asset")
    @Expose
    private List<Asset> asset = new ArrayList<Asset>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Assets() {
    }

    /**
     *
     * @param asset
     */
    public Assets(List<Asset> asset) {
        this.asset = asset;
    }

    /**
     *
     * @return
     * The asset
     */
    public List<Asset> getAsset() {
        return asset;
    }

    /**
     *
     * @param asset
     * The asset
     */
    public void setAsset(List<Asset> asset) {
        this.asset = asset;
    }

}