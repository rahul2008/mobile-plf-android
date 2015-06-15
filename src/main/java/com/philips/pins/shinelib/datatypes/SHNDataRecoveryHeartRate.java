package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
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
