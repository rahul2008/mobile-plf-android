package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 09/07/15.
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

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BodyTemperature;
    }
}
