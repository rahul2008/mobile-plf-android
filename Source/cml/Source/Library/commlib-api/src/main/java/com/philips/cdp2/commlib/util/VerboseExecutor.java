package com.philips.cdp2.commlib.util;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VerboseExecutor extends ThreadPoolExecutor {

    private volatile boolean isExecuting = false;

    private final VerboseLinkedBlockingQueueListener listener = new VerboseLinkedBlockingQueueListener() {
        @Override
        public void onBeforeTake() {
            isExecuting = true;
        }
    };

    public VerboseExecutor() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new VerboseLinkedBlockingQueue<Runnable>());

        ((VerboseLinkedBlockingQueue) getQueue()).setListener(this.listener);
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

