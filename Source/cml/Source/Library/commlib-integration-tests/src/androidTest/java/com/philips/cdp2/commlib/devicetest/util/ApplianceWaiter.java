/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.util;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager.ApplianceListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class ApplianceWaiter {

    public interface Waiter<A extends Appliance> extends ApplianceListener {
        Waiter<A> waitForAppliance(long time, TimeUnit timeUnit) throws InterruptedException;

        A getAppliance();
    }

    private static abstract class BaseWaiter<A extends Appliance> implements Waiter<A> {
        final CountDownLatch latch = new CountDownLatch(1);
        A appliance;

        @Override
        public Waiter<A> waitForAppliance(long time, TimeUnit timeUnit) throws InterruptedException {
            latch.await(time, timeUnit);
            return this;
        }

        @Override
        @NonNull
        public A getAppliance() {
            return appliance;
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance updatedAppliance) {
            // NOP
        }

        @Override
        public void onApplianceLost(@NonNull Appliance lostAppliance) {
            // NOP
        }
    }

    public static <A extends Appliance> Waiter<A> forCppId(@NonNull final String cppId) {
        requireNonNull(cppId);
        return new BaseWaiter<A>() {
            @Override
            @SuppressWarnings("unchecked")
            public void onApplianceFound(@NonNull Appliance foundAppliance) {
                if (cppId.equals(foundAppliance.getNetworkNode().getCppId())) {
                    appliance = (A) requireNonNull(foundAppliance);
                    latch.countDown();
                }
            }
        };
    }

}
