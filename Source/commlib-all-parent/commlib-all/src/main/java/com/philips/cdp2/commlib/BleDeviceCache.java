/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type BleDeviceCache.
 * <p>
 * This stores {@link SHNDevice} references based on their unique identifier. This makes BLE devices
 * that were scanned (or associated) earlier available for CommLib without having an active
 * connection to them.
 */
public class BleDeviceCache implements SHNDeviceScanner.SHNDeviceScannerListener {
    private final Map<String, SHNDevice> deviceMap = new HashMap<>();

    /**
     * Gets the unique identifier for a device.
     *
     * @param device the device
     * @return the unique device identifier
     */
    static String getUniqueDeviceIdentifier(SHNDevice device) {
        return device.getAddress();
    }

    /**
     * Get device map.
     *
     * @return the map
     */
    @NonNull
    public Map<String, SHNDevice> getDeviceMap() {
        return Collections.unmodifiableMap(deviceMap);
    }

    @Override
    public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
        final SHNDevice device = shnDeviceFoundInfo.getShnDevice();
        deviceMap.put(getUniqueDeviceIdentifier(device), device);
    }

    @Override
    public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
        //don't care
    }
}
