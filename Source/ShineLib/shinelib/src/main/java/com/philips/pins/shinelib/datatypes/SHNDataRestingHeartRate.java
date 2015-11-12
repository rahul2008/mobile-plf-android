package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNDataRestingHeartRate extends SHNData {
    private final int restingHeartRateBpm;
    private final boolean valid;

    public SHNDataRestingHeartRate(int restingHeartRateBpm, boolean valid) {
        this.restingHeartRateBpm = restingHeartRateBpm;
        this.valid = valid;
    }

    public int getRestingHeartRateBpm() {
        return restingHeartRateBpm;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.HeartRateResting;
    }
}
