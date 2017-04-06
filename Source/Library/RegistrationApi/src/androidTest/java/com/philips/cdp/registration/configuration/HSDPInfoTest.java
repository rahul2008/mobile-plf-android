package com.philips.cdp.registration.configuration;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class HSDPInfoTest extends InstrumentationTestCase {

    HSDPInfo hsdpInfo;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
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