package com.pins.philips.shinelib.framework;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import java.util.concurrent.Executor;

/**
 * Created by 310188215 on 24/03/15.
 */
public class BluetoothGattCallbackOnExecutor extends BluetoothGattCallback {
    private static final String TAG = BluetoothGattCallbackOnExecutor.class.getSimpleName();
    private BluetoothGattCallback bluetoothGattCallback;
    private Executor executor;

    public BluetoothGattCallbackOnExecutor(Executor executor, BluetoothGattCallback bluetoothGattCallback) {
        this.bluetoothGattCallback = bluetoothGattCallback;
        this.executor = executor;
    }

    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
        Log.e(TAG, "onConnectionStateChange");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onConnectionStateChange(gatt, status, newState);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onServicesDiscovered(gatt, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onCharacteristicRead(gatt, characteristic, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onCharacteristicWrite(gatt, characteristic, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onCharacteristicChanged(gatt, characteristic);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onDescriptorRead(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onDescriptorRead(gatt, descriptor, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onDescriptorWrite(gatt, descriptor, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onReliableWriteCompleted(final BluetoothGatt gatt, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onReliableWriteCompleted(gatt, status);
            }
        };
        executor.execute(runnable);
    }

    @Override
    public void onReadRemoteRssi(final BluetoothGatt gatt, final int rssi, final int status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCallback.onReadRemoteRssi(gatt, rssi, status);
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
//                bluetoothGattCallback.onMtuChanged(gatt, mtu, status);
//            }
//        };
//        executor.execute(runnable);
    }
}
