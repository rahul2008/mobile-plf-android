package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.framework.BleDeviceFoundInfo;
import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScanRequest {

    @NonNull
    private final List<SHNDeviceDefinitionInfo> deviceDefinitions;

    @NonNull
    private final List<String> deviceMacAddresses;

    @NonNull
    private final boolean reportMoreThanOnce;

    @NonNull
    private final int stopScanningAfterMS;

    @NonNull
    private final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener;

    private final List<String> reportedDeviceMacAddresses = new ArrayList<>();

    private SHNDeviceScannerInternal deviceScannerInternal;
    private Handler internalHandler;

    public ScanRequest(@NonNull final List<SHNDeviceDefinitionInfo> deviceDefinitions, @NonNull final List<String> deviceMacAddresses, final boolean reportMoreThanOnce, final int stopScanningAfterMS, @NonNull final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener) {
        this.deviceDefinitions = deviceDefinitions;
        this.deviceMacAddresses = deviceMacAddresses;
        this.reportMoreThanOnce = reportMoreThanOnce;
        this.stopScanningAfterMS = stopScanningAfterMS;
        this.shnDeviceScannerListener = shnDeviceScannerListener;
    }

    void scanningStarted(@NonNull final SHNDeviceScannerInternal deviceScannerInternal, @NonNull final Handler internalHandler) {
        this.deviceScannerInternal = deviceScannerInternal;
        this.internalHandler = internalHandler;
        internalHandler.postDelayed(timeoutRunnable, stopScanningAfterMS);
    }

    public void scanningStopped() {
        if (internalHandler != null) {
            internalHandler.removeCallbacks(timeoutRunnable);
        }

        shnDeviceScannerListener.scanStopped(null);

        deviceScannerInternal = null;
        internalHandler = null;
    }

    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            deviceScannerInternal.stopScanning(ScanRequest.this);
        }
    };

    public void onScanResult(@NonNull final BleDeviceFoundInfo bleDeviceFoundInfo) {
        if (reportMoreThanOnce || !reportedDeviceMacAddresses.contains(bleDeviceFoundInfo.getDeviceAddress())) {
            reportedDeviceMacAddresses.add(bleDeviceFoundInfo.getDeviceAddress());

            BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.scanRecord);
            for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : deviceDefinitions) {
                boolean matched = false;
                if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher()) {
                    matched = shnDeviceDefinitionInfo.matchesOnAdvertisedData(bleDeviceFoundInfo.bluetoothDevice, bleScanRecord, bleDeviceFoundInfo.rssi);
                } else {
                    Set<UUID> primaryServiceUUIDs = shnDeviceDefinitionInfo.getPrimaryServiceUUIDs();
                    for (UUID uuid : bleScanRecord.getUuids()) {
                        if (primaryServiceUUIDs.contains(uuid)) {
                            matched = true;
                            break;
                        }
                    }
                }
                if (matched) {
                    SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(bleDeviceFoundInfo.bluetoothDevice, bleDeviceFoundInfo.rssi, bleDeviceFoundInfo.scanRecord, shnDeviceDefinitionInfo);
                    shnDeviceScannerListener.deviceFound(null, shnDeviceFoundInfo);
                    break;
                }
            }
        }
    }
}
