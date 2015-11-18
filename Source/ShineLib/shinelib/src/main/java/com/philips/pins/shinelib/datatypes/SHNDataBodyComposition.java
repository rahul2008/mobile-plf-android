/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNDataBodyComposition extends SHNData{

    private float fatPercentage;

    private short userId;
    private float basalMetabolismInKiloCalories;
    private float musclePercentage;
    private float muscleMassInKg;
    private float fatFreeMassInKg;
    private float softLeanMassInKg;
    private float bodyWaterMassInKg;
    private float impedanceInOhm;
    private float weightInKg;
    private float heightInMeters;

    private boolean hasUserId;
    private boolean hasBasalMetabolismInKiloCalories;
    private boolean hasMusclePercentage;
    private boolean hasMuscleMassInKg;
    private boolean hasFatFreeMassInKg;
    private boolean hasSoftLeanMassInKg;
    private boolean hasBodyWaterMassInKg;
    private boolean hasImpedanceInOhm;
    private boolean hasWeightInKg;
    private boolean hasHeightInMeters;

    private SHNDataBodyComposition() {
    }

    public short getUserId() {
        return userId;
    }

    public boolean hasUserId() {
        return hasUserId;
    }

    public boolean hasBasalMetabolismInKiloCalories() {
        return hasBasalMetabolismInKiloCalories;
    }

    public boolean hasMusclePercentage() {
        return hasMusclePercentage;
    }

    public boolean hasMuscleMassInKg() {
        return hasMuscleMassInKg;
    }

    public boolean hasFatFreeMassInKg() {
        return hasFatFreeMassInKg;
    }

    public boolean hasSoftLeanMassInKg() {
        return hasSoftLeanMassInKg;
    }

    public boolean hasBodyWaterMassInKg() {
        return hasBodyWaterMassInKg;
    }

    public boolean hasImpedanceInOhm() {
        return hasImpedanceInOhm;
    }

    public boolean hasWeightInKg() {
        return hasWeightInKg;
    }

    public boolean hasHeightInMeters() {
        return hasHeightInMeters;
    }

    public float getHeightInMeters() {
        return heightInMeters;
    }

    public float getFatPercentage() {
        return fatPercentage;
    }

    public float getBasalMetabolismInKiloCalories() {
        return basalMetabolismInKiloCalories;
    }

    public float getMusclePercentage() {
        return musclePercentage;
    }

    public float getMuscleMassInKg() {
        return muscleMassInKg;
    }

    public float getFatFreeMassInKg() {
        return fatFreeMassInKg;
    }

    public float getSoftLeanMassInKg() {
        return softLeanMassInKg;
    }

    public float getBodyWaterMassInKg() {
        return bodyWaterMassInKg;
    }

    public float getImpedanceInOhm() {
        return impedanceInOhm;
    }

    public float getWeightInKg() {
        return weightInKg;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.BodyComposition;
    }

    public static class Builder {
        private SHNDataBodyComposition shnDataBodyComposition;

        private boolean hasFatPercentage;

        public Builder() {
            shnDataBodyComposition = new SHNDataBodyComposition();
        }

        public Builder setFatPercentage(float fatPercentage) {
            shnDataBodyComposition.fatPercentage = fatPercentage;
            hasFatPercentage = true;
            return this;
        }

        public Builder setUserId(short userId) {
            shnDataBodyComposition.userId = userId;
            shnDataBodyComposition.hasUserId = true;
            return this;
        }
        
        public Builder setBasalMetabolismInKiloCalories(float basalMetabolismInKiloCalories) {
            shnDataBodyComposition.basalMetabolismInKiloCalories = basalMetabolismInKiloCalories;
            shnDataBodyComposition.hasBasalMetabolismInKiloCalories = true;
            return this;
        }

        public Builder setMusclePercentage(float musclePercentage) {
            shnDataBodyComposition.musclePercentage = musclePercentage;
            shnDataBodyComposition.hasMusclePercentage = true;
            return this;
        }

        public Builder setMuscleMassInKg(float muscleMassInKg) {
            shnDataBodyComposition.muscleMassInKg = muscleMassInKg;
            shnDataBodyComposition.hasMuscleMassInKg = true;
            return this;
        }

        public Builder setFatFreeMassInKg(float fatFreeMassInKg) {
            shnDataBodyComposition.fatFreeMassInKg = fatFreeMassInKg;
            shnDataBodyComposition.hasFatFreeMassInKg = true;
            return this;
        }

        public Builder setSoftLeanMassInKg(float softLeanMassInKg) {
            shnDataBodyComposition.softLeanMassInKg = softLeanMassInKg;
            shnDataBodyComposition.hasSoftLeanMassInKg = true;
            return this;
        }

        public Builder setBodyWaterMassInKg(float bodyWaterMassInKg) {
            shnDataBodyComposition.bodyWaterMassInKg = bodyWaterMassInKg;
            shnDataBodyComposition.hasBodyWaterMassInKg = true;
            return this;
        }

        public Builder setImpedanceInOhm(float impedanceInOhm) {
            shnDataBodyComposition.impedanceInOhm = impedanceInOhm;
            shnDataBodyComposition.hasImpedanceInOhm = true;
            return this;
        }

        public Builder setWeightInKg(float weightInKg) {
            shnDataBodyComposition.weightInKg = weightInKg;
            shnDataBodyComposition.hasWeightInKg = true;
            return this;
        }

        public Builder setHeightInMeters(float heightInMeters) {
            shnDataBodyComposition.heightInMeters = heightInMeters;
            shnDataBodyComposition.hasHeightInMeters = true;
            return this;
        }

        public SHNDataBodyComposition build() {
            if(hasFatPercentage) {
                return shnDataBodyComposition;
            }else{
                throw new IllegalArgumentException("No fat percentage set");
            }
        }
    }
}
