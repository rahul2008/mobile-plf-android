package com.philips.cl.di.dicomm.communication;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.philips.cl.di.dev.pa.purifier.ExchangeKeyRequest;
import com.philips.cl.di.dev.pa.util.ALog;

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
        	ALog.d(ALog.REQUESTQUEUE, "Added new request - Thread not yet started");
            mThreadNotYetStartedQueue.add(request);
            return;
        }
        ALog.d(ALog.REQUESTQUEUE, "Added new request");
        postRequestOnBackgroundThread(request);
    }

    public synchronized void addRequestInFrontOfQueue(ExchangeKeyRequest request) {
        if (mRequestHandler == null) {
            ALog.d(ALog.REQUESTQUEUE, "Added new request in front of queue - Thread not yet started");
            mThreadNotYetStartedQueue.add(request);
            return;
        }
        ALog.d(ALog.REQUESTQUEUE, "Added new request in front of queue");
        postPriorityRequestOnBackgroundThread(request);
    }

    public synchronized void clearAllPendingRequests() {
    	ALog.d(ALog.REQUESTQUEUE, "Cleared all pending requests");
    	mRequestHandler.removeCallbacksAndMessages(null);
    	mThreadNotYetStartedQueue.clear();
    }

    private void postRequestOnBackgroundThread(final Request request) {
        Runnable requestRunnable = new Runnable() {
            @Override
            public void run() {
                ALog.d(ALog.REQUESTQUEUE, "Processing new request");
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
                ALog.d(ALog.REQUESTQUEUE, "Processing new request");
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
            	ALog.d(ALog.REQUESTQUEUE, "Processing response from request");
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
    	ALog.d(ALog.REQUESTQUEUE, "Initializing requestHandler");
        mRequestHandler = new Handler(looper);
        for (Request request : mThreadNotYetStartedQueue) {
            postRequestOnBackgroundThread(request);
            ALog.d(ALog.REQUESTQUEUE, "Added new request - pending due to Thread not started");
        }
        mThreadNotYetStartedQueue.clear();
    }

}
