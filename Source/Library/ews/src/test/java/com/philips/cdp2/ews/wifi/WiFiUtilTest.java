/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.wifi;

import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.philips.cdp2.ews.logger.EWSLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static com.philips.cdp2.ews.wifi.WiFiUtil.DEVICE_SSID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSLogger.class)
public class WiFiUtilTest {

    private static final String HOME_SSID = "BrightEyes2.4";
    private static final String NOT_HOME_SSID = "Random";
    private static final String UKNOWN_SSID = "<unknown ssid>";

    @Mock
    private WifiManager mockWifiManager;

    @Mock
    private android.net.wifi.WifiInfo mockWifiInfo;

    private WiFiUtil subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        when(mockWifiManager.getConnectionInfo()).thenReturn(mockWifiInfo);
        subject = new WiFiUtil(mockWifiManager);
    }

    @Test
    public void itShouldReturnHomeWiFiSSIDWhenWiFiIsConnected() throws Exception {
        final String HOME_SSID = "\"BrightEyes\"";//double quotes are from device.
        stubSSID(HOME_SSID);

        assertEquals("BrightEyes", subject.getCurrentWiFiSSID());
    }

    private void stubSSID(final String HOME_SSID) {
        when(mockWifiInfo.getSupplicantState()).thenReturn(SupplicantState.COMPLETED);
        when(mockWifiInfo.getSSID()).thenReturn(HOME_SSID);
    }

    private void stubSSIDWithEmptyState() {
        when(mockWifiInfo.getSupplicantState()).thenReturn(SupplicantState.INACTIVE);
        when(mockWifiInfo.getSSID()).thenReturn("");
    }

    @Test
    public void itShouldReturnNullWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(subject.getHomeWiFiSSD());
    }

    @Test
    public void itShouldCheckIfPhoneIsConnectedWithDeviceHotspot() throws Exception {
        stubSSID(DEVICE_SSID);

        subject.getCurrentWiFiSSID();

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.DEVICE_HOTSPOT_WIFI);
        assertTrue(subject.getCurrentWiFiSSID().equals(DEVICE_SSID));

    }

    @Test
    public void itShouldCheckIfPhoneIsConnectedBackToHomeWiFi() throws Exception {
        stubSSID(HOME_SSID);

        subject.getCurrentWiFiSSID();

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void itShouldReturnNullIfWiFiIsNotConnected() throws Exception {
        disconnectNetwork();

        assertNull(subject.getCurrentWiFiSSID());
        assertFalse(subject.getCurrentWifiState() == WiFiUtil.HOME_WIFI);
    }

    @Test
    public void itShouldReturnTrueIfWiFiIsEnabledAndNotConnectedToPhilipsHotspot() throws Exception {
        when(mockWifiManager.isWifiEnabled()).thenReturn(true);
        stubSSID(HOME_SSID);

        assertTrue(subject.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnTrueIfConnectedToADifferentNetwork() throws Exception {
        stubSSID(HOME_SSID);
        subject.getCurrentWiFiSSID();
        stubSSID(NOT_HOME_SSID);

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void itShouldReturnFalseWhenIsConnectedToWrongHomeWifiAndNotConnected() {
        assertFalse(subject.getCurrentWifiState() == WiFiUtil.WRONG_WIFI);
    }

    @Test
    public void itShouldReturnWrongWifiWhenWifiStateIsUnknown() throws Exception {
        stubSSID(UKNOWN_SSID);

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void itShouldReturnWrongWifiWhenWifiWifiStateIsUnknownAndHomeWifiSet() throws Exception {
        stubSSID(HOME_SSID);
        subject.getCurrentWiFiSSID();
        stubSSID(UKNOWN_SSID);

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void itShouldReturnWUNKNOWN_WIFIWhenWifiWifiStateIsDeviceSSIDFirstAndNotHomeSSIDSet() throws Exception {
        stubSSID(DEVICE_SSID);
        subject.getCurrentWiFiSSID();
        stubSSID(NOT_HOME_SSID);

        assertTrue(subject.getCurrentWifiState() == WiFiUtil.UNKNOWN_WIFI);
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsEnabledAndConnectedToPhilipsHotspot() throws Exception {
        when(mockWifiManager.isWifiEnabled()).thenReturn(true);
        stubSSID(DEVICE_SSID);
        subject.getCurrentWiFiSSID();


        assertFalse(subject.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsDisabled() throws Exception {
        when(mockWifiManager.isWifiEnabled()).thenReturn(false);
        assertFalse(subject.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnFalseIfWiFiIsEnabledAndWifiSSIDEmpty() throws Exception {
        when(mockWifiManager.isWifiEnabled()).thenReturn(true);
        stubSSIDWithEmptyState();
        subject.getCurrentWiFiSSID();

        assertFalse(subject.isHomeWiFiEnabled());
    }

    @Test
    public void itShouldReturnTrueIfDeviceConnectedToPhilipsAppliance() throws Exception{
        stubSSID(DEVICE_SSID);
        assertTrue(subject.isConnectedToPhilipsSetup());
    }
    @Test
    public void itShouldReturnFalseIfDeviceIsNotConnectedToPhilipsAppliance() throws Exception{
        stubSSID(HOME_SSID);
        assertFalse(subject.isConnectedToPhilipsSetup());
    }

    @Test
    public void itShouldForgetGivenSSID() throws Exception{
        List<WifiConfiguration> configs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            WifiConfiguration testWifiConfiguration= new WifiConfiguration();
            testWifiConfiguration.SSID = HOME_SSID + "Test"+i;
            configs.add(testWifiConfiguration);
        }
        when(mockWifiManager.getConfiguredNetworks()).thenReturn(configs);
        subject.forgetHotSpotNetwork(HOME_SSID + "Test2");
        verify(mockWifiManager).removeNetwork(anyInt());
    }

    private void disconnectNetwork() {
        when(mockWifiInfo.getSupplicantState()).thenReturn(SupplicantState.DISCONNECTED);
    }
}