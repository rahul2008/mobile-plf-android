/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class UCoreMeasurementGroups {

    @Expose
    @Nullable
    private List<UCoreMeasurementGroups> measurementGroups;

    @Expose
    @Nullable
    private List<UCoreMeasurement> measurements;

    @Expose
    @Nullable
    private List<UCoreMeasurementGroupDetail> details;

    @Nullable
    public List<UCoreMeasurementGroupDetail> getMeasurementGroupDetails() {
        return details;
    }

    public void setDetails(@Nullable final List<UCoreMeasurementGroupDetail> details) {
        this.details = details;
    }

    @Nullable
    public List<UCoreMeasurementGroups> getMeasurementGroups() {
        return measurementGroups;
    }

    public void setMeasurementGroups(@Nullable List<UCoreMeasurementGroups> measurementGroups) {
        this.measurementGroups = measurementGroups;
    }

    @Nullable
    public List<UCoreMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(@Nullable List<UCoreMeasurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurementGroups(final UCoreMeasurementGroups parentUCore) {
        if (measurementGroups == null) {
            measurementGroups = new ArrayList<>();
        }
        this.measurementGroups.add(parentUCore);
    }
}
