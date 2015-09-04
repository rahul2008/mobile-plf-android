package com.philips.pins.shinelib.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDataWeight extends SHNData {
    private float weightInKg;

    public SHNDataWeight(float weightInKg) {
        this.weightInKg = weightInKg;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.Weight;
    }

    public float getWeightInKg() {
        return weightInKg;
    }
}
