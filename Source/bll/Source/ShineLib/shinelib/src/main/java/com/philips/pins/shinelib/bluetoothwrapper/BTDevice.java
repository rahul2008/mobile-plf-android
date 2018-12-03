/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.utility.SHNLogger;

public class BTDevice {
    private static final String TAG = "BTDevice";
    private BluetoothDevice device;
    private Handler handler;

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

    @Nullable
    public BTGatt connectGatt(final Context context, boolean autoConnect, SHNCentral shnCentral, final BTGatt.BTGattCallback callback, final int connectionPriority) {
        BTGatt btGatt = new BTGatt(shnCentral, callback, handler);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, autoConnect, btGatt);

        if (bluetoothGatt == null) {
            SHNLogger.e(TAG, "BluetoothGatt is null.");

            return null;
        } else {
            bluetoothGatt.requestConnectionPriority(connectionPriority);
            btGatt.setBluetoothGatt(bluetoothGatt);

            return btGatt;
        }
    }

    /**
     * Create bond.
     *
     * @return true, if bond didn't exist before and was created successfully, false otherwise.
     */
    public boolean createBond() {
        if (getBondState() == BluetoothDevice.BOND_NONE) {
            return device.createBond();
        }
        return false;
    }
}
