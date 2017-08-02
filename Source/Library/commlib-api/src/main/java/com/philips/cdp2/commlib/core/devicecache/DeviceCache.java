/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

public class DeviceCache<T extends CacheData> {

    @NonNull
    protected final ScheduledExecutorService executor;
    @NonNull
    protected final Map<String, T> deviceMap = new ConcurrentHashMap<>();

    public DeviceCache(@NonNull final ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void clear() {
        deviceMap.clear();
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

        final NetworkNode networkNode = cacheData.getNetworkNode();
        if (deviceMap.containsKey(networkNode.getCppId())) {
            deviceMap.get(networkNode.getCppId()).resetTimer();
        } else {
            deviceMap.put(networkNode.getCppId(), cacheData);
        }
    }

    public T getCacheData(@NonNull final String cppId) {
        return deviceMap.get(cppId);
    }

    public boolean contains(@NonNull final String cppId) {
        return deviceMap.containsKey(cppId);
    }

    public T remove(@NonNull final String cppId) {
        return deviceMap.remove(cppId);
    }

    public interface ExpirationCallback {
        void onCacheExpired(NetworkNode networkNode);
    }
}
