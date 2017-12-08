/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDataFatPercentage extends SHNData {

    private float fatPercentage;

    public SHNDataFatPercentage(float fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public float getFatPercentage() {
        return fatPercentage;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.FatPercentage;
    }
}
