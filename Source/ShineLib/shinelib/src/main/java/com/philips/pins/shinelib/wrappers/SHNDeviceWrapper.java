/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SHNDeviceWrapper implements SHNDevice {
    private static final String TAG = SHNDeviceWrapper.class.getSimpleName();
    private final SHNDevice shnDevice;
    private static Handler tempInternalHandler;
    private static Handler tempUserHandler;
    private final Handler internalHandler;
    private final Handler userHandler;
    private List<SHNDeviceListener> shnDeviceListeners;

    SHNDevice.SHNDeviceListener shnDeviceListener = new SHNDeviceListener() {
        @Override
        public void onStateUpdated(@NonNull SHNDevice shnDevice) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice)
                throw new IllegalArgumentException();
            synchronized (shnDeviceListeners) {
                for (final SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
                    if (shnDeviceListener != null) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnDeviceListener.onStateUpdated(SHNDeviceWrapper.this);
                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onFailedToConnect(@NonNull SHNDevice shnDevice, final SHNResult result) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice)
                throw new IllegalArgumentException();
            synchronized (shnDeviceListeners) {
                for (final SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
                    if (shnDeviceListener != null) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                shnDeviceListener.onFailedToConnect(SHNDeviceWrapper.this, result);
                            }
                        });
                    }
                }
            }
        }
    };

    public static void setHandlers(Handler internalHandler, Handler userHandler) {
        tempInternalHandler = internalHandler;
        tempUserHandler = userHandler;
    }

    public SHNDeviceWrapper(SHNDevice shnDevice) {
        this.shnDevice = shnDevice;
        this.internalHandler = tempInternalHandler;
        this.userHandler = tempUserHandler;
        shnDevice.registerSHNDeviceListener(shnDeviceListener);
        shnDeviceListeners = new ArrayList<>();
    }

    public boolean isBonded() {
        return ((SHNDeviceImpl) shnDevice).isBonded();
    }

    // implements SHNDevice
    @Override
    public State getState() {
        return shnDevice.getState();
    }

    @Override
    public String getAddress() {
        return shnDevice.getAddress();
    }

    @Override
    public String getName() {
        return shnDevice.getName();
    }

    @Override
    public String getDeviceTypeName() {
        return shnDevice.getDeviceTypeName();
    }

    @Override
    public void connect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDevice.connect();
            }
        };
        internalHandler.post(runnable);
    }

    public void connect(final boolean withTimeout, final long timeoutInMS) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ((SHNDeviceImpl) shnDevice).connect(withTimeout, timeoutInMS);
            }
        };
        internalHandler.post(runnable);
    }

    @Override
    public void disconnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDevice.disconnect();
            }
        };
        internalHandler.post(runnable);
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        synchronized (shnDeviceListeners) {
            if (!shnDeviceListeners.contains(shnDeviceListener)) {
                shnDeviceListeners.add(shnDeviceListener);
            }
        }
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        synchronized (shnDeviceListeners) {
            shnDeviceListeners.remove(shnDeviceListener);
        }
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return shnDevice.getSupportedCapabilityTypes();
    }

    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return shnDevice.getCapabilityForType(type);
    }
}
