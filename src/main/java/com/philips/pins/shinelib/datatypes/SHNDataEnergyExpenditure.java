package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 13/05/15.
 */
public class SHNDataEnergyExpenditure extends SHNData {
    private final int energyExpenditure;

    public SHNDataEnergyExpenditure(int energyExpenditure) {
        this.energyExpenditure = energyExpenditure;
    }

    public int getEnergyExpenditure() {
        return energyExpenditure;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.EnergyExpenditure;
    }
}
