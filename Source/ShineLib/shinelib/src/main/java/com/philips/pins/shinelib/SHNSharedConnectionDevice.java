package com.philips.pins.shinelib;

import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import java.util.concurrent.atomic.AtomicInteger;

public class SHNSharedConnectionDevice extends SHNDeviceWrapper {
    private static final String TAG = "SHNSharedConnectionDevice";

    private final AtomicInteger numberOfConnectsOutstanding = new AtomicInteger(0);
    private final Object lock = new Object();

    private final SHNDeviceListener shnDeviceListener = new SHNDeviceListener() {
        @Override
        public void onStateUpdated(SHNDevice shnDevice) {
            if (shnDevice.getState() == State.Disconnected && numberOfConnectsOutstanding.get() > 0) {
                SHNSharedConnectionDevice.super.connect();
            }
        }

        @Override
        public void onFailedToConnect(SHNDevice shnDevice, SHNResult result) {
            // Ignored
        }

        @Override
        public void onReadRSSI(int rssi) {
            // Ignored
        }
    };

    public SHNSharedConnectionDevice(SHNDevice shnDevice) {
        super(shnDevice);
        registerSHNDeviceListener(this.shnDeviceListener);
    }

    @Override
    public void connect() {
        synchronized (lock) {
            if (numberOfConnectsOutstanding.get() == 0) {
                super.connect();
            } else {
                notifyStateUpdated();
            }
            numberOfConnectsOutstanding.incrementAndGet();
            SHNLogger.d(TAG, "Number of connections outstanding: " + numberOfConnectsOutstanding.get());
        }
    }

    @Override
    public void connect(long connectTimeOut) {
        synchronized (lock) {
            if (numberOfConnectsOutstanding.get() == 0) {
                super.connect(connectTimeOut);
            } else {
                notifyStateUpdated();
            }

            numberOfConnectsOutstanding.incrementAndGet();
            SHNLogger.d(TAG, "Number of connections outstanding: " + numberOfConnectsOutstanding.get());
        }
    }

    @Override
    public void disconnect() {
        synchronized (lock) {
            if (numberOfConnectsOutstanding.decrementAndGet() == 0) {
                super.disconnect();
            } else {
                if (numberOfConnectsOutstanding.get() < 0) {
                    throw new IllegalStateException("Number of disconnect calls exceed the number of connect calls.");
                }
                notifyStateUpdated();
            }
            SHNLogger.d(TAG, "Number of connections outstanding: " + numberOfConnectsOutstanding.get());
        }
    }

    private void notifyStateUpdated() {
        for (SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
            shnDeviceListener.onStateUpdated(this);
        }
    }
}
