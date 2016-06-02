/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothDevice;

public class BleDeviceFoundInfo {
    private final BluetoothDevice bluetoothDevice;
    private final int rssi;
    private final byte[] scanRecord;

    public BleDeviceFoundInfo(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.scanRecord = scanRecord.clone();
    }

    public String getDeviceAddress() {
        return (bluetoothDevice == null) ? null : bluetoothDevice.getAddress();
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }
}
