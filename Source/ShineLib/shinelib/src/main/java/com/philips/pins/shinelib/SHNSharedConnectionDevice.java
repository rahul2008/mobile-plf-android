/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import java.util.concurrent.atomic.AtomicInteger;

public class SHNSharedConnectionDevice extends SHNDeviceWrapper implements SHNDevice.SHNDeviceListener {
    private static final String TAG = "SHNSharedConnectionDevice";

    private Runnable connectCommand;
    private final AtomicInteger numberOfConnectsOutstanding = new AtomicInteger(0);
    private final Object lock = new Object();

    public SHNSharedConnectionDevice(SHNDevice shnDevice) {
        super(shnDevice);
        registerSHNDeviceListener(this);
    }

    @Override
    public void connect() {
        synchronized (lock) {
            if (numberOfConnectsOutstanding.get() == 0) {
                connectCommand = createConnectCommand(0L);
                connectCommand.run();
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
                connectCommand = createConnectCommand(connectTimeOut);
                connectCommand.run();
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

    @Override
    public void onStateUpdated(SHNDevice shnDevice) {
        if (shnDevice.getState() == State.Disconnected && numberOfConnectsOutstanding.get() > 0) {
            if (connectCommand != null) {
                connectCommand.run();
            }
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

    @NonNull
    private Runnable createConnectCommand(final long timeout) {
        return new Runnable() {
            @Override
            public void run() {
                if (timeout > 0L) {
                    SHNSharedConnectionDevice.super.connect(timeout);
                } else {
                    SHNSharedConnectionDevice.super.connect();
                }
            }
        };
    }

    private void notifyStateUpdated() {
        for (SHNDeviceListener shnDeviceListener : shnDeviceListeners) {
            shnDeviceListener.onStateUpdated(this);
        }
    }
}
