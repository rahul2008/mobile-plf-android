/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;

import java.util.concurrent.ScheduledExecutorService;

/**
 * The type LanDeviceCache.
 * <p>
 * This stores {@link NetworkNode} references based on their unique identifier.
 */
public class LanDeviceCache extends DeviceCache<CacheData> {

    public LanDeviceCache(@NonNull ScheduledExecutorService executor) {
        super(executor);
    }

    public void addNetworkNode(final @NonNull NetworkNode networkNode, final @NonNull ExpirationCallback expirationCallback, long expirationPeriodMillis) {
        super.add(new CacheData(executor, expirationCallback, expirationPeriodMillis, networkNode));
    }
}
