/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplianceSessionDetailsInfoTest {

    private ApplianceSessionDetailsInfo applianceSessionDetailsInfo;
    @Mock
    private WifiPortProperties wifiPortPropertiesMock;

    @Before
    public void setup() {
        applianceSessionDetailsInfo = new ApplianceSessionDetailsInfo();
        initMocks(this);
    }

    @Test
    public void shouldNotHaveSessionPropertiesOnCreation() {
        assertEquals(applianceSessionDetailsInfo.hasSessionProperties(), false);
    }

    @Test
    public void shouldHaveSessionPropertiesOnSettingWifiProperties() {
        applianceSessionDetailsInfo.setWifiPortProperties(wifiPortPropertiesMock);
        assertEquals(applianceSessionDetailsInfo.hasSessionProperties(), true);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnTryingToGetCppId() {
        applianceSessionDetailsInfo.getCppId();
    }

    @Test
    public void shouldFetchCppIdWhenPortPropertiesSet() {
        applianceSessionDetailsInfo.setWifiPortProperties(wifiPortPropertiesMock);
        when(wifiPortPropertiesMock.getCppid()).thenReturn("C++11");
        assertEquals(applianceSessionDetailsInfo.getCppId(), "C++11");
    }

    @Test
    public void shouldReturnWakeupLightDeviceName() {
        assertEquals(applianceSessionDetailsInfo.getDeviceName(), "Wakeup Light");
    }

}