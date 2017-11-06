/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.Availability;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StrategyChangedWaiter implements Availability.AvailabilityListener<CommunicationStrategy> {
    public boolean strategrySwitchedCorrectly = false;
    Class<? extends  CommunicationStrategy> strategyClass;
    CountDownLatch latch = new CountDownLatch(1);

    public StrategyChangedWaiter(Class<? extends  CommunicationStrategy> strategyClass) {
        this.strategyClass = strategyClass;
    }

    @Override
    public void onAvailabilityChanged(@NonNull CommunicationStrategy strategy) {
        CombinedCommunicationTestingStrategy testingStrategy = (CombinedCommunicationTestingStrategy)strategy;
        Class<? extends  CommunicationStrategy> newStrategy = testingStrategy.findStrategy().getClass();
        if(newStrategy.equals(strategyClass)) {
            strategrySwitchedCorrectly = true;
            latch.countDown();
        }
    }

    public void waitForStrategyChange(long t, TimeUnit unit) throws InterruptedException {
        latch.await(t, unit);
    }
}
