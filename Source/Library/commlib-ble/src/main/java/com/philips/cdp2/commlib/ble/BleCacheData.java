package com.philips.cdp2.commlib.ble;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.CacheData;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;
import com.philips.pins.shinelib.SHNDevice;

import java.util.concurrent.ScheduledExecutorService;

public class BleCacheData extends CacheData {
    @NonNull
    private final SHNDevice device;

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
}
