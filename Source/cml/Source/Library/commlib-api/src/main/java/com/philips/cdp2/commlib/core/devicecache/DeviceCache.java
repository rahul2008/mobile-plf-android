/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.Collections.unmodifiableCollection;

public class DeviceCache<T extends CacheData> {

    public interface DeviceCacheListener<T> {
        void onAdded(T cacheData);

        void onRemoved(T cacheData);
    }

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }

    @NonNull
    protected final ScheduledExecutorService executor;

    @NonNull
    private final Map<String, T> data = new ConcurrentHashMap<>();

    private final Set<DeviceCacheListener<T>> deviceCacheListeners = new CopyOnWriteArraySet<>();

    public DeviceCache(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Clear the cache.
     * <p>
     *
     * @return the collection of items that are removed from the cache.
     */
    public synchronized Collection<T> clear() {
        final Collection<T> cleared = unmodifiableCollection(new ArrayList<>(data.values()));

        for (String key : data.keySet()) {
            remove(key);
        }
        return cleared;
    }

    public void stopTimers() {
        for (T cacheData : data.values()) {
            cacheData.stopTimer();
        }
    }

    public void resetTimers() {
        for (T cacheData : data.values()) {
            cacheData.resetTimer();
        }
    }

    public synchronized void add(T cacheData) {
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

    public T getCacheData(@NonNull final String cppId) {
        return data.get(cppId);
    }

    public boolean contains(@NonNull final String cppId) {
        return data.containsKey(cppId);
    }

    public T remove(@NonNull final String cppId) {
        final T cacheData = data.remove(cppId);

        notifyCacheDataRemoved(cacheData);

        return cacheData;
    }

    public void addDeviceCacheListener(final @NonNull DeviceCacheListener<T> listener) {
        deviceCacheListeners.add(listener);
    }

    public void removeDeviceCacheListener(final @NonNull DeviceCacheListener<T> listener) {
        deviceCacheListeners.remove(listener);
    }

    private void notifyCacheDataAdded(T cacheData) {
        for (DeviceCacheListener<T> listener : deviceCacheListeners) {
            listener.onAdded(cacheData);
        }
    }

    private void notifyCacheDataRemoved(T cacheData) {
        for (DeviceCacheListener<T> listener : deviceCacheListeners) {
            listener.onRemoved(cacheData);
        }
    }
}
