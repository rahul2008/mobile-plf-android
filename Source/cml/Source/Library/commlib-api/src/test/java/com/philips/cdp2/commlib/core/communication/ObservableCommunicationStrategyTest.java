/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.communication;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObservableCommunicationStrategyTest {

    private static final String TEST_DATA = "TestData";

    @Mock
    private SubscriptionEventListener mockListener;
    @Mock
    private SubscriptionEventListener mockListenerTwo;

    private ObservableCommunicationStrategy strategyUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        strategyUnderTest = new ObservableCommunicationStrategy() {
            @Override
            public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {

            }

            @Override
            public void putProperties(final Map<String, Object> dataMap, final String portName, final int productId, final ResponseHandler responseHandler) {

            }

            @Override
            public void addProperties(final Map<String, Object> dataMap, final String portName, final int productId, final ResponseHandler responseHandler) {

            }

            @Override
            public void deleteProperties(final String portName, final int productId, final ResponseHandler responseHandler) {

            }

            @Override
            public void subscribe(final String portName, final int productId, final int subscriptionTtl, final ResponseHandler responseHandler) {

            }

            @Override
            public void unsubscribe(final String portName, final int productId, final ResponseHandler responseHandler) {

            }

            @Override
            public boolean isAvailable() {
                return false;
            }

            @Override
            public void enableCommunication() {

            }

            @Override
            public void disableCommunication() {

            }
        };
    }

    @Test
    public void whenNotifyWithoutListenersThenNoExceptions() {
        strategyUnderTest.notifySubscriptionEventListeners("air", TEST_DATA);
    }

    @Test
    public void whenNotifyWithListenerThenListenerGetsCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners("air", TEST_DATA);

        verify(mockListener).onSubscriptionEventReceived("air", TEST_DATA);
    }

    @Test
    public void whenNotifyWithMultipleListenersThenAllListenersGetCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListenerTwo);

        strategyUnderTest.notifySubscriptionEventListeners("air", TEST_DATA);

        verify(mockListener).onSubscriptionEventReceived("air", TEST_DATA);
        verify(mockListenerTwo).onSubscriptionEventReceived("air", TEST_DATA);
    }

    @Test
    public void whenListenerIsRemovedThenItDoesntGetCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListenerTwo);
        strategyUnderTest.removeSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners("air", TEST_DATA);

        verifyZeroInteractions(mockListener);
    }

    @Test
    public void whenListenerIsAddedTwiceThenItGetsCalledOnce() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners("air", TEST_DATA);

        verify(mockListener,times(1)).onSubscriptionEventReceived("air", TEST_DATA);
    }

    // TODO Tests where no port is provided (null or "")
}
