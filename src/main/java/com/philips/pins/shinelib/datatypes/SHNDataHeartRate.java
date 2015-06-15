package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNDataHeartRate extends SHNData {
    private final int heartRate;
    private final boolean valid;

    public SHNDataHeartRate(int heartRate, boolean valid) {
        this.heartRate = heartRate;
        this.valid = valid;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.HeartRate;
    }
}
