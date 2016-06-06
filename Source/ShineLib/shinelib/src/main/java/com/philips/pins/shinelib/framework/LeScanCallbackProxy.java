/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;

import java.util.UUID;

/* This class prevents the Android Bluetooth stack from holding on the object that implements the
 * LeScanCallback and *everything it references* after stopping the scan.
 *
 * As an extra the class is used to provide a general purpose parameter in the device detected callback.
 */
public class LeScanCallbackProxy implements BluetoothAdapter.LeScanCallback {

    public interface LeScanCallback {
        void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    }

    private LeScanCallback leScanCallback;

    public boolean startLeScan(LeScanCallback leScanCallback, Object callbackParameter) {
        this.leScanCallback = leScanCallback;
        return BleUtilities.startLeScan(this);
    }

    public boolean startLeScan(UUID[] serviceUUIDs, LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
        return BleUtilities.startLeScan(serviceUUIDs, this);
    }

    public void stopLeScan(LeScanCallback leScanCallback) {
        if (leScanCallback == this.leScanCallback) {
            BleUtilities.stopLeScan(this);
            this.leScanCallback = null;
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (leScanCallback != null) {
            leScanCallback.onLeScan(device, rssi, scanRecord);
        }
    }
}

