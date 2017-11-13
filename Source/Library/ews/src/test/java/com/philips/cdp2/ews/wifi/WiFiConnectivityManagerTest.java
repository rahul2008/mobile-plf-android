package com.philips.cdp2.ews.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.logger.EWSLogger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(EWSLogger.class)
public class WiFiConnectivityManagerTest {

    private static final String HOME_WIFI_SSID = "BrightEyes";
    private static final String APPLIANCE_SSID = WiFiUtil.DEVICE_SSID;

    private WiFiConnectivityManager connectivityManager;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private WifiManager wifiManagerMock;

    @Mock
    private Wifi wifiMock;

    @Mock
    Handler mockHandler;

    private WiFiUtil wifiUtil;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        wifiUtil = new WiFiUtil(wifiManagerMock);
        connectivityManager = new WiFiConnectivityManager(wifiManagerMock, wifiMock, wifiUtil);
        connectivityManager.handler = mockHandler;
        implementAsDirectExecutor(mockHandler);
        stubHomeNetworkConnection();
    }

    private void stubHomeNetworkConnection() {
        final WifiInfo wifiInfoMock = mock(WifiInfo.class);
        when(wifiManagerMock.getConnectionInfo()).thenReturn(wifiInfoMock);
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.COMPLETED);
        when(wifiInfoMock.getSSID()).thenReturn("WLAN-PUB");
    }

    @Test
    public void itShouldConfigureOpenNetworkWhenApplianceHotspotIsNotFound() throws Exception {
        connectivityManager.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

        final ArgumentCaptor<WifiConfiguration> wifiConfigurationCaptor = ArgumentCaptor.forClass(WifiConfiguration.class);

        verify(wifiManagerMock).addNetwork(wifiConfigurationCaptor.capture());

        WifiConfiguration openNetworkConfig = wifiConfigurationCaptor.getValue();
        assertEquals("\"" + APPLIANCE_SSID + "\"", openNetworkConfig.SSID);
        assertEquals(WifiConfiguration.Status.ENABLED, openNetworkConfig.status);
        assertEquals(40, openNetworkConfig.priority);

        verify(wifiManagerMock).addNetwork(openNetworkConfig);
    }

    @Test
    public void connectToApplianceHotspotIfFound() throws Exception {
        final ScanResult scannedResultMock = getScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(null).thenReturn(Collections.singletonList(scannedResultMock));
        connectivityManager.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

        verify(wifiMock).connectToConfiguredNetwork(wifiManagerMock, scannedResultMock);
    }

    @Test
    public void itShouldNotConnectToApplianceHotspotWhenNoMatchingScannedResultsFound() throws Exception {
        final ScanResult scannedResultMock = getScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(null);

        verifyApplianceNotConnected(scannedResultMock);
    }

    private void verifyApplianceNotConnected(final ScanResult scannedResultMock) {
        connectivityManager.setMaxScanAttempts(1);
        connectivityManager.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

        verify(wifiMock, never()).connectToConfiguredNetwork(wifiManagerMock, scannedResultMock);
    }

    @Test
    public void itShouldNotConnectToApplianceHotspotWhenScannedResultsIsEmpty() throws Exception {
        final ScanResult scannedResultMock = getScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(new ArrayList<ScanResult>());

        verifyApplianceNotConnected(scannedResultMock);
    }

    @Test
    public void connectToHomeWiFiNetworkWhenRequested() throws Exception {
        final ScanResult scannedResultMock = getScanResult(HOME_WIFI_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(Collections.singletonList(scannedResultMock));

        connectivityManager.connectToHomeWiFiNetwork(HOME_WIFI_SSID);

        verify(wifiMock).connectToConfiguredNetwork(wifiManagerMock, scannedResultMock);
    }

    private ScanResult getScanResult(final String networkSSID) {
        final int networkId = 10;
        final WifiConfiguration wifiConfigMock = mock(WifiConfiguration.class);
        wifiConfigMock.SSID = networkSSID;
        wifiConfigMock.networkId = networkId;
        when(wifiManagerMock.getConfiguredNetworks()).thenReturn(Collections.singletonList(wifiConfigMock));

        final ScanResult scannedResultMock = mock(ScanResult.class);
        scannedResultMock.SSID = networkSSID;

        return scannedResultMock;
    }

    protected void implementAsDirectExecutor(Handler handler) {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Exception {
                ((Runnable) invocation.getArguments()[0]).run();
                return null;
            }
        }).when(handler).postDelayed(any(Runnable.class), anyInt());
    }

}