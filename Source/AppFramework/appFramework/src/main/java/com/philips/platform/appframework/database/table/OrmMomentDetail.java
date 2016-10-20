/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;

import java.io.Serializable;

import com.philips.platform.appframework.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMomentDetail implements MomentDetail, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private com.philips.platform.appframework.database.table.OrmMomentDetailType type;

    @DatabaseField(canBeNull = false)
    private String value;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMoment ormMoment;

    @DatabaseConstructor
    OrmMomentDetail() {
    }

    public OrmMomentDetail(final com.philips.platform.appframework.database.table.OrmMomentDetailType type, final OrmMoment ormMoment) {
        this.type = type;
        this.ormMoment = ormMoment;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public MomentDetailType getType() {
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
    public OrmMoment getMoment() {
        return ormMoment;
    }

    @Override
    public String toString() {
        return "[OrmMomentDetail, id=" + id + ", ormMomentDetailType=" + type + ", value=" + value + ", ormMoment=" + ormMoment + "]";
    }
}
