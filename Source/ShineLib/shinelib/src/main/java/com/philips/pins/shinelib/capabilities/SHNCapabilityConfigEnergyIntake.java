package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SHNCapabilityConfigEnergyIntake extends SHNCapability {

    enum SHNMealType {
        Unknown,
        Breakfast,
        Lunch,
        Dinner,
        Snack,
        Drink
    }

    enum SHNMealSize {
        Unknown,
        Small,
        Medium,
        Large
    }

    class MealConfiguration {

        public static final int DEFAULT_ENERGY_INTAKE = 0;
        private Map<SHNMealSize, Integer> mealSizeToEnergyIntakeMap;

        public MealConfiguration(Map<SHNMealSize, Integer> mealSizeToEnergyIntakeMap) {
            this.mealSizeToEnergyIntakeMap = new HashMap<>(mealSizeToEnergyIntakeMap);
        }

        public int energyIntakeForMealSize(SHNMealSize mealSize) {
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

    void getSupportedMealTypes(ResultListener<Set<SHNMealType>> resultListener);

    void setMealConfiguration(MealConfiguration mealConfiguration, SHNMealType mealType, SHNResultListener resultListener);

    void getMealConfiguration(SHNMealType type, ResultListener<MealConfiguration> resultListener);
}
