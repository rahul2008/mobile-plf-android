/*
 * (C) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class DICommPort<T extends PortProperties> {

    private final String LOG_TAG = getClass().getSimpleName();

    public static final int SUBSCRIPTION_TTL = 300;
    public static final int SUBSCRIPTION_TTL_MS = SUBSCRIPTION_TTL * 1000;

    protected final Gson gson = GsonProvider.get();

    protected CommunicationStrategy mCommunicationStrategy;
    private WrappedHandler mResubscriptionHandler;

    private boolean isRequestInProgress;
    private boolean mIsApplyingChanges;
    private boolean mGetPropertiesRequested;
    private boolean mSubscribeRequested;
    private boolean mUnsubscribeRequested;
    private boolean mStopResubscribe;
    private final Object mResubscribeLock = new Object();
    private T mPortProperties;

    private final Map<String, Object> mPutPropertiesMap = new ConcurrentHashMap<>();
    private final Set<DICommPortListener> mPortListeners = new CopyOnWriteArraySet<DICommPortListener>();

    public DICommPort(CommunicationStrategy communicationStrategy) {
        mCommunicationStrategy = communicationStrategy;
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
        mPutPropertiesMap.put(key, value);
        tryToPerformNextRequest();
    }

    public void putProperties(Map<String, Object> dataMap) {
        DICommLog.d(LOG_TAG, "request putProperties - multiple key values");
        mPutPropertiesMap.putAll(dataMap);
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
        mPortListeners.add(listener);
    }

    public void removePortListener(DICommPortListener listener) {
        mPortListeners.remove(listener);
    }

    private void notifyPortListenersOnUpdate() {
        for (DICommPortListener listener : mPortListeners) {
            listener.onPortUpdate(this);
        }
    }

    private void notifyPortListenersOnError(Error error, String errorData) {
        for (DICommPortListener listener : mPortListeners) {
            listener.onPortError(this, error, errorData);
        }
    }

    private void tryToPerformNextRequest() {
        if (isRequestInProgress) {
            DICommLog.d(LOG_TAG, "Trying to perform next request - Another request already in progress");
            return;
        }
        DICommLog.d(LOG_TAG, "Trying to perform next request - Performing next request");
        isRequestInProgress = true;

        if (isPutPropertiesRequested()) {
            performPutProperties();
        } else if (isSubscribeRequested()) {
            performSubscribe();
        } else if (isUnsubcribeRequested()) {
            performUnsubscribe();
        } else if (isGetPropertiesRequested()) {
            performGetProperties();
        } else {
            isRequestInProgress = false;
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
        return !mPutPropertiesMap.isEmpty();
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
        isRequestInProgress = false;
        tryToPerformNextRequest();
    }

    public void handleResponse(String data) {
        mGetPropertiesRequested = false;
        processResponse(data);
        notifyPortListenersOnUpdate();
    }

    private void performPutProperties() {
        final Map<String, Object> propertiesToSend = Collections.unmodifiableMap(new HashMap<>(mPutPropertiesMap));
        mPutPropertiesMap.clear();

        DICommLog.i(LOG_TAG, "putProperties");
        setIsApplyingChanges(true);
        mCommunicationStrategy.putProperties(propertiesToSend, getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                if (!isPutPropertiesRequested()) {
                    setIsApplyingChanges(false);
                }
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "putProperties - success");
            }

            public void onError(Error error, String errorData) {
                if (!isPutPropertiesRequested()) {
                    setIsApplyingChanges(false);
                }
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "putProperties - error");
            }
        });
    }

    private void performGetProperties() {
        DICommLog.i(LOG_TAG, "getProperties");
        mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "getProperties - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mGetPropertiesRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "getProperties - error");
            }
        });
    }

    private void performSubscribe() {
        DICommLog.i(LOG_TAG, "perform subscribe");
        mCommunicationStrategy.subscribe(getDICommPortName(), getDICommProductId(), SUBSCRIPTION_TTL, new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                mSubscribeRequested = false;
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "subscribe - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mSubscribeRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "subscribe - error");
            }
        });
    }

    private void performUnsubscribe() {
        DICommLog.i(LOG_TAG, "perform unsubscribe");
        mCommunicationStrategy.unsubscribe(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                mUnsubscribeRequested = false;
                handleResponse(data);
                requestCompleted();
                DICommLog.i(LOG_TAG, "unsubscribe - success");
            }

            @Override
            public void onError(Error error, String errorData) {
                mUnsubscribeRequested = false;
                notifyPortListenersOnError(error, errorData);
                requestCompleted();
                DICommLog.e(LOG_TAG, "unsubscribe - error");
            }
        });
    }
}
