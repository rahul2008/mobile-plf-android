/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TrackerDetails {

    public enum Type {
        DATETIME, DURATION
    }

    @NonNull
    private final List<Type> typeList;

    @NonNull
    private DateTime dateTime = DateTime.now();

    private int duration;

    public TrackerDetails(final Type... typeList) {
        this.typeList = Arrays.asList(typeList);
    }

    @NonNull
    public List<Type> getTypeList() {
        return typeList;
    }

    @NonNull
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NonNull final DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void applyDuration(@NonNull final DataServicesManager tracker, @NonNull final Moment moment) {
        applyDuration(tracker, moment, dateTime);
    }

    public void applyDuration(@NonNull final DataServicesManager tracker, @NonNull final Moment moment, @NonNull final DateTime dateTime) {
        if (getDuration() > 0) {
            Measurement measurement = tracker.createMeasurement(MeasurementType.DURATION, moment);
            measurement.setDateTime(dateTime);
            measurement.setValue(duration);
        }
    }
}
