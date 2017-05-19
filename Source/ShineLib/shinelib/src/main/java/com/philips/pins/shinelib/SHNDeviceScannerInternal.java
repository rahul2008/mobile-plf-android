/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SHNDeviceScannerInternal {
    private static final String TAG = SHNDeviceScannerInternal.class.getSimpleName();
    public static final long SCANNING_RESTART_INTERVAL_MS = 3000;

    @Nullable
    private LeScanCallbackProxy leScanCallbackProxy;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable restartScanningRunnable;
    private final SHNCentral shnCentral;

    @NonNull
    private final List<SHNInternalScanRequest> shnInternalScanRequests = new ArrayList<>();

    private boolean isUsingAdvertisedDataMatching = false;
    private UUID[] primaryServiceUUIDs = {};

    @VisibleForTesting
    SHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        return startScanning(new SHNInternalScanRequest(registeredDeviceDefinitions, null, scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, (int) stopScanningAfterMS, shnDeviceScannerListener));
    }

    public boolean startScanning(@NonNull final SHNInternalScanRequest SHNInternalScanRequest) {
        isUsingAdvertisedDataMatching = isAnyDeviceUsingAdvertisedDataMatching(registeredDeviceDefinitions);
        primaryServiceUUIDs = getAllRegisteredPrimaryServiceUUIDs(registeredDeviceDefinitions);

        if (leScanCallbackProxy == null) {
            leScanCallbackProxy = createLeScanCallbackProxy();

            boolean isScanning;

            if (isUsingAdvertisedDataMatching) {
                isScanning = leScanCallbackProxy.startLeScan(leScanCallback, null);
            } else {
                isScanning = leScanCallbackProxy.startLeScan(primaryServiceUUIDs, leScanCallback);
            }

            if (isScanning) {
                SHNLogger.i(TAG, "Started scanning");
                startScanningRestartTimer();
            } else {
                leScanCallbackProxy = null;
                SHNLogger.e(TAG, "Error starting scanning");
            }
        }

        if (leScanCallbackProxy != null) {
            shnInternalScanRequests.add(SHNInternalScanRequest);
            SHNInternalScanRequest.scanningStarted(this, shnCentral.getInternalHandler());
        }
        return leScanCallbackProxy != null;
    }

    @VisibleForTesting
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

    public void stopScanning(final SHNInternalScanRequest shnInternalScanRequest) {
        shnInternalScanRequests.remove(shnInternalScanRequest);
        shnInternalScanRequest.scanningStopped();
        if (shnInternalScanRequests.isEmpty()) {
            stopScanning();
        }
    }

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
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            postBleDeviceFoundInfoOnInternalThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
        }
    };

    private UUID[] getAllRegisteredPrimaryServiceUUIDs(@NonNull List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        Set<UUID> uuidSet = new HashSet<>();
        for (SHNDeviceDefinitionInfo definition : registeredDeviceDefinitions) {
            uuidSet.addAll(definition.getPrimaryServiceUUIDs());
        }
        return uuidSet.toArray(new UUID[uuidSet.size()]);
    }

    private boolean isAnyDeviceUsingAdvertisedDataMatching(@NonNull List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        boolean result = false;

        for (SHNDeviceDefinitionInfo definition : registeredDeviceDefinitions) {
            result |= definition.useAdvertisedDataMatcher();
        }
        return result;
    }

    private SHNDeviceFoundInfo convertToSHNDeviceFoundInfo(final @NonNull BleDeviceFoundInfo bleDeviceFoundInfo) {
        BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.getScanRecord());

        for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : registeredDeviceDefinitions) {
            boolean matched = doesDeviceDefinitionInfoSupportDevice(bleDeviceFoundInfo, bleScanRecord, shnDeviceDefinitionInfo);

            if (matched) {
                return new SHNDeviceFoundInfo(bleDeviceFoundInfo.getBluetoothDevice(), bleDeviceFoundInfo.getRssi(), bleDeviceFoundInfo.getScanRecord(), shnDeviceDefinitionInfo, bleScanRecord);
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
