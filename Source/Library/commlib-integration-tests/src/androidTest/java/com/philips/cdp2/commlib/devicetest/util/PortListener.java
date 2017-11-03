/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PortListener implements DICommPortListener {
    public List<Error> errors = new ArrayList<>();
    public int receivedCount = 0;
    CountDownLatch latch = new CountDownLatch(1);

    public void reset() {
        reset(1);
    }

    public void reset(int count) {
        errors.clear();
        receivedCount = 0;
        latch = new CountDownLatch(count);
    }

    @Override
    public void onPortUpdate(DICommPort port) {
        receivedCount++;
        latch.countDown();
    }

    @Override
    public void onPortError(DICommPort port, Error error, @Nullable String errorData) {
        errors.add(error);
        latch.countDown();
    }

    public void waitForPortUpdate(long t, TimeUnit unit) throws InterruptedException {
        latch.await(t, unit);
    }
}
