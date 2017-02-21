/*
 * (C) Koninklijke Philips N.V., 2017.
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

public class CommunicationStrategyTest {

    private static final String TEST_DATA = "TestData";

    @Mock
    private SubscriptionEventListener mockListener;
    @Mock
    private SubscriptionEventListener mockListenerTwo;

    private CommunicationStrategy strategyUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        strategyUnderTest = new CommunicationStrategy() {
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
            public void enableCommunication(final SubscriptionEventListener subscriptionEventListener) {

            }

            @Override
            public void disableCommunication() {

            }
        };
    }

    @Test
    public void whenNotifyWithoutListenersThenNoExceptions() {
        strategyUnderTest.notifySubscriptionEventListeners(TEST_DATA);
    }

    @Test
    public void whenNotifyWithListenerThenListenerGetsCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners(TEST_DATA);

        verify(mockListener).onSubscriptionEventReceived(TEST_DATA);
    }

    @Test
    public void whenNotifyWithMultipleListenersThenAllListenersGetCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListenerTwo);

        strategyUnderTest.notifySubscriptionEventListeners(TEST_DATA);

        verify(mockListener).onSubscriptionEventReceived(TEST_DATA);
        verify(mockListenerTwo).onSubscriptionEventReceived(TEST_DATA);
    }

    @Test
    public void whenListenerIsRemovedThenItDoesntGetCalled() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListenerTwo);
        strategyUnderTest.removeSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners(TEST_DATA);

        verifyZeroInteractions(mockListener);
    }

    @Test
    public void whenListenerIsAddedTwiceThenItGetsCalledOnce() throws Exception {
        strategyUnderTest.addSubscriptionEventListener(mockListener);
        strategyUnderTest.addSubscriptionEventListener(mockListener);

        strategyUnderTest.notifySubscriptionEventListeners(TEST_DATA);

        verify(mockListener,times(1)).onSubscriptionEventReceived(TEST_DATA);
    }
}
