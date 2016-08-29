/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.CapabilityBluetoothDirect;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import java.util.UUID;

public class CapabilityBluetoothDirectWrapper
        implements CapabilityBluetoothDirect, CapabilityBluetoothDirect.CharacteristicChangedListener {

    @NonNull private final CapabilityBluetoothDirect wrappedShnCapability;
    @NonNull private final Handler userHandler;
    @NonNull private final Handler internalHandler;
    @Nullable private CharacteristicChangedListener characteristicChangedListener;

    public CapabilityBluetoothDirectWrapper(@NonNull CapabilityBluetoothDirect shnCapability,
            @NonNull Handler internalHandler, @NonNull Handler userHandler) {
        this.wrappedShnCapability = shnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
        this.wrappedShnCapability.setCharacteristicChangedListener(this);
    }

    @Override
    public void readCharacteristic(@NonNull final ResultListener<SHNDataRaw> listener, @NonNull final UUID uuid) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.readCharacteristic(new ResultListener<SHNDataRaw>() {

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
    public void writeCharacteristic(@NonNull final SHNResultListener listener, @NonNull final UUID uuid,
            final byte[] data) {
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
    public void onCharacteristicChanged(@NonNull final UUID characteristic, final byte[] data, final int status) {
        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (characteristicChangedListener != null) {
                    characteristicChangedListener.onCharacteristicChanged(characteristic, data, status);
                }
            }
        };
        userHandler.post(callback);
    }

    @Override
    public void setNotifyOnCharacteristicChange(@NonNull final SHNResultListener listener, @NonNull final UUID uuid, final boolean notify) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setNotifyOnCharacteristicChange(new SHNResultListener() {

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
                }, uuid, notify);
            }
        };
        internalHandler.post(command);
    }

    public void setCharacteristicChangedListener(@NonNull CharacteristicChangedListener listener) {
        this.characteristicChangedListener = listener;
    }
}
