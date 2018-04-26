/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

import java.util.concurrent.LinkedBlockingQueue;

public class VerboseLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private VerboseLinkedBlockingQueueListener<E> listener;

    @Override
    public E take() throws InterruptedException {
        E element = super.peek();

        if (listener != null) {
            if (element == null) {
                listener.onQueueEmpty();
            } else {
                listener.onBeforeTake(element);
            }
        }
        return super.take();
    }

    public void setListener(VerboseLinkedBlockingQueueListener<E> listener) {
        this.listener = listener;
    }
}

