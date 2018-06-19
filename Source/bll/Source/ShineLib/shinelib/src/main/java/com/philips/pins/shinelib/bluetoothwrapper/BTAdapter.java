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

public class BTAdapter {

    private static final int DISCOVERY_REPORT_DELAY_MILLIS = 1000;

    private final Handler handler;
    private final BluetoothAdapter bluetoothAdapter;

    public BTAdapter(final @NonNull Handler handler) {
        this.handler = handler;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void startLeScan(final @NonNull ScanCallback scanCallback) {
        if (!isEnabled()) {
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
        if (!isEnabled()) {
            return;
        }
        final BluetoothLeScanner bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothScanner.stopScan(scanCallback);
    }

    public BTDevice getRemoteDevice(String macAddress) {
        return new BTDevice(bluetoothAdapter.getRemoteDevice(macAddress), handler);
    }
}
