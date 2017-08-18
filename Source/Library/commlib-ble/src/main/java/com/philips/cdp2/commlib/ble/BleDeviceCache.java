/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.pins.shinelib.SHNDevice;

import java.util.concurrent.ScheduledExecutorService;

/**
 * The BleDeviceCache stores {@link SHNDevice} references based on their unique identifier.
 * <p>
 * This makes BLE devices that were scanned (or associated) earlier available for CommLib without having an active
 * connection to them.
 */
public class BleDeviceCache extends DeviceCache<BleCacheData> {

    public BleDeviceCache(@NonNull final ScheduledExecutorService executor) {
        super(executor);
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
        final BleCacheData cacheData = new BleCacheData(executor, expirationCallback, expirationPeriodMillis, networkNode, device);
        this.add(cacheData);
    }

}
