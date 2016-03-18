/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class SHNUserConfigurationDispatcher implements SHNUserConfiguration {
    private static final String TAG = "UserConfigWrapper";

    @NonNull
    private final SHNUserConfiguration userConfiguration;

    @NonNull
    private final Handler internalHandler;
    private long internalThreadId;

    public SHNUserConfigurationDispatcher(@NonNull final SHNUserConfiguration userConfiguration, @NonNull final Handler internalHandler) {
        this.userConfiguration = userConfiguration;
        this.internalHandler = internalHandler;
        internalThreadId = getHandlerThreadId(internalHandler);
    }

    @Override
    public void clear() {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.clear();
            }
        });
    }

    @Override
    public ClockFormat getClockFormat() {
        return executeOnInternalThreadAndReturnResult(new Callable<ClockFormat>() {
            @Override
            public ClockFormat call() throws Exception {
                return userConfiguration.getClockFormat();
            }
        });
    }

    @Override
    public void setClockFormat(@NonNull final ClockFormat clockFormat) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setClockFormat(clockFormat);
            }
        });
    }

    @Override
    public String getIsoLanguageCode() {
        return executeOnInternalThreadAndReturnResult(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return userConfiguration.getIsoLanguageCode();
            }
        });
    }

    @Override
    public void setIsoLanguageCode(final String isoLanguageCode) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setIsoLanguageCode(isoLanguageCode);
            }
        });
    }

    @Override
    public String getIsoCountryCode() {
        return executeOnInternalThreadAndReturnResult(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return userConfiguration.getIsoCountryCode();
            }
        });
    }

    @Override
    public void setIsoCountryCode(final String isoCountryCode) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setIsoCountryCode(isoCountryCode);
            }
        });
    }

    @Override
    public Boolean getUseMetricSystem() {
        return executeOnInternalThreadAndReturnResult(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return userConfiguration.getUseMetricSystem();
            }
        });
    }

    @Override
    public void setUseMetricSystem(final Boolean useMetricSystem) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setUseMetricSystem(useMetricSystem);
            }
        });
    }

    @Override
    public Sex getSex() {
        return executeOnInternalThreadAndReturnResult(new Callable<Sex>() {
            @Override
            public Sex call() throws Exception {
                return userConfiguration.getSex();
            }
        });
    }

    @Override
    public void setSex(final Sex sex) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setSex(sex);
            }
        });
    }

    @Override
    public Integer getRestingHeartRate() {
        return executeOnInternalThreadAndReturnResult(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return userConfiguration.getRestingHeartRate();
            }
        });
    }

    @Override
    public void setRestingHeartRate(final Integer restingHeartRate) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setRestingHeartRate(restingHeartRate);
            }
        });
    }

    @Override
    public Integer getHeightInCm() {
        return executeOnInternalThreadAndReturnResult(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return userConfiguration.getHeightInCm();
            }
        });
    }

    @Override
    public void setHeightInCm(final Integer heightInCm) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setHeightInCm(heightInCm);
            }
        });
    }

    @Override
    public Double getWeightInKg() {
        return executeOnInternalThreadAndReturnResult(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return userConfiguration.getWeightInKg();
            }
        });
    }

    @Override
    public void setWeightInKg(final Double weightInKg) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setWeightInKg(weightInKg);
            }
        });
    }

    @Override
    public Handedness getHandedness() {
        return executeOnInternalThreadAndReturnResult(new Callable<Handedness>() {
            @Override
            public Handedness call() throws Exception {
                return userConfiguration.getHandedness();
            }
        });
    }

    @Override
    public void setHandedness(final Handedness handedness) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setHandedness(handedness);
            }
        });
    }

    @Override
    public Character getDecimalSeparator() {
        return executeOnInternalThreadAndReturnResult(new Callable<Character>() {
            @Override
            public Character call() throws Exception {
                return userConfiguration.getDecimalSeparator();
            }
        });
    }

    @Override
    public void setDecimalSeparator(final Character decimalSeparator) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setDecimalSeparator(decimalSeparator);
            }
        });
    }

    @Override
    public Date getDateOfBirth() {
        return executeOnInternalThreadAndReturnResult(new Callable<Date>() {
            @Override
            public Date call() throws Exception {
                return userConfiguration.getDateOfBirth();
            }
        });
    }

    @Override
    public void setDateOfBirth(final Date dateOfBirth) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setDateOfBirth(dateOfBirth);
            }
        });
    }

    @Override
    public Integer getMaxHeartRate() {
        return executeOnInternalThreadAndReturnResult(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return userConfiguration.getMaxHeartRate();
            }
        });
    }

    @Override
    public void setMaxHeartRate(final Integer maxHeartRate) {
        executeOnInternalThreadAndReturnResult(new VoidCallable() {
            @Override
            public void run() {
                userConfiguration.setMaxHeartRate(maxHeartRate);
            }
        });
    }

    @Override
    public Integer getAge() {
        return executeOnInternalThreadAndReturnResult(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return userConfiguration.getAge();
            }
        });
    }

    @Override
    public Integer getBaseMetabolicRate() {
        return executeOnInternalThreadAndReturnResult(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return userConfiguration.getBaseMetabolicRate();
            }
        });
    }

    private <T> T executeOnInternalThreadAndReturnResult(final Callable<T> callback) {
        T value = null;

        if (internalThreadId == getCurrentThreadId()) {
            try {
                value = callback.call();
            } catch (Exception e) {
                SHNLogger.e(TAG, "execute sync exception", e);
            }
        } else {
            FutureTask<T> futureTask = getFutureTask(callback);
            internalHandler.post(futureTask);
            try {
                value = futureTask.get();
            } catch (Exception e) {
                SHNLogger.e(TAG, "execute async exception", e);
            }
        }

        return value;
    }

    @NonNull
    <T> FutureTask getFutureTask(final Callable<T> callback) {
        return new FutureTask<>(callback);
    }

    long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }

    long getHandlerThreadId(final @NonNull Handler internalHandler) {
        return internalHandler.getLooper().getThread().getId();
    }

    @NonNull
    public SHNUserConfiguration getUserConfigurationInternal() {
        return userConfiguration;
    }

    private static abstract class VoidCallable implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            run();
            return null;
        }

        public abstract void run();
    }
}
