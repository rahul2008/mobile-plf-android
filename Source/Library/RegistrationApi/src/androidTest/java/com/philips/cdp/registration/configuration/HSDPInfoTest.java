package com.philips.cdp.registration.configuration;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class HSDPInfoTest extends InstrumentationTestCase {

    HSDPInfo hsdpInfo;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        hsdpInfo = new HSDPInfo();
    }

    @Test
    public void testSetSharedId() throws Exception {
        hsdpInfo.setSharedId("sharedid");
        assertEquals("sharedid",hsdpInfo.getSharedId());
    }

    @Test
    public void testSetSecreteId() throws Exception {
        hsdpInfo.setSecreteId("sharedid");
        assertEquals("sharedid",hsdpInfo.getSecreteId());

    }

    @Test
    public void testSetBaseURL() throws Exception {
        hsdpInfo.setBaseURL("sharedid");
        assertEquals("sharedid",hsdpInfo.getBaseURL());

    }

    @Test
    public void testSetApplicationName() throws Exception {
        hsdpInfo.setApplicationName("sharedid");
        assertEquals("sharedid",hsdpInfo.getApplicationName());
    }
}