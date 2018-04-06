package com.philips.cdp2.commlib.util;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VerboseExecutor extends ThreadPoolExecutor implements VerboseLinkedBlockingQueueListener {

    private volatile boolean isExecuting = false;

    public VerboseExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new VerboseLinkedBlockingQueue<Runnable>());

        ((VerboseLinkedBlockingQueue)getQueue()).listener = this;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        this.isExecuting = false;
    }

    @Override
    public void beforeTakingOperation() {
        this.isExecuting = true;
    }

    public boolean isIdle() {
        return getQueue().isEmpty() && !this.isExecuting;
    }
}

