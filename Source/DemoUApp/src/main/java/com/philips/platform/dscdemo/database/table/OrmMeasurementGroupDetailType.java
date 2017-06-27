package com.philips.platform.dscdemo.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import java.io.Serializable;


/**
 * Created by 310218660 on 11/17/2016.
 */

public class OrmMeasurementGroupDetailType implements Serializable {
    private static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseConstructor
    OrmMeasurementGroupDetailType() {
    }

    public OrmMeasurementGroupDetailType(final int id, final String momentType) {
        this.id = id;
        this.description = momentType;
    }

    public String getType() {
        return description;
    }

    @Override
    public String toString() {
        return "[OrmMeasurementDetailType, id=" + id + ", description=" + description + "]";
    }
}
