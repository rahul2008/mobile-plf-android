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
    @Mock
    private WiFiBroadcastReceiver broadcastReceiverMock;
    private EventingChannel eventingChannel;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        eventingChannel = new EventingChannel(Arrays.asList(broadcastReceiverMock, applianceAccessEventMonitorMock, wifiConnectivityManagerMock));
    }

    @Test
    public void shouldStartAllMonitorsWhenStartIsCalled() throws Exception {
        eventingChannel.start();

        verify(broadcastReceiverMock).onStart();
        verify(applianceAccessEventMonitorMock).onStart();
        verify(wifiConnectivityManagerMock).onStart();
    }

    @Test
    public void shouldStopAllMonitorsWhenStopIsCalled() throws Exception {
        eventingChannel.stop();

        verify(broadcastReceiverMock).onStop();
        verify(applianceAccessEventMonitorMock).onStop();
        verify(wifiConnectivityManagerMock).onStop();
    }
}