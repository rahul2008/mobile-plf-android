/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
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
