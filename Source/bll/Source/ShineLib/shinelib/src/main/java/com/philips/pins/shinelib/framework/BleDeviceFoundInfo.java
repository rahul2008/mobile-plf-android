/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;

public class BleDeviceFoundInfo {
    private final BluetoothDevice bluetoothDevice;
    private final int rssi;
    private final ScanRecord scanRecord;

    public BleDeviceFoundInfo(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
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

    public ScanRecord getScanRecord() {
        return scanRecord;
    }
}
