/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.SCAN_WINDOW_MILLIS;

/**
 * The type BleDeviceCache.
 * <p>
 * This stores {@link SHNDevice} references based on their unique identifier. This makes BLE devices
 * that were scanned (or associated) earlier available for CommLib without having an active
 * connection to them.
 */
public class BleDeviceCache {

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }

    public static final class CacheData {

        private final SHNDevice device;
        private final NetworkNode networkNode;
        @NonNull
        private final ExpirationCallback expirationCallback;
        private Timer expirationTimer;
        private TimerTask expirationTask;

        private CacheData(final @NonNull SHNDevice device, final @NonNull NetworkNode networkNode, final @NonNull ExpirationCallback expirationCallback) {
            this.device = device;
            this.networkNode = networkNode;
            this.expirationCallback = expirationCallback;
            this.expirationTimer = new Timer();

            resetTimer();
        }

        public NetworkNode getNetworkNode() {
            return networkNode;
        }

        public SHNDevice getDevice() {
            return device;
        }

        public void resetTimer() {
            if (expirationTask != null) {
                expirationTask.cancel();
            }

            expirationTask = new TimerTask() {
                @Override
                public void run() {
                    expirationCallback.onCacheExpired(networkNode);
                }
            };
            expirationTimer.schedule(expirationTask, SCAN_WINDOW_MILLIS);
        }
    }

    private final Map<String, CacheData> deviceMap = new ConcurrentHashMap<>();

    /**
     * Add device.
     *
     * @param device             the device
     * @param networkNode        the network node
     * @param expirationCallback the callback that is invoked when the expiration for the cached data was reached
     */
    public void addDevice(@NonNull SHNDevice device, @NonNull NetworkNode networkNode, @NonNull ExpirationCallback expirationCallback) {
        if (deviceMap.containsKey(networkNode.getCppId())) {
            deviceMap.get(networkNode.getCppId()).resetTimer();
        } else {
            deviceMap.put(networkNode.getCppId(), new CacheData(device, networkNode, expirationCallback));
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        deviceMap.clear();
    }

    public CacheData getCacheData(final @NonNull String cppId) {
        return deviceMap.get(cppId);
    }

    public boolean contains(final @NonNull String cppId) {
        return deviceMap.containsKey(cppId);
    }
}
