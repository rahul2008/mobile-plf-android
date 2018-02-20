/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.ews.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.philips.platform.ews.logger.EWSLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WiFiConnectivityManager.class, EWSLogger.class, WifiConfiguration.class})
public class WiFiConnectivityManagerTest {

    private static final String HOME_WIFI_SSID = "BrightEyes";
    private static final String APPLIANCE_SSID = WiFiUtil.DEVICE_SSID;

    private WiFiConnectivityManager subject;

    @Mock
    private WifiManager wifiManagerMock;

    @Mock
    private Wifi wifiMock;

    @Mock
    private Handler mockHandler;

    @Mock
    private WifiConfiguration mockWifiConfiguration;

    @Mock
    private EWSLogger mockEWSLogger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockStatic(EWSLogger.class);
        implementAsDirectExecutor(mockHandler);

        WiFiUtil wifiUtil = new WiFiUtil(wifiManagerMock, mockEWSLogger);
        subject = spy(new WiFiConnectivityManager(wifiManagerMock, wifiMock, wifiUtil, mockEWSLogger));
        subject.handler = mockHandler;

        stubHomeNetworkConnection();
    }

    private void stubHomeNetworkConnection() throws Exception {
        final WifiInfo wifiInfoMock = mock(WifiInfo.class);
        when(wifiManagerMock.getConnectionInfo()).thenReturn(wifiInfoMock);
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.COMPLETED);
        when(wifiInfoMock.getSSID()).thenReturn("WLAN-PUB");

        whenNew(WifiConfiguration.class).withAnyArguments().thenAnswer(new Answer<WifiConfiguration>() {
            @Override
            public WifiConfiguration answer(InvocationOnMock invocation) throws Throwable {
                mockWifiConfiguration.allowedAuthAlgorithms = PowerMockito.mock(BitSet.class);
                mockWifiConfiguration.allowedGroupCiphers = PowerMockito.mock(BitSet.class);
                mockWifiConfiguration.allowedKeyManagement = PowerMockito.mock(BitSet.class);
                mockWifiConfiguration.allowedPairwiseCiphers = PowerMockito.mock(BitSet.class);
                mockWifiConfiguration.allowedProtocols = PowerMockito.mock(BitSet.class);
                return mockWifiConfiguration;
            }
        });
    }

    @Test
    public void itShouldConfigureOpenNetworkWhenApplianceHotspotIsNotFound() throws Exception {
        subject.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

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
        mockWifiConfiguration(APPLIANCE_SSID);

        final ScanResult scanResultMock = mockScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(null).thenReturn(Collections.singletonList(scanResultMock));
        subject.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

        verify(wifiMock).connectToConfiguredNetwork(wifiManagerMock, scanResultMock);
    }

    @Test
    public void itShouldNotConnectToApplianceHotspotWhenNoMatchingScannedResultsFound() throws Exception {
        mockWifiConfiguration(APPLIANCE_SSID);

        final ScanResult scanResultMock = mockScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(null);

        verifyApplianceNotConnected(scanResultMock);
    }

    private void verifyApplianceNotConnected(final ScanResult scannedResultMock) {
        subject.setMaxScanAttempts(1);
        subject.connectToApplianceHotspotNetwork(APPLIANCE_SSID);

        verify(wifiMock, never()).connectToConfiguredNetwork(wifiManagerMock, scannedResultMock);
    }

    @Test
    public void itShouldNotConnectToApplianceHotspotWhenScannedResultsIsEmpty() throws Exception {
        mockWifiConfiguration(APPLIANCE_SSID);

        final ScanResult scanResultMock = mockScanResult(APPLIANCE_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(new ArrayList<ScanResult>());

        verifyApplianceNotConnected(scanResultMock);
    }

    @Test
    public void itShouldStopToFindNetwork() {
        subject.stopFindNetwork();

        verify(mockHandler).removeCallbacks((Runnable) any());
    }

    @Test
    public void connectToHomeWiFiNetworkWhenRequested() throws Exception {
        final ScanResult scanResultMock = mockScanResult(HOME_WIFI_SSID);
        when(wifiManagerMock.getScanResults()).thenReturn(Collections.singletonList(scanResultMock));

        subject.connectToHomeWiFiNetwork(HOME_WIFI_SSID);

        verify(wifiMock).connectToConfiguredNetwork(wifiManagerMock, scanResultMock);
    }

    private ScanResult mockScanResult(final String networkSSID) {
        final ScanResult scanResult = mock(ScanResult.class);
        scanResult.SSID = networkSSID;

        return scanResult;
    }

    private void mockWifiConfiguration(String networkSSID) {
        final int networkId = 10;

        final WifiConfiguration wifiConfigMock = mock(WifiConfiguration.class);
        wifiConfigMock.SSID = networkSSID;
        wifiConfigMock.networkId = networkId;
        when(wifiManagerMock.getConfiguredNetworks()).thenReturn(Collections.singletonList(wifiConfigMock));
    }

    private static void implementAsDirectExecutor(Handler handler) {
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Exception {
                ((Runnable) invocation.getArgument(0)).run();
                return null;
            }
        }).when(handler).postDelayed(any(Runnable.class), anyLong());
    }

}
