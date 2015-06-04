package com.pins.philips.shinelib.services.healththermometer;

import java.nio.ByteBuffer;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNTemperatureMeasurementInterval {
    private final int seconds;

    public SHNTemperatureMeasurementInterval(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }

    public int getSeconds() {
        return seconds;
    }
}
