/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.philips.pins.shinelib.SHNDataRawResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityGenericCharacteristic;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import java.util.UUID;

public class CapabilityGenericCharacteristicWrapper
        implements CapabilityGenericCharacteristic, CapabilityGenericCharacteristic.CharacteristicChangedListener {

    private static final String TAG = CapabilityGenericCharacteristicWrapper.class.getSimpleName();

    private final CapabilityGenericCharacteristic wrappedShnCapability;
    private final Handler userHandler;
    private final Handler internalHandler;
    private CharacteristicChangedListener characteristicChangedListener;

    public CapabilityGenericCharacteristicWrapper(CapabilityGenericCharacteristic shnCapability, Handler internalHandler, Handler userHandler) {
        wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
        wrappedShnCapability.setCharacteristicChangedListener(this);
    }

    @Override
    public void readCharacteristic(final SHNDataRawResultListener listener, final UUID uuid) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.readCharacteristic(new SHNDataRawResultListener() {

                    @Override
                    public void onActionCompleted(final SHNDataRaw value, @NonNull final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(value, result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                }, uuid);
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void writeCharacteristic(final SHNResultListener listener, final UUID uuid, final byte[] data) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.writeCharacteristic(new SHNResultListener() {

                    @Override
                    public void onActionCompleted(@NonNull final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                }, uuid, data);
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void onCharacteristicChanged(final UUID aChar, final byte[] data, final int status) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (characteristicChangedListener != null) {
                    characteristicChangedListener.onCharacteristicChanged(aChar, data, status);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void setNotify(final SHNResultListener listener, final boolean notify, final UUID uuid) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setNotify(new SHNResultListener() {

                    @Override
                    public void onActionCompleted(@NonNull final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                listener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                }, notify, uuid);
            }
        };
        internalHandler.post(command);
    }

    public void setCharacteristicChangedListener(CharacteristicChangedListener listener) {
        this.characteristicChangedListener = listener;
    }
}
