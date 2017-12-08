/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

/**
 * @publicPluginApi
 */
package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.ByteBuffer;

public class SHNTemperatureMeasurementInterval {
    private final int seconds;

    public SHNTemperatureMeasurementInterval(ByteBuffer byteBuffer) {
        seconds = ScalarConverters.ushortToInt(byteBuffer.getShort());
    }

    public int getSeconds() {
        return seconds;
    }
}
