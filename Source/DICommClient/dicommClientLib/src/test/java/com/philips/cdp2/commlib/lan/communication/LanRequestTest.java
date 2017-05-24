/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LanRequestTest {

    private static final boolean HTTPS = true;
    private static final boolean HTTP = false;
    private LanRequest requestUnderTest;

    @Before
    public void setUp() throws Exception {
        requestUnderTest = new LanRequest("ipaddress", 1, HTTPS, "portname", 0, LanRequestType.GET, null, null, null);
    }

    @Test
    public void generatesCorrectUrlHttps() {
        requestUnderTest = new LanRequest("ipaddress", 1, HTTPS, "portname", 0, LanRequestType.GET, null, null, null);

        assertEquals("https://ipaddress/di/v1/products/0/portname", requestUnderTest.mUrl);
    }

    @Test
    public void generatesCorrectUrlHttp() {
        requestUnderTest = new LanRequest("ipaddress", 1, HTTP, "portname", 0, LanRequestType.GET, null, null, null);

        assertEquals("http://ipaddress/di/v1/products/0/portname", requestUnderTest.mUrl);
    }
}