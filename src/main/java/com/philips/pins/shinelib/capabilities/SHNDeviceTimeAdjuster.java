package com.philips.pins.shinelib.capabilities;

/**
 * Created by 310188215 on 08/07/15.
 */
public interface SHNDeviceTimeAdjuster {
    long adjustTimestampToHostTime(long timestamp);
}
