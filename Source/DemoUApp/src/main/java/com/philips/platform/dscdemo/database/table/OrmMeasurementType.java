/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import java.io.Serializable;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMeasurementType implements Serializable {

    final long serialVersionId = 11L;
    static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private String unit;

    @DatabaseConstructor
    OrmMeasurementType() {
    }

    public OrmMeasurementType(final int id, final String momentType, final String unit) {
        this.id = id;
        this.description = momentType;
        this.unit = unit;
    }

    public String getType() {
        return description;
    }

    @Override
    public String toString() {
        return "[OrmMeasurementType, id=" + id + ", description=" + description + ", unit=" + unit + "]";
    }
}
