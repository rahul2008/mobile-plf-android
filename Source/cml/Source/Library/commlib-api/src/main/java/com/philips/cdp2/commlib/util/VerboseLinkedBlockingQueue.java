package com.philips.cdp2.commlib.util;

import java.util.concurrent.LinkedBlockingQueue;

public class VerboseLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private VerboseLinkedBlockingQueueListener listener;

    @Override
    public E take() throws InterruptedException {
        if (listener != null) {
            listener.onBeforeTake();
        }
        return super.take();
    }

    public void setListener(VerboseLinkedBlockingQueueListener listener) {
        this.listener = listener;
    }
}

