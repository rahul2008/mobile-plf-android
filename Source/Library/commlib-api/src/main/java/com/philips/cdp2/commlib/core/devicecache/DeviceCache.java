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

    public void pauseTimers() {
        for (T cachedata : deviceMap.values()) {
            cachedata.pauseTimer();
        }
    }

    public void resetTimeres() {
        for (T cachedata : deviceMap.values()) {
            cachedata.resetTimer();
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
