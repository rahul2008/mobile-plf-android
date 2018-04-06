package com.philips.cdp2.commlib.util;

import java.util.concurrent.LinkedBlockingQueue;

public class VerboseLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    VerboseLinkedBlockingQueueListener listener;

    @Override
    public E take() throws InterruptedException {
        if (listener != null) {
            listener.beforeTakingOperation();
        }
        return super.take();
    }
}

