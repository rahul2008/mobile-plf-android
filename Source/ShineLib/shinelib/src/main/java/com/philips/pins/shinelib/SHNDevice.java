/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Set;

/**
 * Created by 310188215 on 05/05/15.
 */
public interface SHNDevice {
    State getState();

    String getAddress();

    String getName();

    String getDeviceTypeName();

    void connect();

    void disconnect();

    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    Set<SHNCapabilityType> getSupportedCapabilityTypes();

    SHNCapability getCapabilityForType(SHNCapabilityType type);

    enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    interface SHNDeviceListener {
        void onStateUpdated(SHNDevice shnDevice);
        void onFailedToConnect(SHNDevice shnDevice, SHNResult result);
    }
}
