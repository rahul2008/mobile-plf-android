package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.ByteBuffer;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNTemperatureMeasurementInterval {
    private final int seconds;

    public SHNTemperatureMeasurementInterval(ByteBuffer byteBuffer) {
        seconds = ScalarConverters.ushortToInt(byteBuffer.getShort());
    }

    public int getSeconds() {
        return seconds;
    }
}
