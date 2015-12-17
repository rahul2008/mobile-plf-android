/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceScanner implements LeScanCallbackProxy.LeScanCallback {
    private static final String TAG = SHNDeviceScanner.class.getSimpleName();
    private static final int SCANNING_RESTART_INTERVAL_MS = 3000;
    private ScannerSettingDuplicates scannerSettingDuplicates;

    public enum ScannerSettingDuplicates {
        DuplicatesNotAllowed, DuplicatesAllowed
    }

    private Set<String> macAddressesOfFoundDevices = new HashSet<>();
    private LeScanCallbackProxy leScanCallbackProxy;
    private SHNDeviceScannerListener shnDeviceScannerListener;
    private boolean scanning = false;
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private Runnable scanningTimer;
    private Runnable scanningRestartTimer;
    private final SHNCentral shnCentral;

    public interface SHNDeviceScannerListener {
        void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo);
        void scanStopped(SHNDeviceScanner shnDeviceScanner);
    }

    public SHNDeviceScanner(SHNCentral shnCentral, List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
        SHNDeviceFoundInfo.setSHNCentral(shnCentral);
        this.shnCentral = shnCentral;
        this.registeredDeviceDefinitions = registeredDeviceDefinitions;
    }

    public boolean startScanning(SHNDeviceScannerListener shnDeviceScannerListener, ScannerSettingDuplicates scannerSettingDuplicates, long stopScanningAfterMS) {
        if (scanning) {
            return false;
        }
        this.scannerSettingDuplicates = scannerSettingDuplicates;
        macAddressesOfFoundDevices.clear();
        this.shnDeviceScannerListener = shnDeviceScannerListener;
        leScanCallbackProxy = new LeScanCallbackProxy();
        scanning = leScanCallbackProxy.startLeScan(this, null);

        scanningTimer = new Runnable() {
            @Override
            public void run() {
                stopScanning();
            }
        };
        shnCentral.getInternalHandler().postDelayed(scanningTimer, stopScanningAfterMS);

        startScanningRestartTimer();

        return true;
    }

    private void startScanningRestartTimer() {
        scanningRestartTimer = new Runnable() {
            @Override
            public void run() {
                leScanCallbackProxy.stopLeScan(SHNDeviceScanner.this);
                startScanningRestartTimer();
                leScanCallbackProxy.startLeScan(SHNDeviceScanner.this, null);
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
            shnDeviceScannerListener.scanStopped(this);
            shnDeviceScannerListener = null;
        }
    }

    private void postBleDeviceFoundInfoOnBleThread(final BleDeviceFoundInfo bleDeviceFoundInfo) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDeviceFoundEvent(bleDeviceFoundInfo);
            }
        };
        shnCentral.getInternalHandler().post(runnable);
    }

    private void handleDeviceFoundEvent(BleDeviceFoundInfo bleDeviceFoundInfo) {
        if (scannerSettingDuplicates == ScannerSettingDuplicates.DuplicatesAllowed || !macAddressesOfFoundDevices.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            if (scannerSettingDuplicates == ScannerSettingDuplicates.DuplicatesNotAllowed) {
                macAddressesOfFoundDevices.add(bleDeviceFoundInfo.getDeviceAddress());
            }

            BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.scanRecord);
            List<UUID> UUIDs = bleScanRecord.getUuids();
            for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: registeredDeviceDefinitions) {
                boolean matched = false;
                if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher()) {
                    matched = shnDeviceDefinitionInfo.matchesOnAdvertisedData(bleDeviceFoundInfo.bluetoothDevice, BleScanRecord.createNewInstance(bleDeviceFoundInfo.scanRecord), bleDeviceFoundInfo.rssi);
                } else {
                    Set<UUID> primaryServiceUUIDs = shnDeviceDefinitionInfo.getPrimaryServiceUUIDs();
                    for (UUID uuid: UUIDs) {
                        if (primaryServiceUUIDs.contains(uuid)) {
                            matched = true;
                            break;
                        }
                    }
                }
                if(matched) {
                    final  SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(bleDeviceFoundInfo.bluetoothDevice, bleDeviceFoundInfo.rssi, bleDeviceFoundInfo.scanRecord, shnDeviceDefinitionInfo);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (shnDeviceScannerListener != null) {
                                shnDeviceScannerListener.deviceFound(SHNDeviceScanner.this, shnDeviceFoundInfo);
                            }
                        }
                    };
                    shnCentral.runOnUserHandlerThread(runnable);
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
        postBleDeviceFoundInfoOnBleThread(new BleDeviceFoundInfo(device, rssi, scanRecord));
    }

}
