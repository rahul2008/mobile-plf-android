/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public enum SHNHeightUnit {
    Meter("m"), Inch("in");

    public final String unit;

    SHNHeightUnit(String unit) {
        this.unit = unit;
    }
}
