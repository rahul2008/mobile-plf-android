
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class DhpApiClientConfigurationTest {

    private DhpApiClientConfiguration mDhpApiClientConfiguration;

    @Before
    public void setUp() throws Exception {
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
