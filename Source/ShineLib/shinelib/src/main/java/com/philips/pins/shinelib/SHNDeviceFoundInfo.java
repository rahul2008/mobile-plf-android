/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.BleScanRecord;

import java.lang.ref.WeakReference;

/**
 * Created by 310188215 on 10/03/15.
 */
public class SHNDeviceFoundInfo {
    private static WeakReference<SHNCentral> weakSHNCentral = new WeakReference<SHNCentral>(null);

    public static void setSHNCentral(SHNCentral shnCentral) {
        weakSHNCentral = new WeakReference<SHNCentral>(shnCentral);
    }

    @NonNull
    private final String deviceAddress;

    @NonNull
    private final String deviceName;

    @NonNull
    private final int rssi;

    @NonNull
    private final byte[] scanRecord;

    @NonNull
    private final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo;

    @NonNull
    private final SHNDevice shnDevice;

    @NonNull
    private final BleScanRecord bleScanRecord;

    public SHNDeviceFoundInfo(@NonNull final BluetoothDevice bluetoothDevice, final int rssi, final byte[] scanRecord, @NonNull final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, @NonNull final BleScanRecord bleScanRecord) {
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
        this.bleScanRecord = bleScanRecord;
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

    public BleScanRecord getBleScanRecord() {
        return bleScanRecord;
    }

    @Override
    public String toString() {
        return deviceName + " [" + deviceAddress + "]" + " (" + rssi + ")";
    }
}
