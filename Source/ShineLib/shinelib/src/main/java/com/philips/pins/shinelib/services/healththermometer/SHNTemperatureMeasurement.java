/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.datatypes.SHNTemperatureType;
import com.philips.pins.shinelib.datatypes.SHNTemperatureUnit;
import com.philips.pins.shinelib.utility.SHNBluetoothDataConverter;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Date;

public class SHNTemperatureMeasurement {
    private final Flags flags;
    private final float temperature;
    private final Date timestamp;
    private final SHNTemperatureType shnTemperatureType;

    public SHNTemperatureMeasurement(ByteBuffer byteBuffer) {
        try {
            flags = new Flags(byteBuffer.get());
            temperature = getIEEE11073Float(byteBuffer);

            timestamp = (flags.hasTimestamp()) ? SHNBluetoothDataConverter.getDateTime(byteBuffer) : null;
            if (flags.hasTimestamp() && timestamp == null) {
                throw new IllegalArgumentException();
            }

            shnTemperatureType = (flags.hasTemperatureType()) ? getSHNTemperatureType(byteBuffer) : null;
            if (flags.hasTemperatureType() && shnTemperatureType == null) {
                throw new IllegalArgumentException();
            }
        } catch (BufferUnderflowException e) {
            throw new IllegalArgumentException();
        }
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

    public SHNTemperatureType getSHNTemperatureType() {
        return shnTemperatureType;
    }

    /*
    IEEE 11073 format 1 byte 10 based exponent 3 bytes integer.
    0x007FFFFF indicated NaN
     */
    private float getIEEE11073Float(ByteBuffer byteBuffer) {
        float result = Float.NaN;
        int value = byteBuffer.getInt();
        if (value != 0x007FFFFF) { // Literal value for NaN in IEEE 11073
            int exponent = (int)(byte)((value >> 24) & 0xFF);
            int mantissa = (value & 0x00FFFFFF);
            result = (float) (mantissa * Math.pow(10, exponent));
        }
        return result;
    }



    private SHNTemperatureType getSHNTemperatureType(ByteBuffer byteBuffer) {
        int value = ScalarConverters.ubyteToInt(byteBuffer.get());
        switch(value) {
            case 1:
                return SHNTemperatureType.Armpit;
            case 2:
                return SHNTemperatureType.Body;
            case 3:
                return SHNTemperatureType.Ear;
            case 4:
                return SHNTemperatureType.Finger;
            case 5:
                return SHNTemperatureType.GastroIntestinalTract;
            case 6:
                return SHNTemperatureType.Mouth;
            case 7:
                return SHNTemperatureType.Rectum;
            case 8:
                return SHNTemperatureType.Toe;
            case 9:
                return SHNTemperatureType.Tympanum;
        }
        return SHNTemperatureType.Unknown;
    }

    public float getTemperatureInCelcius() {
        if (SHNTemperatureUnit.Celsius == flags.shnTemperatureUnit) {
            return temperature;
        }
        return (temperature - 32f) *  5f / 9f; // Convert fahrenheit to celsius
    }

    public static class Flags {
        private static final byte FLAGS_CELSIUS                 = 0x01;
        private static final byte FLAGS_HAS_TIMESTAMP           = 0x02;
        private static final byte FLAGS_HAS_TEMPERATURE_TYPE    = 0x04;

        private final SHNTemperatureUnit shnTemperatureUnit;
        private final boolean hasTimestamp;
        private final boolean hasTemperatureType;

        private Flags(byte bitField) {
            boolean isCelsius = (bitField & FLAGS_CELSIUS) == 0;
            shnTemperatureUnit = isCelsius ? SHNTemperatureUnit.Celsius : SHNTemperatureUnit.Fahrenheit;
            hasTimestamp = (bitField & FLAGS_HAS_TIMESTAMP) != 0;
            hasTemperatureType = (bitField & FLAGS_HAS_TEMPERATURE_TYPE) != 0;
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
