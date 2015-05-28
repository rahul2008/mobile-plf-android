package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 310188215 on 10/03/15.
 */
public class SHNDeviceFoundInfo {
    public final String deviceAddress;
    public final String deviceName;
    public final int rssi;
    private final byte[] scanRecord;
    public final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;

    public SHNDeviceFoundInfo(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        this.deviceAddress = bluetoothDevice.getAddress();
        this.deviceName = bluetoothDevice.getName();
        this.rssi = rssi;
        this.scanRecord = scanRecord.clone();
        this.shnDeviceDefinitionInfo = shnDeviceDefinitionInfo;
    }

    public int getRssi() { return rssi; }

    @Override
    public String toString() {
        return deviceName + " [" + deviceAddress + "]" + " (" + rssi + ")";
    }
}
