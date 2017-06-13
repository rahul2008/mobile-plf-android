package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class HSDPInfoTest extends RegistrationApiInstrumentationBase {

    HSDPInfo hsdpInfo;

    @Before
    public void setUp() throws Exception {
        super.setUp();
            hsdpInfo = new HSDPInfo("sharedId", "secretId", "baseURL", "applicationName");
    }

    @Test
    public void testSetSharedId() throws Exception {
        assertEquals("sharedId",hsdpInfo.getSharedId());
    }

    @Test
    public void testSetSecreteId() throws Exception {
        assertEquals("secretId",hsdpInfo.getSecreteId());

    }

    @Test
    public void testSetBaseURL() throws Exception {
        assertEquals("baseURL",hsdpInfo.getBaseURL());

    }

    @Test
    public void testSetApplicationName() throws Exception {
        assertEquals("applicationName",hsdpInfo.getApplicationName());
    }
}