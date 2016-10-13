/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.cloudcontroller.CloudController;

public class StartDcsRequest extends Request {

    private long TIME_OUT = 10 * 1000L;

    private final CloudController cloudController;
    private final Object lock;

    public StartDcsRequest(final CloudController cloudController, final ResponseHandler responseHandler) {
        super(null, responseHandler);
        this.cloudController = cloudController;

        lock = new Object();

    }

    private final CloudController.DCSStartListener dcsStartListener = new CloudController.DCSStartListener() {
        @Override
        public void onResponseReceived() {
            synchronized (lock) {
                lock.notify();
            }
        }
    };

    @Override
    public Response execute() {
        synchronized (lock) {
            cloudController.startDCSService(dcsStartListener);

            try {
                lock.wait(TIME_OUT);

                if (cloudController.getState() != CloudController.ICPClientDCSState.STARTED) {
                    return new Response(null, Error.REQUESTFAILED, mResponseHandler);
                }
            } catch (InterruptedException e) {
                return new Response(null, Error.REQUESTFAILED, mResponseHandler);
            }
        }

        return new Response(null, null, mResponseHandler);
    }

    /**
     * Visible for testing
     *
     * @param TIME_OUT of the synchronization
     */
    void setTimeOut(long TIME_OUT) {
        this.TIME_OUT = TIME_OUT;
    }
}
