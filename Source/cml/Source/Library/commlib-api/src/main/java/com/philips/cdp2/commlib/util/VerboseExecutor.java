package com.philips.cdp2.commlib.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VerboseExecutor extends ThreadPoolExecutor {

    private boolean isExecuting = false;

    public VerboseExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue <Runnable>());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        this.isExecuting = true;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        this.isExecuting = false;
    }

    public boolean isIdle() {
        return getQueue().isEmpty() && !this.isExecuting;
    }
}
