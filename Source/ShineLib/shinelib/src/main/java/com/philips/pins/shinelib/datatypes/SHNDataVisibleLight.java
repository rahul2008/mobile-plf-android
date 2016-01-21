package com.philips.pins.shinelib.datatypes;

public class SHNDataVisibleLight extends SHNData{
    private final int visibleLight;

    public SHNDataVisibleLight(int visibleLight) {

        this.visibleLight = visibleLight;
    }

    public int getVisibleLight() {
        return visibleLight;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.VisibleLight;
    }

    @Override
    public String toString() {
        return "VisibleLight: " + getVisibleLight();
    }
}
