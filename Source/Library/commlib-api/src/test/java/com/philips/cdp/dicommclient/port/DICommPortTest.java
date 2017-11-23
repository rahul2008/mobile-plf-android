/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;

import static com.philips.cdp.dicommclient.port.DICommPort.SUBSCRIPTION_TTL_MS;
import static com.philips.cdp.dicommclient.request.Error.NOT_CONNECTED;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DICommPortTest {

    private static final int PORT_PRODUCTID = 1;
    private static final String PORT_NAME = "air";
    private static final String FANSPEED_KEY = "fs";
    private static final String FANSPEED_VALUE = "turbo";
    private static final String POWER_KEY = "pwr";
    private static final String POWER_VALUE = "1";
    private static final String CHILDLOCK_KEY = "childlock";
    private static final String CHILDLOCK_VALUE = "0";

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private DICommPortListener portListenerMock;

    @Mock
    private Handler handlerMock;

    @Captor
    private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResponseHandler> responseHandlerCaptor;

    private DICommPort diCommPort;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        diCommPort = new TestPort(communicationStrategyMock);
    }

    @Test
    public void testPutProperties() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        verifyPutPropertiesCalled(true);
        Map<String, Object> capturedMap = mapArgumentCaptor.getValue();

        assertTrue(capturedMap.containsKey(FANSPEED_KEY));
        assertEquals(FANSPEED_VALUE, capturedMap.get(FANSPEED_KEY));
    }

    @Test
    public void testGetProperties() {
        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testSubscribe() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);
    }

    @Test
    public void testUnsubscribe() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testPerformSubscribeAfterPutPropertiesSuccess() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.subscribe();
        verifySubscribeCalled(false);

        responseHandlerCaptor.getValue().onSuccess(null);
        verifySubscribeCalled(true);
    }

    @Test
    public void testPerformUnsubscribeAfterPutPropertiesSuccess() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        responseHandlerCaptor.getValue().onSuccess(null);
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testDoNotPerformGetAfterPutPropertiesSuccess() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformPutAfterPutPropertiesSuccess() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testPerformSubscribeAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.subscribe();
        verifySubscribeCalled(false);

        responseHandlerCaptor.getValue().onError(null, null);
        verifySubscribeCalled(true);
    }

    @Test
    public void testPerformUnsubscribeAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testPerformGetAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testPerformPutAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onError(null, null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testDoNotRetryPutAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        reset(communicationStrategyMock);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyPutPropertiesCalled(false);
    }

    @Test
    public void testDoNotPerformSuscribeAfterSubscribeSuccess() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.subscribe();
        verifySubscribeCalled(false);

        responseHandler.onSuccess(null);
        verifySubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterSubscribeSuccess() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformGetAfterSubscribeError() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testDoNotRetrySuscribeAfterSubscribeError() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);
        reset(communicationStrategyMock);

        responseHandlerCaptor.getValue().onError(null, null);
        verifySubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformUnsubscribeAfterUnsubscribeSuccess() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        responseHandler.onSuccess(null);
        verifyUnsubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterUnsubscribeSuccess() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testDonotRetryUnSubscribeAfterUnsubscribeError() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
        reset(communicationStrategyMock);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyUnsubscribeCalled(false);
    }

    @Test
    public void testPerformGetAfterUnsubscribeError() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testDoNotPerformGetAfterGetSuccess() {
        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandler.onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterGetError() {
        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandler.onError(null, null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformRequestsOnPriority() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.reloadProperties();
        diCommPort.unsubscribe();
        diCommPort.subscribe();
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        responseHandler.onSuccess(null);
        verifySubscribeCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        responseHandler.onSuccess(null);
        verifyUnsubscribeCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        responseHandler.onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testIsApplyingChangesAfterPutProperties() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);

        responseHandler.onSuccess(null);

        assertFalse(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);

        responseHandler.onError(NOT_CONNECTED, null);

        assertFalse(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesBetweenTwoPutProperties() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onSuccess(null);

        assertTrue(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesBetweenTwoPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onError(NOT_CONNECTED, null);

        assertTrue(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void test_ShouldPostRunnable_WhenSubscribeIsCalled() {
        diCommPort.subscribe();

        verify(handlerMock).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotPostRunnableTwice_WhenSubscribeIsCalledTwice() {
        diCommPort.subscribe();
        diCommPort.subscribe();

        verify(handlerMock).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldPostRunnableAgain_WhenSubscribeIsCalled_AfterSubscribeResponseIsReceived() {
        diCommPort.subscribe();
        verify(handlerMock, times(1)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        diCommPort.subscribe();

        verify(handlerMock, times(2)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldSubscribeToCommunicationStrategy_WhenSubscribeIsCalled() {
        diCommPort.subscribe();

        verifySubscribeCalled(true);
    }

    @Test
    public void test_ShouldUnsubscribeFromCommunicationStrategy_WhenUnsubscribeIsCalled() {
        diCommPort.unsubscribe();

        verifyUnsubscribeCalled(true);
    }

    @Test
    public void test_ShouldRemoveAndAddRunnable_WhenSubscribeIsCalled() {
        diCommPort.subscribe();

        Runnable runnable = captureResubscribeHandler();
        verify(handlerMock).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenStopResubscribeIsCalled() {
        diCommPort.subscribe();
        diCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(handlerMock, times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenUnsubscribeIsCalled() {
        diCommPort.subscribe();
        diCommPort.unsubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(handlerMock, times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterSubscribeResponseIsReceived() {
        diCommPort.subscribe();
        verify(handlerMock, times(1)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(handlerMock, times(2)).postDelayed(eq(runnable), eq(SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeIsCalled() {
        diCommPort.subscribe();
        verify(handlerMock, times(1)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        diCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(handlerMock, times(1)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldRePostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeAndSubscribeIsCalled() {
        diCommPort.stopResubscribe();

        diCommPort.subscribe();
        verify(handlerMock, times(1)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(handlerMock, times(2)).postDelayed(any(Runnable.class), eq(SUBSCRIPTION_TTL_MS));
    }

    private Runnable captureResubscribeHandler() {
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(handlerMock).postDelayed(runnableCaptor.capture(), Mockito.anyInt());

        return runnableCaptor.getValue();
    }

    @Test
    public void testMultiplePutRequests() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategyMock);

        diCommPort.putProperties(POWER_KEY, POWER_VALUE);
        diCommPort.putProperties(CHILDLOCK_KEY, CHILDLOCK_VALUE);

        responseHandler.onSuccess(null);

        verifyPutPropertiesCalled(true);

        Map<String, Object> capturedMap = mapArgumentCaptor.getValue();
        assertTrue(capturedMap.containsKey(POWER_KEY));
        assertTrue(capturedMap.containsKey(CHILDLOCK_KEY));
        assertEquals(POWER_VALUE, capturedMap.get(POWER_KEY));
        assertEquals(CHILDLOCK_VALUE, capturedMap.get(CHILDLOCK_KEY));
        assertEquals(2, capturedMap.size());
    }

    @Test
    public void testGetPropertiesWhenPortInfoNull() {
        diCommPort.getPortProperties();
        verifyGetPropertiesCalled(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRegisterListener() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.handleResponse("");

        verify(portListenerMock, times(1)).onPortUpdate(diCommPort);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUnregisterListener() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.removePortListener(portListenerMock);
        diCommPort.handleResponse("");

        verify(portListenerMock, times(0)).onPortUpdate(diCommPort);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testShouldNotCrashIfListenerIsUnregisteredTwice() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.removePortListener(portListenerMock);
        diCommPort.removePortListener(portListenerMock);

        diCommPort.handleResponse("");

        verify(portListenerMock, times(0)).onPortUpdate(diCommPort);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListenerShouldNotBeRegisteredTwice() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.addPortListener(portListenerMock);
        diCommPort.handleResponse("");

        verify(portListenerMock, times(1)).onPortUpdate(diCommPort);
    }

    @Test
    public void givenSubscribeIsInvoked_thenASubscriptionEventListenerIsAddedOnTheCommunicationStrategy() {
        diCommPort.subscribe();

        verify(communicationStrategyMock).addSubscriptionEventListener(any(SubscriptionEventListener.class));
    }

    @Test
    public void givenUnsubscribeIsInvoked_thenASubscriptionEventListenerIsRemovedFromTheCommunicationStrategy() {
        diCommPort.unsubscribe();

        verify(communicationStrategyMock).removeSubscriptionEventListener(any(SubscriptionEventListener.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenSubscribed_whenASubscriptionEventIsReceivedForThisPort_thenPortListenersShouldBeNotified() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.subscribe();

        ArgumentCaptor<SubscriptionEventListener> captor = ArgumentCaptor.forClass(SubscriptionEventListener.class);
        verify(communicationStrategyMock).addSubscriptionEventListener(captor.capture());

        captor.getValue().onSubscriptionEventReceived(PORT_NAME, "don't care");

        verify(portListenerMock).onPortUpdate(diCommPort);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenSubscribed_whenASubscriptionEventIsReceivedForAnotherPort_thenNoPortListenersShouldBeNotified() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.subscribe();

        ArgumentCaptor<SubscriptionEventListener> captor = ArgumentCaptor.forClass(SubscriptionEventListener.class);
        verify(communicationStrategyMock).addSubscriptionEventListener(captor.capture());

        captor.getValue().onSubscriptionEventReceived("anotherPortName", "don't care");

        verify(portListenerMock, never()).onPortUpdate(diCommPort);
    }

    @Test
    public void givenSubscribed_whenASubscriptionEventForThisPortCantBeDecrypted_thenAGetPropsShouldBeTriggered() {
        diCommPort.addPortListener(portListenerMock);
        diCommPort.subscribe();
        verifySubscribeCalled(true);
        responseHandlerCaptor.getValue().onSuccess(null);

        ArgumentCaptor<SubscriptionEventListener> captor = ArgumentCaptor.forClass(SubscriptionEventListener.class);
        verify(communicationStrategyMock).addSubscriptionEventListener(captor.capture());
        captor.getValue().onSubscriptionEventDecryptionFailed(PORT_NAME);

        verifyGetPropertiesCalled(true);
    }

    private void verifyPutPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategyMock, times(1)).putProperties(mapArgumentCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategyMock, never()).putProperties(mapArgumentCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private void verifyGetPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategyMock, times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategyMock, never()).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private void verifySubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategyMock, times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL_S), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategyMock, never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL_S), responseHandlerCaptor.capture());
        }
    }

    private void verifyUnsubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategyMock, times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategyMock, never()).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private ResponseHandler addListenerAndGetResponseHandler(DICommPortListener listener) {
        diCommPort.addPortListener(listener);
        verifyPutPropertiesCalled(true);

        return responseHandlerCaptor.getValue();
    }

    private class TestPort<P extends PortProperties> extends DICommPort<P> {

        private TestPort(final @NonNull CommunicationStrategy communicationStrategy) {
            super(communicationStrategy);
        }

        @Override
        public void processResponse(String jsonResponse) {
            // NOP
        }

        @Override
        public String getDICommPortName() {
            return PORT_NAME;
        }

        @Override
        public int getDICommProductId() {
            return PORT_PRODUCTID;
        }

        @Override
        public boolean supportsSubscription() {
            return false;
        }
    }

    private final class TestPortListener implements DICommPortListener {

        private boolean isApplyingChangesOnCallback = false;

        @Override
        public void onPortUpdate(DICommPort port) {
            isApplyingChangesOnCallback = port.isApplyingChanges();
        }

        @Override
        public void onPortError(DICommPort port, Error error, String errorData) {
            isApplyingChangesOnCallback = port.isApplyingChanges();
        }
    }
}
