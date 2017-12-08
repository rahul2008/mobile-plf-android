/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Heart rate measurement data in beats per minute.
 *
 * @publicApi
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

    /**
     * @return {@link SHNDataType#HeartRate}
     */
    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.HeartRate;
    }

    @Override
    public String toString() {
        return "HeartRate: " + getHeartRate() + " valid: " + isValid();
    }
}
