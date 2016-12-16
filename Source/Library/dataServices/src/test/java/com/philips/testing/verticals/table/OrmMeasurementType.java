/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.testing.verticals.table;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmMeasurementType implements Serializable {

    final long serialVersionId = 11L;
    static final long serialVersionUID = 11L;

    private int id;

    private String description;

    private String unit;


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
