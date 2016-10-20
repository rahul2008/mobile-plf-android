/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.MomentDetailType;

import java.io.Serializable;

import com.philips.platform.appframework.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmMomentDetailType implements Serializable {
    static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseConstructor
    OrmMomentDetailType() {
    }

    public OrmMomentDetailType(final MomentDetailType momentType) {
        this.id = momentType.getId();
        this.description = momentType.getDescription();
    }

    public MomentDetailType getType() {
        return MomentDetailType.fromId(id);
    }

    @Override
    public String toString() {
        return "[OrmMomentDetailType, id=" + id + ", description=" + description + "]";
    }
}
