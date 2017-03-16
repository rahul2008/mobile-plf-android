/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The type BleDeviceCache.
 * <p>
 * This stores {@link SHNDevice} references based on their unique identifier. This makes BLE devices
 * that were scanned (or associated) earlier available for CommLib without having an active
 * connection to them.
 */
public class BleDeviceCache {

    @NonNull
    private final ScheduledExecutorService executor;

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }

    public class CacheData {
        private final SHNDevice device;
        private final NetworkNode networkNode;

        @NonNull
        private final ExpirationCallback expirationCallback;
        private final long expirationPeriodMillis;

        private ScheduledFuture future;

        private CacheData(@NonNull final SHNDevice device, @NonNull final NetworkNode networkNode, @NonNull final ExpirationCallback expirationCallback, long expirationPeriodMillis) {
            this.device = device;
            this.networkNode = networkNode;
            this.expirationCallback = expirationCallback;
            this.expirationPeriodMillis = expirationPeriodMillis;

            resetTimer();
        }

        public NetworkNode getNetworkNode() {
            return networkNode;
        }

        public SHNDevice getDevice() {
            return device;
        }

        public void resetTimer() {
            if (future != null) {
                future.cancel(true);
            }

            Callable callable = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    expirationCallback.onCacheExpired(networkNode);
                    return null;
                }
            };
            future = executor.schedule(callable, this.expirationPeriodMillis, TimeUnit.MILLISECONDS);
        }

        @NonNull
        public ExpirationCallback getExpirationCallback() {
            return this.expirationCallback;
        }
    }

    private final Map<String, CacheData> deviceMap = new ConcurrentHashMap<>();

    public BleDeviceCache(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Add device.
     *
     * @param device                 the device
     * @param networkNode            the network node
     * @param expirationCallback     the callback that is invoked when the expiration for the cached data was reached
     * @param expirationPeriodMillis the expiration period in milliseconds
     */
    public void addDevice(@NonNull SHNDevice device, @NonNull NetworkNode networkNode, @NonNull ExpirationCallback expirationCallback, long expirationPeriodMillis) {
        if (expirationPeriodMillis <= 0L) {
            throw new IllegalArgumentException("Expiration period must be a positive non-zero value.");
        }

        if (deviceMap.containsKey(networkNode.getCppId())) {
            deviceMap.get(networkNode.getCppId()).resetTimer();
        } else {
            deviceMap.put(networkNode.getCppId(), new CacheData(device, networkNode, expirationCallback, expirationPeriodMillis));
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        deviceMap.clear();
    }

    public CacheData getCacheData(@NonNull final String cppId) {
        return deviceMap.get(cppId);
    }

    public boolean contains(@NonNull final String cppId) {
        return deviceMap.containsKey(cppId);
    }
}
