package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 09/07/15.
 */
public class SHNDataBodyWeight extends SHNData {
    private final float weightInKg;
    private final int userId;
    private final float heightInMeters;

    private final float bmi;

    public SHNDataBodyWeight(float weightInKg, int userId, float heightInMeters, float bmi) {
        this.weightInKg = weightInKg;
        this.userId = userId;
        this.heightInMeters = heightInMeters;
        this.bmi = bmi;
    }

    public float getBmi() {
        return bmi;
    }

    public float getHeightInMeters() {
        return heightInMeters;
    }

    public int getUserId() {
        return userId;
    }

    public float getWeightInKg() {
        return weightInKg;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BodyWeight;
    }
}
