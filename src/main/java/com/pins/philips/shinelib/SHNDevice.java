package com.pins.philips.shinelib;

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

    void setSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    Set<SHNCapabilityType> getSupportedCapabilityTypes();

    SHNCapability getCapabilityForType(SHNCapabilityType type);

    public enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    public interface SHNDeviceListener {
        void onStateUpdated(SHNDevice shnDevice);
    }
}
