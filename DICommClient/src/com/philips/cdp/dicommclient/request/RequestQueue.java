package com.philips.cdp.dicommclient.request;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.philips.cdp.dicommclient.util.DLog;

public class RequestQueue {

	// TODO: DICOMM Refactor, add mechanism to start and stop thread.
    private HandlerThread mRequestThread;
    private Handler mRequestHandler;
    private final Handler mResponseHandler;

    private final ArrayList<Request> mThreadNotYetStartedQueue = new ArrayList<Request>();

    public RequestQueue() {
        initializeRequestThread();
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    public synchronized void addRequest(Request request) {
        if (mRequestHandler == null) {
        	DLog.d(DLog.REQUESTQUEUE, "Added new request - Thread not yet started");
            mThreadNotYetStartedQueue.add(request);
            return;
        }
        DLog.d(DLog.REQUESTQUEUE, "Added new request");
        postRequestOnBackgroundThread(request);
    }

    public synchronized void addRequestInFrontOfQueue(ExchangeKeyRequest request) {
        if (mRequestHandler == null) {
            DLog.d(DLog.REQUESTQUEUE, "Added new request in front of queue - Thread not yet started");
            mThreadNotYetStartedQueue.add(request);
            return;
        }
        DLog.d(DLog.REQUESTQUEUE, "Added new request in front of queue");
        postPriorityRequestOnBackgroundThread(request);
    }

    public synchronized void clearAllPendingRequests() {
    	DLog.d(DLog.REQUESTQUEUE, "Cleared all pending requests");
    	mRequestHandler.removeCallbacksAndMessages(null);
    	mThreadNotYetStartedQueue.clear();
    }

    private void postRequestOnBackgroundThread(final Request request) {
        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                DLog.d(DLog.REQUESTQUEUE, "Processing new request");
                Response response = request.execute();
                postResponseOnUIThread(response);
            };
        };
        mRequestHandler.post(requestRunnable);
    }

    private void postPriorityRequestOnBackgroundThread(final Request request) {
        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                DLog.d(DLog.REQUESTQUEUE, "Processing new request");
                Response response = request.execute();
                postResponseOnUIThread(response);
            };
        };
        mRequestHandler.postAtFrontOfQueue(requestRunnable);
    }

    private void postResponseOnUIThread(final Response response) {
        Runnable responseRunnable = new Runnable() {
            @Override
            public void run() {
            	DLog.d(DLog.REQUESTQUEUE, "Processing response from request");
            	response.notifyResponseHandler();
        }};
        mResponseHandler.post(responseRunnable);
    }

	private void initializeRequestThread() {
		mRequestThread = new HandlerThread(this.getClass().getSimpleName()) {
            @Override
            protected void onLooperPrepared() {
                initializeRequestHandler(getLooper());
                super.onLooperPrepared();
            }
        };
        mRequestThread.start();
	}

    private synchronized void initializeRequestHandler(Looper looper) {
    	DLog.d(DLog.REQUESTQUEUE, "Initializing requestHandler");
        mRequestHandler = new Handler(looper);
        for (Request request : mThreadNotYetStartedQueue) {
            postRequestOnBackgroundThread(request);
            DLog.d(DLog.REQUESTQUEUE, "Added new request - pending due to Thread not started");
        }
        mThreadNotYetStartedQueue.clear();
    }

}
