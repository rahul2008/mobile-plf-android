/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.WrappedHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DICommPort<T> {

    private final String LOG_TAG = getClass().getSimpleName();

    public static final int SUBSCRIPTION_TTL = 300;
    public static final int SUBSCRIPTION_TTL_MS = SUBSCRIPTION_TTL * 1000;

    protected final NetworkNode mNetworkNode;
    protected CommunicationStrategy mCommunicationStrategy;
    private WrappedHandler mResubscriptionHandler;

    private boolean mHasOutstandingRequest;
    private boolean mIsApplyingChanges;
    private boolean mGetPropertiesRequested;
    private boolean mSubscribeRequested;
    private boolean mUnsubscribeRequested;
    private boolean mStopResubscribe;
    private Object mResubscribeLock = new Object();

    private Map<String, Object> mPutPropertiesMap;
    private T mPortProperties;

    private List<DICommPortListener> mPortListeners;

    public DICommPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        mNetworkNode = networkNode;
        mCommunicationStrategy = communicationStrategy;
        mPutPropertiesMap = new HashMap<String, Object>();
        mPortListeners = new ArrayList<DICommPortListener>();
    }

    public abstract boolean isResponseForThisPort(String jsonResponse);

    protected abstract void processResponse(String jsonResponse);

    protected abstract String getDICommPortName();

    protected abstract int getDICommProductId();

    public abstract boolean supportsSubscription();

    /**
     * Get the properties for this port, possibly triggering a {@link DICommPort#reloadProperties()} when they are not yet available.
     *
     * @return The locally available properties, or null if not available yet.
     */
    public T getPortProperties() {
        if (mPortProperties == null) {
            reloadProperties();
        }
        return mPortProperties;
    }

    protected void setPortProperties(T portProperties) {
        mGetPropertiesRequested = false;
        mPortProperties = portProperties;
    }

    public void putProperties(String key, String value) {
        DICommLog.d(LOG_TAG, "request putProperties - " + key + " : " + value);
        synchronized (mPutPropertiesMap) {
            mPutPropertiesMap.put(key, value);
        }
        tryToPerformNextRequest();
    }

    public void putProperties(Map<String, Object> dataMap) {
        DICommLog.d(LOG_TAG, "request putProperties - multiple key values");
        synchronized (mPutPropertiesMap) {
            mPutPropertiesMap.putAll(dataMap);
        }
        tryToPerformNextRequest();
    }

    /**
     * Synchronize local port properties with the (remote) appliance.
     */
    public void reloadProperties() {
        DICommLog.d(LOG_TAG, "request reloadProperties");
        mGetPropertiesRequested = true;
        tryToPerformNextRequest();
    }

    public void subscribe() {
        if (mSubscribeRequested) return;
        DICommLog.d(LOG_TAG, "request subscribe");

        mSubscribeRequested = true;
        mStopResubscribe = false;

        getResubscriptionHandler().removeCallbacks(mResubscribtionRunnable);
        getResubscriptionHandler().postDelayed(mResubscribtionRunnable, SUBSCRIPTION_TTL_MS);

        tryToPerformNextRequest();
    }

    protected WrappedHandler getResubscriptionHandler() {
        if (mResubscriptionHandler == null) {
            mResubscriptionHandler = new WrappedHandler(new Handler(Looper.getMainLooper()));
        }
        return mResubscriptionHandler;
    }

    private final Runnable mResubscribtionRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mResubscribeLock) {
                if (!mStopResubscribe) {
                    subscribe();
                }
            }
        }
    };

    public void unsubscribe() {
        DICommLog.d(LOG_TAG, "request unsubscribe");
        mUnsubscribeRequested = true;
        stopResubscribe();
        tryToPerformNextRequest();
    }

    public void stopResubscribe() {
        DICommLog.d(LOG_TAG, "stop resubscribing");
        synchronized (mResubscribeLock) {
            mStopResubscribe = true;
        }
        getResubscriptionHandler().removeCallbacks(mResubscribtionRunnable);
    }

    public void addPortListener(DICommPortListener listener) {
        if (!mPortListeners.contains(listener)) {
            mPortListeners.add(listener);
        }
    }

    public void removePortListener(DICommPortListener listener) {
        mPortListeners.remove(listener);
    }

    private void notifyPortListenersOnUpdate() {
        ArrayList<DICommPortListener> copyListeners = new ArrayList<DICommPortListener>(mPortListeners);
        for (DICommPortListener listener : copyListeners) {
            listener.onPortUpdate(this);
        }
    }

    private void notifyPortListenersOnError(Error error, String errorData) {
        ArrayList<DICommPortListener> copyListeners = new ArrayList<DICommPortListener>(mPortListeners);
        for (DICommPortListener listener : copyListeners) {
            listener.onPortError(this, error, errorData);
        }
    }

    private void tryToPerformNextRequest() {
        if (mHasOutstandingRequest) {
            DICommLog.d(LOG_TAG, "Trying to perform next request - Request outstanding");
            return;
        }
        DICommLog.d(LOG_TAG, "Trying to perform next request - Performing next request");
        mHasOutstandingRequest = true;

        if (isPutPropertiesRequested()) {
            performPutProperties();
        } else if (isSubscribeRequested()) {
            performSubscribe();
        } else if (isUnsubcribeRequested()) {
            performUnsubscribe();
        } else if (isGetPropertiesRequested()) {
            performGetProperties();
        } else {
            mHasOutstandingRequest = false;
        }
    }

    private void setIsApplyingChanges(boolean isApplyingChanges) {
        DICommLog.d(LOG_TAG, isApplyingChanges ? "Started applying changes" : "Stopped applying changes");
        this.mIsApplyingChanges = isApplyingChanges;
    }

    public boolean isApplyingChanges() {
        return mIsApplyingChanges;
    }

    private boolean isPutPropertiesRequested() {
        synchronized (mPutPropertiesMap) {
            return !mPutPropertiesMap.isEmpty();
        }
    }

    private boolean isGetPropertiesRequested() {
        return mGetPropertiesRequested;
    }

    private boolean isSubscribeRequested() {
        return mSubscribeRequested;
    }

    private boolean isUnsubcribeRequested() {
        return mUnsubscribeRequested;
    }

    private void requestCompleted() {
        mHasOutstandingRequest = false;
        tryToPerformNextRequest();
    }

    public void handleResponse(String data) {
        mGetPropertiesRequested = false;
        processResponse(data);
        notifyPortListenersOnUpdate();
    }

    private void performPutProperties() {
        final HashMap<String, Object> propertiesToSend;

        synchronized (mPutPropertiesMap) {
            propertiesToSend = new HashMap<String, Object>(mPutPropertiesMap);
            mPutPropertiesMap.clear();
        }

        DICommLog.i(LOG_TAG, "Start putProperties");
        setIsApplyingChanges(true);
        mCommunicationStrategy.putProperties(propertiesToSend, getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                if (!isPutPropertiesRequested()) {
                    setIsApplyingChanges(false);
                }
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "End putProperties - success");
            }

            public void onError(Error error, String errorData) {
                if (!isPutPropertiesRequested()) {
                    setIsApplyingChanges(false);
                }
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "End putProperties - error");
            }
        });
    }

    private void performGetProperties() {
        DICommLog.i(LOG_TAG, "Start reloadProperties");
        mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "End reloadProperties - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mGetPropertiesRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "End putProperties - error");
            }
        });
    }

    private void performSubscribe() {
        DICommLog.i(LOG_TAG, "Start subscribe");
        mCommunicationStrategy.subscribe(getDICommPortName(), getDICommProductId(), SUBSCRIPTION_TTL, new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                mSubscribeRequested = false;
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "End subscribe - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mSubscribeRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "End subscribe - error");
            }
        });
    }

    private void performUnsubscribe() {
        DICommLog.i(LOG_TAG, "Start unsubscribe");
        mCommunicationStrategy.unsubscribe(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                mUnsubscribeRequested = false;
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "End unsubscribe - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mUnsubscribeRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "End unsubscribe - success");
            }
        });
    }
}
