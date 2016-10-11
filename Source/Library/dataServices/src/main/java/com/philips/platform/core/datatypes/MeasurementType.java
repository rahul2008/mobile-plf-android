/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015. // 9164753573
 * All rights reserved.
 */
public enum MeasurementType {
    UNKNOWN(-1, "UNKNOWN", "UnknownUnit"),
    TEMPERATURE(41, "TEMPERATURE", "\u2103") /** Unicode of Degree Celsius */,
    AMOUNT(42, "AMOUNT", "ml"),
    WEIGHT(43, "WEIGHT", "kg"),
    DURATION(44, "DURATION", "seconds"),
    RELATIVE_HUMIDITY(45, "RELATIVEHUMIDITY", "RelativeHumidity"),
    LENGTH(46, "LENGTH", "cm");


    private final int id;
    private final String description;
    private String unit;

    MeasurementType(final int id, final String description, String unit) {
        this.id = id;
        this.description = description;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }
    

    public static MeasurementType fromId(final int id) {
        final MeasurementType[] values = MeasurementType.values();

        for (MeasurementType item : values) {
            if (id == item.getId()) {
                return item;
            }
        }

        return UNKNOWN;
    }
}
