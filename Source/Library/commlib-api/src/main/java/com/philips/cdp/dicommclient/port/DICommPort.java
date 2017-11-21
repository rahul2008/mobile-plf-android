/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;
import com.philips.cdp2.commlib.core.util.GsonProvider;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * A DiComm Port on an {@link Appliance}.
 * <p>
 * Ports hold a set of {@link PortProperties} and can have methods to perform complex actions
 * on a port. It is possible to subscribe to changes on a port to get informed when the port's
 * properties have changed.
 *
 * @param <T> The {@link PortProperties} associated with this port.
 * @publicApi
 */
public abstract class DICommPort<T extends PortProperties> {

    private final String LOG_TAG = getClass().getSimpleName();

    public static final int SUBSCRIPTION_TTL = 300; // Seconds

    @VisibleForTesting
    static final long SUBSCRIPTION_TTL_MS = TimeUnit.SECONDS.toMillis(SUBSCRIPTION_TTL);

    protected final Gson gson = GsonProvider.get();

    private Handler resubscriptionHandler = HandlerProvider.createHandler();
    private boolean isRequestInProgress;

    private boolean mIsApplyingChanges;
    private boolean mGetPropertiesRequested;
    private boolean mSubscribeRequested;
    private boolean mUnsubscribeRequested;
    private boolean mStopResubscribe;
    private final Object mResubscribeLock = new Object();
    private T mPortProperties;
    private final Map<String, Object> mPutPropertiesMap = new ConcurrentHashMap<>();

    private final Set<DICommPortListener> mPortListeners = new CopyOnWriteArraySet<>();

    protected CommunicationStrategy communicationStrategy;

    private final Runnable resubscriptionRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mResubscribeLock) {
                if (!mStopResubscribe) {
                    subscribe();
                }
            }
        }
    };

    private final SubscriptionEventListener subscriptionEventListener = new SubscriptionEventListener() {
        @Override
        public void onSubscriptionEventReceived(String portName, String data) {
            if (getDICommPortName().equals(portName)) {
                DICommLog.d(LOG_TAG, "Handling subscription event: " + data);

                handleResponse(data);
            }
        }

        @Override
        public void onSubscriptionEventDecryptionFailed(String portName) {
            if (getDICommPortName().equals(portName)) {
                DICommLog.w(LOG_TAG, "Subscription event decryption failed, scheduling a reload instead.");

                reloadProperties();
            }
        }
    };

    public DICommPort(@NonNull final CommunicationStrategy communicationStrategy) {
        this.communicationStrategy = communicationStrategy;
    }

    protected abstract void processResponse(String jsonResponse);

    public abstract String getDICommPortName();

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

        this.communicationStrategy.addSubscriptionEventListener(subscriptionEventListener);

        mSubscribeRequested = true;
        mStopResubscribe = false;

        resubscriptionHandler.removeCallbacks(resubscriptionRunnable);
        resubscriptionHandler.postDelayed(resubscriptionRunnable, SUBSCRIPTION_TTL_MS);

        tryToPerformNextRequest();
    }

    public void unsubscribe() {
        DICommLog.d(LOG_TAG, "request unsubscribe");

        this.communicationStrategy.removeSubscriptionEventListener(subscriptionEventListener);

        mUnsubscribeRequested = true;
        stopResubscribe();
        tryToPerformNextRequest();
    }

    public void stopResubscribe() {
        DICommLog.d(LOG_TAG, "stop resubscribing");

        synchronized (mResubscribeLock) {
            mStopResubscribe = true;
        }
        resubscriptionHandler.removeCallbacks(resubscriptionRunnable);
    }

    public void addPortListener(DICommPortListener listener) {
        mPortListeners.add(listener);
    }

    public void removePortListener(DICommPortListener listener) {
        mPortListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private void notifyPortListenersOnUpdate() {
        for (DICommPortListener listener : mPortListeners) {
            listener.onPortUpdate(this);
        }
    }

    @SuppressWarnings("unchecked")
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

    boolean isApplyingChanges() {
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

    void handleResponse(String data) {
        mGetPropertiesRequested = false;
        processResponse(data);
        notifyPortListenersOnUpdate();
    }

    private void performPutProperties() {
        final Map<String, Object> propertiesToSend = Collections.unmodifiableMap(new HashMap<>(mPutPropertiesMap));
        mPutPropertiesMap.clear();

        DICommLog.i(LOG_TAG, "putProperties");
        setIsApplyingChanges(true);
        this.communicationStrategy.putProperties(propertiesToSend, getDICommPortName(), getDICommProductId(), new ResponseHandler() {

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
        this.communicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

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
        this.communicationStrategy.subscribe(getDICommPortName(), getDICommProductId(), SUBSCRIPTION_TTL, new ResponseHandler() {

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
        this.communicationStrategy.unsubscribe(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

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
