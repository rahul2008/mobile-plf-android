/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum MeasurementDetailType {
    UNKNOWN(-1, "UNKNOWN"),
    LOCATION(75, "LOCATION"),
    BODY_PART(70, "BODY_PART"),;


    private final int id;
    private final String description;

    MeasurementDetailType(final int id, final String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static MeasurementDetailType fromId(final int id) {
        final MeasurementDetailType[] values = MeasurementDetailType.values();

        for (MeasurementDetailType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
