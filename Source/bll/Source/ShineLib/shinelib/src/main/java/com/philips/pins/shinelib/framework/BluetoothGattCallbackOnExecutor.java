/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.philips.pins.shinelib.ISHNBluetoothGattCallback;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.Utilities;

import java.util.concurrent.Executor;

public class BluetoothGattCallbackOnExecutor extends BluetoothGattCallback {
    private static final String TAG = BluetoothGattCallbackOnExecutor.class.getSimpleName();
    private ISHNBluetoothGattCallback iShnBluetoothGattCallback;
    private Executor executor;

    public BluetoothGattCallbackOnExecutor(Executor executor, ISHNBluetoothGattCallback iShnBluetoothGattCallback) {
        this.iShnBluetoothGattCallback = iShnBluetoothGattCallback;
        this.executor = executor;
    }

    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onConnectionStateChange(gatt, status, newState);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onServicesDiscovered(gatt, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        final byte[] value = characteristic.getValue();
        final byte[] data = value.clone();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onCharacteristicReadWithData(gatt, characteristic, status, data);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onCharacteristicWrite(gatt, characteristic, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        final byte[] data = characteristic.getValue().clone();
        SHNLogger.e(TAG, "onCharacteristicChanged: " + Utilities.byteToString(data));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onCharacteristicChangedWithData(gatt, characteristic, data);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onDescriptorRead(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        final byte[] data = descriptor.getValue().clone();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onDescriptorReadWithData(gatt, descriptor, status, data);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onDescriptorWrite(gatt, descriptor, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onReliableWriteCompleted(final BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onReliableWriteCompleted(gatt, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onReadRemoteRssi(final BluetoothGatt gatt, final int rssi, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                iShnBluetoothGattCallback.onReadRemoteRssi(gatt, rssi, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onMtuChanged(final BluetoothGatt gatt, final int mtu, final int status) {
// TODO This callback is introduced in v21 so a version test is needed.
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                iShnBluetoothGattCallback.onMtuChanged(gatt, mtu, status);
//            }
//        };
//        executor.execute(runnable);
    }
}
