/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class PollingSubscription implements Runnable {

    @NonNull
    private final CommunicationStrategy communicationStrategy;
    @NonNull
    private final PortParameters portParameters;
    @NonNull
    private final ResponseHandler responseHandler;
    private final long endTime;
    @NonNull
    private ScheduledFuture<?> future;

    PollingSubscription(@NonNull CommunicationStrategy communicationStrategy, @NonNull ScheduledExecutorService executor, @NonNull PortParameters portParameters, final long intervalMillis, final int timeToLiveMillis, final @NonNull ResponseHandler responseHandler) {
        this.communicationStrategy = communicationStrategy;
        this.portParameters = portParameters;
        this.endTime = currentTimeMillis() + timeToLiveMillis;
        this.responseHandler = responseHandler;

        this.future = executor.scheduleWithFixedDelay(this, 0, intervalMillis, MILLISECONDS);
    }

    @Override
    public void run() {
        if (currentTimeMillis() > endTime) {
            cancel();
        } else {
            communicationStrategy.getProperties(portParameters.portName, portParameters.productId, responseHandler);
        }
    }

    void cancel() {
        future.cancel(false);
    }

    @VisibleForTesting
    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
