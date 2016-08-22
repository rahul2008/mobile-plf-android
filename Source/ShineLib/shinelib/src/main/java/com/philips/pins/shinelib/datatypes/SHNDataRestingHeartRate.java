/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

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
