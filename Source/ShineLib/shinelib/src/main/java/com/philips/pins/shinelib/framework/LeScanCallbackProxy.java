/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.List;
import java.util.UUID;

/* This class prevents the Android Bluetooth stack from holding on the object that implements the
 * LeScanCallback and *everything it references* after stopping the scan.
 *
 * As an extra the class is used to provide a general purpose parameter in the device detected callback.
 */
public class LeScanCallbackProxy extends ScanCallback {

    @NonNull
    private final BleUtilities bleUtilities;

    public interface LeScanCallback {
        void onScanResult(BluetoothDevice device, int rssi, ScanRecord scanRecord);

        void onScanFailed(int errorCode);
    }

    private LeScanCallback leScanCallback;

    public LeScanCallbackProxy(final @NonNull BleUtilities bleUtilities) {
        this.bleUtilities = bleUtilities;
    }

    public void startLeScan(LeScanCallback leScanCallback) {
        this.leScanCallback = leScanCallback;
        bleUtilities.startLeScan(this);
    }

    public void stopLeScan(LeScanCallback leScanCallback) {
        if (leScanCallback == this.leScanCallback) {
            bleUtilities.stopLeScan(this);
            this.leScanCallback = null;
        }
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        SHNLogger.e("LeScanCallbackProxy", String.format("onScanResult %s", result));
        if (leScanCallback != null) {
            leScanCallback.onScanResult(result.getDevice(), result.getRssi(), result.getScanRecord());
        }
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        SHNLogger.e("LeScanCallbackProxy", String.format("onBatchScanResults %d", results.size()));
        for (ScanResult result : results) {
            if (leScanCallback != null) {
                //SHNLogger.e(getClass().getName(), String.format("onScanResult address: %s, device: %s", result.getDevice().getAddress(), result.getScanRecord().getDeviceName()));
                leScanCallback.onScanResult(result.getDevice(), result.getRssi(), result.getScanRecord());
            }
        }
    }

    @Override
    public void onScanFailed(int errorCode) {
        if (leScanCallback != null) {
            leScanCallback.onScanFailed(errorCode);
        }
    }
}
