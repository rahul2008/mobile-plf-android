/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.cdp2.commlib.core.util.Availability;
import com.philips.pins.shinelib.SHNDevice;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;

public class BleCacheData extends CacheData implements Availability<BleCacheData> {
    @NonNull
    private final SHNDevice device;

    private boolean isAvailable = true;
    private CopyOnWriteArraySet<AvailabilityListener<BleCacheData>> availabilityListeners = new CopyOnWriteArraySet<>();

    BleCacheData(@NonNull final ScheduledExecutorService executor,
                 @NonNull final ExpirationCallback expirationCallback,
                 long expirationPeriodMillis,
                 @NonNull final NetworkNode networkNode,
                 @NonNull final SHNDevice device) {
        super(executor, expirationCallback, expirationPeriodMillis, networkNode);
        this.device = device;
    }

    @NonNull
    public SHNDevice getDevice() {
        return device;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
        for (AvailabilityListener<BleCacheData> listener : availabilityListeners) {
            listener.onAvailabilityChanged(this);
        }
    }

    @Override
    public void addAvailabilityListener(@NonNull AvailabilityListener<BleCacheData> listener) {
        availabilityListeners.add(listener);
        listener.onAvailabilityChanged(this);
    }

    @Override
    public void removeAvailabilityListener(@NonNull AvailabilityListener<BleCacheData> listener) {
        availabilityListeners.remove(listener);
    }
}
