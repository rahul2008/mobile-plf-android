/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;

public class BTDevice {
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

    public int getBondState() { return device.getBondState(); }

    public BTGatt connectGatt(final Context context, boolean autoConnect, final BTGatt.BTGattCallback callback) {
        btGatt = new BTGatt(callback, handler);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, autoConnect, btGatt);
        btGatt.setBluetoothGatt(bluetoothGatt);
        return btGatt;
    }


}
