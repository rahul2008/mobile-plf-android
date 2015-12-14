package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNResult;

public class QuickTestConnection {

    public interface Listener {
        void onSuccess();

        void onFailure();
    }

    private SHNDevice.SHNDeviceListener deviceListener;
    private SHNDevice device;

    public void execute(@NonNull final SHNDevice device, @NonNull final Listener listener) {
        stop();

        SHNDevice.State state = device.getState();
        if (state == SHNDevice.State.Disconnecting) {
            listener.onFailure();
        } else if (state == SHNDevice.State.Connected) {
            listener.onSuccess();
        } else {
            registerAndConnect(device, listener);
        }
    }

    private void registerAndConnect(final @NonNull SHNDevice device, final @NonNull Listener listener) {
        this.device = device;
        deviceListener = new SHNDevice.SHNDeviceListener() {
            @Override
            public void onStateUpdated(final SHNDevice shnDevice) {
                if (SHNDevice.State.Connected == shnDevice.getState()) {
                    listener.onSuccess();
                    stop();
                }
            }

            @Override
            public void onFailedToConnect(final SHNDevice shnDevice, final SHNResult result) {
                listener.onFailure();
                stop();
            }
        };

        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    public void stop() {
        if (device != null) {
            device.unregisterSHNDeviceListener(deviceListener);
            device = null;
            deviceListener = null;
        }
    }
}
