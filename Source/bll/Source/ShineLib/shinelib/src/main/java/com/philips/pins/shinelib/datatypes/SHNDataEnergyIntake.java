package com.philips.pins.shinelib.datatypes;

public class SHNDataEnergyIntake extends SHNData{
    private final short energyIntake;

    public SHNDataEnergyIntake(short energyIntake) {
        this.energyIntake = energyIntake;
    }

    public short getEnergyIntake() {
        return energyIntake;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.EnergyIntake;
    }

    @Override
    public String toString() {
        return "Energy Intake: " + getEnergyIntake();
    }
}
