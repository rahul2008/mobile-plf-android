/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommPortTest {

    private final String PORT_NAME = "air";
    private final int PORT_PRODUCTID = 1;
    private final String FANSPEED_KEY = "fs";
    private final String FANSPEED_VALUE = "turbo";
    private final String POWER_KEY = "pwr";
    private final String POWER_VALUE = "1";
    private final String CHILDLOCK_KEY = "childlock";
    private final String CHILDLOCK_VALUE = "0";

    @Mock
    private CommunicationStrategy communicationStrategy;

    @Mock
    private DICommPortListener portListener;

    @Mock
    private WrappedHandler mHandler;

    @Captor
    private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResponseHandler> responseHandlerCaptor;

    private DICommPort diCommPort;

    @Before
    public void setUp() {
        initMocks(this);
        DICommLog.disableLogging();

        diCommPort = new TestPort(communicationStrategy, mHandler);
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
    public void testPeformSubscribeAfterPutPropertiesSuccess() {
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
        reset(communicationStrategy);

        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testPeformSubscribeAfterPutPropertiesError() {
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
        reset(communicationStrategy);

        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onError(null, null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testDoNotRetryPutAfterPutPropertiesError() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        reset(communicationStrategy);

        responseHandlerCaptor.getValue().onError(null, null);
        verifyPutPropertiesCalled(false);
    }

    @Test
    public void testDoNotPerformSuscribeAfterSubscribeSuccess() {
        diCommPort.subscribe();
        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

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
        reset(communicationStrategy);

        responseHandlerCaptor.getValue().onError(null, null);
        verifySubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformUnsubscribeAfterUnsubscribeSuccess() {
        diCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

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
        reset(communicationStrategy);

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
        reset(communicationStrategy);

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
        reset(communicationStrategy);

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
        reset(communicationStrategy);

        diCommPort.reloadProperties();
        diCommPort.unsubscribe();
        diCommPort.subscribe();
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

        responseHandler.onSuccess(null);
        verifySubscribeCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

        responseHandler.onSuccess(null);
        verifyUnsubscribeCalled(true);
        responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

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

        responseHandler.onError(Error.NOT_CONNECTED, null);

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

        responseHandler.onError(Error.NOT_CONNECTED, null);

        assertTrue(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void test_ShouldPostRunnable_WhenSubscribeIsCalled() throws Exception {
        diCommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotPostRunnableTwice_WhenSubscribeIsCalledTwice() throws Exception {
        diCommPort.subscribe();
        diCommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldPostRunnableAgain_WhenSubscribeIsCalled_AfterSubscribeResponseIsReceived() throws Exception {
        diCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        diCommPort.subscribe();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldSubscribeToCommunicationStrategy_WhenSubscribeIsCalled() throws Exception {
        diCommPort.subscribe();

        verifySubscribeCalled(true);
    }

    @Test
    public void test_ShouldUnsubscribeFromCommunicationStrategy_WhenUnsubscribeIsCalled() throws Exception {
        diCommPort.unsubscribe();

        verifyUnsubscribeCalled(true);
    }

    @Test
    public void test_ShouldRemoveAndAddRunnable_WhenSubscribeIsCalled() throws Exception {
        diCommPort.subscribe();

        Runnable runnable = captureResubscribeHandler();
        verify(mHandler).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenStopResubscribeIsCalled() throws Exception {
        diCommPort.subscribe();
        diCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenUnsubscribeIsCalled() throws Exception {
        diCommPort.subscribe();
        diCommPort.unsubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterSubscribeResponseIsReceived() throws Exception {
        diCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.eq(runnable), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeIsCalled() throws Exception {
        diCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        diCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldRePostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeAndSubscribeIsCalled() throws Exception {
        diCommPort.stopResubscribe();

        diCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    private Runnable captureResubscribeHandler() {
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(mHandler).postDelayed(runnableCaptor.capture(), Mockito.anyInt());

        return runnableCaptor.getValue();
    }

    @Test
    public void testMultiplePutRequests() {
        diCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = responseHandlerCaptor.getValue();
        reset(communicationStrategy);

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
    public void testRegisterListener() {
        diCommPort.addPortListener(portListener);
        diCommPort.handleResponse("");

        verify(portListener, times(1)).onPortUpdate(diCommPort);
    }

    @Test
    public void testUnregisterListener() {
        diCommPort.addPortListener(portListener);
        diCommPort.removePortListener(portListener);
        diCommPort.handleResponse("");

        verify(portListener, times(0)).onPortUpdate(diCommPort);
    }

    @Test
    public void testShouldNotCrashIfListenerIsUnregisteredTwice() {
        diCommPort.addPortListener(portListener);
        diCommPort.removePortListener(portListener);
        diCommPort.removePortListener(portListener);

        diCommPort.handleResponse("");

        verify(portListener, times(0)).onPortUpdate(diCommPort);
    }

    @Test
    public void testListenerShouldNotBeRegisteredTwice() {
        diCommPort.addPortListener(portListener);
        diCommPort.addPortListener(portListener);
        diCommPort.handleResponse("");

        verify(portListener, times(1)).onPortUpdate(diCommPort);
    }

    private void verifyPutPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategy, times(1)).putProperties(mapArgumentCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategy, never()).putProperties(mapArgumentCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private void verifyGetPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategy, times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategy, never()).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private void verifySubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategy, times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategy, never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL), responseHandlerCaptor.capture());
        }
    }

    private void verifyUnsubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(communicationStrategy, times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        } else {
            verify(communicationStrategy, never()).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), responseHandlerCaptor.capture());
        }
    }

    private ResponseHandler addListenerAndGetResponseHandler(DICommPortListener listener) {
        diCommPort.addPortListener(listener);
        verifyPutPropertiesCalled(true);

        return responseHandlerCaptor.getValue();
    }

    private class TestPort<P extends PortProperties> extends DICommPort<P> {

        private WrappedHandler handler;

        private TestPort(CommunicationStrategy communicationStrategy, WrappedHandler handler) {
            super(communicationStrategy);
            this.handler = handler;
        }

        @Override
        protected WrappedHandler getResubscriptionHandler() {
            return handler;
        }

        @Override
        public boolean isResponseForThisPort(String jsonResponse) {
            return true;
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

    private class TestPortListener implements DICommPortListener<DICommPort<?>> {

        private boolean isApplyingChangesOnCallback = false;

        @Override
        public void onPortUpdate(DICommPort<?> port) {
            isApplyingChangesOnCallback = port.isApplyingChanges();
        }

        @Override
        public void onPortError(DICommPort<?> port, Error error, String errorData) {
            isApplyingChangesOnCallback = port.isApplyingChanges();
        }
    }
}
