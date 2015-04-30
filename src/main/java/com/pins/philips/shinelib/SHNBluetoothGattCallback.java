package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

/**
 * Created by 310188215 on 30/04/15.
 */
public class SHNBluetoothGattCallback implements ISHNBluetoothGattCallback {
    private static final boolean LOGGING = false;
    private static final String TAG = SHNBluetoothGattCallback.class.getSimpleName();

    private final SHNCentral shnCentral;
    private final SHNDevice shnDevice;

    public SHNBluetoothGattCallback(SHNCentral shnCentral, SHNDevice shnDevice) {
        this.shnCentral = shnCentral;
        this.shnDevice = shnDevice;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (LOGGING) Log.i(TAG, "onConnectionStateChange");
        try {
            shnDevice.handleOnConnectionStateChange(gatt, status, newState);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (LOGGING) Log.i(TAG, "onServicesDiscovered");
        try {
            shnDevice.handleOnServicesDiscovered(gatt, status);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onCharacteristicReadWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
        if (LOGGING) Log.i(TAG, "onCharacteristicRead");
        try {
            shnDevice.handleOnCharacteristicRead(gatt, characteristic, status, data);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (LOGGING) Log.i(TAG, "onCharacteristicWrite: " + status);
        try {
            shnDevice.handleOnCharacteristicWrite(gatt, characteristic, status);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onCharacteristicChangedWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
        if (LOGGING) Log.i(TAG, "onCharacteristicChanged");
        try {
            shnDevice.handleOnCharacteristicChanged(gatt, characteristic, data);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onDescriptorReadWithData(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        if (LOGGING) Log.i(TAG, "onDescriptorRead");
        try {
            shnDevice.handleOnDescriptorRead(gatt, descriptor, status, data);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (LOGGING) Log.i(TAG, "onDescriptorWrite");
        try {
            shnDevice.handleOnDescriptorWrite(gatt, descriptor, status);
        } catch (Exception e) {
            shnCentral.reportExceptionOnAppMainThread(e);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        if (LOGGING) Log.i(TAG, "onReliableWriteCompleted");
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if (LOGGING) Log.i(TAG, "onReadRemoteRssi");
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if (LOGGING) Log.i(TAG, "onMtuChanged");
    }
}
