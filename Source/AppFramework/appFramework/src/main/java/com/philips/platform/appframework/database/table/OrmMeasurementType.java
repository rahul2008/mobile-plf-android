/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.MeasurementType;

import java.io.Serializable;

import com.philips.platform.appframework.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMeasurementType implements Serializable{

    final long serialVersionId = 11L;
    
    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private String unit;

    @DatabaseConstructor
    OrmMeasurementType() {
    }

    public OrmMeasurementType(final MeasurementType momentType) {
        this.id = momentType.getId();
        this.description = momentType.getDescription();
        this.unit = momentType.getUnit();
    }

    public MeasurementType getType() {
        return MeasurementType.fromId(id);
    }

    @Override
    public String toString() {
        return "[OrmMeasurementType, id=" + id + ", description=" + description + ", unit=" + unit + "]";
    }
}
