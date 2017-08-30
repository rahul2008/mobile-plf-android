/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.os.Handler;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDevice.SHNDeviceListener;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp.dicommclient.request.Error.TIMED_OUT;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static com.philips.pins.shinelib.dicommsupport.StatusCode.NoError;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleRequestTest {
    private static final String CPP_ID = "Sinterklaas";
    private static final String PORT_NAME = "PoliticallyCorrectPiet";
    private static final int PRODUCT_ID = 1337;

    private BleRequest request;

    @Mock
    private BleDeviceCache mockDeviceCache;

    @Mock
    private BleCacheData mockCacheData;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private DiCommResponse mockDicommResponse;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private CapabilityDiComm mockCapability;

    @Mock
    CountDownLatch mockInProgressLatch;

    SHNDeviceListener stateListener;

    @Mock
    private Handler handlerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(mockDevice.getCapabilityForType(SHNCapabilityType.DI_COMM)).thenReturn(mockCapability);
        when(mockDevice.getState()).thenReturn(Connected);

        when(mockDeviceCache.getCacheData(anyString())).thenReturn(mockCacheData);
        when(mockCacheData.getDevice()).thenReturn(mockDevice);

        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                request.processDiCommResponse(mockDicommResponse);
                return null;
            }
        }).when(mockInProgressLatch).await();

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                stateListener = (SHNDeviceListener) invocation.getArguments()[0];
                return null;
            }
        }).when(mockDevice).registerSHNDeviceListener(any(SHNDeviceListener.class));

        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");

        request = new BleGetRequest(mockDeviceCache, CPP_ID, PORT_NAME, PRODUCT_ID, responseHandlerMock, handlerMock, new AtomicBoolean(true));
        request.inProgressLatch = mockInProgressLatch;

        when(handlerMock.post(runnableCaptor.capture())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                runnableCaptor.getValue().run();
                return null;
            }
        });
    }

    @Test
    public void whenRequestIsCancelledAfterSuccessThenNoErrorIsReported() {
        request.run();

        request.cancel("timeout");

        verify(responseHandlerMock, times(0)).onError(any(Error.class), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenErrorIsReported() {

        request.cancel("timeout");

        verify(responseHandlerMock).onError(eq(TIMED_OUT), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenRequestIsNeverExecuted() {
        request.cancel("timeout");

        request.run();

        verify(responseHandlerMock, times(0)).onSuccess(anyString());
    }

    @Test
    public void callsOnSuccessOnHandlerAndDisconnects() {
        request.run();

        verify(responseHandlerMock).onSuccess(anyString());
        verify(mockDevice).disconnect();
    }

    @Test
    public void doesntCallDisconnectWhenStayingConnected() {
        request = new BleGetRequest(mockDeviceCache, CPP_ID, PORT_NAME, PRODUCT_ID, responseHandlerMock, handlerMock, new AtomicBoolean(false));
        request.inProgressLatch = mockInProgressLatch;

        request.run();

        verify(responseHandlerMock).onSuccess(anyString());
        verify(mockDevice, times(0)).disconnect();
    }

    @Test
    public void callsConnectWhenNotConnected() {
        when(mockDevice.getState()).thenReturn(Disconnected);

        request.run();

        verify(mockDevice).connect(eq(30000L));
    }

    @Test
    public void unregistersListenerAfterDisconnected() throws InterruptedException {
        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                request.processDiCommResponse(mockDicommResponse);
                when(mockDevice.getState()).thenReturn(Disconnected);
                stateListener.onStateUpdated(mockDevice);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(mockDevice).registerSHNDeviceListener(null);
    }

    @Test
    public void givenRequestIsExecutingWhenDeviceDisconnectsThenOnErrorIsReported() throws InterruptedException {
        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                when(mockDevice.getState()).thenReturn(Disconnected);
                stateListener.onStateUpdated(mockDevice);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(Error.REQUEST_FAILED), anyString());
    }
}
