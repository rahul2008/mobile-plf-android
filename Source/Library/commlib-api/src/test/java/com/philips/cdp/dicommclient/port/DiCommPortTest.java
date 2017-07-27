/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DiCommPortTest extends RobolectricTest {

    private final String PORT_NAME = "air";
    private final int PORT_PRODUCTID = 1;
    private final String FANSPEED_KEY = "fs";
    private final String FANSPEED_VALUE = "turbo";
    private final String POWER_KEY = "pwr";
    private final String POWER_VALUE = "1";
    private final String CHILDLOCK_KEY = "childlock";
    private final String CHILDLOCK_VALUE = "0";

    private CommunicationStrategy mCommunicationStrategy;

    private DICommPort<?> mDiCommPort;

    @Captor
    private ArgumentCaptor<Map<String, Object>> mMapCaptor;

    @Captor
    private ArgumentCaptor<ResponseHandler> mResponseHandlerCaptor;
    private WrappedHandler mHandler;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mCommunicationStrategy = mock(CommunicationStrategy.class);
        mHandler = mock(WrappedHandler.class);
        mDiCommPort = new TestPort(mCommunicationStrategy, mHandler);
    }

    @Test
    public void testPutProperties() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        verifyPutPropertiesCalled(true);
        Map<String, Object> capturedMap = mMapCaptor.getValue();
        assertTrue(capturedMap.containsKey(FANSPEED_KEY));
        assertEquals(FANSPEED_VALUE, capturedMap.get(FANSPEED_KEY));
    }

    @Test
    public void testGetProperties() {
        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testSubscribe() {
        mDiCommPort.subscribe();
        verifySubscribeCalled(true);
    }

    @Test
    public void testUnsubscribe() {
        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testPeformSubscribeAfterPutPropertiesSuccess() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.subscribe();
        verifySubscribeCalled(false);

        mResponseHandlerCaptor.getValue().onSuccess(null);
        verifySubscribeCalled(true);
    }

    @Test
    public void testPerformUnsubscribeAfterPutPropertiesSuccess() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        mResponseHandlerCaptor.getValue().onSuccess(null);
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testDoNotPerformGetAfterPutPropertiesSuccess() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformPutAfterPutPropertiesSuccess() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testPeformSubscribeAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.subscribe();
        verifySubscribeCalled(false);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifySubscribeCalled(true);
    }

    @Test
    public void testPerformUnsubscribeAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyUnsubscribeCalled(true);
    }

    @Test
    public void testPerformGetAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testPerformPutAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(false);

        responseHandler.onError(null, null);
        verifyPutPropertiesCalled(true);
    }

    @Test
    public void testDoNotRetryPutAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        reset(mCommunicationStrategy);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyPutPropertiesCalled(false);
    }

    @Test
    public void testDoNotPerformSuscribeAfterSubscribeSuccess() {
        mDiCommPort.subscribe();
        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.subscribe();
        verifySubscribeCalled(false);

        responseHandler.onSuccess(null);
        verifySubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterSubscribeSuccess() {
        mDiCommPort.subscribe();
        verifySubscribeCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformGetAfterSubscribeError() {
        mDiCommPort.subscribe();
        verifySubscribeCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testDoNotRetrySuscribeAfterSubscribeError() {
        mDiCommPort.subscribe();
        verifySubscribeCalled(true);
        reset(mCommunicationStrategy);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifySubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformUnsubscribeAfterUnsubscribeSuccess() {
        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(false);

        responseHandler.onSuccess(null);
        verifyUnsubscribeCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterUnsubscribeSuccess() {
        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testDonotRetryUnSubscribeAfterUnsubscribeError() {
        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);
        reset(mCommunicationStrategy);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyUnsubscribeCalled(false);
    }

    @Test
    public void testPerformGetAfterUnsubscribeError() {
        mDiCommPort.unsubscribe();
        verifyUnsubscribeCalled(true);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        mResponseHandlerCaptor.getValue().onError(null, null);
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testDoNotPerformGetAfterGetSuccess() {
        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandler.onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testDoNotPerformGetAfterGetError() {
        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.reloadProperties();
        verifyGetPropertiesCalled(false);

        responseHandler.onError(null, null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testPerformRequestsOnPriority() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.reloadProperties();
        mDiCommPort.unsubscribe();
        mDiCommPort.subscribe();
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onSuccess(null);
        verifyPutPropertiesCalled(true);
        responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        responseHandler.onSuccess(null);
        verifySubscribeCalled(true);
        responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        responseHandler.onSuccess(null);
        verifyUnsubscribeCalled(true);
        responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        responseHandler.onSuccess(null);
        verifyGetPropertiesCalled(false);
    }

    @Test
    public void testIsApplyingChangesAfterPutProperties() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);

        responseHandler.onSuccess(null);

        assertFalse(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesAfterPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);

        responseHandler.onError(Error.NOT_CONNECTED, null);

        assertFalse(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesBetweenTwoPutProperties() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onSuccess(null);

        assertTrue(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void testIsApplyingChangesBetweenTwoPutPropertiesError() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        TestPortListener listener = new TestPortListener();
        ResponseHandler responseHandler = addListenerAndGetResponseHandler(listener);
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

        responseHandler.onError(Error.NOT_CONNECTED, null);

        assertTrue(listener.isApplyingChangesOnCallback);
    }

    @Test
    public void test_ShouldPostRunnable_WhenSubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotPostRunnableTwice_WhenSubscribeIsCalledTwice() throws Exception {
        mDiCommPort.subscribe();
        mDiCommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldPostRunnableAgain_WhenSubscribeIsCalled_AfterSubscribeResponseIsReceived() throws Exception {
        mDiCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        mDiCommPort.subscribe();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldSubscribeToCommunicationStrategy_WhenSubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();

        verifySubscribeCalled(true);
    }

    @Test
    public void test_ShouldUnsubscribeFromCommunicationStrategy_WhenUnsubscribeIsCalled() throws Exception {
        mDiCommPort.unsubscribe();

        verifyUnsubscribeCalled(true);
    }

    @Test
    public void test_ShouldRemoveAndAddRunnable_WhenSubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();

        Runnable runnable = captureResubscribeHandler();
        verify(mHandler).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenStopResubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();
        mDiCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRemoveSubscribeRunnable_WhenUnsubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();
        mDiCommPort.unsubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    @Test
    public void test_ShouldRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterSubscribeResponseIsReceived() throws Exception {
        mDiCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.eq(runnable), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldNotRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeIsCalled() throws Exception {
        mDiCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        mDiCommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    @Test
    public void test_ShouldRePostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeAndSubscribeIsCalled() throws Exception {
        mDiCommPort.stopResubscribe();

        mDiCommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    private Runnable captureResubscribeHandler() {
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(mHandler).postDelayed(runnableCaptor.capture(), Mockito.anyInt());
        Runnable runnable = runnableCaptor.getValue();
        return runnable;
    }

    @Test
    public void testMultiplePutRequests() {
        mDiCommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        reset(mCommunicationStrategy);

        mDiCommPort.putProperties(POWER_KEY, POWER_VALUE);
        mDiCommPort.putProperties(CHILDLOCK_KEY, CHILDLOCK_VALUE);

        responseHandler.onSuccess(null);

        verifyPutPropertiesCalled(true);

        Map<String, Object> capturedMap = mMapCaptor.getValue();
        assertTrue(capturedMap.containsKey(POWER_KEY));
        assertTrue(capturedMap.containsKey(CHILDLOCK_KEY));
        assertEquals(POWER_VALUE, capturedMap.get(POWER_KEY));
        assertEquals(CHILDLOCK_VALUE, capturedMap.get(CHILDLOCK_KEY));
        assertEquals(2, capturedMap.size());
    }

    @Test
    public void testGetPropertiesWhenPortInfoNull() {
        mDiCommPort.getPortProperties();
        verifyGetPropertiesCalled(true);
    }

    @Test
    public void testRegisterListener() {
        DICommPortListener listener = mock(DICommPortListener.class);

        mDiCommPort.addPortListener(listener);
        mDiCommPort.handleResponse("");

        verify(listener, times(1)).onPortUpdate(mDiCommPort);
    }

    @Test
    public void testUnregisterListener() {
        DICommPortListener listener = mock(DICommPortListener.class);

        mDiCommPort.addPortListener(listener);
        mDiCommPort.removePortListener(listener);
        mDiCommPort.handleResponse("");

        verify(listener, times(0)).onPortUpdate(mDiCommPort);
    }

    @Test
    public void testShouldNotCrashIfListenerIsUnregisteredTwice() {
        DICommPortListener listener = mock(DICommPortListener.class);

        mDiCommPort.addPortListener(listener);
        mDiCommPort.removePortListener(listener);
        mDiCommPort.removePortListener(listener);

        mDiCommPort.handleResponse("");

        verify(listener, times(0)).onPortUpdate(mDiCommPort);
    }

    @Test
    public void testListenerShouldNotBeRegisteredTwice() {
        DICommPortListener listener = mock(DICommPortListener.class);

        mDiCommPort.addPortListener(listener);
        mDiCommPort.addPortListener(listener);
        mDiCommPort.handleResponse("");

        verify(listener, times(1)).onPortUpdate(mDiCommPort);
    }

    private void verifyPutPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(mCommunicationStrategy, times(1)).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        } else {
            verify(mCommunicationStrategy, never()).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        }
    }

    private void verifyGetPropertiesCalled(boolean invoked) {
        if (invoked) {
            verify(mCommunicationStrategy, times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        } else {
            verify(mCommunicationStrategy, never()).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        }
    }

    private void verifySubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(mCommunicationStrategy, times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL), mResponseHandlerCaptor.capture());
        } else {
            verify(mCommunicationStrategy, never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(DICommPort.SUBSCRIPTION_TTL), mResponseHandlerCaptor.capture());
        }
    }

    private void verifyUnsubscribeCalled(boolean invoked) {
        if (invoked) {
            verify(mCommunicationStrategy, times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        } else {
            verify(mCommunicationStrategy, never()).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID), mResponseHandlerCaptor.capture());
        }
    }

    private ResponseHandler addListenerAndGetResponseHandler(DICommPortListener listener) {
        mDiCommPort.addPortListener(listener);
        verifyPutPropertiesCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();

        return responseHandler;
    }

    class TestPort<P extends PortProperties> extends DICommPort<P> {

        private WrappedHandler handler;

        public TestPort(CommunicationStrategy communicationStrategy, WrappedHandler handler) {
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

    public class TestPortListener implements DICommPortListener<DICommPort<?>> {

        public boolean isApplyingChangesOnCallback = false;

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
