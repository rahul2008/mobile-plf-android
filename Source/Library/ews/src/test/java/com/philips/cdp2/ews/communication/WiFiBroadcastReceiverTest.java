/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ApplianceConnectErrorEvent;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.FetchDevicePortPropertiesEvent;
import com.philips.cdp2.ews.communication.events.FoundHomeNetworkEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.wifi.WiFiUtil.DEVICE_HOTSPOT_WIFI;
import static com.philips.cdp2.ews.wifi.WiFiUtil.HOME_WIFI;
import static com.philips.cdp2.ews.wifi.WiFiUtil.WRONG_WIFI;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSLogger.class)
public class WiFiBroadcastReceiverTest {

    @Mock
    private Context contextMock;

    @Mock
    private EventBus eventBusMock;

    private WiFiBroadcastReceiver broadcastReceiver;

    @Mock
    private WiFiUtil wifiUtilMock;

    @Mock
    private NetworkInfo networkInfoMock;

    @Mock
    private ApplianceSessionDetailsInfo applianceSessionDetailsInfoMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        broadcastReceiver = new WiFiBroadcastReceiver(contextMock, eventBusMock, wifiUtilMock, applianceSessionDetailsInfoMock);
    }

    @Test
    public void shouldRegisterReceiverWhenOnStartIsCalled() throws Exception {
        broadcastReceiver.onStart();

        verify(contextMock).registerReceiver(eq(broadcastReceiver), isA(IntentFilter.class));
        assertTrue(broadcastReceiver.isRegistered());
    }

    @Test
    public void shouldUnRegisterReceiverWhenOnStopIsCalled() throws Exception {
        broadcastReceiver.onStart();
        broadcastReceiver.onStop();

        verify(contextMock).unregisterReceiver(broadcastReceiver);
        assertFalse(broadcastReceiver.isRegistered());
    }

    @Test
    public void shouldRequestToFetchDevicePortDetailsWhenConnectedToDeviceHotspot() throws Exception {
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(DEVICE_HOTSPOT_WIFI);

        broadcastWiFiChangeRequest();

        verify(eventBusMock).post(isA(FetchDevicePortPropertiesEvent.class));
    }

    @Test
    public void shouldRequestToConnectBackToHomeOnceApplianceConnectedToHomeWiFiAndPortPropertiesReceived() throws Exception {
        when(applianceSessionDetailsInfoMock.hasSessionProperties()).thenReturn(true);
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(HOME_WIFI);

        broadcastWiFiChangeRequest();

        verify(eventBusMock).post(isA(DiscoverApplianceEvent.class));
    }

    @Test
    public void shouldSendErrorOnceApplianceConnectedToHomeWiFiAndPortPropertiesNotReceived() throws Exception {
        when(applianceSessionDetailsInfoMock.hasSessionProperties()).thenReturn(false);
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(HOME_WIFI);

        broadcastWiFiChangeRequest();

        verify(eventBusMock).post(isA(ApplianceConnectErrorEvent.class));
        verifyNoMoreInteractions(eventBusMock);
    }

    @Test
    public void shouldSendFoundHomeNetworkEventWhenNetworkIsConnectedFirstTime() throws Exception {
        broadcastWiFiChangeRequest();

        verify(eventBusMock).post(isA(FoundHomeNetworkEvent.class));
    }

    @Test
    public void shouldSendApplianceConnectErrorWrongWifiWhenConnectedToDifferentNewtwork() throws Exception {
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(WRONG_WIFI);

        broadcastWiFiChangeRequest();

        verify(eventBusMock).post(isA(ApplianceConnectErrorEvent.class));
    }

    private void broadcastWiFiChangeRequest() {
        final Intent intentMock = mock(Intent.class);

        when(intentMock.getAction()).thenReturn(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        when(intentMock.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).thenReturn(networkInfoMock);
        when(networkInfoMock.getState()).thenReturn(NetworkInfo.State.CONNECTED);

        broadcastReceiver.onReceive(contextMock, intentMock);
    }
}