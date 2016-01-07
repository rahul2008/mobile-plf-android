/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    private Runnable scanningTimer;
    private Runnable scanningRestartTimer;
    private final SHNCentral shnCentral;

    /* package */ SHNDeviceScannerInternal(SHNCentral shnCentral, List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(@NonNull SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        if (scanning) {
            return false;
        }
        this.scannerSettingDuplicates = scannerSettingDuplicates;
        macAddressesOfFoundDevices.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
        leScanCallbackProxy = new LeScanCallbackProxy();
        scanning = leScanCallbackProxy.startLeScan(this, null);

        if (scanning) {
            scanningTimer = new Runnable() {
                @Override
                public void run() {
                    stopScanning();
                }
            };
            shnCentral.getInternalHandler().postDelayed(scanningTimer, stopScanningAfterMS);

            startScanningRestartTimer();
        }

        return scanning;
    }

    private void startScanningRestartTimer() {
        scanningRestartTimer = new Runnable() {
            @Override
            public void run() {
                leScanCallbackProxy.stopLeScan(SHNDeviceScannerInternal.this);
                startScanningRestartTimer();
                if (!leScanCallbackProxy.startLeScan(SHNDeviceScannerInternal.this, null)) {
                    SHNLogger.w(TAG, "Error starting scanning.");
                }
            }
        };
        shnCentral.getInternalHandler().postDelayed(scanningRestartTimer, SCANNING_RESTART_INTERVAL_MS);
    }

    public void stopScanning() {
        if (scanning) {
            scanning = false;
            shnCentral.getInternalHandler().removeCallbacks(scanningTimer);
            shnCentral.getInternalHandler().removeCallbacks(scanningRestartTimer);
            scanningTimer = null;
            scanningRestartTimer = null;
            leScanCallbackProxy.stopLeScan(this);
            leScanCallbackProxy = null;
            shnDeviceScannerListener.scanStopped(null);
            shnDeviceScannerListener = null;
        }
    }

    private void postBleDeviceFoundInfoOnInternalThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDeviceFoundEvent(bleDeviceFoundInfo);
            }
        };
        shnCentral.getInternalHandler().post(runnable);
    }

    private void handleDeviceFoundEvent(BleDeviceFoundInfo bleDeviceFoundInfo) {
        if (scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed || !macAddressesOfFoundDevices.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            if (scannerSettingDuplicates == SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed) {
                macAddressesOfFoundDevices.add(bleDeviceFoundInfo.getDeviceAddress());
            }

            BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.scanRecord);
            for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: registeredDeviceDefinitions) {
                boolean matched = false;
                if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher()) {
                    matched = shnDeviceDefinitionInfo.matchesOnAdvertisedData(bleDeviceFoundInfo.bluetoothDevice, bleScanRecord, bleDeviceFoundInfo.rssi);
                } else {
                    Set<UUID> primaryServiceUUIDs = shnDeviceDefinitionInfo.getPrimaryServiceUUIDs();
                    for (UUID uuid: bleScanRecord.getUuids()) {
                        if (primaryServiceUUIDs.contains(uuid)) {
                            matched = true;
                            break;
                        }
                    }
                }
                if(matched) {
                    if (shnDeviceScannerListener != null) {
                        SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(bleDeviceFoundInfo.bluetoothDevice, bleDeviceFoundInfo.rssi, bleDeviceFoundInfo.scanRecord, shnDeviceDefinitionInfo);
                        shnDeviceScannerListener.deviceFound(null, shnDeviceFoundInfo);
                    }
                    break;
                }
            }
        }
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
