package com.philips.platform.appframework.testmicroappfw.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by philips on 13/02/17.
 */

public class CommonComponent implements Serializable{
    @SerializedName("coconame")
    private String cocoName;

    public String getCocoName() {
        return cocoName;
    }

    public void setCocoName(String cocoName) {
        this.cocoName = cocoName;
    }
}
