/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.security.DISecurity.EncryptionDecryptionFailedListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.ObservableCommunicationStrategy;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.core.util.ObservableCollection.ModificationListener;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.security.SslPinTrustManager;
import com.philips.cdp2.commlib.lan.subscription.LocalSubscriptionHandler;
import com.philips.cdp2.commlib.lan.subscription.UdpEventReceiver;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import static java.util.Objects.requireNonNull;


/**
 * The type LanCommunicationStrategy.
 *
 * @publicApi
 */
public class LanCommunicationStrategy extends ObservableCommunicationStrategy {
    @NonNull
    private final RequestQueue requestQueue;

    @NonNull
    private final DISecurity diSecurity;

    @NonNull
    private final NetworkNode networkNode;

    @NonNull
    private final LocalSubscriptionHandler localSubscriptionHandler;

    @Nullable
    private SSLContext sslContext;

    private boolean isKeyExchangeOngoing;
    private boolean isAvailable;
    private boolean isConnected;
    private boolean isCached;

    private final EncryptionDecryptionFailedListener encryptionDecryptionFailedListener = new EncryptionDecryptionFailedListener() {

        @Override
        public void onDecryptionFailed(NetworkNode networkNode) {
            triggerKeyExchange(networkNode);
        }

        @Override
        public void onEncryptionFailed(NetworkNode networkNode) {
            triggerKeyExchange(networkNode);
        }
    };

    private final AvailabilityListener<ConnectivityMonitor> availabilityListener = new AvailabilityListener<ConnectivityMonitor>() {

        @Override
        public void onAvailabilityChanged(@NonNull ConnectivityMonitor connectivityMonitor) {
            isConnected = connectivityMonitor.isAvailable();
            handleAvailabilityChanged();
        }
    };

    private final ModificationListener<String> deviceCacheListener = new ModificationListener<String>() {
        @Override
        public void onRemoved(String cppId) {
            isCached = false;
            handleAvailabilityChanged();
        }

        @Override
        public void onAdded(String cppId) {
            isCached = true;
            handleAvailabilityChanged();
        }
    };

    public LanCommunicationStrategy(final @NonNull NetworkNode networkNode, final @NonNull LanDeviceCache deviceCache, final @NonNull ConnectivityMonitor connectivityMonitor) {
        this.networkNode = requireNonNull(networkNode);
        requireNonNull(deviceCache);
        requireNonNull(connectivityMonitor);

        this.diSecurity = new DISecurity(networkNode);
        localSubscriptionHandler = new LocalSubscriptionHandler(diSecurity, UdpEventReceiver.getInstance());

        this.isCached = deviceCache.contains(networkNode.getCppId());

        if (networkNode.isHttps()) {
            try {
                sslContext = createSSLContext();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new IllegalStateException("Error initializing SSL context.", e);
            }
        }

        requestQueue = createRequestQueue();

        deviceCache.addModificationListener(networkNode.getCppId(), deviceCacheListener);
        connectivityMonitor.addAvailabilityListener(availabilityListener);
        this.diSecurity.setEncryptionDecryptionFailedListener(encryptionDecryptionFailedListener);
    }

    @VisibleForTesting
    @NonNull
    RequestQueue createRequestQueue() {
        return new RequestQueue();
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.GET, null, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.PUT, dataMap, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.POST, dataMap, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.DELETE, null, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        localSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListeners);
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, sslContext, portName, productId, LanRequestType.DELETE, getUnsubscriptionData(), responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @VisibleForTesting
    @Nullable
    SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{
                new SslPinTrustManager(networkNode)
        }, new SecureRandom());

        return context;
    }

    private void triggerKeyExchange(NetworkNode networkNode) {
        networkNode.setEncryptionKey(null);
        exchangeKeyIfNecessary(networkNode);
    }

    private void exchangeKeyIfNecessary(NetworkNode networkNode) {
        if (networkNode.getEncryptionKey() == null && !isKeyExchangeOngoing) {
            doKeyExchange(networkNode);
        }
    }

    private void doKeyExchange(final @NonNull NetworkNode networkNode) {
        final ResponseHandler responseHandler = new ResponseHandler() {

            @Override
            public void onSuccess(String key) {
                networkNode.setEncryptionKey(key);
                isKeyExchangeOngoing = false;
            }

            @Override
            public void onError(Error error, String errorData) {
                isKeyExchangeOngoing = false;
            }
        };

        final Request request = networkNode.isHttps() ?
                new GetKeyRequest(networkNode, sslContext, responseHandler) :
                new ExchangeKeyRequest(networkNode, responseHandler);

        isKeyExchangeOngoing = true;
        requestQueue.addRequestInFrontOfQueue(request);
    }

    /**
     * This method does nothing.
     */
    @Override
    public void enableCommunication() {

    }

    /**
     * This method does nothing.
     */
    @Override
    public void disableCommunication() {

    }

    private synchronized void handleAvailabilityChanged() {
        boolean currentAvailability = isAvailable;

        isAvailable = isCached && isConnected;
        if (isAvailable != currentAvailability) {
            notifyAvailabilityChanged();
        }

        DICommLog.i("LanCommunicationStrategy", "isAvailable " + isAvailable + " for " + networkNode.getName());

        if (isAvailable) {
            localSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListeners);
        } else {
            localSubscriptionHandler.disableSubscription();
        }
    }
}
