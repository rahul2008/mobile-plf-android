package com.philips.pins.shinelib.utility;

import android.os.Handler;
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
    private final Handler internalHandler;

    public QuickTestConnection(Handler handler) {
        internalHandler = handler;
    }

    public void execute(@NonNull final SHNDevice device, @NonNull final Listener listener) {
        stop();

        SHNDevice.State state = device.getState();
        if (state == SHNDevice.State.Disconnecting) {
            listener.onFailure();
        } else if (state == SHNDevice.State.Connected) {
            listener.onSuccess();
        } else {
            registerAndConnect(device, new Listener() {

                private boolean alreadyReportedResult;

                @Override
                public void onSuccess() {
                    if (!alreadyReportedResult) {
                        alreadyReportedResult = true;
                        listener.onSuccess();
                    }
                }

                @Override
                public void onFailure() {
                    if (!alreadyReportedResult) {
                        alreadyReportedResult = true;
                        listener.onFailure();
                    }
                }
            });
        }
    }

    private void registerAndConnect(final @NonNull SHNDevice device, final @NonNull Listener listener) {
        this.device = device;
        deviceListener = new SHNDevice.SHNDeviceListener() {

            private boolean skippedFirstStateUpdate;

            @Override
            public void onStateUpdated(final SHNDevice shnDevice) {

                if (!skippedFirstStateUpdate) {
                    skippedFirstStateUpdate = true;
                    return;
                }

                internalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (SHNDevice.State.Connected == shnDevice.getState()) {
                            stop();
                            listener.onSuccess();
                        } else if (SHNDevice.State.Disconnected == shnDevice.getState()) {
                            stop();
                            listener.onFailure();
                        }
                    }
                });
            }

            @Override
            public void onFailedToConnect(final SHNDevice shnDevice, final SHNResult result) {

                internalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stop();
                        listener.onFailure();
                    }
                });
            }

            @Override
            public void onReadRSSI(int rssi) {
            }
        };

        device.registerSHNDeviceListener(deviceListener);
        device.connect();
    }

    public void stop() {
        if (device != null) {
            device.unregisterSHNDeviceListener(deviceListener);
            device.disconnect();
            device = null;
            deviceListener = null;
        }
    }
}
