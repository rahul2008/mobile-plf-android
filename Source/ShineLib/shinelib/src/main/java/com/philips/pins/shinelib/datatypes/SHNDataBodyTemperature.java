/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Body temperature measurement data.
 * <p/>
 * Contains temperature and location of temperature measurement.
 *
 * @see SHNTemperatureType
 */
public class SHNDataBodyTemperature extends SHNData {
    private final float temperatureInCelsius;
    private final SHNTemperatureType location;

    public SHNDataBodyTemperature(float temperatureInCelsius, SHNTemperatureType location) {
        this.temperatureInCelsius = temperatureInCelsius;
        this.location = location;
    }

    public float getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public SHNTemperatureType getLocation() {
        return location;
    }

    /**
     * @return {@link SHNDataType#BodyTemperature}
     */
    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BodyTemperature;
    }
}
