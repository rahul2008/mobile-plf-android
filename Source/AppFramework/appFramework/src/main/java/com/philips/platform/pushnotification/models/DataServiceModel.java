package com.philips.platform.pushnotification.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by philips on 29/03/17.
 */

public class DataServiceModel {
    @SerializedName("dataSync")
    private String dataSync;

    public String getDataSync() {
        return dataSync;
    }

    public void setDataSync(String dataSync) {
        this.dataSync = dataSync;
    }
}
