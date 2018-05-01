/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.util.VerboseExecutor;
import com.philips.pins.shinelib.SHNDevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleCommunicationStrategyTest {

    private static final String PORT_NAME = "thePort";
    private static final int PRODUCT_ID = 0;
    private static final String CPP_ID = "NCC-1701";
    private static final int SUBSCRIPTION_TTL = 5000;

    @Mock
    private BleDeviceCache deviceCacheMock;

    @Mock
    private VerboseExecutor executorMock;

    @Mock
    private BleCacheData bleCacheDataMock;

    @Mock
    private SHNDevice deviceMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private Handler callbackHandlerMock;

    @Mock
    private SubscriptionEventListener subscriptionListenerMock;

    private List<PollingSubscription> pollingSubscriptionMocks = new ArrayList<>();

    private int lastSubscriptionTtl;

    private PortParameters lastPortParameters;

    private ResponseHandler lastResponseHandler;

    private BleCommunicationStrategy strategy;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        when(deviceCacheMock.getCacheData(eq(CPP_ID))).thenReturn(bleCacheDataMock);
        when(bleCacheDataMock.isAvailable()).thenReturn(true);
        when(bleCacheDataMock.getDevice()).thenReturn(deviceMock);

        strategy = new BleCommunicationStrategy(CPP_ID, deviceCacheMock, callbackHandlerMock, 2000, executorMock) {
            @NonNull
            @Override
            protected PollingSubscription createPollingSubscription(final int subscriptionTtl, final PortParameters portParameters, final ResponseHandler responseHandler) {
                lastSubscriptionTtl = subscriptionTtl;
                lastPortParameters = portParameters;
                lastResponseHandler = responseHandler;

                final PollingSubscription mock = mock(PollingSubscription.class);
                pollingSubscriptionMocks.add(mock);
                return mock;
            }
        };

        strategy.addSubscriptionEventListener(subscriptionListenerMock);
    }

    @Test
    public void testSubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribedWhenSubscribingAgainThenSuccessReturned() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void testUnsubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void whenSubscribing_thenAPollingSubscriptionIsCreated() {

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        assertThat(lastSubscriptionTtl).isEqualTo(SUBSCRIPTION_TTL);
        assertThat(lastPortParameters).isEqualTo(new PortParameters(PORT_NAME, PRODUCT_ID));
        assertThat(lastResponseHandler).isNotNull();
    }

    @Test
    public void givenSubscribed_whenPollerGetsResponse_thenSubscriptionEventIsReported() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        final String SUBSCRIPTION_EVENT_DATA = "Rainer";
        lastResponseHandler.onSuccess(SUBSCRIPTION_EVENT_DATA);

        verify(subscriptionListenerMock).onSubscriptionEventReceived(PORT_NAME, SUBSCRIPTION_EVENT_DATA);
    }

    @Test
    public void whenSubscribing_thenSuccessIsReported() {

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribed_whenUnsubscribing_thenSuccessIsReported() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribed_whenUnsubscribing_thenPollingIsStopped() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(pollingSubscriptionMocks.get(0)).cancel();
    }

    @Test
    public void givenSubscribed_whenSubscribingAgainForSamePort_thenOldPollerIsCancelledAndNewPollerCreated() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        verify(pollingSubscriptionMocks.get(0)).cancel();
        assertThat(pollingSubscriptionMocks.size()).isEqualTo(2);
    }

    @Test
    public void givenNoSubscriptionWhenUnsubscribingThenSuccessReturned() {
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void whenCommunicationIsEnabled_thenDeviceIsConnectedAndDisconnectAfterRequestIsFalse() {

        strategy.enableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isFalse();
        verify(deviceMock).connect(anyLong());
    }

    @Test
    public void givenCommunicationIsEnabledAndExecutorIsIdle_whenCommunicationIsDisabled_thenDisconnectDeviceAndDisconnectAfterRequestIsSet() {
        strategy.enableCommunication();
        when(executorMock.isIdle()).thenReturn(true);

        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock).disconnect();
    }

    @Test
    public void givenCommunicationIsEnabledAndExecutorIsNotIdle_whenCommunicationIsDisabled_thenNotDisconnectDeviceButDisconnectAfterRequestIsSet() {
        strategy.enableCommunication();
        when(executorMock.isIdle()).thenReturn(false);

        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock, never()).disconnect();
    }
}
