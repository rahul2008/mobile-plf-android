/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.cdp2.commlib.lan.LanDeviceCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanCommunicationStrategyTest {

    private static final String PORT_NAME = "test";
    private static final int PRODUCT_ID = 1;
    private static final int SUBSCRIPTION_TTL = 10;

    @Mock
    private Handler handlerMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private LanDeviceCache lanDeviceCacheMock;

    @Mock
    private RequestQueue requestQueueMock;

    @Mock
    private ConnectivityMonitor connectivityMonitor;

    private LanCommunicationStrategy lanCommunicationStrategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        lanCommunicationStrategy = new LanCommunicationStrategy(networkNodeMock, lanDeviceCacheMock, connectivityMonitor) {
            @Override
            @NonNull
            RequestQueue createRequestQueue() {
                return requestQueueMock;
            }
        };
    }

    @Test
    public void whenSubscribingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenUnsubscribingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenUnsubscribingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenUnsubscribingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenUnsubscribingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenPuttingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.putProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenPuttingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.putProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenPuttingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.putProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenPuttingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.putProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenGettingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenGettingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenGettingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenGettingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenAddingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.addProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenAddingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.addProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenAddingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.addProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenAddingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.addProperties(null, PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenDeletingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithoutKeyPresent();

        lanCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenDeletingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        setupForHttpWithKeyPresent();

        lanCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenDeletingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithoutKeyPresent();

        lanCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenDeletingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        setupForHttpsWithKeyPresent();

        lanCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenCreatingSSLContext_thenNoExceptionShouldBeThrown() {
        try {
            lanCommunicationStrategy.createSSLContext();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            fail();
        }
    }

    private void setupForHttpsWithKeyPresent() {
        when(networkNodeMock.isHttps()).thenReturn(true);
        when(networkNodeMock.getEncryptionKey()).thenReturn("tha_key");
    }

    private void setupForHttpsWithoutKeyPresent() {
        when(networkNodeMock.isHttps()).thenReturn(true);
        when(networkNodeMock.getEncryptionKey()).thenReturn(null);
    }

    private void setupForHttpWithKeyPresent() {
        when(networkNodeMock.isHttps()).thenReturn(false);
        when(networkNodeMock.getEncryptionKey()).thenReturn("tha_key");
    }

    private void setupForHttpWithoutKeyPresent() {
        when(networkNodeMock.isHttps()).thenReturn(false);
        when(networkNodeMock.getEncryptionKey()).thenReturn(null);
    }
}
