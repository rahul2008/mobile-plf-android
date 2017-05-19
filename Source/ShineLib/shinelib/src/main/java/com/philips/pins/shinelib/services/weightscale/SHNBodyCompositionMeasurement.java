/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import com.philips.pins.shinelib.datatypes.SHNHeightUnit;
import com.philips.pins.shinelib.datatypes.SHNWeightUnit;
import com.philips.pins.shinelib.utility.SHNBluetoothDataConverter;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @publicPluginApi
 */
public class SHNBodyCompositionMeasurement {
    private static final float FAT_PERCENTAGE_RESOLUTION = 0.1f;
    private static final float MUSCLE_PERCENTAGE_RESOLUTION = 0.1f;

    private static final float MASS_RESOLUTION_SI = 0.005f;
    private static final float MASS_RESOLUTION_IMPERIAL = 0.01f;

    private static final float IMPEDANCE_RESOLUTION = 0.01f;

    private static final float WEIGHT_RESOLUTION_SI = 0.005f;
    private static final float WEIGHT_RESOLUTION_IMPERIAL = 0.01f;

    private static final float HEIGHT_RESOLUTION_SI = 0.001f;
    private static final float HEIGHT_RESOLUTION_IMPERIAL = 0.1f;

    private final Flags flags;

    private final float bodyFatPercentage;
    private final Date timestamp;
    private final int userId;

    private final int basalMetabolismInKiloJoules;
    private final float musclePercentage;
    private final float muscleMass;
    private final float fatFreeMass;
    private final float softLeanMass;
    private final float bodyWaterMass;

    private final float impedance;

    private final float weight;
    private final float height;

    private final static int MEASUREMENT_UNSUCCESSFUL = 0xFFFF;
    private final static int UNKNOWN_USER_ID = 255;

    public SHNBodyCompositionMeasurement(ByteBuffer byteBuffer) {
        flags = new Flags(byteBuffer);

        bodyFatPercentage = extractBodyFatPercentage(byteBuffer);

        timestamp = flags.hasTimestamp() ? SHNBluetoothDataConverter.getDateTime(byteBuffer) : null;
        if (flags.hasTimestamp() && timestamp == null) {
            throw new IllegalArgumentException();
        }

        if (flags.hasUserId()) {
            userId = ScalarConverters.ubyteToInt(byteBuffer.get());
        } else {
            userId = UNKNOWN_USER_ID;
        }

        if (flags.hasBaselMetabolism()) {
            basalMetabolismInKiloJoules = ScalarConverters.ushortToInt(byteBuffer.getShort());
        } else {
            basalMetabolismInKiloJoules = 0;
        }

        musclePercentage = initUShortIfAvailable(flags.hasMusclePercentage(), byteBuffer, MUSCLE_PERCENTAGE_RESOLUTION);

        muscleMass = initUShortIfAvailable(flags.hasMuscleMass(), byteBuffer, MASS_RESOLUTION_SI, MASS_RESOLUTION_IMPERIAL);
        fatFreeMass = initUShortIfAvailable(flags.hasFatFreeMass(), byteBuffer, MASS_RESOLUTION_SI, MASS_RESOLUTION_IMPERIAL);
        softLeanMass = initUShortIfAvailable(flags.hasSoftLeanMass(), byteBuffer, MASS_RESOLUTION_SI, MASS_RESOLUTION_IMPERIAL);
        bodyWaterMass = initUShortIfAvailable(flags.hasBodyWaterMass(), byteBuffer, MASS_RESOLUTION_SI, MASS_RESOLUTION_IMPERIAL);

        impedance = initUShortIfAvailable(flags.hasImpedance(), byteBuffer, IMPEDANCE_RESOLUTION);

        weight = initUShortIfAvailable(flags.hasWeight(), byteBuffer, WEIGHT_RESOLUTION_SI, WEIGHT_RESOLUTION_IMPERIAL);
        height = initUShortIfAvailable(flags.hasHeight(), byteBuffer, HEIGHT_RESOLUTION_SI, HEIGHT_RESOLUTION_IMPERIAL);
    }

    public Flags getFlags() {
        return flags;
    }

