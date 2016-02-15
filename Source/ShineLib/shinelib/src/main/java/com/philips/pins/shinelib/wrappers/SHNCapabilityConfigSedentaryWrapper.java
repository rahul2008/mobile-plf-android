package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary;

public class SHNCapabilityConfigSedentaryWrapper implements SHNCapabilityConfigSedentary {

    private final SHNCapabilityConfigSedentary wrappedShnCapability;
    private final Handler internalHandler;
    private final Handler userHandler;

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
                wrappedShnCapability.setSedentaryPeriodInMinutes(minutes, new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }

    @Override
    public void getSedentaryPeriodInMinutes(@NonNull final ResultListener<Short> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSedentaryPeriodInMinutes(new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }

    @Override
    public void setSedentaryNotificationEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setSedentaryNotificationEnabled(enabled, new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }

    @Override
    public void getSedentaryNotificationEnabled(@NonNull final ResultListener<Boolean> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSedentaryNotificationEnabled(new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }
}
