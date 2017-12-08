/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public enum SHNTemperatureUnit {
    Celsius("C"), Fahrenheit("F");

    public final String unit;

    SHNTemperatureUnit(String unit) {
        this.unit = unit;
    }
}
