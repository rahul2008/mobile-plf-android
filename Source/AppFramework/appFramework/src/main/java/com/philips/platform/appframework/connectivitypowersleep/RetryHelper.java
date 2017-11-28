/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryHelper {

    private static final int MAX_ATTEMPTS = 3;

    AtomicInteger retryCount;

    public RetryHelper() {
        this.retryCount = new AtomicInteger();
    }

    public void reset() {
        retryCount.set(0);
    }

    public boolean canRetry() {
        return retryCount.incrementAndGet() < MAX_ATTEMPTS + 1;
    }
}
