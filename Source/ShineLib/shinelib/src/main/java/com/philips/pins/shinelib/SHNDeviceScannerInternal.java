/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

public class SHNDeviceScannerInternal {
    private static final String TAG = SHNDeviceScannerInternal.class.getSimpleName();
    public static final long SCANNING_RESTART_INTERVAL_MS = 3000;

    @Nullable
    private LeScanCallbackProxy leScanCallbackProxy;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable restartScanningRunnable;
    private final SHNCentral shnCentral;

    @NonNull
    private final List<ScanRequest> scanRequests = new ArrayList<>();

    /* package */ SHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        return startScanning(new ScanRequest(registeredDeviceDefinitions, null, scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));
    }

    public boolean startScanning(@NonNull final ScanRequest scanRequest) {
        if (leScanCallbackProxy == null) {
            leScanCallbackProxy = createLeScanCallbackProxy();
            boolean scanning = leScanCallbackProxy.startLeScan(leScanCallback, null);

            if (scanning) {
                SHNLogger.i(TAG, "Started scanning");
                startScanningRestartTimer();
            } else {
                leScanCallbackProxy = null;
                SHNLogger.e(TAG, "Error starting scanning");
            }
        }

        if (leScanCallbackProxy != null) {
            scanRequests.add(scanRequest);
            scanRequest.scanningStarted(this, shnCentral.getInternalHandler());
        }

        return leScanCallbackProxy != null;
    }

    LeScanCallbackProxy createLeScanCallbackProxy() {
        return new LeScanCallbackProxy();
    }

    private void startScanningRestartTimer() {
        restartScanningRunnable = new Runnable() {
            @Override
            public void run() {
                if (leScanCallbackProxy != null) {
                    leScanCallbackProxy.stopLeScan(leScanCallback);
                    startScanningRestartTimer();
                    if (!leScanCallbackProxy.startLeScan(leScanCallback, null)) {
                        SHNLogger.w(TAG, "Error restarting scanning");
                    }
                }
            }
        };
        shnCentral.getInternalHandler().postDelayed(restartScanningRunnable, SCANNING_RESTART_INTERVAL_MS);
    }

    void stopScanning(final ScanRequest scanRequest) {
        scanRequests.remove(scanRequest);
        scanRequest.scanningStopped();
        if (scanRequests.isEmpty()) {
            stopScanning();
        }
    }

    public void stopScanning() {
        if (leScanCallbackProxy != null) {

            shnCentral.getInternalHandler().removeCallbacks(restartScanningRunnable);
            restartScanningRunnable = null;
            leScanCallbackProxy.stopLeScan(leScanCallback);
            leScanCallbackProxy = null;

            for (final ScanRequest scanRequest : scanRequests) {
                scanRequest.scanningStopped();
            }

            scanRequests.clear();
            SHNLogger.i(TAG, "Stopped scanning");
        }
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                for (final ScanRequest scanRequest : scanRequests) {
                    scanRequest.onScanResult(bleDeviceFoundInfo);
                }
            }
        });
    }

    private LeScanCallbackProxy.LeScanCallback leScanCallback = new LeScanCallbackProxy.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
        }
    };
}
