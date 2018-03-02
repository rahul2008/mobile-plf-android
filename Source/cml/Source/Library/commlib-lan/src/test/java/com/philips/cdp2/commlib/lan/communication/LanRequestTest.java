/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import static com.philips.cdp.dicommclient.request.Error.TIMED_OUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanRequestTest {

    @Mock
    private NetworkNode networkNodeMock;
    @Mock
    private HttpURLConnection connectionMock;

    private LanRequest lanRequest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        when(networkNodeMock.getIpAddress()).thenReturn("ipaddress");
        when(networkNodeMock.getDICommProtocolVersion()).thenReturn(1);
        when(networkNodeMock.isHttps()).thenReturn(true);

        lanRequest = new LanRequest(networkNodeMock, null, "portname", 0, LanRequestType.GET, null, null, null) {
            @NonNull
            @Override
            HttpURLConnection createConnection(@NonNull URL url, @NonNull String requestMethod) throws IOException, TransportUnavailableException {
                return connectionMock;
            }
        };
    }

    @Test
    public void givenARequestIsCreated_whenNetworkNodeIsMarkedSecure_thenTheUrlProtocolShouldBeHttps() {
        assertEquals("https://ipaddress/di/v1/products/0/portname", lanRequest.url);
    }

    @Test
    public void givenARequestIsCreated_whenNetworkNodeIsNotMarkedSecure_thenTheUrlProtocolShouldBeHttp() {
        when(networkNodeMock.isHttps()).thenReturn(false);
        LanRequest lanRequest = new LanRequest(networkNodeMock, null, "portname", 0, LanRequestType.GET, null, null, null);

        assertEquals("http://ipaddress/di/v1/products/0/portname", lanRequest.url);
    }

    @Test
    public void givenARequestIsExecuted_whenSocketTimeoutOccurs_thenRequestCompletesWithATimeoutError() throws Exception {
        doThrow(SocketTimeoutException.class).when(connectionMock).connect();

        final Response response = lanRequest.execute();

        assertThat(response.getError()).isEqualTo(TIMED_OUT);
    }


}
