/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum SHNWeightUnit {
    KG("kg"), LB("lb");

    public final String unit;

    SHNWeightUnit(String unit) {
        this.unit = unit;
    }
}
