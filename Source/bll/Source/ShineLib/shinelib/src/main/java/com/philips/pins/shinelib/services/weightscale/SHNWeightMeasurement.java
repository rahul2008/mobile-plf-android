/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import com.philips.pins.shinelib.datatypes.SHNHeightUnit;
import com.philips.pins.shinelib.datatypes.SHNWeightUnit;
import com.philips.pins.shinelib.utility.SHNBluetoothDataConverter;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @publicPluginApi
 */
public class SHNWeightMeasurement {

    private static final String TAG = SHNWeightMeasurement.class.getSimpleName();

    private final Flags flags;
    private final Date timestamp;
    private final float weight;
    private final int userId;
    private final float bmi;
    private final float height;

    private final static float WEIGHT_KG_RESOLUTION = 0.005f;
    private final static float WEIGHT_LB_RESOLUTION = 0.01f;

    private final static float BMI_RESOLUTION = 0.1f;
    private final static float HEIGHT_METER_RESOLUTION = 0.001f;
    private final static float HEIGHT_INCH_RESOLUTION = 0.1f;

    private final static int MEASUREMENT_UNSUCCESSFUL = 0xFFFF;
    private final static int UNKNOWN_USER_ID = 255;

    public SHNWeightMeasurement(ByteBuffer byteBuffer) {
        try {
            flags = new Flags(byteBuffer.get());

            short weightRaw = byteBuffer.getShort();
            weight = extractWeight(ScalarConverters.ushortToInt(weightRaw));

            timestamp = flags.hasTimestamp() ? SHNBluetoothDataConverter.getDateTime(byteBuffer) : null;
            if (flags.hasTimestamp() && timestamp == null) {
                throw new IllegalArgumentException();
            }

            if (flags.hasUserId()) {
                userId = ScalarConverters.ubyteToInt(byteBuffer.get());
            } else {
                userId = UNKNOWN_USER_ID;
            }

            if (flags.hasBmiAndHeight) {
                short bmiRaw = byteBuffer.getShort();
                bmi = extractBMI(ScalarConverters.ushortToInt(bmiRaw));
                short heightRaw = byteBuffer.getShort();
                height = extractHeight(ScalarConverters.ushortToInt(heightRaw));
            } else {
                bmi = Float.NaN;
                height = Float.NaN;
            }
        } catch (BufferUnderflowException e) {
            throw new IllegalArgumentException();
        }
    }

    public Flags getFlags() {
        return flags;
    }

    private float extractWeight(int rawData) {
        if (rawData == MEASUREMENT_UNSUCCESSFUL) {
            SHNLogger.w(TAG, "Received a measurement with the special weight-value 0xFFFF that represents \"Measurement Unsuccessful\"");
        } else {
            SHNWeightUnit unit = getFlags().getShnWeightUnit();
            float resolution;
            if (unit == SHNWeightUnit.KG) {
                resolution = WEIGHT_KG_RESOLUTION;
            } else {
                resolution = WEIGHT_LB_RESOLUTION;
            }
            return rawData * resolution;
        }
        return Float.NaN;
    }

    private float extractBMI(int rawData) {
        return rawData * BMI_RESOLUTION;
    }

    private float extractHeight(int rawData) {
        SHNHeightUnit unit = getFlags().getShnHeightUnit();
        float resolution;
        if (unit == SHNHeightUnit.Meter) {
            resolution = HEIGHT_METER_RESOLUTION;
        } else {
            resolution = HEIGHT_INCH_RESOLUTION;
        }
        return rawData * resolution;
    }

    public float getWeight() {
        return weight;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isUserIdUnknown() {
        return userId == UNKNOWN_USER_ID;
    }

    public float getBMI() {
        return bmi;
    }

    public float getHeight() {
        return height;
    }

    public float getWeightInKg() {
        if (flags.shnWeightUnit == SHNWeightUnit.KG) {
            return weight;
        }
        return weight * 0.45359237f;
    }

    public static class Flags {
        private static final byte FLAGS_SI = 0x01;
        private static final byte FLAGS_HAS_TIMESTAMP = 0x02;
        private static final byte FLAGS_HAS_USER_ID = 0x04;
        private static final byte FLAGS_HAS_BMI_AND_HEIGHT = 0x08;

        private final SHNWeightUnit shnWeightUnit;
        private final SHNHeightUnit shnHeightUnit;
        private final boolean hasTimestamp;
        private final boolean hasUserId;
        private final boolean hasBmiAndHeight;

        private Flags(byte bitField) {
            boolean isSI = (bitField & FLAGS_SI) == 0;
            shnWeightUnit = isSI ? SHNWeightUnit.KG : SHNWeightUnit.LB;
            shnHeightUnit = isSI ? SHNHeightUnit.Meter : SHNHeightUnit.Inch;

            hasTimestamp = (bitField & FLAGS_HAS_TIMESTAMP) != 0;
            hasUserId = (bitField & FLAGS_HAS_USER_ID) != 0;
            hasBmiAndHeight = (bitField & FLAGS_HAS_BMI_AND_HEIGHT) != 0;
        }

        public SHNWeightUnit getShnWeightUnit() {
            return shnWeightUnit;
        }

        public SHNHeightUnit getShnHeightUnit() {
            return shnHeightUnit;
        }

        public boolean hasTimestamp() {
            return hasTimestamp;
        }

        public boolean hasUserId() {
            return hasUserId;
        }

        public boolean hasBmiAndHeight() {
            return hasBmiAndHeight;
        }
    }
}
