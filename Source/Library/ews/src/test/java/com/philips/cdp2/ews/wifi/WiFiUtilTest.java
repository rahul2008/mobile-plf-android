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
    public void itShouldReturnHomeWiFiSSIDWhenWiFiIsConnected() throws Exception {
        final String HOME_SSID = "\"BrightEyes\"";//double quotes are from device.
        stubSSID(HOME_SSID);

        assertEquals("BrightEyes", wiFiUtil.getCurrentWiFiSSID());
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
    public void itShouldReturnNullWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(wiFiUtil.getHomeWiFiSSD());
    }

    @Test
    public void itShouldCheckIfPhoneIsConnectedWithDeviceHotspot() throws Exception {
        stubSSID(WiFiUtil.DEVICE_SSID);

        wiFiUtil.getCurrentWiFiSSID();

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.DEVICE_HOTSPOT_WIFI);
        assertTrue(wiFiUtil.getCurrentWiFiSSID().equals(WiFiUtil.DEVICE_SSID));

    }

    @Test
    public void itShouldCheckIfPhoneIsConnectedBackToHomeWiFi() throws Exception {
        stubSSID(HOME_SSID);

        wiFiUtil.getCurrentWiFiSSID();

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void itShouldReturnNullIfWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(wiFiUtil.getCurrentWiFiSSID());
        assertFalse(wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void itShouldReturnTrueIfWiFiIsEnabledAndNotConnectedToPhilipsHotspot() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSID(HOME_SSID);

        assertTrue(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnTrueIfConnectedToADifferentNetwork() throws Exception {
        stubSSID(HOME_SSID);
        wiFiUtil.getCurrentWiFiSSID();
        stubSSID(NOT_HOME_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void itShouldReturnFalseWhenIsConnectedToWrongHomeWifiAndNotConnected() {
        assertFalse(wiFiUtil.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void itShouldReturnWrongWifiWhenWifiStateIsUnknown() throws Exception {
        stubSSID(UKNOWN_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void itShouldReturnWrongWifiWhenWifiWifiStateIsUnknownAndHomeWifiSet() throws Exception {
        stubSSID(HOME_SSID);
        wiFiUtil.getCurrentWiFiSSID();
        stubSSID(UKNOWN_SSID);

        assertTrue(wiFiUtil.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsEnabledAndConnectedToPhilipsHotspot() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSID(WiFiUtil.DEVICE_SSID);
        wiFiUtil.getCurrentWiFiSSID();


        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsDisabled() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(false);
        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsEnabledAndWifiSSIDEmpty() throws Exception {
        when(wifiManagerMock.isWifiEnabled()).thenReturn(true);
        stubSSIDWithEmptyState();
        wiFiUtil.getCurrentWiFiSSID();

        assertFalse(wiFiUtil.isHomeWiFiEnabled());
    }

    private void disconnectNetwork() {
        when(wifiInfoMock.getSupplicantState()).thenReturn(SupplicantState.DISCONNECTED);
    }
}