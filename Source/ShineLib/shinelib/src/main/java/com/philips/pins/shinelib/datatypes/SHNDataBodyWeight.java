/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataBodyWeight extends SHNData {
    private float weightInKg;
    private int userId;
    private float heightInMeters;
    private float bmi;

    private SHNDataBodyWeight() {
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

    public static class Builder {

        private SHNDataBodyWeight shnDataBodyWeight;

        private boolean hasWeightInKg;

        public Builder() {
            shnDataBodyWeight = new SHNDataBodyWeight();
        }

        public Builder setWeightInKg(float weightInKg) {
            shnDataBodyWeight.weightInKg = weightInKg;
            hasWeightInKg = true;
            return this;
        }

        public Builder setUserId(int userId) {
            shnDataBodyWeight.userId = userId;
            return this;
        }

        public Builder setHeightInMeters(float heightInMeters) {
            shnDataBodyWeight.heightInMeters = heightInMeters;
            return this;
        }

        public Builder setBmi(float bmi) {
            shnDataBodyWeight.bmi = bmi;
            return this;
        }

        public SHNDataBodyWeight build() {
            if (hasWeightInKg) {
                return shnDataBodyWeight;
            } else {
                throw new IllegalArgumentException("No weight set");
            }
        }
    }
}
