package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.cpp.CppController;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
public class StartDcsRequest extends Request {

    private final CppController cppController;
    private final Object lock;

    public StartDcsRequest(final CppController cppController, final ResponseHandler responseHandler) {
        super(null, responseHandler);
        this.cppController = cppController;

        lock = new Object();
    }

    @Override
    public Response execute() {
        synchronized (lock) {
            //pass the lock to cppcontroller in a safe way
            cppController.startDCSService(lock);
            try {
                lock.wait();
                //todo: add timeout and after waiting check if state of cppcontroller moved to 'started'
                //if not: respond with error
            } catch (InterruptedException e) {
                return new Response(null, Error.REQUESTFAILED, mResponseHandler);
            }
        }

        return new Response("", null, mResponseHandler);
    }
}
