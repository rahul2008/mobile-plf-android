/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest({DICommLog.class})
@RunWith(PowerMockRunner.class)
public class GetKeyRequestTest {

    @Mock
    ResponseHandler responseHandlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(DICommLog.class);
    }

    @Test
    public void whenExecuting_ThenKeyWillReturnInResponse() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, null) {
            @Override
            Response doExecute() {
                return new Response("{'key':'test key'}", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();

        assertEquals("test key", getKeyRequestResponse.getResponseMessage());
    }

    @Test
    public void whenExecutingWithIncorrectJson_ThenResponseWithError() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("This is not json", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(eq(Error.REQUEST_FAILED), anyString());
    }

    @Test
    public void whenExecutingWithJsonThatDoesntContainKey_ThenResponseWithError() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("{'bas':'peter'}", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUEST_FAILED, GetKeyRequest.KEY_MISSING_IN_RESPONSE_MESSAGE);
    }

    @Test
    public void whenExecutingWithJsonThatContainEmptyKey_ThenResponseWithError() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("{'key':''}", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUEST_FAILED, GetKeyRequest.KEY_MISSING_IN_RESPONSE_MESSAGE);
    }

    @Test
    public void whenRequestFailed_andResponseMessageIsNull_thenErrorIsReported() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("don't care", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response(null, Error.REQUEST_FAILED, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUEST_FAILED, null);
    }

    @Test
    public void whenRequestFailed_andResponseMessageIsNotNull_thenErrorIsReportedWithThatResponseMessage() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("don't care", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("tha message", Error.REQUEST_FAILED, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(eq(Error.REQUEST_FAILED), eq("tha message"));
    }

    @Test
    public void whenLoggingImplicitly_thenDontForwardToLogger() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("don't care", 42, true, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("don't care", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        PowerMockito.verifyStatic(never());
        DICommLog.log(any(DICommLog.Verbosity.class), anyString(), anyString());
    }

    @Test
    public void whenLoggingExplicitly_thenDontForwardToLogger() {
        GetKeyRequest getKeyRequest = new GetKeyRequest("don't care", 42, true, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("don't care", null, mResponseHandler);
            }
        };

        getKeyRequest.log(DICommLog.Verbosity.ERROR, "TEST", "This should not be logged!");

        PowerMockito.verifyStatic(never());
        DICommLog.log(any(DICommLog.Verbosity.class), anyString(), anyString());
    }
}
