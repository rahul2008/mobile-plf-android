/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanSettings;
import android.os.Handler;
import android.support.annotation.NonNull;

/**
 * Utility class to scan for BLE devices
 *
 * @publicApi
 */
public class BTAdapter {

    private static final int DISCOVERY_REPORT_DELAY_MILLIS = 1000;

    private final Handler handler;
    private final BluetoothAdapter bluetoothAdapter;

    public BTAdapter(final @NonNull Handler handler) {
        this.handler = handler;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
     * Get {@code BTDevice} based on the MAC address
     *
     * @param macAddress MAC address of the device
     * @return Bluetooth device
     */
    public BTDevice getRemoteDevice(String macAddress) {
        return new BTDevice(bluetoothAdapter.getRemoteDevice(macAddress), handler);
    }
}
