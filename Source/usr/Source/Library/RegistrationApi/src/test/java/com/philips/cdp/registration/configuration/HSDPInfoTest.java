package com.philips.cdp.registration.configuration;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HSDPInfoTest extends TestCase {

    private HSDPInfo hsdpInfo;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        hsdpInfo = new HSDPInfo("sharedId", "secretId", "baseURL", "applicationName");
    }

    @Test
    public void testSetSharedId() {
        assertEquals("sharedId",hsdpInfo.getSharedId());
    }

    @Test
    public void testSetSecreteId() {
        assertEquals("secretId",hsdpInfo.getSecreteId());

    }

    @Test
    public void testSetBaseURL() {
        assertEquals("baseURL",hsdpInfo.getBaseURL());

    }

    @Test
    public void testSetApplicationName() {
        assertEquals("applicationName",hsdpInfo.getApplicationName());
    }
}