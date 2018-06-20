/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.Collections.unmodifiableCollection;

public class DeviceCache {

    public interface DeviceCacheListener {
        void onAdded(CacheData cacheData);

        void onRemoved(CacheData cacheData);
    }

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }

    @NonNull
    protected final ScheduledExecutorService executor;

    @NonNull
    private final Map<String, CacheData> data = new ConcurrentHashMap<>();

    private final Set<DeviceCacheListener> deviceCacheListeners = new CopyOnWriteArraySet<>();

    public DeviceCache(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Clear the cache.
     * <p>
     *
     * @return the collection of items that are removed from the cache.
     */
    public synchronized Collection<CacheData> clear() {
        final Collection<CacheData> cleared = unmodifiableCollection(new ArrayList<>(data.values()));

        for (String key : data.keySet()) {
            remove(key);
        }
        return cleared;
    }

    public void stopTimers() {
        for (CacheData cacheData : data.values()) {
            cacheData.stopTimer();
        }
    }

    public void add(final @NonNull NetworkNode networkNode, final @NonNull ExpirationCallback expirationCallback, long expirationPeriodMillis) {
        add(new CacheData(executor, expirationCallback, expirationPeriodMillis, networkNode));
    }

    @VisibleForTesting
    synchronized void add(final @NonNull CacheData cacheData) {
        if (cacheData.getExpirationPeriodMillis() <= 0L) {
            throw new IllegalArgumentException("Expiration period must be a positive non-zero value.");
        }

        final String cppId = cacheData.getNetworkNode().getCppId();
        if (data.containsKey(cppId)) {
            data.get(cppId).resetTimer();
        } else {
            data.put(cppId, cacheData);

            notifyCacheDataAdded(cacheData);
        }
    }

    public CacheData getCacheData(@NonNull final String cppId) {
        return data.get(cppId);
    }

    public boolean contains(@NonNull final String cppId) {
        return data.containsKey(cppId);
    }

    public CacheData remove(@NonNull final String cppId) {
        final CacheData cacheData = data.remove(cppId);
        cacheData.stopTimer();

        notifyCacheDataRemoved(cacheData);

        return cacheData;
    }

    public void addDeviceCacheListener(final @NonNull DeviceCacheListener listener, String forCppId) {
        deviceCacheListeners.add(listener);

        final CacheData cacheData = data.get(forCppId);
        if (cacheData != null) {
            notifyCacheDataAdded(cacheData);
        }
    }

    public void removeDeviceCacheListener(final @NonNull DeviceCacheListener listener) {
        deviceCacheListeners.remove(listener);
    }

    private void notifyCacheDataAdded(CacheData cacheData) {
        for (DeviceCacheListener listener : deviceCacheListeners) {
            listener.onAdded(cacheData);
        }
    }

    private void notifyCacheDataRemoved(CacheData cacheData) {
        for (DeviceCacheListener listener : deviceCacheListeners) {
            listener.onRemoved(cacheData);
        }
    }
}
