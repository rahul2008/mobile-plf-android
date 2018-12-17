/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.models;

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
