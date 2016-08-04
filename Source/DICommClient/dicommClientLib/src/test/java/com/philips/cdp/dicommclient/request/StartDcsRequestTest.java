package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.cpp.CppController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
public class StartDcsRequestTest {

    public static final long TIME_OUT = 10L;

    @Mock
    CppController cppControllerMock;

    @Mock
    ResponseHandler responseHandlerMock;

    @Captor
    ArgumentCaptor<CppController.DCSStartListener> dcsStartedListenerCaptor;

    private StartDcsRequest startDcsRequest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        startDcsRequest = new StartDcsRequest(cppControllerMock, responseHandlerMock);
    }

    @Test
    public void canInit() throws Exception {
        new StartDcsRequest(cppControllerMock, responseHandlerMock);
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(TIME_OUT);

                verify(cppControllerMock).startDCSService(dcsStartedListenerCaptor.capture());
                dcsStartedListenerCaptor.getValue().onResponseReceived();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    @Test
    public void whenExecuteIsCalledThenDSCIsStarted() throws Exception {
        thread.start();
        startDcsRequest.execute();

        verify(cppControllerMock).startDCSService(any(CppController.DCSStartListener.class));
    }

    @Test
    public void whenTheTreadWasNotifiedThenResponseMessageIsEmpty() throws Exception {
        thread.start();

        Response response = startDcsRequest.execute();
        verify(cppControllerMock).startDCSService(dcsStartedListenerCaptor.capture());

        assertEquals(null, response.getResponseMessage());
    }

    @Test
    public void whenTheTreadWasNotifiedAndDSCIsStartedThenNoErrorResponseIsReturned() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTED);
        thread.start();

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onSuccess(null);
    }

    @Test
    public void whenTheTreadWasNotifiedButDSCIsNotStartedThenNoErrorResponseIsReturned() throws Exception {
        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTING);
        thread.start();

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUESTFAILED, null);
    }

    @Test
    public void whenTimeOutOccursAndDSCIsNotStartedThenErrorResponseIsReported() throws Exception {
        startDcsRequest.setTimeOut(TIME_OUT);

        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTING);

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUESTFAILED, null);
    }

    @Test
    public void whenTimeOutOccursAndDSCIsStartedThenNoResponseIsReported() throws Exception {
        startDcsRequest.setTimeOut(TIME_OUT);

        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTED);

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onSuccess(null);
    }
}