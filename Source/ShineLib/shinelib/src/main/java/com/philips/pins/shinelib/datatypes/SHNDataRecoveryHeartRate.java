/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataRecoveryHeartRate extends SHNData {
    private final int recoveryHeartRate;
    private final boolean valid;

    public SHNDataRecoveryHeartRate(int recoveryHeartRate, boolean valid) {
        this.recoveryHeartRate = recoveryHeartRate;
        this.valid = valid;
    }

    public int getRecoveryHeartRate() {
        return recoveryHeartRate;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.HeartRateRecovery;
    }
}
