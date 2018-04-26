/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.util;

public interface VerboseLinkedBlockingQueueListener<E> {
    void onBeforeTake(E element);

    void onQueueEmpty();
}
