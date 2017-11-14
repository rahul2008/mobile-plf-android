/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.util.ObservableCollection;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;

public class DeviceCache<T extends CacheData> implements ObservableCollection<String> {

    @NonNull
    protected final ScheduledExecutorService executor;
    @NonNull
    private final Map<String, T> deviceMap = new ConcurrentHashMap<>();

    private final Map<String, Set<ModificationListener<String>>> modificationListeners = new ConcurrentHashMap<>();

    public DeviceCache(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void clear() {
        for (String ccpId : deviceMap.keySet()) {
            remove(ccpId);
        }
    }

    public void stopTimers() {
        for (T cachedata : deviceMap.values()) {
            cachedata.stopTimer();
        }
    }

    public void resetTimers() {
        for (T cachedata : deviceMap.values()) {
            cachedata.resetTimer();
        }
    }

    public void add(T cacheData) {
        if (cacheData.getExpirationPeriodMillis() <= 0L) {
            throw new IllegalArgumentException("Expiration period must be a positive non-zero value.");
        }

        final String cppId = cacheData.getNetworkNode().getCppId();
        if (deviceMap.containsKey(cppId)) {
            deviceMap.get(cppId).resetTimer();
        } else {
            deviceMap.put(cppId, cacheData);

            if (modificationListeners.containsKey(cppId)) {
                for (ModificationListener<String> listener : modificationListeners.get(cppId)) {
                    listener.onAdded(cppId);
                }
            }
        }
    }

    public T getCacheData(@NonNull final String cppId) {
        return deviceMap.get(cppId);
    }

    public boolean contains(@NonNull final String cppId) {
        return deviceMap.containsKey(cppId);
    }

    public T remove(@NonNull final String cppId) {
        final T removed = deviceMap.remove(cppId);

        if (modificationListeners.containsKey(cppId)) {
            for (ModificationListener<String> listener : modificationListeners.get(cppId)) {
                listener.onRemoved(cppId);
            }
        }

        return removed;
    }

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }

    @Override
    public void addModificationListener(String forCppId, ModificationListener<String> listener) {
        if (modificationListeners.get(forCppId) == null) {
            modificationListeners.put(forCppId, new CopyOnWriteArraySet<ModificationListener<String>>());
        }

        modificationListeners.get(forCppId).add(listener);

        if (deviceMap.containsKey(forCppId)) {
            listener.onAdded(forCppId);
        }
    }

    @Override
    public void removeModificationListener(String forCppId, ModificationListener<String> listener) {
        if (modificationListeners.get(forCppId) != null) {
            modificationListeners.get(forCppId).remove(listener);
        }
    }
}
