/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import java.nio.ByteBuffer;

/**
 * @publicPluginApi
 */
public class SHNBodyCompositionFeatures {

    private static final int TIMESTAMP_SUPPORTED = 0x01;
    private static final int MULTIPLE_USER_SUPPORTED = 0x02;
    private static final int BASAL_METABOLISM_SUPPORTED = 0x04;

    private static final int MUSCLE_PERCENTAGE_SUPPORTED = 0x08;
    private static final int MUSCLE_MASS_SUPPORTED = 0x10;
    private static final int FAT_FREE_MASS_SUPPORTED = 0x20;
    private static final int SOFT_LEAN_MASS_SUPPORTED = 0x40;

    private static final int BODY_WATER_MASS_SUPPORTED = 0x80;

    private static final int IMPEDANCE_SUPPORTED = 0x100;
    private static final int WEIGHT_SUPPORTED = 0x200;
    private static final int HEIGHT_SUPPORTED = 0x400;

    private static final int WEIGHT_MEASUREMENT_RESOLUTION = 30720; // 0111 1000 0000 0000
    private static final int HEIGHT_MEASUREMENT_RESOLUTION = 229376; // 0000 0011 1000 0000 0000 0000

    private final boolean timeStampSupported;
    private final boolean multiUserSupported;

    private final boolean baselMetabolismSupported;

    private final boolean musclePercentageSupported;
    private final boolean muscleMassSupported;

    private final boolean fatFreeMassSupported;
    private final boolean softLeanMassSupported;

    private final boolean bodyWaterMassSupported;
    private final boolean impedanceSupported;
    private final boolean weightSupported;
    private final boolean heightSupported;

    private boolean weightResolutionSpecified;
    private boolean heightResolutionSpecified;

    private float weightResolutionInKG;
    private float weightResolutionInLBS;
    private float heightResolutionInMeters;
    private float heightResolutionInInch;

    private float[] weightResolutionsInKG = new float[]{0f, 0.5f, 0.2f, 0.1f, 0.05f, 0.02f, 0.01f, 0.005f};
    private float[] weightResolutionsInLBS = new float[]{0f, 1f, 0.5f, 0.2f, 0.1f, 0.05f, 0.02f, 0.01f};

    private float[] heightResolutionsInMeters = new float[]{0f, 0.01f, 0.005f, 0.001f};
    private float[] heightResolutionsInInch = new float[]{0f, 1f, 0.5f, 0.1f};

    public SHNBodyCompositionFeatures(ByteBuffer byteBuffer) {
        int data = byteBuffer.getInt();

        timeStampSupported = (data & TIMESTAMP_SUPPORTED) != 0;
        multiUserSupported = (data & MULTIPLE_USER_SUPPORTED) != 0;
        baselMetabolismSupported = (data & BASAL_METABOLISM_SUPPORTED) != 0;

        musclePercentageSupported = (data & MUSCLE_PERCENTAGE_SUPPORTED) != 0;
        muscleMassSupported = (data & MUSCLE_MASS_SUPPORTED) != 0;
        fatFreeMassSupported = (data & FAT_FREE_MASS_SUPPORTED) != 0;
        softLeanMassSupported = (data & SOFT_LEAN_MASS_SUPPORTED) != 0;
        bodyWaterMassSupported = (data & BODY_WATER_MASS_SUPPORTED) != 0;

        impedanceSupported = (data & IMPEDANCE_SUPPORTED) != 0;
        weightSupported = (data & WEIGHT_SUPPORTED) != 0;
        heightSupported = (data & HEIGHT_SUPPORTED) != 0;

        getWeightResolution(data);
        getHeightResolution(data);
    }

    public boolean isTimeStampSupported() {
        return timeStampSupported;
    }

    public boolean isMultiUserSupported() {
        return multiUserSupported;
    }

    public boolean isBaselMetabolismSupported() {
        return baselMetabolismSupported;
    }

    public boolean isMusclePercentageSupported() {
        return musclePercentageSupported;
    }

    public boolean isMuscleMassSupported() {
        return muscleMassSupported;
    }

    public boolean isFatFreeMassSupported() {
        return fatFreeMassSupported;
    }

    public boolean isSoftLeanMassSupported() {
        return softLeanMassSupported;
    }

    public boolean isBodyWaterMassSupported() {
        return bodyWaterMassSupported;
    }

    public boolean isImpedanceSupported() {
        return impedanceSupported;
    }

    public boolean isWeightSupported() {
        return weightSupported;
    }

    public boolean isHeightSupported() {
        return heightSupported;
    }

    public boolean isWeightResolutionSpecified() {
        return weightResolutionSpecified;
    }

    private void getWeightResolution(int data) {
        int value = (data & WEIGHT_MEASUREMENT_RESOLUTION) >> 8 >> 3;
        if (value != 0 && value < weightResolutionsInKG.length) {
            weightResolutionInKG = weightResolutionsInKG[value];
            weightResolutionInLBS = weightResolutionsInLBS[value];
            weightResolutionSpecified = true;
        }
    }

    private void getHeightResolution(int data) {
        int value = (data & HEIGHT_MEASUREMENT_RESOLUTION) >> 8 >> 7;
        if (value != 0 && value < heightResolutionsInMeters.length) {
            heightResolutionInMeters = heightResolutionsInMeters[value];
            heightResolutionInInch = heightResolutionsInInch[value];
            heightResolutionSpecified = true;
        }
    }

    public boolean isHeightResolutionSpecified() {
        return heightResolutionSpecified;
    }

    public float getWeightResolutionInKG() {
        return weightResolutionInKG;
    }

    public float getWeightResolutionInLBS() {
        return weightResolutionInLBS;
    }

    public float getHeightResolutionInMeters() {
        return heightResolutionInMeters;
    }

    public float getHeightResolutionInInch() {
        return heightResolutionInInch;
    }
}
