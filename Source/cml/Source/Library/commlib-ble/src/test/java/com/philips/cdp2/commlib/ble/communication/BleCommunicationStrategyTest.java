/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.SHNDevice;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleCommunicationStrategyTest {

    private static final String PORT_NAME = "thePort";
    private static final int PRODUCT_ID = 0;
    private static final String CPP_ID = "NCC-1701";

    @Mock
    private BleDeviceCache deviceCacheMock;

    @Mock
    private BleCacheData bleCacheDataMock;

    @Mock
    private SHNDevice deviceMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private Handler callbackHandlerMock;

    private BleCommunicationStrategy strategy;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        when(deviceCacheMock.getCacheData(eq(CPP_ID))).thenReturn(bleCacheDataMock);
        when(bleCacheDataMock.isAvailable()).thenReturn(true);
        when(bleCacheDataMock.getDevice()).thenReturn(deviceMock);

        strategy = new BleCommunicationStrategy(CPP_ID, deviceCacheMock, callbackHandlerMock);
    }

    @Test
    public void testSubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribedWhenSubscribingAgainThenSuccessReturned() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, mock(ResponseHandler.class));

        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void testUnsubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, mock(ResponseHandler.class));
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenNoSubscriptionWhenUnsubscribingThenSuccessReturned() {
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenCommunicationIsEnabled_andExecutorIsIdle_whenCommunicationIsDisabled_thenDisconnectDevice() {
        strategy.enableCommunication();
        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock, times(1)).disconnect();
    }

    @Test
    public void givenCommunicationIsEnabled_andExecutorIsNotIdle_whenCommunicationIsDisabled_thenNotDisconnectDevice() {
        strategy.enableCommunication();
        strategy.requestExecutor.getQueue().add(generateTask());
        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock, never()).disconnect();
    }

    @Test
    public void givenCommunicationIsEnabled_andAGetPropsIsPerformed_whenCommunicationIsDisabled_thenDisconnectDevice() {
        strategy.enableCommunication();

        verify(deviceMock).connect(anyLong());

        strategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);
        strategy.disableCommunication();

        verify(deviceMock).disconnect();
    }

    @NonNull
    private Runnable generateTask() {
        return new Runnable() {
            @Override
            public void run() {
            }
        };
    }
}
