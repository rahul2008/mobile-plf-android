/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataEnergyExpenditure extends SHNData {
    private final double energyExpenditure;
    private final SHNDataType shnDataType;

    public SHNDataEnergyExpenditure(double energyExpenditure, SHNDataType shnDataType) {
        this.energyExpenditure = energyExpenditure;
        this.shnDataType = shnDataType;
    }

    public double getEnergyExpenditure() {
        return energyExpenditure;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return shnDataType;
    }

    @Override
    public String toString() {
        return shnDataType.name() + ": " + getEnergyExpenditure();
    }
}
