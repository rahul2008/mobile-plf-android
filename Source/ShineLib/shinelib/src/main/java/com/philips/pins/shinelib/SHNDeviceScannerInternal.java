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
import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScannerInternal {
    private static final String TAG = SHNDeviceScannerInternal.class.getSimpleName();
    private static final int SCANNING_RESTART_INTERVAL_MS = 3000;

    @NonNull
    private final LeScanCallbackProxy leScanCallbackProxy;
    private SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener;
    private boolean scanning = false;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable stopScanningRunnable;
    private Runnable restartScanningRunnable;
    private final SHNCentral shnCentral;

    private final List<ScanRecord> scanRecords = new ArrayList<>();

    /* package */ SHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final LeScanCallbackProxy leScanCallbackProxy, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.leScanCallbackProxy = leScanCallbackProxy;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        if (scanning) {
            return false;
        }

        scanRecords.add(new ScanRecord(registeredDeviceDefinitions, null, scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));

        this.shnDeviceScannerListener = shnDeviceScannerListener;
        scanning = leScanCallbackProxy.startLeScan(leScanCallback, null);

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
                leScanCallbackProxy.stopLeScan(leScanCallback);
                startScanningRestartTimer();
                if (!leScanCallbackProxy.startLeScan(leScanCallback, null)) {
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
            leScanCallbackProxy.stopLeScan(leScanCallback);
            shnDeviceScannerListener.scanStopped(null);
            shnDeviceScannerListener = null;
            SHNLogger.i(TAG, "Stopped scanning");
        }
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                for (final ScanRecord scanRecord : scanRecords) {
                    scanRecord.onScanResult(bleDeviceFoundInfo);
                }
            }
        });
    }

    public void shutdown() {
        stopScanning();
        // TODO What else: release references???
    }

    private LeScanCallbackProxy.LeScanCallback leScanCallback = new LeScanCallbackProxy.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
        }
    };
}
