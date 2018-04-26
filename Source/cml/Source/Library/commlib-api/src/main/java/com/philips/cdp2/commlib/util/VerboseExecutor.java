/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

import android.support.annotation.VisibleForTesting;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VerboseExecutor extends ThreadPoolExecutor {

    private volatile boolean isExecuting = false;

    public VerboseExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new VerboseLinkedBlockingQueue<Runnable>());

        ((VerboseLinkedBlockingQueue<Runnable>) getQueue()).setListener(createQueueListener());
    }

    @VisibleForTesting
    VerboseLinkedBlockingQueueListener<Runnable> createQueueListener() {
        return new VerboseLinkedBlockingQueueListener<Runnable>() {
            @Override
            public void onBeforeTake(Runnable task) {
                isExecuting = true;
            }

            @Override
            public void onQueueEmpty() {
            }
        };
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        isExecuting = true;
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        isExecuting = false;
    }

    public boolean isIdle() {
        return getQueue().isEmpty() && !isExecuting;
    }
}

