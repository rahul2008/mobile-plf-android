/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanRequestTest {

    @Mock
    NetworkNode networkNodeMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(networkNodeMock.getIpAddress()).thenReturn("ipaddress");
        when(networkNodeMock.getDICommProtocolVersion()).thenReturn(1);
    }

    @Test
    public void generatesCorrectUrlHttps() {
        when(networkNodeMock.isHttps()).thenReturn(true);
        LanRequest request = new LanRequest(networkNodeMock, null, "portname", 0, LanRequestType.GET, null, null, null);

        assertEquals("https://ipaddress/di/v1/products/0/portname", request.url);
    }

    @Test
    public void generatesCorrectUrlHttp() {
        when(networkNodeMock.isHttps()).thenReturn(false);
        LanRequest request = new LanRequest(networkNodeMock, null, "portname", 0, LanRequestType.GET, null, null, null);

        assertEquals("http://ipaddress/di/v1/products/0/portname", request.url);
    }
}
