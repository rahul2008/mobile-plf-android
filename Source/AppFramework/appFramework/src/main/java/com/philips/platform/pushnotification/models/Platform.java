package com.philips.platform.pushnotification.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by philips on 29/03/17.
 */

public class Platform {

    @SerializedName("dsc")
    private DataServiceModel dataServiceModel;

    public DataServiceModel getDataServiceModel() {
        return dataServiceModel;
    }

    public void setDataServiceModel(DataServiceModel dataServiceModel) {
        this.dataServiceModel = dataServiceModel;
    }
}
