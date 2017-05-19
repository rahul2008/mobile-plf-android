/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNSharedConnectionDeviceTest {

    private static final long CONNECT_TIMEOUT_MILLIS = 1000L;
    @Mock
    private Handler mockInternalHandler;

    @Mock
    private Handler mockUserHandler;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private SHNDevice.SHNDeviceListener mockDeviceListener;

    private SHNSharedConnectionDevice sharedConnectionDevice;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }
        }).when(mockInternalHandler).post(any(Runnable.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }
        }).when(mockUserHandler).post(any(Runnable.class));

        SHNSharedConnectionDevice.setHandlers(mockInternalHandler, mockUserHandler);
        sharedConnectionDevice = new SHNSharedConnectionDevice(mockDevice);

        sharedConnectionDevice.registerSHNDeviceListener(mockDeviceListener);
    }

    @Test
    public void connectDeviceOnceWhenIncomingConnectIsInvoked() {
        sharedConnectionDevice.connect();

        verify(mockDevice, times(1)).connect();
    }

    @Test
    public void connectDeviceOnceWhenMultipleIncomingConnectsAreInvoked() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.connect();

        verify(mockDevice, times(1)).connect();
    }

    @Test
    public void connectDeviceOnceWhenIncomingConnectWithTimeoutIsInvoked() {
        sharedConnectionDevice.connect(CONNECT_TIMEOUT_MILLIS);

        verify(mockDevice, times(1)).connect(anyLong());
    }

    @Test
    public void connectDeviceOnceWhenMultipleIncomingConnectsWithTimeoutAreInvoked() {
        sharedConnectionDevice.connect(CONNECT_TIMEOUT_MILLIS);
        sharedConnectionDevice.connect(CONNECT_TIMEOUT_MILLIS);

        verify(mockDevice, times(1)).connect(anyLong());
    }

    @Test
    public void reconnectDeviceWithoutTimeoutWhenDeviceGotDisconnected() {
        sharedConnectionDevice.connect();

        when(mockDevice.getState()).thenReturn(SHNDevice.State.Disconnected);
        sharedConnectionDevice.onStateUpdated(mockDevice);

        verify(mockDevice, times(2)).connect();
    }

    @Test
    public void reconnectDeviceWithTimeoutWhenDeviceGotDisconnected() {
        sharedConnectionDevice.connect(CONNECT_TIMEOUT_MILLIS);

        when(mockDevice.getState()).thenReturn(SHNDevice.State.Disconnected);
        sharedConnectionDevice.onStateUpdated(mockDevice);

        verify(mockDevice, times(2)).connect(CONNECT_TIMEOUT_MILLIS);
    }

    @Test
    public void disconnectDeviceOnceWhenIncomingDisconnectIsInvoked() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.disconnect();

        verify(mockDevice, times(1)).disconnect();
    }

    @Test
    public void disconnectDeviceOnceWhenMultipleIncomingDisconnectsAreInvoked() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.connect();
        sharedConnectionDevice.disconnect();
        sharedConnectionDevice.disconnect();

        verify(mockDevice, times(1)).disconnect();
    }

    @Test(expected = IllegalStateException.class)
    public void throwExceptionWhenNumberOfDisconnectsExceedNumberOfConnects() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.disconnect();
        sharedConnectionDevice.disconnect();
    }

    @Test
    public void notifyStateUpdatedWhenMultipleConnectsAreInvoked() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.connect();

        verify(mockDeviceListener, times(1)).onStateUpdated(sharedConnectionDevice);
    }

    @Test
    public void notifyStateUpdatedWhenDisconnectIsInvokedAfterMultipleConnects() {
        sharedConnectionDevice.connect();
        sharedConnectionDevice.connect();
        sharedConnectionDevice.disconnect();

        verify(mockDeviceListener, times(2)).onStateUpdated(sharedConnectionDevice);
    }
}
