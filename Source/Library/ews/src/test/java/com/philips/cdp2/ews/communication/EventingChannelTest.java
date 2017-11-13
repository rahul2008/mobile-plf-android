package com.philips.cdp2.ews.communication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class EventingChannelTest {

    @Mock
    private ApplianceAccessEventMonitor applianceAccessEventMonitorMock;
    @Mock
    private WiFiEventMonitor wifiConnectivityManagerMock;

    private EventingChannel eventingChannel;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        eventingChannel = new EventingChannel(Arrays.asList(applianceAccessEventMonitorMock, wifiConnectivityManagerMock));
    }

    @Test
    public void itShouldStartAllMonitorsWhenStartIsCalled() throws Exception {
        eventingChannel.start();

        verify(applianceAccessEventMonitorMock).onStart();
        verify(wifiConnectivityManagerMock).onStart();
    }

    @Test
    public void itShouldStopAllMonitorsWhenStopIsCalled() throws Exception {
        eventingChannel.stop();

        verify(applianceAccessEventMonitorMock).onStop();
        verify(wifiConnectivityManagerMock).onStop();
    }
}