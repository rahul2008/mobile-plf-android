/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.MeasurementDetail;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmMeasurementDetail implements MeasurementDetail, Serializable {

    private static final long serialVersionUID = 11L;

    private int id;

    private OrmMeasurementDetailType type;

    private String value;

    private OrmMeasurement ormMeasurement;


    public OrmMeasurementDetail(final OrmMeasurementDetailType type, final OrmMeasurement ormMeasurement) {
        this.type = type;
        this.ormMeasurement = ormMeasurement;
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
    public OrmMeasurement getMeasurement() {
        return ormMeasurement;
    }

    @Override
    public String toString() {
        return "[OrmMeasurementDetail, id=" + id + ", ormMeasurementDetailType=" + type + ", value=" + value + ", ormMeasurement=" + ormMeasurement + "]";
    }
}
