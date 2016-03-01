/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityBatteryWrapper implements SHNCapabilityBattery, SHNCapabilityBattery.SHNCapabilityBatteryListener {
    private static final String TAG = SHNCapabilityBatteryWrapper.class.getSimpleName();
    private final SHNCapabilityBattery wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;
    private SHNCapabilityBatteryListener shnCapabilityBatteryListener;

    public SHNCapabilityBatteryWrapper(SHNCapabilityBattery shnCapability, Handler internalHandler, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
        wrappedShnCapability.setSetSHNCapabilityBatteryListener(this);
    }

    // implements SHNCapabilityBattery
    @Override
    public void getBatteryLevel(final SHNIntegerResultListener listener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getBatteryLevel(new SHNIntegerResultListener() {
                    @Override
                    public void onActionCompleted(final int value, final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(value, result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setBatteryLevelNotifications(final boolean enabled, final SHNResultListener listener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setBatteryLevelNotifications(enabled, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setSetSHNCapabilityBatteryListener(final SHNCapabilityBatteryListener shnCapabilityBatteryListener) {
        this.shnCapabilityBatteryListener = shnCapabilityBatteryListener;
    }

    @Override
    public void onBatteryLevelChanged(final int level) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (shnCapabilityBatteryListener != null) {
                    shnCapabilityBatteryListener.onBatteryLevelChanged(level);
                }
            }
        };
        userHandler.post(callback);
    }
}

