/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@SuppressWarnings("FieldCanBeLocal")
public class SHNDeviceWrapper implements SHNDevice {
    private final SHNDevice shnDevice;
    private static Handler tempInternalHandler;
    private static Handler tempUserHandler;
    private final Handler internalHandler;
    private final Handler userHandler;
    private final Set<SHNDeviceListener> shnDeviceListeners;
    private final Set<DiscoveryListener> discoveryListeners;

    private final SHNDevice.SHNDeviceListener shnDeviceListener = new SHNDeviceListener() {
        @Override
        public void onStateUpdated(@NonNull SHNDevice shnDevice, @NonNull final State state) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice) {
                throw new IllegalArgumentException();
            }

            for (final SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
                if (shnDeviceListener != null) {
                    userHandler.post(() -> shnDeviceListener.onStateUpdated(SHNDeviceWrapper.this, state));
                }
            }
        }

        @Override
        public void onFailedToConnect(@NonNull SHNDevice shnDevice, @NonNull final SHNResult result) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice) {
                throw new IllegalArgumentException();
            }

            for (final SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
                if (shnDeviceListener != null) {
                    userHandler.post(() -> shnDeviceListener.onFailedToConnect(SHNDeviceWrapper.this, result));
                }
            }
        }

        @Override
        public void onReadRSSI(final int rssi) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice) {
                throw new IllegalArgumentException();
            }

            for (final SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
                if (shnDeviceListener != null) {
                    userHandler.post(() -> shnDeviceListener.onReadRSSI(rssi));
                }
            }
        }
    };

    private final DiscoveryListener discoveryListener = new DiscoveryListener() {
        @Override
        public void onServiceDiscovered(@NonNull final UUID serviceUuid, @Nullable final SHNService service) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice) {
                throw new IllegalArgumentException();
            }

            for (final DiscoveryListener discoveryListener : discoveryListeners) {
                if (discoveryListener != null) {
                    userHandler.post(() -> discoveryListener.onServiceDiscovered(serviceUuid, service));
                }
            }
        }

        @Override
        public void onCharacteristicDiscovered(@NonNull final UUID characteristicUuid, final byte[] data, @Nullable final SHNCharacteristic characteristic) {
            if (BuildConfig.DEBUG && SHNDeviceWrapper.this.shnDevice != shnDevice) {
                throw new IllegalArgumentException();
            }

            for (final DiscoveryListener discoveryListener : discoveryListeners) {
                if (discoveryListener != null) {
                    userHandler.post(() -> discoveryListener.onCharacteristicDiscovered(characteristicUuid, data, characteristic));
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
        shnDevice.registerDiscoveryListener(discoveryListener);
        shnDeviceListeners = new CopyOnWriteArraySet<>();
        discoveryListeners = new CopyOnWriteArraySet<>();
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
    public void setName(String name) {
        shnDevice.setName(name);
    }

    @Override
    public String getDeviceTypeName() {
        return shnDevice.getDeviceTypeName();
    }

    @Override
    public void connect() {
        Runnable runnable = shnDevice::connect;
        internalHandler.post(runnable);
    }

    @Override
    public void connect(final long connectTimeOut) {
        Runnable runnable = () -> shnDevice.connect(connectTimeOut);
        internalHandler.post(runnable);
    }

    @Override
    public void disconnect() {
        Runnable runnable = shnDevice::disconnect;
        internalHandler.post(runnable);
    }

    @Override
    public void readRSSI() {
        final Runnable runnable = shnDevice::readRSSI;
        internalHandler.post(runnable);
    }

    @Override
    public void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        shnDeviceListeners.add(shnDeviceListener);
    }

    @Override
    public void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener) {
        shnDeviceListeners.remove(shnDeviceListener);
    }

    @Override
    public void registerDiscoveryListener(final DiscoveryListener discoveryListener) {
        discoveryListeners.add(discoveryListener);
    }

    @Override
    public void unregisterDiscoveryListener(final DiscoveryListener discoveryListener) {
        discoveryListeners.remove(discoveryListener);
    }

    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return shnDevice.getSupportedCapabilityTypes();
    }

    @Override
    public Set<Class<? extends SHNCapability>> getSupportedCapabilityClasses() {
        return shnDevice.getSupportedCapabilityClasses();
    }

    @Override
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return shnDevice.getCapabilityForType(type);
    }

    @Nullable
    @Override
    public <T extends SHNCapability> T getCapability(@NonNull Class<T> type) {
        return shnDevice.getCapability(type);
    }
}
