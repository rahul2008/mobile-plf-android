/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

public interface ISHNBluetoothGattCallback {
    void onConnectionStateChange(BluetoothGatt gatt, int status, int newState);

    void onServicesDiscovered(BluetoothGatt gatt, int status);

    void onCharacteristicReadWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data);

    void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicChangedWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data);

    void onDescriptorReadWithData(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data);

    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

    void onReliableWriteCompleted(BluetoothGatt gatt, int status);

    void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status);

    void onMtuChanged(BluetoothGatt gatt, int mtu, int status);
}
