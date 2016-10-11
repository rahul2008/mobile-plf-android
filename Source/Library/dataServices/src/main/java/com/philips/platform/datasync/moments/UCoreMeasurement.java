/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UCoreMeasurement {

    @Expose
    private String timestamp;
    @Expose
    private double value;
    @Expose
    private String type;

    @Expose
    @Nullable
    private List<UCoreDetail> details;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull final String timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(final double value) {
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
}
