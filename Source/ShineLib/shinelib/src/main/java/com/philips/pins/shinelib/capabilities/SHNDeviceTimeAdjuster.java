/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

public interface SHNDeviceTimeAdjuster {
    long adjustTimestampToHostTime(long timestamp);
}
