package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 09/07/15.
 */
public class SHNDataBodyTemperature extends SHNData {
    private final float temperatureInCelcius;
    private final SHNTemperatureType location;

    public SHNDataBodyTemperature(float temperatureInCelcius, SHNTemperatureType location) {
        this.temperatureInCelcius = temperatureInCelcius;
        this.location = location;
    }

    public float getTemperatureInCelcius() {
        return temperatureInCelcius;
    }

    public SHNTemperatureType getLocation() {
        return location;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BodyTemperature;
    }
}
