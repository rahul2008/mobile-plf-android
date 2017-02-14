/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.testing.verticals.table;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmMeasurement implements Measurement, Serializable {
    private static final long serialVersionUID = 11L;

    private int id;

    private OrmMeasurementType type;

    private String value;

    private String unit;

    private DateTime dateTime = new DateTime();

    private OrmMeasurementGroup ormMeasurementGroup;

    List<OrmMeasurementDetail> ormMeasurementDetails = new ArrayList<>();

    public OrmMeasurement(final OrmMeasurementType type, final OrmMeasurementGroup ormMeasurementGroup) {
        this.type = type;
        this.ormMeasurementGroup = ormMeasurementGroup;
        this.id = -1;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getType() {
        return type.getType();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public DateTime getDateTime() {
        return (dateTime);
    }

    @Override
    public void setDateTime(final @NonNull DateTime date) {
        this.dateTime = date;
    }

    @Override
    public Collection<? extends OrmMeasurementDetail> getMeasurementDetails() {
        return ormMeasurementDetails;
    }

    @Override
    public void addMeasurementDetail(final MeasurementDetail measurementDetail) {
        ormMeasurementDetails.add((OrmMeasurementDetail) measurementDetail);
    }

   /* @Override
    public OrmMoment getMoments() {
        return ormMoment;
    }*/

    @Override
    public OrmMeasurementGroup getMeasurementGroup() {
        return ormMeasurementGroup;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "[OrmMeasurement, id=" + id + ", OrmMeasurementType=" + type + ", value=" + value + ", dateTime=" + dateTime + ", ormMoment=" + ormMeasurementGroup + "]";
    }
}
