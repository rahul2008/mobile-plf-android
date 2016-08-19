package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class DhpApiClientConfigurationTest extends InstrumentationTestCase{

    DhpApiClientConfiguration mDhpApiClientConfiguration;

    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        mDhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey","signingSecret");
        assertNotNull(mDhpApiClientConfiguration);
    }

    @Test
    public void testGetApiBaseUrl() throws Exception {
        assertEquals("apiBaseUrl",mDhpApiClientConfiguration.getApiBaseUrl());

    }

    @Test
    public void testGetDhpApplicationName() throws Exception {
        assertEquals("dhpApplicationName",mDhpApiClientConfiguration.getDhpApplicationName());

    }

    @Test
    public void testGetSigningKey() throws Exception {
        assertEquals("signingKey",mDhpApiClientConfiguration.getSigningKey());

    }

    @Test
    public void testGetSigningSecret() throws Exception {
        assertEquals("signingSecret",mDhpApiClientConfiguration.getSigningSecret());

    }
}