/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * @publicPluginApi
 */
public class SHNWeightScaleFeatures {

    private static final byte TIME_STAMP_SUPPORTED = 0x01;
    private static final byte MULTIPLE_USER_SUPPORTED = 0x02;
    private static final byte BMI_SUPPORTED = 0x04;

    private static final int WEIGHT_MEASUREMENT_RESOLUTION = 120; // 0111 1000
    private static final int HEIGHT_MEASUREMENT_RESOLUTION = 896; // 0000 0011 1000 0000

    private boolean timeStampSupported;
    private boolean multiUserSupported;
    private boolean bmiSupported;

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

    public SHNWeightScaleFeatures(ByteBuffer byteBuffer) {
        try {
            int data = byteBuffer.getInt();

            timeStampSupported = (data & TIME_STAMP_SUPPORTED) != 0;
            multiUserSupported = (data & MULTIPLE_USER_SUPPORTED) != 0;
            bmiSupported = (data & BMI_SUPPORTED) != 0;

            getWeightResolution(data);
            getHeightResolution(data);

        } catch (BufferUnderflowException e) {
            throw new IllegalArgumentException();
        }
    }


    private void getWeightResolution(int data) {
        int value = (data & WEIGHT_MEASUREMENT_RESOLUTION) >> 3;
        if (value != 0 && value < weightResolutionsInKG.length) {
            weightResolutionInKG = weightResolutionsInKG[value];
            weightResolutionInLBS = weightResolutionsInLBS[value];
            weightResolutionSpecified = true;
        }
    }

    private void getHeightResolution(int data) {
        int value = (data & HEIGHT_MEASUREMENT_RESOLUTION) >> 7;
        if (value != 0 && value < heightResolutionsInMeters.length) {
            heightResolutionInMeters = heightResolutionsInMeters[value];
            heightResolutionInInch = heightResolutionsInInch[value];
            heightResolutionSpecified = true;
        }
    }

    public boolean isTimeStampSupported() {
        return timeStampSupported;
    }

    public boolean isMultiUserSupported() {
        return multiUserSupported;
    }

    public boolean isBmiSupported() {
        return bmiSupported;
    }

    public boolean isWeightResolutionSpecified() {
        return weightResolutionSpecified;
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
