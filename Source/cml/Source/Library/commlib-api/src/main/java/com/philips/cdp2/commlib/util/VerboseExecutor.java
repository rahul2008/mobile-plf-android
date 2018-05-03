/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VerboseExecutor extends ThreadPoolExecutor {

    private final Object idleLock = new Object();

    private int nrOfScheduledTasks = 0;

    public VerboseExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void execute(final Runnable command) {
        synchronized (idleLock) {
            nrOfScheduledTasks++;
        }
        super.execute(command);
    }

    @Override
    protected void afterExecute(final Runnable r, final Throwable t) {
        super.afterExecute(r, t);
        synchronized (idleLock) {
            nrOfScheduledTasks--;
        }
    }

    public boolean isIdle() {
        synchronized (idleLock) {
            return nrOfScheduledTasks == 0;
        }
    }
}

