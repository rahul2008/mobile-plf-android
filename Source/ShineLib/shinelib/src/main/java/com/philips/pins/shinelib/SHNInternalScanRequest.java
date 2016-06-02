/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by the SHNDeviceScannerInternal to scan for devices. When the SHNDeviceScannerInternal finds a device that it supports, it will inform all SHNInternalScanRequests. Each SHNInternalScanRequest will use its parameters to determine if it should inform its listener.
 */
public class SHNInternalScanRequest {

    @NonNull
    private final List<SHNDeviceDefinitionInfo> deviceDefinitions;

    @NonNull
    private final List<String> deviceMacAddresses;

    private final boolean allowDuplicates;

    private final long stopScanningAfterMS;

    @NonNull
    final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener;

    private final List<String> reportedDeviceMacAddresses = new ArrayList<>();

    private SHNDeviceScannerInternal deviceScannerInternal;
    private Handler internalHandler;

    /**
     * @param deviceDefinitionsToFilter  optional list of SHNDeviceDefinitionInfo to filter. When a non-empty list is supplied, only devices
     *                                   of the supplied type will be reported.
     * @param deviceMacAddressesToFilter optional list of mac addresses to filter. When a non-empty list is supplied, only devices
     *                                   with addresses on the list will be reported.
     * @param allowDuplicates            flag to indicate if a certain device should be reported more than once.
     * @param stopScanningAfterMS        timeout for scanning in milliseconds.
     * @param shnDeviceScannerListener   listener for callbacks.
     */
    public SHNInternalScanRequest(@Nullable final List<SHNDeviceDefinitionInfo> deviceDefinitionsToFilter, @Nullable final List<String> deviceMacAddressesToFilter, final boolean allowDuplicates, final long stopScanningAfterMS, @NonNull final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener) {
        this.deviceDefinitions = (deviceDefinitionsToFilter != null ? deviceDefinitionsToFilter : new ArrayList<SHNDeviceDefinitionInfo>());
        this.deviceMacAddresses = (deviceMacAddressesToFilter != null ? deviceMacAddressesToFilter : new ArrayList<String>());
        this.allowDuplicates = allowDuplicates;
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

    /**
     * Stops current running scan.
     */
    public void stopScanning() {
        deviceScannerInternal.stopScanning(SHNInternalScanRequest.this);
    }

    void onDeviceFound(@NonNull final SHNDeviceFoundInfo deviceFoundInfo) {
        if (isMacAddressToReport(deviceFoundInfo) && isDeviceTypeToReport(deviceFoundInfo)) {
            shnDeviceScannerListener.deviceFound(null, deviceFoundInfo);
        }
    }

    private boolean isDeviceTypeToReport(final @NonNull SHNDeviceFoundInfo deviceFoundInfo) {
        boolean shouldFilterDeviceDefinitions = !deviceDefinitions.isEmpty();

        if (shouldFilterDeviceDefinitions) {
            for (final SHNDeviceDefinitionInfo deviceDefinition : deviceDefinitions) {
                if (deviceDefinition.getDeviceTypeName().equals(deviceFoundInfo.getShnDevice().getDeviceTypeName())) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    private boolean isMacAddressToReport(final @NonNull SHNDeviceFoundInfo deviceFoundInfo) {
        String deviceAddress = deviceFoundInfo.getDeviceAddress();
        boolean isAlreadyReported = reportedDeviceMacAddresses.contains(deviceAddress);
        if (!isAlreadyReported) {
            reportedDeviceMacAddresses.add(deviceFoundInfo.getDeviceAddress());
        }

        boolean allowMacAddress = deviceMacAddresses.isEmpty() || deviceMacAddresses.contains(deviceAddress);

        return allowMacAddress && (allowDuplicates || !isAlreadyReported);
    }
}
