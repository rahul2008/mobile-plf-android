/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Response;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.communication.ExchangeKeyRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest({DICommLog.class})
@RunWith(PowerMockRunner.class)
public class ExchangeKeyRequestTest {
    @Mock
    NetworkNode networkNodeMock;

    @Mock
    ResponseHandler responseHandlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(DICommLog.class);
    }

    @Test
    public void whenLoggingImplicitly_thenDontForwardToLogger() {
        ExchangeKeyRequest exchangeKeyRequest = new ExchangeKeyRequest(networkNodeMock, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("don't care", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = exchangeKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        PowerMockito.verifyStatic(never());
        DICommLog.log(any(DICommLog.Verbosity.class), anyString(), anyString());
    }

    @Test
    public void whenLoggingExplicitly_thenDontForwardToLogger() {
        ExchangeKeyRequest exchangeKeyRequest = new ExchangeKeyRequest(networkNodeMock, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("don't care", null, mResponseHandler);
            }
        };

        exchangeKeyRequest.log(DICommLog.Verbosity.ERROR, "TEST", "This should not be logged!");

        PowerMockito.verifyStatic(never());
        DICommLog.log(any(DICommLog.Verbosity.class), anyString(), anyString());
    }
}
