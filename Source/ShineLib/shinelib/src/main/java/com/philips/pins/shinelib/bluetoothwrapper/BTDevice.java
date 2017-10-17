/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.workarounds.Workaround;

import java.lang.reflect.Method;

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

    public BTGatt connectGatt(final Context context, boolean autoConnect, SHNCentral shnCentral, final BTGatt.BTGattCallback callback) {
        btGatt = new BTGatt(shnCentral, callback, handler);
        BluetoothGatt bluetoothGatt = device.connectGatt(context, autoConnect, btGatt);
        btGatt.setBluetoothGatt(bluetoothGatt);

        if (Workaround.CORRUPTED_CACHE.isRequiredOnThisDevice()) {
            refresh(bluetoothGatt);
        }

        //Guard test for the null pointer deference and log lines to be able to detect that this problem would have occurred.
        if (bluetoothGatt == null) {
            SHNLogger.e(TAG, "device.connectGatt returns null");
        }
        return btGatt;
    }

    public boolean createBond() {
        if (getBondState() == BluetoothDevice.BOND_NONE) {
            return device.createBond();
        }
        return false;
    }

    /**
     * Clears the internal cache and forces a refresh of the services from the remote device.
     */
    private void refresh(final @NonNull BluetoothGatt bluetoothGatt) {
        try {
            /*
              Reflection is used because the method is hidden. There is no other method that offers
              this functionality. The refresh function solved Android BLE problems such as
              bonds being removed unexpected and not seeing when the services in the peripheral
              are changed (DFU). The impact of this reflection is very low, if the function is
              removed the library still works, only the BLE cache won't be cleared.
            */
            Method localMethod = BluetoothGatt.class.getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                boolean success = ((Boolean) localMethod.invoke(bluetoothGatt, new Object[0])).booleanValue();

                if (!success) {
                    SHNLogger.w(TAG, "BluetoothGatt refresh method failed to execute");
                }
            }
        } catch (Exception localException) {
            SHNLogger.e(TAG, "An exception occurred while refreshing BLE cache");
        }
    }

}
