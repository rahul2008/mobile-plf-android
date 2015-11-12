package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;

import java.lang.ref.WeakReference;

/**
 * Created by 310188215 on 10/03/15.
 */
public class SHNDeviceFoundInfo {
    private static WeakReference<SHNCentral> weakSHNCentral = new WeakReference<SHNCentral>(null);
    public static void setSHNCentral(SHNCentral shnCentral) {
        weakSHNCentral = new WeakReference<SHNCentral>(shnCentral);
    }

    private final String deviceAddress;
    private final String deviceName;
    private final int rssi;
    private final byte[] scanRecord;
    private final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;
    private final SHNDevice shnDevice;

    public SHNDeviceFoundInfo(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        SHNCentral shnCentral = weakSHNCentral.get();
        if (shnCentral == null) {
            throw new IllegalStateException("SHNCentral not set");
        }
        this.deviceAddress = bluetoothDevice.getAddress();
        this.deviceName = bluetoothDevice.getName();
        this.shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(deviceAddress, shnDeviceDefinitionInfo);
        this.rssi = rssi;
        this.scanRecord = scanRecord.clone();
        this.shnDeviceDefinitionInfo = shnDeviceDefinitionInfo;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getRssi() {
        return rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public SHNDeviceDefinitionInfo getShnDeviceDefinitionInfo() {
        return shnDeviceDefinitionInfo;
    }

    public SHNDevice getShnDevice() {
        return shnDevice;
    }

    @Override
    public String toString() {
        return deviceName + " [" + deviceAddress + "]" + " (" + rssi + ")";
    }

}
