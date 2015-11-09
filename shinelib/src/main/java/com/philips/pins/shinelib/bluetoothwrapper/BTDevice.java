package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;

/**
 * Created by 310188215 on 04/05/15.
 */
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

    public BTGatt connectGatt(final Context context, boolean autoConnect, final BTGatt.BTGattCallback callback) {
        btGatt = new BTGatt(callback, handler);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, false, btGatt);
        btGatt.setBluetoothGatt(bluetoothGatt);
        return btGatt;
    }


}
