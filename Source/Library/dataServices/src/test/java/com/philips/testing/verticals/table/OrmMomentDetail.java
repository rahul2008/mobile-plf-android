/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.MomentDetail;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmMomentDetail implements MomentDetail, Serializable {

    private static final long serialVersionUID = 11L;

    private int id;

    private OrmMomentDetailType type;

    private String value;

    private OrmMoment ormMoment;


    public OrmMomentDetail(final OrmMomentDetailType type, final OrmMoment ormMoment) {
        this.type = type;
        this.ormMoment = ormMoment;
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
    public OrmMoment getMoment() {
        return ormMoment;
    }

    @Override
    public String toString() {
        return "[OrmMomentDetail, id=" + id + ", ormMomentDetailType=" + type + ", value=" + value + ", ormMoment=" + ormMoment + "]";
    }
}
