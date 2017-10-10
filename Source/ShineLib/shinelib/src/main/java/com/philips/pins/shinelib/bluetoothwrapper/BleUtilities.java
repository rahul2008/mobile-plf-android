/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.workarounds.Workaround;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleUtilities {
    @NonNull
    private final Context applicationContext;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothLeScanner bluetoothScanner;

    public BleUtilities(final @NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
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
        startLeScan(new UUID[0], scanCallback);
    }

    public void startLeScan(UUID[] serviceUUIDs, ScanCallback scanCallback) {

        ScanSettings.Builder settingsBuilder = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(250);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            settingsBuilder = settingsBuilder
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
                    .setMatchMode(ScanSettings.MATCH_MODE_STICKY);
        }

        ScanSettings settings = settingsBuilder.build();

        List<ScanFilter> filters = new ArrayList();
        for (int i = 0; i < serviceUUIDs.length; i++) {
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(serviceUUIDs[i]))
                    .build();
            filters.add(filter);
        }

        bluetoothScanner.startScan(filters, settings, scanCallback);
    }

    public void stopLeScan(ScanCallback scanCallback) {
        bluetoothScanner.stopScan(scanCallback);
    }

    public BluetoothDevice getRemoteDevice(String macAddress) {
        return bluetoothAdapter.getRemoteDevice(macAddress);
    }
}
