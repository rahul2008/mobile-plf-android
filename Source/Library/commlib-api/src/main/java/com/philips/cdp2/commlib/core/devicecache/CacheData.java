/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CacheData {
    @NonNull
    private final NetworkNode networkNode;
    @NonNull
    private final DeviceCache.ExpirationCallback expirationCallback;
    @NonNull
    private final ScheduledExecutorService executor;

    private final long expirationPeriodMillis;
    private ScheduledFuture future;

    public CacheData(@NonNull final ScheduledExecutorService executor,
                     @NonNull final DeviceCache.ExpirationCallback expirationCallback,
                     long expirationPeriodMillis,
                     @NonNull final NetworkNode networkNode) {
        this.executor = executor;
        this.networkNode = networkNode;
        this.expirationPeriodMillis = expirationPeriodMillis;
        this.expirationCallback = expirationCallback;

        resetTimer();
    }

    @NonNull
    public NetworkNode getNetworkNode() {
        return networkNode;
    }

    public void resetTimer() {
        stopTimer();

        Callable callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                expirationCallback.onCacheExpired(networkNode);
                return null;
            }
        };
        future = executor.schedule(callable, this.expirationPeriodMillis, TimeUnit.MILLISECONDS);
    }

    public void stopTimer() {
        if (future != null) {
            future.cancel(false);
        }
    }

    @NonNull
    public DeviceCache.ExpirationCallback getExpirationCallback() {
        return this.expirationCallback;
    }

    public long getExpirationPeriodMillis() {
        return expirationPeriodMillis;
    }
}
