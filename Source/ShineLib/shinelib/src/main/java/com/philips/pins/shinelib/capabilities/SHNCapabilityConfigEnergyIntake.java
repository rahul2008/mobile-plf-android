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

        MealConfiguration(Map<SHNMealSize, Integer> mealSizeToEnergyIntakeMap) {
            this.mealSizeToEnergyIntakeMap = new HashMap<>(mealSizeToEnergyIntakeMap);
        }

        public int energyIntakeForMealSize(SHNMealSize mealSize) {
            Integer energyIntake = mealSizeToEnergyIntakeMap.get(mealSize);
            return (energyIntake != null) ? energyIntake : DEFAULT_ENERGY_INTAKE;
        }
    }

    void getSupportedMealTypes(ResultListener<Set<SHNMealType>> resultListener);

    void setMealConfiguration(MealConfiguration mealConfiguration, SHNMealType mealType, SHNResultListener resultListener);

    void getMealConfiguration(SHNMealType type, ResultListener<MealConfiguration> resultListener);
}
