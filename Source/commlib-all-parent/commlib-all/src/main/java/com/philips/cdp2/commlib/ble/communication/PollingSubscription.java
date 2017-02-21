/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class PollingSubscription implements Runnable {
    interface Callback {
        void onCancel();
    }

    @NonNull
    private final CommunicationStrategy communicationStrategy;

    @NonNull
    private final PortParameters portParameters;

    @NonNull
    private final ResponseHandler responseHandler;

    @NonNull
    private ScheduledFuture<?> future;

    private Callback callback;

    private final long endTime;

    PollingSubscription(@NonNull CommunicationStrategy communicationStrategy,
                        @NonNull PortParameters portParameters,
                        final long intervalMillis,
                        final int timeToLiveMillis,
                        final @NonNull ResponseHandler responseHandler) {
        this(communicationStrategy, new ScheduledThreadPoolExecutor(1), portParameters, intervalMillis, timeToLiveMillis, responseHandler);
    }

    @VisibleForTesting
    PollingSubscription(@NonNull CommunicationStrategy communicationStrategy,
                        @NonNull ScheduledExecutorService executor,
                        @NonNull PortParameters portParameters,
                        final long intervalMillis,
                        final int timeToLiveMillis,
                        final @NonNull ResponseHandler responseHandler) {
        this.communicationStrategy = communicationStrategy;
        this.portParameters = portParameters;
        this.endTime = currentTimeMillis() + timeToLiveMillis;
        this.responseHandler = responseHandler;
        this.future = executor.scheduleWithFixedDelay(this, 0, intervalMillis, MILLISECONDS);
    }

    @Override
    public void run() {
        final CountDownLatch latch = createCountdownLatch();
        ResponseHandler internalResponseHandler = new ResponseHandler() {

            @Override
            public void onSuccess(final String data) {
                responseHandler.onSuccess(data);
                latch.countDown();
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
                latch.countDown();
            }
        };

        if (currentTimeMillis() > endTime) {
            cancel();
        } else {
            communicationStrategy.getProperties(portParameters.portName, portParameters.productId, internalResponseHandler);
            try {
                latch.await();
            } catch (InterruptedException ignored) {
                // Just means the next getProps will get scheduled a bit early
            }
        }
    }

    void addCancelCallback(Callback callback) {
        this.callback = callback;
    }

    void cancel() {
        future.cancel(false);

        if (this.callback != null) {
            this.callback.onCancel();
        }
    }

    @VisibleForTesting
    long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @VisibleForTesting
    CountDownLatch createCountdownLatch() {
        return new CountDownLatch(1);
    }
}
