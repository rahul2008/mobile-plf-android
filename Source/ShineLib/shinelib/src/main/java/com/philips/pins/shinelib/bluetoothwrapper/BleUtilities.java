/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
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

public class BleUtilities {

    private static final int DISCOVERY_REPORT_DELAY_MILLIS = 1000;

    @NonNull
    private final Context applicationContext;
    private final BluetoothAdapter bluetoothAdapter;

    public BleUtilities(final @NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

    public void startLeScan(final @NonNull ScanCallback scanCallback) {
        if (!isBluetoothAdapterEnabled()) {
            return;
        }

        ScanSettings.Builder builder = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);

        if (bluetoothAdapter.isOffloadedScanBatchingSupported()) {
            builder.setReportDelay(DISCOVERY_REPORT_DELAY_MILLIS);
        }

        final ScanSettings settings = builder.build();
        final BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.startScan(null, settings, scanCallback);
    }

    public void stopLeScan(final @NonNull ScanCallback scanCallback) {
        if (!isBluetoothAdapterEnabled()) {
            return;
        }
        final BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.stopScan(scanCallback);
    }

    public BluetoothDevice getRemoteDevice(String macAddress) {
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }
}
