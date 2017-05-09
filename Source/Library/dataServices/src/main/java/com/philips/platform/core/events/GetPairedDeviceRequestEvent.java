package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DevicePairingListener;

public class GetPairedDeviceRequestEvent extends Event{
    private DevicePairingListener mDevicePairingListener;

    public GetPairedDeviceRequestEvent(DevicePairingListener devicePairingListener){
        mDevicePairingListener = devicePairingListener;
    }

    public DevicePairingListener getDevicePairingListener() {
        return mDevicePairingListener;
    }
}
