/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;
import cdp.philips.com.mydemoapp.database.datatypes.MeasurementType;

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

    @DatabaseField(canBeNull = true)
    private String description;

    @DatabaseField(canBeNull = true)
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
