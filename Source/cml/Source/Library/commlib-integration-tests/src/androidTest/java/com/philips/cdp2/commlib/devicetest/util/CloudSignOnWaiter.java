/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import com.philips.cdp.cloudcontroller.api.listener.SignonListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CloudSignOnWaiter implements SignonListener {
    CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void signonStatus(boolean signon) {
        latch.countDown();
    }

    public void waitForSignOn(long t, TimeUnit unit) throws InterruptedException {
        latch.await(t, unit);
    }
}
