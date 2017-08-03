/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.cloud.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CloudCommunicationStrategyTest {

    private static final int SUBSCRIPTION_TTL = 0;
    private final Map<String, Object> dataMap = new HashMap<>();
    private final String PORT_NAME = "AirPort";
    private final int PRODUCT_ID = 0;

    @Mock
    private Context contextMock;

    @Mock
    private Handler handlerMock;

    @Mock
    ConnectivityManager connectivityManagerMock;

    @Mock
    ConnectivityMonitor connectivityMonitorMock;

    @Mock
    private NetworkInfo activeNetworkInfoMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private CloudController cloudControllerMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private RequestQueue requestQueueMock;

    @Mock
    ArgumentCaptor<StartDcsRequest> startRequestArgumentCaptor;

    @Mock
    RemoteSubscriptionHandler remoteSubscriptionHandlerMock;

    private CloudCommunicationStrategy cloudCommunicationStrategy;
    private ResponseHandler capturedResponseHandler;

    @Before
    public void setUp() {
        initMocks(this);

        mockStatic(Log.class);

        HandlerProvider.enableMockedHandler(handlerMock);
        DICommLog.disableLogging();

        when(contextMock.getApplicationContext()).thenReturn(contextMock);

//        ConnectivityMonitor connectivityMonitor = ConnectivityMonitor.forNetworkTypes(contextMock, TYPE_MOBILE, TYPE_WIFI);

        cloudCommunicationStrategy = new CloudCommunicationStrategyForTesting(networkNodeMock, cloudControllerMock, connectivityMonitorMock);
    }

    @Test
    public void whenPutPropsIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCHasStartedSuccessfullyThenPutPropsRequestIsExecuted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledWhileDSCIsStartedThenPutPropsRequestIsExecuted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTED);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(StartDcsRequest.class));
        verify(requestQueueMock).addRequest(any(RemoteRequest.class));
    }

    @Test
    public void whenPutPropsIsCalledTwiceThenAnotherDSCStartRequestIsNotIssued() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTING);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(1)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISFailedToStartThenItIsStartedAgainWithNextRequest() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onError(Error.REQUEST_FAILED, null);

        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDSCISStoppedThenItIsRestartedAgainWithNextRequest() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(startRequestArgumentCaptor.capture());
        capturedResponseHandler.onSuccess(null);

        cloudCommunicationStrategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock, times(2)).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenGetPropsIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenAddPropsIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.addProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenDeletePropsIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.deleteProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenSubscribeIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenUnsubscribeIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenDSCIsStarted() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        cloudCommunicationStrategy.enableCommunication(subscriptionEventListener);

        verify(requestQueueMock).addRequestInFrontOfQueue(any(StartDcsRequest.class));
    }

    @Test
    public void whenEnableSubscriptionIsCalledThenEnableSubscriptionIsCalled() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        SubscriptionEventListener subscriptionEventListener = mock(SubscriptionEventListener.class);
        cloudCommunicationStrategy.enableCommunication(subscriptionEventListener);

        verify(remoteSubscriptionHandlerMock).enableSubscription(networkNodeMock, subscriptionEventListener);
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDSCIsStopped() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.disableCommunication();

        verify(cloudControllerMock).stopDCSService();
    }

    @Test
    public void whenDisableCommunicationIsCalledThenDisableSubscriptionIsCalled() {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STOPPED);
        cloudCommunicationStrategy.disableCommunication();

        verify(remoteSubscriptionHandlerMock).disableSubscription();
    }

    @Test
    public void isAvailableWhenWifiConnected() {
        doAnswer(new Answer<Intent>() {
            @Override
            public Intent answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, BroadcastReceiver.class).onReceive(contextMock, null);
                return null;
            }
        }).when(contextMock).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));

        when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(activeNetworkInfoMock);
        when(activeNetworkInfoMock.getType()).thenReturn(TYPE_WIFI);
        when(activeNetworkInfoMock.isConnected()).thenReturn(true);

        cloudCommunicationStrategy = new CloudCommunicationStrategyForTesting(networkNodeMock, cloudControllerMock, connectivityMonitorMock);

        assertThat(cloudCommunicationStrategy.isAvailable()).isTrue();
    }

    @Test
    public void isNotAvailableWhenWifiNotConnected() {
        doAnswer(new Answer<Intent>() {
            @Override
            public Intent answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, BroadcastReceiver.class).onReceive(contextMock, null);
                return null;
            }
        }).when(contextMock).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));

        when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(activeNetworkInfoMock);
        when(activeNetworkInfoMock.getType()).thenReturn(TYPE_WIFI);
        when(activeNetworkInfoMock.isConnected()).thenReturn(false);

        cloudCommunicationStrategy = new CloudCommunicationStrategyForTesting(networkNodeMock, cloudControllerMock, connectivityMonitorMock);

        assertThat(cloudCommunicationStrategy.isAvailable()).isFalse();
    }

    class CloudCommunicationStrategyForTesting extends CloudCommunicationStrategy {

        CloudCommunicationStrategyForTesting(NetworkNode networkNode, CloudController cloudController, ConnectivityMonitor connectivityMonitor) {
            super(networkNode, cloudController, connectivityMonitor);
        }

        @Override
        protected RequestQueue createRequestQueue() {
            return requestQueueMock;
        }

        @Override
        protected StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
            CloudCommunicationStrategyTest.this.capturedResponseHandler = responseHandler;
            return null;
        }

        @Override
        protected RemoteSubscriptionHandler createRemoteSubscriptionHandler(CloudController cloudController) {
            return remoteSubscriptionHandlerMock;
        }
    }
}