    public float getBodyFatPercentage() {
        return bodyFatPercentage;
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

    public int getBasalMetabolismInKiloJoules() {
        return basalMetabolismInKiloJoules;
    }

    public float getMusclePercentage() {
        return musclePercentage;
    }

    public float getMuscleMass() {
        return muscleMass;
    }

    public float getFatFreeMass() {
        return fatFreeMass;
    }

    public float getSoftLeanMass() {
        return softLeanMass;
    }

    public float getBodyWaterMass() {
        return bodyWaterMass;
    }

    public float getImpedance() {
        return impedance;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    private float extractBodyFatPercentage(ByteBuffer byteBuffer) {
        int value = ScalarConverters.ushortToInt(byteBuffer.getShort());
        if (value == MEASUREMENT_UNSUCCESSFUL) {
            return Float.NaN;
        } else {
            return value * FAT_PERCENTAGE_RESOLUTION;
        }
    }

    private float convertUShortWithResolution(short rawValue, float resolution) {
        int value = ScalarConverters.ushortToInt(rawValue);
        return value * resolution;
    }

    private float convertUShortWithResolution(short rawValue, float resolutionSI, float resolutionImperial) {
        int value = ScalarConverters.ushortToInt(rawValue);
        if (flags.getShnWeightUnit() == SHNWeightUnit.KG && flags.getShnHeightUnit() == SHNHeightUnit.Meter) {
            return value * resolutionSI;
        } else if (flags.getShnWeightUnit() == SHNWeightUnit.LB && flags.getShnHeightUnit() == SHNHeightUnit.Inch) {
            return value * resolutionImperial;
        }
        return Float.NaN;
    }

    private float initUShortIfAvailable(boolean available, ByteBuffer byteBuffer, float resolutionSI, float resolutionImperial) {
        if (available) {
            return convertUShortWithResolution(byteBuffer.getShort(), resolutionSI, resolutionImperial);
        } else {
            return Float.NaN;
        }
    }

    private float initUShortIfAvailable(boolean available, ByteBuffer byteBuffer, float resolution) {
        if (available) {
            return convertUShortWithResolution(byteBuffer.getShort(), resolution);
        } else {
            return Float.NaN;
        }
    }

    public static class Flags {
        private static final byte FLAGS_SI = 0x01;
        private static final byte FLAGS_HAS_TIMESTAMP = 0x02;
        private static final byte FLAGS_HAS_USER_ID = 0x04;
        private static final byte FLAGS_HAS_BASAL_METABOLISM = 0x08;
        private static final byte FLAGS_HAS_MUSCLE_PERCENTAGE = 0x10;
        private static final byte FLAGS_HAS_MUSCLE_MASS = 0x20;
        private static final byte FLAGS_HAS_FAT_FREE_MASS = 0x40;
        private static final byte FLAGS_HAS_SOFT_LEAN_MASS = (byte) 0x80;

        private static final byte FLAGS_HAS_BODY_WATER_MASS = 0x01;
        private static final byte FLAGS_HAS_IMPEDANCE = 0x02;
        private static final byte FLAGS_HAS_WEIGHT = 0x04;
        private static final byte FLAGS_HAS_HEIGHT = 0x08;
        private static final byte FLAGS_HAS_MULTIPLE_PACKET_MEASUREMENT = 0x10;

        private final SHNWeightUnit shnWeightUnit;
        private final SHNHeightUnit shnHeightUnit;

        private final boolean hasTimestamp;
        private final boolean hasUserId;
        private final boolean hasBaselMetabolism;

        private final boolean hasMusclePercentage;
        private final boolean hasMuscleMass;
        private final boolean hasFatFreeMass;
        private final boolean hasSoftLeanMass;

        private final boolean hasBodyWaterMass;
        private final boolean hasImpedance;
        private final boolean hasWeight;
        private final boolean hasHeight;
        private final boolean hasMultiplePacketMeasurement;

        private Flags(ByteBuffer byteBuffer) {
            byte firstBitField = byteBuffer.get();
            boolean isSI = (firstBitField & FLAGS_SI) == 0;
            shnWeightUnit = isSI ? SHNWeightUnit.KG : SHNWeightUnit.LB;
            shnHeightUnit = isSI ? SHNHeightUnit.Meter : SHNHeightUnit.Inch;

            hasTimestamp = (firstBitField & FLAGS_HAS_TIMESTAMP) != 0;
            hasUserId = (firstBitField & FLAGS_HAS_USER_ID) != 0;
            hasBaselMetabolism = (firstBitField & FLAGS_HAS_BASAL_METABOLISM) != 0;

            hasMusclePercentage = (firstBitField & FLAGS_HAS_MUSCLE_PERCENTAGE) != 0;
            hasMuscleMass = (firstBitField & FLAGS_HAS_MUSCLE_MASS) != 0;
            hasFatFreeMass = (firstBitField & FLAGS_HAS_FAT_FREE_MASS) != 0;
            hasSoftLeanMass = (firstBitField & FLAGS_HAS_SOFT_LEAN_MASS) != 0;

            byte secondBitField = byteBuffer.get();

            hasBodyWaterMass = (secondBitField & FLAGS_HAS_BODY_WATER_MASS) != 0;
            hasImpedance = (secondBitField & FLAGS_HAS_IMPEDANCE) != 0;
            hasWeight = (secondBitField & FLAGS_HAS_WEIGHT) != 0;
            hasHeight = (secondBitField & FLAGS_HAS_HEIGHT) != 0;
            hasMultiplePacketMeasurement = (secondBitField & FLAGS_HAS_MULTIPLE_PACKET_MEASUREMENT) != 0;
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

        public boolean hasBaselMetabolism() {
            return hasBaselMetabolism;
        }

        public boolean hasMusclePercentage() {
            return hasMusclePercentage;
        }

        public boolean hasMuscleMass() {
            return hasMuscleMass;
        }

        public boolean hasFatFreeMass() {
            return hasFatFreeMass;
        }

        public boolean hasSoftLeanMass() {
            return hasSoftLeanMass;
        }

        public boolean hasBodyWaterMass() {
            return hasBodyWaterMass;
        }

        public boolean hasImpedance() {
            return hasImpedance;
        }

        public boolean hasWeight() {
            return hasWeight;
        }

        public boolean hasHeight() {
            return hasHeight;
        }

        public boolean isMultiplePacketMeasurement() {
            return hasMultiplePacketMeasurement;
        }
    }
}
