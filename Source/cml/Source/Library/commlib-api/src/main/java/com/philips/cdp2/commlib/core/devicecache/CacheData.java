/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.devicecache;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache.ExpirationCallback;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CacheData {
    @NonNull
    private final NetworkNode networkNode;
    @NonNull
    private final ExpirationCallback expirationCallback;
    @NonNull
    private final ScheduledExecutorService executor;

    private final long expirationPeriodMillis;
    private ScheduledFuture future;
    private boolean isExpiryNotificationRequired;

    public CacheData(@NonNull final ScheduledExecutorService executor, @NonNull final ExpirationCallback expirationCallback,
                     long expirationPeriodMillis,
                     @NonNull final NetworkNode networkNode) {
        this.executor = executor;
        this.networkNode = networkNode;
        this.expirationPeriodMillis = expirationPeriodMillis;
        this.expirationCallback = expirationCallback;

        startTimer();
    }

    @NonNull
    public NetworkNode getNetworkNode() {
        return networkNode;
    }

    public void resetTimer() {
        synchronized (this) {
            stopTimer();
            startTimer();
        }
    }

    private void startTimer() {
        synchronized (this) {
            isExpiryNotificationRequired = true;

            Callable callable = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    synchronized (CacheData.this) {
                        if (isExpiryNotificationRequired) {
                            expirationCallback.onCacheExpired(networkNode);
                        }
                        return null;
                    }
                }
            };
            future = executor.schedule(callable, this.expirationPeriodMillis, TimeUnit.MILLISECONDS);
        }
    }

    public void stopTimer() {
        synchronized (this) {
            isExpiryNotificationRequired = false;
            if (future != null) {
                future.cancel(false);
            }
        }
    }

    @VisibleForTesting
    @NonNull
    public ExpirationCallback getExpirationCallback() {
        return this.expirationCallback;
    }

    long getExpirationPeriodMillis() {
        return expirationPeriodMillis;
    }
}
