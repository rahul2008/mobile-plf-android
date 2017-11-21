package com.philips.platform.appframework.connectivity.demouapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by Ritesh Jha on 11/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RefAppKpsConfigurationInfoTest {
    private RefAppKpsConfigurationInfo refAppKpsConfigurationInfo;

    @Before
    public void setUp() {
        refAppKpsConfigurationInfo = new RefAppKpsConfigurationInfo();
    }

    @Test
    public void getBootStrapId() {
        assertEquals("000000ffe0000003", refAppKpsConfigurationInfo.getBootStrapId());
    }

    @Test
    public void getBootStrapKey() {
        assertEquals("45240d84f206035f9f19856fd266e59b", refAppKpsConfigurationInfo.getBootStrapKey());
    }

    @Test
    public void getProductId() {
        assertEquals("FI-AIR_KPSPROV", refAppKpsConfigurationInfo.getProductId());
    }

    @Test
    public void getProductVersion() {
        assertEquals(0, refAppKpsConfigurationInfo.getProductVersion());
    }

    @Test
    public void getComponentId() {
        assertEquals("FI-AIR-AND", refAppKpsConfigurationInfo.getComponentId());
    }

    @Test
    public void getComponentCount() {
        assertEquals(0, refAppKpsConfigurationInfo.getComponentCount());

    }

    @Test
    public void getAppId() {
        assertEquals("com.philips.cdp.dicommclientsample", refAppKpsConfigurationInfo.getAppId());
    }

    @Test
    public void getAppVersion() {
        assertEquals(0, refAppKpsConfigurationInfo.getAppVersion());
    }

    @Test
    public void getAppType() {
        assertEquals("FI-AIR-AND-DEV", refAppKpsConfigurationInfo.getAppType());
    }

    @Test
    public void getCountryCode() {
        assertEquals("NL", refAppKpsConfigurationInfo.getCountryCode());
    }

    @Test
    public void getLanguageCode() {
        assertEquals("nl", refAppKpsConfigurationInfo.getLanguageCode());
    }

    @Test
    public void getDevicePortUrl() {
        assertEquals("https://www.uat.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx", refAppKpsConfigurationInfo.getDevicePortUrl());
    }
}