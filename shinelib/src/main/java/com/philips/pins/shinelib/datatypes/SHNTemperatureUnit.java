package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 04/06/15.
 */
public enum SHNTemperatureUnit {
    Celsius("C"), Fahrenheit("F");

    public final String unit;

    SHNTemperatureUnit(String unit) {
        this.unit = unit;
    }
}
