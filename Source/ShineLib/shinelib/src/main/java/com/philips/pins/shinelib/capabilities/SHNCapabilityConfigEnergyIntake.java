package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SHNCapabilityConfigEnergyIntake extends SHNCapability {

    enum MealType {
        UNKNOWN,
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACK,
        DRINK
    }

    enum MealSize {
        UNKNOWN,
        SMALL,
        MEDIUM,
        LARGE
    }

    class MealConfiguration {

        public static final int DEFAULT_ENERGY_INTAKE = 0;
        private Map<MealSize, Integer> mealSizeToEnergyIntakeMap;

        public MealConfiguration(Map<MealSize, Integer> mealSizeToEnergyIntakeMap) {
            this.mealSizeToEnergyIntakeMap = new HashMap<>(mealSizeToEnergyIntakeMap);
        }

        public int energyIntakeForMealSize(MealSize mealSize) {
            Integer energyIntake = mealSizeToEnergyIntakeMap.get(mealSize);
            return (energyIntake != null) ? energyIntake : DEFAULT_ENERGY_INTAKE;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MealConfiguration that = (MealConfiguration) o;

            return !(mealSizeToEnergyIntakeMap != null ? !mealSizeToEnergyIntakeMap.equals(that.mealSizeToEnergyIntakeMap) : that.mealSizeToEnergyIntakeMap != null);

        }

        @Override
        public int hashCode() {
            return mealSizeToEnergyIntakeMap != null ? mealSizeToEnergyIntakeMap.hashCode() : 0;
        }
    }

    void getSupportedMealTypes(ResultListener<Set<MealType>> resultListener);

    void setMealConfiguration(MealConfiguration mealConfiguration, MealType mealType, SHNResultListener resultListener);

    void getMealConfiguration(MealType type, ResultListener<MealConfiguration> resultListener);
}
