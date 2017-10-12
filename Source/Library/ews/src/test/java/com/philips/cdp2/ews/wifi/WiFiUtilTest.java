package com.philips.cdp2.ews.wifi;

import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;

import com.philips.cdp2.ews.logger.EWSLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSLogger.class)
public class WiFiUtilTest {

    private static final String HOME_SSID = "BrightEyes2.4";
    private static final String NOT_HOME_SSID = "Random";
    private static final String UKNOWN_SSID = "<unknown ssid>";

    @Mock
    private WifiManager wifiManagerMock;

    @Mock
    private android.net.wifi.WifiInfo wifiInfoMock;

    private WiFiUtil wiFiUtil;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        when(wifiManagerMock.getConnectionInfo()).thenReturn(wifiInfoMock);
        wiFiUtil = new WiFiUtil(wifiManagerMock);
    }

    @Test
    public void shouldReturnHomeWiFiSSIDWhenWiFiIsConnected() throws Exception {
        final String HOME_SSID = "\"BrightEyes\"";//double quotes are from device.
        stubSSID(HOME_SSID);

        assertEquals("BrightEyes", wiFiUtil.getCurrentHomeWiFiSSID());
    }

    private void stubSSID(final String HOME_SSID) {
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.COMPLETED);
        when(wifiInfoMock.getSSID()).thenReturn(HOME_SSID);
    }

    private void stubSSIDWithEmptyState() {
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.INACTIVE);
        when(wifiInfoMock.getSSID()).thenReturn("");
    }

    @Test
    public void shouldReturnNullWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(wiFiUtil.getHomeWiFiSSD());
    }

    @Test
    public void shouldCheckIfPhoneIsConnectedWithDeviceHotspot() throws Exception {
        stubSSID(WiFiUtil.DEVICE_SSID);

        wiFiUtil.getCurrentHomeWiFiSSID();

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.DEVICE_HOTSPOT_WIFI);
        assertTrue(wiFiUtil.getCurrentHomeWiFiSSID().equals(WiFiUtil.DEVICE_SSID));

    }

    @Test
    public void shouldCheckIfPhoneIsConnectedBackToHomeWiFi() throws Exception {
        stubSSID(HOME_SSID);

        wiFiUtil.getCurrentHomeWiFiSSID();

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void shouldReturnNullIfWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(wiFiUtil.getCurrentHomeWiFiSSID());
        assertFalse(wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void ShouldReturnTrueIfWiFiIsEnabledAndNotConnectedToPhilipsHotspot() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSID(HOME_SSID);

        assertTrue(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void shouldReturnTrueIfConnectedToADifferentNetwork() throws Exception {
        stubSSID(HOME_SSID);
        wiFiUtil.getCurrentHomeWiFiSSID();
        stubSSID(NOT_HOME_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void shouldReturnFalseWhenIsConnectedToWrongHomeWifiAndNotConnected() {
        assertFalse(wiFiUtil.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void shouldReturnWrongWifiWhenWifiStateIsUnknown() throws Exception {
        stubSSID(UKNOWN_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void shouldReturnWrongWifiWhenWifiWifiStateIsUnknownAndHomeWifiSet() throws Exception {
        stubSSID(HOME_SSID);
        wiFiUtil.getCurrentHomeWiFiSSID();
        stubSSID(UKNOWN_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void ShouldReturnFalseIfWiFiIsEnabledAndConnectedToPhilipsHotspot() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSID(WiFiUtil.DEVICE_SSID);
        wiFiUtil.getCurrentHomeWiFiSSID();


        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void ShouldReturnFalseIfWiFiIsDisabled() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(false);
        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void ShouldReturnFalseIfWiFiIsEnabledAndWifiSSIDEmpty() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSIDWithEmptyState();
        wiFiUtil.getCurrentHomeWiFiSSID();

        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    private void disconnectNetwork() {
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.DISCONNECTED);
    }
}