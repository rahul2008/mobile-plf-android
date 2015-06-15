package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNDataRestingHeartRate extends SHNData {
    private final int restingHeartRate;
    private final boolean valid;

    public SHNDataRestingHeartRate(int restingHeartRate, boolean valid) {
        this.restingHeartRate = restingHeartRate;
        this.valid = valid;
    }

    public int getRestingHeartRate() {
        return restingHeartRate;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.HeartRateResting;
    }
}
