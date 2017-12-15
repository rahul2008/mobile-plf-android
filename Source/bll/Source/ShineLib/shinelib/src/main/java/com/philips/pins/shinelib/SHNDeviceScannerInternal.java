/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates;
import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed;

/**
 * Used to scan for devices.
 *
 * @publicPluginApi
 */
public class SHNDeviceScannerInternal implements SHNCentral.SHNCentralListener {
    private static final String TAG = SHNDeviceScannerInternal.class.getSimpleName();

    static final long SCANNING_RESTART_INTERVAL_MS = 30_000L;

    @Nullable
    private LeScanCallbackProxy leScanCallbackProxy;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable restartScanningRunnable;
    private final SHNCentral shnCentral;

    @NonNull
    private final List<SHNInternalScanRequest> shnInternalScanRequests = new ArrayList<>();

    @VisibleForTesting
    SHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.shnCentral.registerShnCentralListener(this);
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    @Override
    public void onStateUpdated(@NonNull SHNCentral shnCentral) {
        if (leScanCallbackProxy != null && shnCentral.isBluetoothAdapterEnabled()) {
            leScanCallbackProxy.stopLeScan(leScanCallback);
            leScanCallbackProxy.startLeScan(leScanCallback);
        }
    }

    /**
     * Start scanning for devices
     * When a device is found the {@code SHNDeviceScanner.SHNDeviceScannerListener} will be informed.
     *
     * @param shnDeviceScannerListener Callback listener
     * @param scannerSettingDuplicates Scan settings
     * @param stopScanningAfterMS      Stop scanning after time in Ms
     * @return Scan successfully stated
     */
    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        return startScanning(new SHNInternalScanRequest(registeredDeviceDefinitions, null, scannerSettingDuplicates == DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));
    }

    /**
     * Start scanning for devices
     *
     * @param SHNInternalScanRequest Contains scan settings and callback
     * @return Scan successfully stated
     */
    public boolean startScanning(@NonNull final SHNInternalScanRequest SHNInternalScanRequest) {
        SHNLogger.i(TAG, "Start scanning");

        if (leScanCallbackProxy == null) {
            leScanCallbackProxy = createLeScanCallbackProxy();
            leScanCallbackProxy.startLeScan(leScanCallback);
            startScanningRestartTimer();
        }

        shnInternalScanRequests.add(SHNInternalScanRequest);
        SHNInternalScanRequest.scanningStarted(this, shnCentral.getInternalHandler());

        return true;
    }

    @VisibleForTesting
    LeScanCallbackProxy createLeScanCallbackProxy() {
        return new LeScanCallbackProxy(shnCentral.getBleUtilities());
    }

    private void startScanningRestartTimer() {
        restartScanningRunnable = new Runnable() {
            @Override
            public void run() {
                if (leScanCallbackProxy != null) {
                    leScanCallbackProxy.stopLeScan(leScanCallback);
                    leScanCallbackProxy.startLeScan(leScanCallback);
                    startScanningRestartTimer();
                }
            }
        };
        shnCentral.getInternalHandler().postDelayed(restartScanningRunnable, SCANNING_RESTART_INTERVAL_MS);
    }

    public void stopScanning(final SHNInternalScanRequest shnInternalScanRequest) {
        shnInternalScanRequests.remove(shnInternalScanRequest);
        shnInternalScanRequest.scanningStopped();
        if (shnInternalScanRequests.isEmpty()) {
            stopScanning();
        }
    }

    /**
     * Stop scanning for devices
     */
    public void stopScanning() {
        if (leScanCallbackProxy != null) {
            shnCentral.getInternalHandler().removeCallbacks(restartScanningRunnable);
            restartScanningRunnable = null;
            leScanCallbackProxy.stopLeScan(leScanCallback);
            leScanCallbackProxy = null;

            for (final SHNInternalScanRequest shnInternalScanRequest : shnInternalScanRequests) {
                shnInternalScanRequest.scanningStopped();
            }

            shnInternalScanRequests.clear();
            SHNLogger.i(TAG, "Stopped scanning");
        }
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                SHNDeviceFoundInfo deviceFoundInfo = convertToSHNDeviceFoundInfo(bleDeviceFoundInfo);
                if (deviceFoundInfo != null) {
                    for (final SHNInternalScanRequest shnInternalScanRequest : shnInternalScanRequests) {
                        shnInternalScanRequest.onDeviceFound(deviceFoundInfo);
                    }
                }
            }
        });
    }

    private final LeScanCallbackProxy.LeScanCallback leScanCallback = new LeScanCallbackProxy.LeScanCallback() {
        @Override
        public void onScanResult(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
            postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
        }

        @Override
        public void onScanFailed(int errorCode) {
            leScanCallbackProxy = null;
            SHNLogger.e(TAG, String.format(Locale.US, "Error starting scanning, errorCode: %d", errorCode));
        }
    };

    private SHNDeviceFoundInfo convertToSHNDeviceFoundInfo(final @NonNull BleDeviceFoundInfo bleDeviceFoundInfo) {
        BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.getScanRecord());

        for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : registeredDeviceDefinitions) {
            boolean matched = doesDeviceDefinitionInfoSupportDevice(bleDeviceFoundInfo, bleScanRecord, shnDeviceDefinitionInfo);

            if (matched) {
                return new SHNDeviceFoundInfo(bleDeviceFoundInfo.getBluetoothDevice(), bleDeviceFoundInfo.getRssi(), bleDeviceFoundInfo.getScanRecord().getBytes(), shnDeviceDefinitionInfo, bleScanRecord);
            }
        }
        return null;
    }

    private boolean doesDeviceDefinitionInfoSupportDevice(final BleDeviceFoundInfo bleDeviceFoundInfo, final BleScanRecord bleScanRecord, final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        boolean matched = false;

        if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher()) {
            matched = shnDeviceDefinitionInfo.matchesOnAdvertisedData(bleDeviceFoundInfo.getBluetoothDevice(), bleScanRecord, bleDeviceFoundInfo.getRssi());
        } else {
            Set<UUID> primaryServiceUUIDs = shnDeviceDefinitionInfo.getPrimaryServiceUUIDs();
            for (UUID uuid : bleScanRecord.getUuids()) {
                if (primaryServiceUUIDs.contains(uuid)) {
                    matched = true;
                    break;
                }
            }
        }
        return matched;
    }
}
