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
public class TestMomentDetail implements MomentDetail, Serializable {

    private static final long serialVersionUID = 11L;

    private int id;

    private TestMomentDetailType type;

    private String value;

    private TestMoment testMoment;


    public TestMomentDetail(final TestMomentDetailType type, final TestMoment testMoment) {
        this.type = type;
        this.testMoment = testMoment;
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
    public TestMoment getMoment() {
        return testMoment;
    }

    @Override
    public String toString() {
        return "[TestMomentDetail, id=" + id + ", ormMomentDetailType=" + type + ", value=" + value + ", testMoment=" + testMoment + "]";
    }
}
