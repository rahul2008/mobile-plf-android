/*
 * © Koninklijke Philips N.V., 2016
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import android.util.Log;

import com.philips.cdp.cloudcontroller.CloudController;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RemoteCommunicationStrategyTest {

    public static final int SUBSCRIPTION_TTL = 0;
    private final Map<String, Object> dataMap = new HashMap<>();
    private final String PORT_NAME = "AirPort";
    private final int PRODUCT_ID = 0;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private CloudController CloudControllerMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private RequestQueue requestQueueMock;

    @Mock
    ArgumentCaptor<StartDcsRequest> startRequestArgumentCaptor;

    @Mock
    RemoteSubscriptionHandler remoteSubscriptionHandlerMock;

    private RemoteCommunicationStrategy remoteCommunicationStrategy;
    private ResponseHandler capturedResponseHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockStatic(Log.class);

        DICommLog.disableLogging();

        remoteCommunicationStrategy = new RemoteCommunicationStrategyForTesting(networkNodeMock, CloudControllerMock);
    }

    @Test
    public void whenPutPropsIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCHasStartedSuccessfullyThenPutPropsRequestIsExecuted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledWhileDSCIsStartedThenPutPropsRequestIsExecuted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTED);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(StartDcsRequest.class));
        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledTwiceThenAnotherDSCStartRequestIsNotIssued() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTING);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(1)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISFailedToStartThenItIsStartedAgainWithNextRequest() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onError(Error.REQUEST_FAILED, null);

        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISStoppedThenItIsRestartedAgainWithNextRequest() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        remoteCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenGetPropsIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenAddPropsIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.addProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDeletePropsIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenSubscribeIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenUnsubscribeIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenDSCIsStarted() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        remoteCommunicationStrategy.enableCommunication(subscriptionEventListener);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenEnableSubscriptionIsCalled() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        remoteCommunicationStrategy.enableCommunication(subscriptionEventListener);

        verify(remoteSubscriptionHandlerMock).enableSubscription(networkNodeMock, subscriptionEventListener);
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDSCIsStopped() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.disableCommunication();

        verify(CloudControllerMock).stopDCSService();
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDisableSubscriptionIsCalled() throws Exception {
        when(CloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        remoteCommunicationStrategy.disableCommunication();

        verify(remoteSubscriptionHandlerMock).disableSubscription();
    }

    @Test
    public void isAvailableWhenNetworkNodeIsCONNECTED_REMOTELY() throws Exception {
        when(networkNodeMock.getConnectionState()).thenReturn(ConnectionState.CONNECTED_REMOTELY);

        assertTrue(remoteCommunicationStrategy.isAvailable());
    }

    @Test
    public void isNotAvailableWhenNetworkNodeIsDISCONNECTED() throws Exception {
        when(networkNodeMock.getConnectionState()).thenReturn(ConnectionState.DISCONNECTED);

        assertFalse(remoteCommunicationStrategy.isAvailable());
    }

    class RemoteCommunicationStrategyForTesting extends RemoteCommunicationStrategy {

        public RemoteCommunicationStrategyForTesting(NetworkNode networkNode, CloudController cloudController) {
            super(networkNode, cloudController);
        }

        @Override
        protected RequestQueue createRequestQueue() {
            return requestQueueMock;
        }

        @Override
        protected StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
            RemoteCommunicationStrategyTest.this.capturedResponseHandler = responseHandler;
            return null;
        }

        @Override
        protected RemoteSubscriptionHandler createRemoteSubscriptionHandler(CloudController cloudController) {
            return remoteSubscriptionHandlerMock;
        }
    }
}
