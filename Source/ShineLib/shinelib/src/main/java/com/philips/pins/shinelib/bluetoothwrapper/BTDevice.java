/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;

import com.philips.pins.shinelib.utility.SHNLogger;

public class BTDevice {
    private static final String TAG = "BTDevice";
    private BluetoothDevice device;
    private Handler handler;
    private BTGatt btGatt;

    public BTDevice(BluetoothDevice device, Handler handler) {
        this.device = device;
        this.handler = handler;
    }

    public String getName() {
        return device.getName();
    }

    public String getAddress() {
        return device.getAddress();
    }

    public int getBondState() {
        return device.getBondState();
    }

    public BTGatt connectGatt(final Context context, boolean autoConnect, final BTGatt.BTGattCallback callback) {
        btGatt = new BTGatt(callback, handler);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, autoConnect, btGatt);
        btGatt.setBluetoothGatt(bluetoothGatt);

         //Guard test for the null pointer deference and log lines to be able to detect that this problem would have occurred.
        if (bluetoothGatt == null) {
            SHNLogger.e(TAG, "device.connectGatt returns null");
        }
        return btGatt;
    }


}
