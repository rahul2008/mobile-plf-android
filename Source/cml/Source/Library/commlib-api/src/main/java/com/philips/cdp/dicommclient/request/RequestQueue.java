/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.ArrayList;

public class RequestQueue {

    private Handler mRequestHandler;

    private final Handler responseHandler;
    private final ArrayList<Request> threadNotYetStartedQueue = new ArrayList<>();

    public RequestQueue() {
        initializeRequestThread();
        responseHandler = HandlerProvider.createHandler();
    }

    public synchronized void addRequest(Request request) {
        if (mRequestHandler == null) {
        	DICommLog.d(DICommLog.REQUESTQUEUE, "Added new request - Thread not yet started");
            threadNotYetStartedQueue.add(request);
            return;
        }
        DICommLog.d(DICommLog.REQUESTQUEUE, "Added new request");
        postRequestOnBackgroundThread(request);
    }

    public synchronized void addRequestInFrontOfQueue(Request request) {
        if (mRequestHandler == null) {
            DICommLog.d(DICommLog.REQUESTQUEUE, "Added new request in front of queue - Thread not yet started");
            threadNotYetStartedQueue.add(request);
            return;
        }
        DICommLog.d(DICommLog.REQUESTQUEUE, "Added new request in front of queue");
        postPriorityRequestOnBackgroundThread(request);
    }

    public synchronized void clearAllPendingRequests() {
    	DICommLog.d(DICommLog.REQUESTQUEUE, "Cleared all pending requests");
    	mRequestHandler.removeCallbacksAndMessages(null);
    	threadNotYetStartedQueue.clear();
    }

    private void postRequestOnBackgroundThread(final Request request) {
        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                DICommLog.d(DICommLog.REQUESTQUEUE, "Processing new request");
                Response response = request.execute();
                postResponseOnUIThread(response);
            }
        };
        mRequestHandler.post(requestRunnable);
    }

    private void postPriorityRequestOnBackgroundThread(final Request request) {
        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                DICommLog.d(DICommLog.REQUESTQUEUE, "Processing new request");
                Response response = request.execute();
                postResponseOnUIThread(response);
            }
        };
        mRequestHandler.postAtFrontOfQueue(requestRunnable);
    }

    private void postResponseOnUIThread(final Response response) {
        Runnable responseRunnable = new Runnable() {
            @Override
            public void run() {
            	DICommLog.d(DICommLog.REQUESTQUEUE, "Processing response from request");
            	response.notifyResponseHandler();
        }};
        responseHandler.post(responseRunnable);
    }

	private void initializeRequestThread() {
        HandlerThread mRequestThread = new HandlerThread(this.getClass().getSimpleName()) {
            @Override
            protected void onLooperPrepared() {
                initializeRequestHandler(getLooper());
                super.onLooperPrepared();
            }
        };
        mRequestThread.start();
	}

    private synchronized void initializeRequestHandler(Looper looper) {
    	DICommLog.d(DICommLog.REQUESTQUEUE, "Initializing requestHandler");
        mRequestHandler = HandlerProvider.createHandler(looper);
        for (Request request : threadNotYetStartedQueue) {
            postRequestOnBackgroundThread(request);
            DICommLog.d(DICommLog.REQUESTQUEUE, "Added new request - pending due to Thread not started");
        }
        threadNotYetStartedQueue.clear();
    }

}
