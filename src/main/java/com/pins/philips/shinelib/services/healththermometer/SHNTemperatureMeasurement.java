package com.pins.philips.shinelib.services.healththermometer;

import com.pins.philips.shinelib.datatypes.SHNTemperatureType;
import com.pins.philips.shinelib.datatypes.SHNTemperatureUnit;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by 310188215 on 04/06/15.
 */
public class SHNTemperatureMeasurement {
    private final Flags flags;
    private final float temperature;
    private final Date timestamp;
    private final SHNTemperatureType shnTemperatureType;

    public SHNTemperatureMeasurement(ByteBuffer byteBuffer) {
        throw new UnsupportedOperationException();
    }

    public Flags getFlags() {
        return flags;
    }

    public float getTemperature() {
        return temperature;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public SHNTemperatureType getShnTemperatureType() {
        return shnTemperatureType;
    }

    public static class Flags {
        private final SHNTemperatureUnit shnTemperatureUnit;
        private final boolean hasTimestamp;
        private final boolean hasTemperatureType;

        private Flags(byte bitField) {
            throw new UnsupportedOperationException();
        }

        public SHNTemperatureUnit getShnTemperatureUnit() {
            return shnTemperatureUnit;
        }

        public boolean hasTimestamp() {
            return hasTimestamp;
        }

        public boolean hasTemperatureType() {
            return hasTemperatureType;
        }
    }
}
