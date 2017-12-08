package com.philips.pins.shinelib.datatypes;

public class SHNDataUVLight extends SHNData{
    private final int uvLight;

    public SHNDataUVLight(int uvLight) {

        this.uvLight = uvLight;
    }

    public int getUvLight() {
        return uvLight;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.UVLight;
    }

    @Override
    public String toString() {
        return "UVLight: " + getUvLight();
    }
}
