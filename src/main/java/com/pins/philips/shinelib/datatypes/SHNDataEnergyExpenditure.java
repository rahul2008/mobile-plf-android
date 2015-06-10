package com.pins.philips.shinelib.datatypes;

/**
 * Created by 310188215 on 13/05/15.
 */
public class SHNDataEnergyExpenditure extends SHNData {
    private final double energyExpenditure;

    public SHNDataEnergyExpenditure(double energyExpenditure) {
        this.energyExpenditure = energyExpenditure;
    }

    public double getEnergyExpenditure() {
        return energyExpenditure;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.EnergyExpenditure;
    }
}
