/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNDevice;

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
public class BleDeviceCache {
    private final Map<String, SHNDevice> deviceMap = new HashMap<>();

    /**
     * Gets the unique identifier for a device.
     *
     * @param device the device
     * @return the unique device identifier
     */
    private static final String getUniqueDeviceIdentifier(SHNDevice device) {
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

    /**
     * Add device.
     *
     * @param device the device
     * @return true, if the device didn't exist in the cache yet and was therefore added
     */
    public boolean addDevice(@NonNull SHNDevice device) {
        return deviceMap.put(getUniqueDeviceIdentifier(device), device) == null;
    }

    /**
     * Clear.
     */
    public void clear() {
        deviceMap.clear();
    }
}
