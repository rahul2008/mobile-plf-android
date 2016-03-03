/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScannerInternal implements LeScanCallbackProxy.LeScanCallback {
    private static final String TAG = SHNDeviceScannerInternal.class.getSimpleName();
    private static final int SCANNING_RESTART_INTERVAL_MS = 3000;
    private SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates;

    private Set<String> macAddressesOfFoundDevices = new HashSet<>();
    private LeScanCallbackProxy leScanCallbackProxy;
    private SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener;
    private boolean scanning = false;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable stopScanningRunnable;
    private Runnable restartScanningRunnable;
    private final SHNCentral shnCentral;

    private final List<ScanRecord> scanRecords = new ArrayList<>();

    /* package */ SHNDeviceScannerInternal(SHNCentral shnCentral, List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        if (scanning) {
            return false;
        }

        scanRecords.add(new ScanRecord(registeredDeviceDefinitions, null, scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));

        this.scannerSettingDuplicates = scannerSettingDuplicates;
        macAddressesOfFoundDevices.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
        leScanCallbackProxy = new LeScanCallbackProxy();
        scanning = leScanCallbackProxy.startLeScan(this, null);

        if (scanning) {
            SHNLogger.i(TAG, "Started scanning");
            stopScanningRunnable = new Runnable() {
                @Override
                public void run() {
                    stopScanning();
                }
            };
            shnCentral.getInternalHandler().postDelayed(stopScanningRunnable, stopScanningAfterMS);

            startScanningRestartTimer();
        } else {
            SHNLogger.e(TAG, "Error starting scanning");
        }

        return scanning;
    }

    private void startScanningRestartTimer() {
        restartScanningRunnable = new Runnable() {
            @Override
            public void run() {
                leScanCallbackProxy.stopLeScan(SHNDeviceScannerInternal.this);
                startScanningRestartTimer();
                if (!leScanCallbackProxy.startLeScan(SHNDeviceScannerInternal.this, null)) {
                    SHNLogger.w(TAG, "Error restarting scanning");
                }
            }
        };
        shnCentral.getInternalHandler().postDelayed(restartScanningRunnable, SCANNING_RESTART_INTERVAL_MS);
    }

    public void stopScanning() {
        if (scanning) {
            scanning = false;
            shnCentral.getInternalHandler().removeCallbacks(stopScanningRunnable);
            shnCentral.getInternalHandler().removeCallbacks(restartScanningRunnable);
            stopScanningRunnable = null;
            restartScanningRunnable = null;
            leScanCallbackProxy.stopLeScan(this);
            leScanCallbackProxy = null;
            shnDeviceScannerListener.scanStopped(null);
            shnDeviceScannerListener = null;
            SHNLogger.i(TAG, "Stopped scanning");
        }
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (final ScanRecord scanRecord : scanRecords) {
                    scanRecord.onScanResult(bleDeviceFoundInfo);
                }
            }
        };
        shnCentral.getInternalHandler().post(runnable);
    }

    public void shutdown() {
        stopScanning();
        // TODO What else: release references???
    }

    // SHNDeviceScanner.LeScanCallback
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
    }
}
