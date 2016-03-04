package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    private final boolean reportMoreThanOnce;

    private final long stopScanningAfterMS;

    @NonNull
    private final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener;

    private final List<String> reportedDeviceMacAddresses = new ArrayList<>();

    private SHNDeviceScannerInternal deviceScannerInternal;
    private Handler internalHandler;

    public ScanRequest(@NonNull final List<SHNDeviceDefinitionInfo> deviceDefinitions, @Nullable final List<String> deviceMacAddresses, final boolean reportMoreThanOnce, final long stopScanningAfterMS, @NonNull final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener) {
        this.deviceDefinitions = deviceDefinitions;
        this.deviceMacAddresses = (deviceMacAddresses != null ? deviceMacAddresses : new ArrayList<String>());
        this.reportMoreThanOnce = reportMoreThanOnce;
        this.stopScanningAfterMS = stopScanningAfterMS;
        this.shnDeviceScannerListener = shnDeviceScannerListener;
    }

    void scanningStarted(@NonNull final SHNDeviceScannerInternal deviceScannerInternal, @NonNull final Handler internalHandler) {
        this.deviceScannerInternal = deviceScannerInternal;
        this.internalHandler = internalHandler;
        internalHandler.postDelayed(timeoutRunnable, stopScanningAfterMS);
    }

    void scanningStopped() {
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
            stopScanning();
        }
    };

    public void stopScanning() {
        deviceScannerInternal.stopScanning(ScanRequest.this);
    }

    public void onScanResult(@NonNull final BleDeviceFoundInfo bleDeviceFoundInfo) {
        String deviceAddress = bleDeviceFoundInfo.getDeviceAddress();
        boolean allowMacAddress = deviceMacAddresses.isEmpty() || deviceMacAddresses.contains(deviceAddress);

        if (allowMacAddress && (reportMoreThanOnce || !reportedDeviceMacAddresses.contains(deviceAddress))) {
            reportedDeviceMacAddresses.add(bleDeviceFoundInfo.getDeviceAddress());

            BleScanRecord bleScanRecord = BleScanRecord.createNewInstance(bleDeviceFoundInfo.getScanRecord());
            for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : deviceDefinitions) {
                boolean matched = determineMatch(bleDeviceFoundInfo, bleScanRecord, shnDeviceDefinitionInfo);
                if (matched) {
                    SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(bleDeviceFoundInfo.getBluetoothDevice(), bleDeviceFoundInfo.getRssi(), bleDeviceFoundInfo.getScanRecord(), shnDeviceDefinitionInfo, bleScanRecord);
                    shnDeviceScannerListener.deviceFound(null, shnDeviceFoundInfo);
                    break;
                }
            }
        }
    }

    private boolean determineMatch(final @NonNull BleDeviceFoundInfo bleDeviceFoundInfo, final BleScanRecord bleScanRecord, final SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
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
