package com.philips.cdp2.ews.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(String.class)
public class ConfigurationSecuritiesTest {

    private ConfigurationSecurities subject;

    @Mock
    private WifiConfiguration mockWifiConfiguration;

    @Mock
    private BitSet mockBitSet;

    @Mock
    private ScanResult mockScanResult;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new ConfigurationSecurities();
        mockWifiConfiguration.allowedKeyManagement = mockBitSet;
    }

    @Test
    public void itShouldVerifyGetWifiConfigurationSecurityForPSKSecurity() throws Exception{

        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)).thenReturn(true);
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_PSK), subject.getWifiConfigurationSecurity(mockWifiConfiguration));
    }

    @Test
    public void itShouldVerifyGetWifiConfigurationSecurityForEAPSecurity() throws Exception{
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)).thenReturn(true);
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_EAP), subject.getWifiConfigurationSecurity(mockWifiConfiguration));
    }

    @Test
    public void itShouldVerifyGetWifiConfigurationSecurityForEAPSecurityForIEEE() throws Exception{
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)).thenReturn(true);
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_EAP), subject.getWifiConfigurationSecurity(mockWifiConfiguration));
    }

    @Test
    public void itShouldVerifyGetWifiConfigurationSecurityForWEPKeysReturnSecurityNone() throws Exception{
        mockWifiConfiguration.wepKeys = new String[1];
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)).thenReturn(false);
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)).thenReturn(false);
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_NONE), subject.getWifiConfigurationSecurity(mockWifiConfiguration));
    }

    @Test
    public void itShouldVerifyGetWifiConfigurationSecurityForWEPKeysReturnSecurityWEP() throws Exception{
        mockWifiConfiguration.wepKeys = new String[] {"securityKey",""};
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP)).thenReturn(false);
        when(mockWifiConfiguration.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)).thenReturn(false);
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_WEP), subject.getWifiConfigurationSecurity(mockWifiConfiguration));
    }

    @Test
    public void itShouldVerifyScanResultSecurityForWEP() throws Exception{
        mockScanResult.capabilities = "WEP";
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_WEP), subject.getScanResultSecurity(mockScanResult));
    }

    @Test
    public void itShouldVerifyScanResultSecurityForPSK() throws Exception{
        mockScanResult.capabilities = "PSK";
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_PSK), subject.getScanResultSecurity(mockScanResult));
    }

    @Test
    public void itShouldVerifyScanResultSecurityForEAP() throws Exception{
        mockScanResult.capabilities = "EAP";
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_EAP), subject.getScanResultSecurity(mockScanResult));
    }

    @Test
    public void itShouldVerifyScanResultSecurityForNone() throws Exception{
        mockScanResult.capabilities = "NONE";
        assertEquals(String.valueOf(ConfigurationSecurities.SECURITY_NONE), subject.getScanResultSecurity(mockScanResult));
    }

}