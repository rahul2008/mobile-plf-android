/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import com.philips.cdp.dicommclient.port.common.PairingListener;
import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PairingWaiter implements PairingListener<Appliance> {
    public boolean pairingSucceeded = false;
    CountDownLatch latch = new CountDownLatch(1);

    public void reset() {
        pairingSucceeded = false;
        latch = new CountDownLatch(1);
    }

    @Override
    public void onPairingSuccess(Appliance appliance) {
        pairingSucceeded = true;
        latch.countDown();
    }

    @Override
    public void onPairingFailed(Appliance appliance) {
        pairingSucceeded = false;
        latch.countDown();
    }

    public void waitForPairingCompleted(long t, TimeUnit unit) throws InterruptedException {
        latch.await(t, unit);
    }
}
