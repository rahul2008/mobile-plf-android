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

    private final Callable expirationTask = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            if (future != null && !future.isDone()) {
                expirationCallback.onCacheExpired(networkNode);
            }
            return null;
        }
    };

    public CacheData(@NonNull final ScheduledExecutorService executor, @NonNull final ExpirationCallback expirationCallback, long expirationPeriodMillis, @NonNull final NetworkNode networkNode) {
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
        future = executor.schedule(expirationTask, this.expirationPeriodMillis, TimeUnit.MILLISECONDS);
    }

    void stopTimer() {
        if (future != null) {
            future.cancel(true);
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
