/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * Weight measurement data in kilograms (SI).
 *
 * @publicApi
 */
public class SHNDataWeight extends SHNData {
    private float weightInKg;

    public SHNDataWeight(float weightInKg) {
        this.weightInKg = weightInKg;
    }

    /**
     * @return {@link SHNDataType#Weight}
     */
    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.Weight;
    }

    public float getWeightInKg() {
        return weightInKg;
    }
}
