package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

/**
 * Created by 310218660 on 11/19/2016.
 */

public class UCoreMeasurementGroupDetail {

    @Expose
    private String value;
    @Expose
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(@NonNull final String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(@NonNull final String type) {
        this.type = type;
    }

}
