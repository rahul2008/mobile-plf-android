/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
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
import com.philips.cdp2.commlib.lan.security.SslPinTrustManager;
import com.philips.cdp2.commlib.lan.subscription.LocalSubscriptionHandler;
import com.philips.cdp2.commlib.lan.subscription.UdpEventReceiver;
import com.philips.cdp2.commlib.lan.util.SsidProvider;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;

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
    private SsidProvider ssidProvider;

    @NonNull
    private final NetworkNode networkNode;

    @NonNull
    private final ConnectivityMonitor connectivityMonitor;

    @NonNull
    private final LocalSubscriptionHandler localSubscriptionHandler;

    @Nullable
    private SSLContext sslContext;

    private boolean isKeyExchangeOngoing;
    private boolean isAvailable;

    @NonNull
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

    @NonNull
    private final AvailabilityListener<ConnectivityMonitor> availabilityListener = new AvailabilityListener<ConnectivityMonitor>() {

        @Override
        public void onAvailabilityChanged(@NonNull ConnectivityMonitor connectivityMonitor) {
            handleAvailabilityChanged();
        }
    };

    private final SsidProvider.NetworkChangeListener networkChangeListener = new SsidProvider.NetworkChangeListener() {
        @Override
        public void onNetworkChanged() {
            handleAvailabilityChanged();
        }
    };

    @NonNull
    private final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            handleAvailabilityChanged();
        }
    };

    public LanCommunicationStrategy(final @NonNull NetworkNode networkNode, final @NonNull ConnectivityMonitor connectivityMonitor, final @NonNull SsidProvider ssidProvider) {
        this.networkNode = requireNonNull(networkNode);
        this.connectivityMonitor = requireNonNull(connectivityMonitor);
        this.ssidProvider = requireNonNull(ssidProvider);

        this.diSecurity = new DISecurity(networkNode);
        this.localSubscriptionHandler = new LocalSubscriptionHandler(diSecurity, UdpEventReceiver.getInstance());

        if (networkNode.isHttps()) {
            try {
                sslContext = createSSLContext();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new IllegalStateException("Error initializing SSL context.", e);
            }
        }

        this.connectivityMonitor.addAvailabilityListener(availabilityListener);
        this.networkNode.addPropertyChangeListener(propertyChangeListener);
        this.ssidProvider.addNetworkChangeListener(networkChangeListener);
        this.requestQueue = createRequestQueue();

        this.diSecurity.setEncryptionDecryptionFailedListener(encryptionDecryptionFailedListener);

        isAvailable = isAvailable();
    }

    @VisibleForTesting
    @NonNull
    RequestQueue createRequestQueue() {
        return new RequestQueue();
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.GET, null, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.PUT, dataMap, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.POST, dataMap, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.DELETE, null, responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        localSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListeners);
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode, connectivityMonitor, sslContext, portName, productId, LanRequestType.DELETE, getUnsubscriptionData(), responseHandler, diSecurity);
        requestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return networkNode.getIpAddress() != null && connectivityMonitor.isAvailable() && isOnSameNetwork();
    }

    private boolean isOnSameNetwork() {
        return ssidProvider.getCurrentSsid() == null || ssidProvider.getCurrentSsid().equals(networkNode.getNetworkSsid());
    }

    @VisibleForTesting
    @Nullable
    SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new SslPinTrustManager(networkNode)}, new SecureRandom());

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

        final Request request = networkNode.isHttps() ? new GetKeyRequest(networkNode, connectivityMonitor, sslContext, responseHandler) : new ExchangeKeyRequest(networkNode, connectivityMonitor, responseHandler);

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
        DICommLog.d("LanCommunicationStrategy", String.format(Locale.US, "NetworkNode: [%s] : isAvailable: [%s]", networkNode.getName(), isAvailable()));

        boolean newIsAvailable = isAvailable();
        if (newIsAvailable != isAvailable) {
            isAvailable = newIsAvailable;
            notifyAvailabilityChanged();
        }

        if (isAvailable()) {
            localSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListeners);
        } else {
            localSubscriptionHandler.disableSubscription();
        }
    }
}
