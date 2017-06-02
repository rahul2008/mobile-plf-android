/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetKeyRequestTest {
    @Mock
    ResponseHandler responseHandlerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();
    }

    @Test
    public void whenExecuting_ThenKeyWillReturnInResponse() throws Exception {
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
    public void whenExecutingWithIncorrectJson_ThenResponseWithError() throws Exception {
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
    public void whenExecutingWithJsonThatDoesntContainKey_ThenResponseWithError() throws Exception {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("{'bas':'peter'}", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUEST_FAILED, "Key missing in response");
    }

    @Test
    public void whenExecutingWithJsonThatContainEmptyKey_ThenResponseWithError() throws Exception {
        GetKeyRequest getKeyRequest = new GetKeyRequest("", 1, false, responseHandlerMock) {
            @Override
            Response doExecute() {
                return new Response("{'key':''}", null, mResponseHandler);
            }
        };

        Response getKeyRequestResponse = getKeyRequest.execute();
        getKeyRequestResponse.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUEST_FAILED, "Key missing in response");
    }
}