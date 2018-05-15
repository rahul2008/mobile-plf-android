/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by the {@link SHNDeviceScannerInternal} to scan for devices.
 * <p/>
 * When the {@code SHNDeviceScannerInternal} finds a device that it supports, it will inform all {@code SHNInternalScanRequests}.
 * Each {@code SHNInternalScanRequest} will use its parameters to determine if it should inform its listener.
 *
 * @publicPluginApi
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

    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            stopScanning();
        }
    };

    /**
     * @param deviceDefinitionsToFilter  optional list of SHNDeviceDefinitionInfo to filter. When a non-empty list is supplied, only devices
     *                                   of the supplied type will be reported.
     * @param deviceMacAddressesToFilter optional list of mac addresses to filter. When a non-empty list is supplied, only devices
     *                                   with addresses on the list will be reported.
     * @param allowDuplicates            flag to indicate if a certain device should be reported more than once.
     * @param stopScanningAfterMS        timeout for scanning in milliseconds.
     * @param shnDeviceScannerListener   listener for callbacks.
     */
    SHNInternalScanRequest(@Nullable final List<SHNDeviceDefinitionInfo> deviceDefinitionsToFilter, @Nullable final List<String> deviceMacAddressesToFilter, final boolean allowDuplicates, final long stopScanningAfterMS, @NonNull final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener) {
        this.deviceDefinitions = (deviceDefinitionsToFilter != null ? deviceDefinitionsToFilter : new ArrayList<SHNDeviceDefinitionInfo>());
        this.deviceMacAddresses = (deviceMacAddressesToFilter != null ? deviceMacAddressesToFilter : new ArrayList<String>());
        this.allowDuplicates = allowDuplicates;
        this.stopScanningAfterMS = stopScanningAfterMS;
        this.shnDeviceScannerListener = shnDeviceScannerListener;
    }

    void onScanningStarted(@NonNull final SHNDeviceScannerInternal deviceScannerInternal, @NonNull final Handler internalHandler) {
        this.deviceScannerInternal = deviceScannerInternal;
        this.internalHandler = internalHandler;
        this.internalHandler.postDelayed(timeoutRunnable, stopScanningAfterMS);
    }

    void onScanningStopped() {
        if (internalHandler != null) {
            internalHandler.removeCallbacks(timeoutRunnable);
        }
        shnDeviceScannerListener.scanStopped(null);

        deviceScannerInternal = null;
        internalHandler = null;
    }

    private void stopScanning() {
        deviceScannerInternal.stopScanning(SHNInternalScanRequest.this);
    }

    void onDeviceFound(@NonNull final SHNDeviceFoundInfo deviceFoundInfo) {
        if (isMacAddressToReport(deviceFoundInfo) && isDeviceTypeToReport(deviceFoundInfo)) {
            shnDeviceScannerListener.deviceFound(null, deviceFoundInfo);
        }
    }

    private boolean isDeviceTypeToReport(final @NonNull SHNDeviceFoundInfo deviceFoundInfo) {
        if (deviceDefinitions.isEmpty()) {
            return true;
        } else {
            for (final SHNDeviceDefinitionInfo deviceDefinition : deviceDefinitions) {
                if (deviceDefinition.getDeviceTypeName().equals(deviceFoundInfo.getShnDevice().getDeviceTypeName())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isMacAddressToReport(final @NonNull SHNDeviceFoundInfo deviceFoundInfo) {
        final String deviceAddress = deviceFoundInfo.getDeviceAddress();

        final boolean isAlreadyReported = reportedDeviceMacAddresses.contains(deviceAddress);
        if (!reportedDeviceMacAddresses.contains(deviceAddress)) {
            reportedDeviceMacAddresses.add(deviceFoundInfo.getDeviceAddress());
        }
        boolean allowMacAddress = deviceMacAddresses.isEmpty() || deviceMacAddresses.contains(deviceAddress);

        return allowMacAddress && (allowDuplicates || !isAlreadyReported);
    }
}
