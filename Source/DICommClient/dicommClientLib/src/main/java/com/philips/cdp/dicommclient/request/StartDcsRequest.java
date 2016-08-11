/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.cpp.CppController;

public class StartDcsRequest extends Request {

    private long TIME_OUT = 10 * 1000L;

    private final CppController cppController;
    private final Object lock;

    public StartDcsRequest(final CppController cppController, final ResponseHandler responseHandler) {
        super(null, responseHandler);
        this.cppController = cppController;

        lock = new Object();

    }

    private final CppController.DCSStartListener dcsStartListener = new CppController.DCSStartListener() {
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
            cppController.startDCSService(dcsStartListener);

            try {
                lock.wait(TIME_OUT);

                if (cppController.getState() != CppController.ICP_CLIENT_DCS_STATE.STARTED) {
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
