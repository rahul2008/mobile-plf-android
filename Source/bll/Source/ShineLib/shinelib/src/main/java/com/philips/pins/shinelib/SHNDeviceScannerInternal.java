/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.SHNCentral.SHNCentralListener;
import com.philips.pins.shinelib.SHNDeviceScanner.SHNDeviceScannerListener;
import com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates;
import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy.LeScanCallback;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateReady;
import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed;

/**
 * Used to scan for devices.
 *
 * @publicPluginApi
 */
public class SHNDeviceScannerInternal {
    private static final String TAG = "SHNDeviceScannerInternal";

    static final long SCANNING_RESTART_INTERVAL_MS = 30_000L;

    @Nullable
    private LeScanCallbackProxy leScanCallbackProxy;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private final SHNCentral shnCentral;

    @NonNull
    private final List<SHNInternalScanRequest> shnInternalScanRequests = new ArrayList<>();

    private final Runnable restartScanningRunnable = new Runnable() {
        @Override
        public void run() {
            if (leScanCallbackProxy != null) {
                leScanCallbackProxy.stopLeScan(leScanCallback);
                leScanCallbackProxy.startLeScan(leScanCallback);
                startScanningRestartTimer();
            }
        }
    };

    @SuppressWarnings("FieldCanBeLocal")
    private final SHNCentralListener shnCentralListener = new SHNCentralListener() {
        @Override
        public void onStateUpdated(@NonNull SHNCentral shnCentral) {
            if (leScanCallbackProxy == null) {
                return;
            }

            if (shnCentral.getShnCentralState() == SHNCentralStateReady) {
                leScanCallbackProxy.stopLeScan(leScanCallback);
                leScanCallbackProxy.startLeScan(leScanCallback);
            }
        }
    };

    private final LeScanCallback leScanCallback = new LeScanCallback() {
        @Override
        public void onScanResult(BluetoothDevice device, int rssi, ScanRecord scanRecord) {
            postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
        }

        @Override
        public void onScanFailed(final int errorCode) {
            shnCentral.getInternalHandler().post(new Runnable() {
                @Override
                public void run() {
                    leScanCallbackProxy = null;
                    final String errorMsg = String.format(Locale.US, "Error starting scanning, errorCode: %d", errorCode);

                    SHNLogger.e(TAG, errorMsg);
                    SHNTagger.sendTechnicalError(errorMsg);
                }
            });
        }
    };

    SHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> deviceDefinitions) {
        this.shnCentral = shnCentral;
        this.shnCentral.registerShnCentralListener(shnCentralListener);
        this.registeredDeviceDefinitions = deviceDefinitions;
    }

    /**
     * @param shnDeviceScannerListener Device scanner listener
     * @param scannerSetting           Scanner setting indicating whether duplicates are allowed
     * @param stopScanningAfterMS      Time in milliseconds to wait until scanning is stopped
     * @return true, if scanning was started
     * <p>
     * Start scanning for devices
     * When a device is found the {@code SHNDeviceScanner.SHNDeviceScannerListener} will be informed.
     */
    public boolean startScanning(@NonNull SHNDeviceScannerListener shnDeviceScannerListener, ScannerSettingDuplicates scannerSetting, long stopScanningAfterMS) {
        return startScanning(new SHNInternalScanRequest(registeredDeviceDefinitions, null, scannerSetting == DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));
    }

    /**
     * Start scanning for devices
     *
     * @param shnInternalScanRequest Contains scan settings and callback
     * @return Scan successfully started
     */
    public boolean startScanning(@NonNull final SHNInternalScanRequest shnInternalScanRequest) {
        SHNLogger.i(TAG, "Start scanning");

        if (leScanCallbackProxy == null) {
            leScanCallbackProxy = createLeScanCallbackProxy();
            leScanCallbackProxy.startLeScan(leScanCallback);
            startScanningRestartTimer();
        }

        if (shnInternalScanRequests.add(shnInternalScanRequest)) {
            shnInternalScanRequest.onScanningStarted(this, shnCentral.getInternalHandler());
        }
        return true;
    }

    /**
     * Stop scanning for devices
     *
     * @param shnInternalScanRequest the scan request to stop scanning for
     */
    public void stopScanning(final @NonNull SHNInternalScanRequest shnInternalScanRequest) {
        shnInternalScanRequests.remove(shnInternalScanRequest);
        shnInternalScanRequest.onScanningStopped();

        if (shnInternalScanRequests.isEmpty()) {
            onStopScanning();
        }
    }

    @VisibleForTesting
    synchronized void onStopScanning() {
        if (leScanCallbackProxy == null) {
            return;
        }
        stopScanningRestartTimer();

        leScanCallbackProxy.stopLeScan(leScanCallback);
        leScanCallbackProxy = null;

        for (final SHNInternalScanRequest shnInternalScanRequest : shnInternalScanRequests) {
            shnInternalScanRequest.onScanningStopped();
        }
        shnInternalScanRequests.clear();

        SHNLogger.i(TAG, "Stopped scanning");
    }

    @VisibleForTesting
    LeScanCallbackProxy createLeScanCallbackProxy() {
        return new LeScanCallbackProxy(shnCentral.getBleUtilities());
    }

    @Nullable
    private static SHNDeviceFoundInfo fromBleDeviceFoundInfo(final @NonNull SHNCentral shnCentral, final @NonNull BleDeviceFoundInfo bleDeviceFoundInfo, Collection<SHNDeviceDefinitionInfo> deviceDefinitions) {
        final BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.getScanRecord());

        for (final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : deviceDefinitions) {
            if (isDeviceSupported(bleDeviceFoundInfo, bleScanRecord, shnDeviceDefinitionInfo)) {
                return new SHNDeviceFoundInfo(shnCentral, bleDeviceFoundInfo.getBluetoothDevice(), bleDeviceFoundInfo.getRssi(), bleDeviceFoundInfo.getScanRecord().getBytes(), shnDeviceDefinitionInfo, bleScanRecord);
            }
        }
        return null;
    }

    private static boolean isDeviceSupported(final @NonNull BleDeviceFoundInfo bleDeviceFoundInfo, final @NonNull BleScanRecord bleScanRecord, final @NonNull SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher()) {
            return shnDeviceDefinitionInfo.matchesOnAdvertisedData(bleDeviceFoundInfo.getBluetoothDevice(), bleScanRecord, bleDeviceFoundInfo.getRssi());
        } else {
            final Set<UUID> primaryServiceUUIDs = shnDeviceDefinitionInfo.getPrimaryServiceUUIDs();

            for (UUID uuid : bleScanRecord.getUuids()) {
                if (primaryServiceUUIDs.contains(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                final SHNDeviceFoundInfo deviceFoundInfo = fromBleDeviceFoundInfo(shnCentral, bleDeviceFoundInfo, registeredDeviceDefinitions);

                if (deviceFoundInfo != null) {
                    for (final SHNInternalScanRequest shnInternalScanRequest : shnInternalScanRequests) {
                        shnInternalScanRequest.onDeviceFound(deviceFoundInfo);
                    }
                }
            }
        });
    }

    private void startScanningRestartTimer() {
        shnCentral.getInternalHandler().postDelayed(restartScanningRunnable, SCANNING_RESTART_INTERVAL_MS);
    }

    private void stopScanningRestartTimer() {
        shnCentral.getInternalHandler().removeCallbacks(restartScanningRunnable);
    }
}
