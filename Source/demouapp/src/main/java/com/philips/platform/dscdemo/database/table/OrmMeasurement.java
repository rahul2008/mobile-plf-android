/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database.table;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.dscdemo.database.EmptyForeignCollection;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMeasurement implements Measurement, Serializable {
    private static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true, unique = true,canBeNull = false)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private OrmMeasurementType type;

    @DatabaseField
    private String value;

    @DatabaseField
    private String unit;

    @DatabaseField(canBeNull = false)
    private DateTime dateTime = new DateTime();

   /* @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMoment ormMoment;*/

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMeasurementGroup ormMeasurementGroup;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurementDetail> ormMeasurementDetails = new EmptyForeignCollection<>();

    @DatabaseConstructor
    OrmMeasurement() {
    }

   /* public OrmMeasurement(final OrmMeasurementType type, final OrmMoment ormMoment) {
        this.type = type;
        this.ormMoment = ormMoment;
    }*/

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
