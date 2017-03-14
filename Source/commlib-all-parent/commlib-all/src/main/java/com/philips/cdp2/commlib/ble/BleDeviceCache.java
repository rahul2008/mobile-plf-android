/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type BleDeviceCache.
 * <p>
 * This stores {@link SHNDevice} references based on their unique identifier. This makes BLE devices
 * that were scanned (or associated) earlier available for CommLib without having an active
 * connection to them.
 */
public final class BleDeviceCache {

    private final class CacheData {
        private final SHNDevice device;
        private final NetworkNode networkNode;

        // TODO timer
        // TODO callback

        private CacheData(final @NonNull SHNDevice device, final @NonNull NetworkNode networkNode) {
            this.device = device;
            this.networkNode = networkNode;
        }

        public NetworkNode getNetworkNode() {
            return networkNode;
        }

        public SHNDevice getDevice() {
            return device;
        }
    }

    private final Map<String, CacheData> deviceMap = new ConcurrentHashMap<>();

    /**
     * Add device.
     *
     * @param device      the device
     * @param networkNode the network node
     * @return true, if the device didn't exist in the cache yet and was therefore added
     */
    public boolean addDevice(@NonNull SHNDevice device, @NonNull NetworkNode networkNode) {
        return deviceMap.put(networkNode.getCppId(), new CacheData(device, networkNode)) == null;
    }

    /**
     * Clear.
     */
    public void clear() {
        deviceMap.clear();
    }

    public SHNDevice getDevice(final @NonNull String cppId) {
        return Collections.unmodifiableMap(deviceMap).get(cppId).getDevice();
    }

    public NetworkNode getNetworkNode(final @NonNull String cppId) {
        return Collections.unmodifiableMap(deviceMap).get(cppId).getNetworkNode();
    }

    public boolean contains(final @NonNull String cppId) {
        return deviceMap.containsKey(cppId);
    }
}
