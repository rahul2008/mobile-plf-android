/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UCoreMeasurement {

    @Expose
    private String timestamp;
    @Expose
    private String value;
    @Expose
    private String type;
    @Expose
    private String unit;

    @Expose
    @Nullable
    private List<UCoreDetail> details;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(@NonNull final String type) {
        this.type = type;
    }

    @Nullable
    public List<UCoreDetail> getDetails() {
        return details;
    }

    public void setDetails(@Nullable final List<UCoreDetail> details) {
        this.details = details;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
