package com.philips.platform.ews.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Build.VERSION.class)
public class WifiTest {

    @Mock
    WifiManager mockWifiManager;

    @Mock
    WifiConfiguration mockWifiConfiguration;

    @Mock
    ConfigurationSecurities mockConfigurationSecurities;

    @Mock
    ScanResult mockScanResult;

    List<WifiConfiguration> wifiConfigurationList = new ArrayList<WifiConfiguration>();

    String testString = "\"wifi\"";

    Wifi subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = spy(new Wifi());
        Whitebox.setInternalState(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.LOLLIPOP);
        subject.ConfigSec = mockConfigurationSecurities;
        mockWifiConfiguration.SSID = testString;
        mockWifiConfiguration.priority = 1000000;
        wifiConfigurationList.add(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenconvertToQuotedStringIsNull() throws Exception {
        assertEquals(subject.convertToQuotedString(null), "");
    }

    @Test
    public void itShouldVerifyWhenConvertToQuotedStringIsNotNull() throws Exception {
        assertEquals(subject.convertToQuotedString(testString), testString);  //"\"" + testString + "\""
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetwork() throws Exception {
        subject.connectToConfiguredNetwork(mockWifiManager, mockWifiConfiguration, true);
        verify(mockConfigurationSecurities).getWifiConfigurationSecurity(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithSecurity() throws Exception {
        when(mockWifiManager.getConfiguredNetworks()).thenReturn(wifiConfigurationList);
        when(mockConfigurationSecurities.getWifiConfigurationSecurity(mockWifiConfiguration)).thenReturn(testString);
        subject.connectToConfiguredNetwork(mockWifiManager, mockWifiConfiguration, true);
        verify(mockConfigurationSecurities, times(2)).getWifiConfigurationSecurity(mockWifiConfiguration);
    }


    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithEnableNetwork() throws Exception {
        when(mockWifiManager.enableNetwork(0, false)).thenReturn(true);
        subject.connectToConfiguredNetwork(mockWifiManager, mockWifiConfiguration, true);
        verify(mockConfigurationSecurities).getWifiConfigurationSecurity(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithSaveConfiguration() throws Exception {
        when(mockWifiManager.enableNetwork(0, false)).thenReturn(true);
        when(mockWifiManager.saveConfiguration()).thenReturn(true);
        subject.connectToConfiguredNetwork(mockWifiManager, mockWifiConfiguration, true);
        verify(mockConfigurationSecurities, times(2)).getWifiConfigurationSecurity(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkSaveConfigurationDisconnect() throws Exception {
        when(mockWifiManager.enableNetwork(0, false)).thenReturn(true);
        when(mockWifiManager.saveConfiguration()).thenReturn(true);
        doReturn(mockWifiConfiguration).when(subject).getWifiConfiguration(mockWifiManager, mockWifiConfiguration, null);
        when(mockWifiManager.disconnect()).thenReturn(true);
        subject.connectToConfiguredNetwork(mockWifiManager, mockWifiConfiguration, true);
        verify(mockConfigurationSecurities).getWifiConfigurationSecurity(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenGetWifiConfigurationWithNullSecurity() throws Exception {
        subject.getWifiConfiguration(mockWifiManager, mockWifiConfiguration, null);
        verify(mockConfigurationSecurities).getWifiConfigurationSecurity(mockWifiConfiguration);
    }

    @Test
    public void itShouldVerifyWhenGetWifiConfigurationWithSecurity() throws Exception {
        subject.getWifiConfiguration(mockWifiManager, mockWifiConfiguration, testString);
        verify(mockWifiManager).getConfiguredNetworks();
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResult() throws Exception {
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResultWithBSSIDNull() throws Exception {
        mockScanResult.SSID = testString;
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }


    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResultWithSecurityNull() throws Exception {
        mockScanResult.SSID = testString;
        mockScanResult.BSSID = testString;
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }


    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResultWithConfiguredNetwork() throws Exception {
        mockScanResult.SSID = testString;
        mockScanResult.BSSID = testString;
        when(mockConfigurationSecurities.getScanResultSecurity(mockScanResult)).thenReturn(testString);
        when(mockWifiManager.getConfiguredNetworks()).thenReturn(wifiConfigurationList);
        when(mockConfigurationSecurities.getWifiConfigurationSecurity(mockWifiConfiguration)).thenReturn(testString);
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }


    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResultWithSSIDChanged() throws Exception {
        mockScanResult.SSID = testString;
        mockScanResult.BSSID = testString;
        when(mockConfigurationSecurities.getScanResultSecurity(mockScanResult)).thenReturn(testString);
        when(mockWifiManager.getConfiguredNetworks()).thenReturn(wifiConfigurationList);
        mockWifiConfiguration.SSID = "wifi";
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }

    @Test
    public void itShouldVerifyWhenConnectToConfiguredNetworkWithScanResultWithConfiguredNetworkNull() throws Exception {
        mockScanResult.SSID = testString;
        mockScanResult.BSSID = testString;
        when(mockConfigurationSecurities.getScanResultSecurity(mockScanResult)).thenReturn(testString);
        when(mockWifiManager.getConfiguredNetworks()).thenReturn(null);
        subject.connectToConfiguredNetwork(mockWifiManager, mockScanResult);
        verify(mockWifiManager).startScan();
    }

}