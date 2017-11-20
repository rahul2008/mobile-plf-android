/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplianceSessionDetailsInfoTest {

    private ApplianceSessionDetailsInfo applianceSessionDetailsInfo;
    @Mock
    private WifiPortProperties wifiPortPropertiesMock;

    @Before
    public void setup() {
        initMocks(this);
        applianceSessionDetailsInfo = new ApplianceSessionDetailsInfo();
    }

    @Test
    public void itShouldNotHaveSessionPropertiesOnCreation() {
        assertEquals(applianceSessionDetailsInfo.hasSessionProperties(), false);
    }

    @Test
    public void itShouldHaveSessionPropertiesOnSettingWifiProperties() {
        applianceSessionDetailsInfo.setWifiPortProperties(wifiPortPropertiesMock);
        assertEquals(applianceSessionDetailsInfo.hasSessionProperties(), true);
    }

    @Test
    public void itShouldWifiPropertiesWhileGet() {
        applianceSessionDetailsInfo.setWifiPortProperties(wifiPortPropertiesMock);
        assertNotNull(applianceSessionDetailsInfo.getWifiPortProperties());
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowExceptionOnTryingToGetCppId() {
        applianceSessionDetailsInfo.getCppId();
    }

    @Test
    public void itShouldFetchCppIdWhenPortPropertiesSet() {
        applianceSessionDetailsInfo.setWifiPortProperties(wifiPortPropertiesMock);
        when(wifiPortPropertiesMock.getCppid()).thenReturn("C++11");
        assertEquals(applianceSessionDetailsInfo.getCppId(), "C++11");
    }

}