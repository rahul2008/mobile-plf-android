package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DevicePairingListener;

public class UnPairDeviceRequestEvent extends Event{
    private String mDeviceId;
    private DevicePairingListener mDevicePairingListener;

    public UnPairDeviceRequestEvent(String deviceID, DevicePairingListener devicePairingListener) {
        mDeviceId = deviceID;
        mDevicePairingListener = devicePairingListener;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public DevicePairingListener getDevicePairingListener() {
        return mDevicePairingListener;
    }
}
