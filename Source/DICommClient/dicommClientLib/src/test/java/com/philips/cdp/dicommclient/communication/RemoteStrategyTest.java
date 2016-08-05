/*
 * © Koninklijke Philips N.V., 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

public class RemoteStrategyTest {

    public static final int SUBSCRIPTION_TTL = 0;
    private final Map<String, Object> dataMap = new HashMap<>();
    private final String PORT_NAME = "AirPort";
    private final int PRODUCT_ID = 0;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private CppController cppControllerMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private RequestQueue requestQueueMock;

    @Mock
    ArgumentCaptor<StartDcsRequest> startRequestArgumentCaptor;

    @Mock
    RemoteSubscriptionHandler remoteSubscriptionHandlerMock;

    private RemoteStrategy remoteStrategy;
    private ResponseHandler capturedResponseHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        remoteStrategy = new RemoteStrategyForTesting(networkNodeMock, cppControllerMock);
    }

    @Test
    public void whenPutPropsIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCHasStartedSuccessfullyThenPutPropsRequestIsExecuted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledWhileDSCIsStartedThenPutPropsRequestIsExecuted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTED);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(StartDcsRequest.class));
        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledTwiceThenAnotherDSCStartRequestIsNotIssued() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTING);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(1)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISFailedToStartThenItIsStartedAgainWithNextRequest() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onError(Error.REQUESTFAILED, null);

        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISStoppedThenItIsRestartedAgainWithNextRequest() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        remoteStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenGetPropsIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenAddPropsIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.addProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDeletePropsIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenSubscribeIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenUnsubscribeIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenDSCIsStarted() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        remoteStrategy.enableSubscription(subscriptionEventListener);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenEnableSubscriptionIsCalled() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        remoteStrategy.enableSubscription(subscriptionEventListener);

        verify(remoteSubscriptionHandlerMock).enableSubscription(networkNodeMock, subscriptionEventListener);
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDSCIsStopped() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.disableCommunication();

        verify(cppControllerMock).stopDCSService();
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDisableSubscriptionIsCalled() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STOPPED);
        remoteStrategy.disableCommunication();

        verify(remoteSubscriptionHandlerMock).disableSubscription();
    }

    @Test
    public void isAvailableWhenNetworkNodeIsCONNECTED_REMOTELY() throws Exception {
        when(networkNodeMock.getConnectionState()).thenReturn(ConnectionState.CONNECTED_REMOTELY);

        assertTrue(remoteStrategy.isAvailable());
    }

    @Test
    public void isNotAvailableWhenNetworkNodeIsDISCONNECTED() throws Exception {
        when(networkNodeMock.getConnectionState()).thenReturn(ConnectionState.DISCONNECTED);

        assertFalse(remoteStrategy.isAvailable());
    }

    class RemoteStrategyForTesting extends RemoteStrategy {

        public RemoteStrategyForTesting(NetworkNode networkNode, CppController cppController) {
            super(networkNode, cppController);
        }

        @Override
        protected RequestQueue createRequestQueue() {
            return requestQueueMock;
        }

        @Override
        protected StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
            RemoteStrategyTest.this.capturedResponseHandler = responseHandler;
            return null;
        }

        @Override
        protected RemoteSubscriptionHandler createRemoteSubscriptionHandler(CppController cppController) {
            return remoteSubscriptionHandlerMock;
        }
    }
}