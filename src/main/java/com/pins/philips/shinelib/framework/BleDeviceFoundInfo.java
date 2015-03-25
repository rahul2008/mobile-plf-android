package com.pins.philips.shinelib.framework;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 310188215 on 10/03/15.
 */
public class BleDeviceFoundInfo {
    public final BluetoothDevice bluetoothDevice;
    public final int rssi;
    public final byte[] scanRecord;
    public final Object callbackParameter;

    public BleDeviceFoundInfo(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord, Object callbackParameter) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
        this.scanRecord = scanRecord.clone();
        this.callbackParameter = callbackParameter;
    }

    public String getDeviceAddress() {
        return (bluetoothDevice == null) ? null : bluetoothDevice.getAddress();
    }
}
