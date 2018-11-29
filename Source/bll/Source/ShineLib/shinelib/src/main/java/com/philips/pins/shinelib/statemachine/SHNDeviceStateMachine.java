package com.philips.pins.shinelib.statemachine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;

public class SHNDeviceStateMachine extends StateMachine<SHNDeviceState> {

    private final SHNDevice shnDevice;
    private final SHNDeviceResources sharedResources;

    public SHNDeviceStateMachine(@NonNull final SHNDevice shnDevice, @NonNull final SHNDeviceResources sharedResources) {
        super();

        this.shnDevice = shnDevice;
        this.sharedResources = sharedResources;
    }

    public SHNDevice getShnDevice() {
        return shnDevice;
    }

    public SHNDeviceResources getSharedResources() {
        return sharedResources;
    }

    private SHNDevice.SHNDeviceListener shnDeviceListener;

    public void registerSHNDeviceListener(SHNDevice.SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
    }

    public void unregisterSHNDeviceListener() {
        this.shnDeviceListener = null;
    }

    public void notifyFailureToListener(SHNResult result) {
        if (shnDeviceListener != null) {
            shnDeviceListener.onFailedToConnect(shnDevice, result);
        }
    }

    public void notifyStateToListener() {
        if (shnDeviceListener != null) {
            shnDeviceListener.onStateUpdated(shnDevice, shnDevice.getState());
        }
    }

    @Nullable
    public SHNDevice.SHNDeviceListener getDeviceListener() {
        return this.shnDeviceListener;
    }
}
