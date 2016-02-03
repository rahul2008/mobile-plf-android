package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake;

import java.util.Set;

public class SHNCapabilityConfigEnergyIntakeWrapper implements SHNCapabilityConfigEnergyIntake {
    private final SHNCapabilityConfigEnergyIntake wrappedShnCapability;
    private final Handler internalHandler;
    private final Handler userHandler;


    public SHNCapabilityConfigEnergyIntakeWrapper(SHNCapabilityConfigEnergyIntake wrappedShnCapability, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapability = wrappedShnCapability;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void getSupportedMealTypes(final ResultListener<Set<MealType>> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSupportedMealTypes(new ResultListener<Set<MealType>>() {
                    @Override
                    public void onActionCompleted(final Set<MealType> value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setMealConfiguration(final MealConfiguration mealConfiguration, final MealType mealType, final SHNResultListener resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setMealConfiguration(mealConfiguration, mealType, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void getMealConfiguration(final MealType type, final ResultListener<MealConfiguration> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getMealConfiguration(type, new ResultListener<MealConfiguration>() {
                    @Override
                    public void onActionCompleted(final MealConfiguration value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }
}
