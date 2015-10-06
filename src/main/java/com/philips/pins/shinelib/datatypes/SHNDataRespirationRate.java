package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 05/10/15.
 */
public class SHNDataRespirationRate extends SHNData {
    private final int respirationRateBrpm;

    public SHNDataRespirationRate(int respirationRateBrpm) {
        super();
        this.respirationRateBrpm = respirationRateBrpm;
    }

    public int getRespirationRateBrpm() {
        return respirationRateBrpm;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.RespirationRate;
    }

    @Override
    public String toString() {
        return "RespirationRate: " + getRespirationRateBrpm();
    }
}
