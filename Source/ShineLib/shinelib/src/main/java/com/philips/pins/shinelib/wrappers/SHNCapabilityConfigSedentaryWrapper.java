package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary;

public class SHNCapabilityConfigSedentaryWrapper implements SHNCapabilityConfigSedentary {

    private final SHNCapabilityConfigSedentary wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;

    public SHNCapabilityConfigSedentaryWrapper(SHNCapabilityConfigSedentary shnCapability, Handler internalHandler, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void setSedentaryPeriodInMinutes(final short minutes, @NonNull final ResultListener<Short> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setSedentaryPeriodInMinutes(minutes, createResultListener(resultListener));
            }
        });
    }

    @Override
    public void getSedentaryPeriodInMinutes(@NonNull final ResultListener<Short> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSedentaryPeriodInMinutes(createResultListener(resultListener));
            }
        });
    }

    @Override
    public void setSedentaryNotificationEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setSedentaryNotificationEnabled(enabled, createResultListener(resultListener));
            }
        });
    }

    @Override
    public void getSedentaryNotificationEnabled(@NonNull final ResultListener<Boolean> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSedentaryNotificationEnabled(createResultListener(resultListener));
            }
        });
    }

    @NonNull
    private <T> ResultListener<T> createResultListener(@NonNull final ResultListener<T> resultListener) {
        return new ResultListener<T>() {
            @Override
            public void onActionCompleted(final T value, @NonNull final SHNResult result) {
                Runnable resultRunnable = new Runnable() {
                    @Override
                    public void run() {
                        resultListener.onActionCompleted(value, result);
                    }
                };
                userHandler.post(resultRunnable);
            }
        };
    }
}
