package com.philips.cdp2.ews.communication;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class EventMonitorTest {

    @Mock
    private EventBus eventBusMock;
    private EventMonitor eventMonitor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        eventMonitor = new TestEventMonitor(eventBusMock);
    }

    @Test
    public void itShouldRegisterWithEventBusWhenOnStartIsCalledFirstTime() throws Exception {
        when(eventBusMock.isRegistered(eventMonitor)).thenReturn(false);

        eventMonitor.onStart();

        verify(eventBusMock).register(eventMonitor);
    }

    @Test
    public void itShouldNotRegisterWithEventBusAgainIfAlreadyRegisteredWhenOnStartIsCalled() throws Exception {
        when(eventBusMock.isRegistered(eventMonitor)).thenReturn(true);

        eventMonitor.onStart();

        verify(eventBusMock, never()).register(eventMonitor);
    }

    @Test
    public void itShouldUnRegisterWithEventBusIfAlreadyRegisteredWhenOnStopIsCalled() throws Exception {
        when(eventBusMock.isRegistered(eventMonitor)).thenReturn(true);

        eventMonitor.onStop();

        verify(eventBusMock).unregister(eventMonitor);
    }

    private class TestEventMonitor extends EventMonitor {

        TestEventMonitor(@NonNull final EventBus eventBus) {
            super(eventBus);
        }
    }
}