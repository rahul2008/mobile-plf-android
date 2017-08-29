package com.philips.platform.appframework;

/**
 * Created by philips on 8/24/17.
 */

public enum ConnectivityDeviceType {
    POWER_SLEEP(1),
    REFERENCE_NODE(2);
    private int value;
    private ConnectivityDeviceType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}