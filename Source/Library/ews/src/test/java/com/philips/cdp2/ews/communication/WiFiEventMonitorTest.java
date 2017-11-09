/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.events.NetworkConnectEvent;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class WiFiEventMonitorTest {

    @Mock
    private WiFiConnectivityManager wifiConnectivityManagerMock;

    private WiFiEventMonitor eventMonitor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        eventMonitor = new WiFiEventMonitor(wifiConnectivityManagerMock, mock(EventBus.class));
    }

    @Test
    public void itShouldConnectToApplianceHotspotWhenRequested() throws Exception {
        final NetworkConnectEvent event = new NetworkConnectEvent(NetworkType.DEVICE_HOTSPOT, WiFiUtil.DEVICE_SSID);

        eventMonitor.connectToNetwork(event);

        verify(wifiConnectivityManagerMock).connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
    }

    @Test
    public void itShouldConnectToHomeWiFiNetworkWhenRequested() throws Exception {
        final String HOME_WIFI_SSID = "BrightEyes";

        final NetworkConnectEvent event = new NetworkConnectEvent(NetworkType.HOME_WIFI, HOME_WIFI_SSID);

        eventMonitor.connectToNetwork(event);

        verify(wifiConnectivityManagerMock).connectToHomeWiFiNetwork(HOME_WIFI_SSID);
    }
}