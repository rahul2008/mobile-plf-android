/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;

public class BleUtilities {

    private static final int DISCOVERY_REPORT_DELAY = 1000;

    @NonNull
    private final Context applicationContext;
    private final BluetoothAdapter bluetoothAdapter;
    private final ScanSettings settings;

    public BleUtilities(final @NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(DISCOVERY_REPORT_DELAY)
                .build();
    }

    public boolean isBleFeatureAvailable() {
        return applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void startEnableBluetoothActivity() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    public void startLeScan(ScanCallback scanCallback) {
        SHNLogger.i(getClass().getName(), "startLeScan");

        if (!isBluetoothAdapterEnabled())
            return;

        BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.startScan(null, settings, scanCallback);
    }

    public void stopLeScan(ScanCallback scanCallback) {
        SHNLogger.i(getClass().getName(), "stopLeScan");

        if (!isBluetoothAdapterEnabled())
            return;

        BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.stopScan(scanCallback);
    }

    public BluetoothDevice getRemoteDevice(String macAddress) {
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }
}
