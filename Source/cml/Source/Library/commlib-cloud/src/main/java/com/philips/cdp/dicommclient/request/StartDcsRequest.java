/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.CloudController.DCSStartListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp.cloudcontroller.api.CloudController.ICPClientDCSState.STARTED;

public class StartDcsRequest extends Request {

    private long timeout = TimeUnit.SECONDS.toSeconds(10);

    private CloudController cloudController;

    public StartDcsRequest(final @NonNull CloudController cloudController, final @NonNull ResponseHandler responseHandler) {
        super(null, responseHandler);
        this.cloudController = cloudController;
    }

    @Override
    public Response execute() {
        final CountDownLatch latch = new CountDownLatch(1);

        cloudController.startDCSService(new DCSStartListener() {
            @Override
            public void onResponseReceived() {
                latch.countDown();
            }
        });

        try {
            latch.await(timeout, TimeUnit.SECONDS);

            if (cloudController.getState() != STARTED) {
                return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
            }
        } catch (InterruptedException e) {
            return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
        }
        return new Response(null, null, mResponseHandler);
    }

    /**
     * @param timeout of the synchronization in seconds
     */
    @VisibleForTesting
    void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
