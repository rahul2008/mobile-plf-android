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

/**
 * Utility class to scan for BLE devices
 *
 * @publicApi
 */
public class BleUtilities {

    private static final int DISCOVERY_REPORT_DELAY_MILLIS = 1000;

    @NonNull
    private final Context applicationContext;
    private final BluetoothAdapter bluetoothAdapter;

    public BleUtilities(final @NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Check if the current phone supports BLE
     *
     * @return BLE support
     */
    public boolean isBleFeatureAvailable() {
        return applicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * Check if BLE is enabled
     *
     * @return BLE enabled
     */
    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    /**
     * Request BLE to be turned on
     */
    public void startEnableBluetoothActivity() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    /**
     * Start scanning for BLE devices
     *
     * @param scanCallback Callback for found devices
     */
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

    /**
     * Stop scanning for devices
     *
     * @param scanCallback Callback that should not receive found devices anymore
     */
    public void stopLeScan(final @NonNull ScanCallback scanCallback) {
        if (!isBluetoothAdapterEnabled()) {
            return;
        }
        final BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.stopScan(scanCallback);
    }

    /**
     * Get {@code BluetoothDevice} based on the MAC address
     * Returns null if no device is found
     *
     * @param macAddress MAC address of the device
     * @return Bluetooth device
     */
    public BluetoothDevice getRemoteDevice(String macAddress) {
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }
}
