package com.pins.philips.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNLogItemDataHeartRate implements SHNLogItemData {
    private final int heartRate;
    private final boolean valid;

    public SHNLogItemDataHeartRate(int heartRate, boolean valid) {
        this.heartRate = heartRate;
        this.valid = valid;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public boolean isValid() {
        return valid;
    }
}
