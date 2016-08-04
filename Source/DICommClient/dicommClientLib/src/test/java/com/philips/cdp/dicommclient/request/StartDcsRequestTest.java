package com.philips.cdp.dicommclient.request;

import com.philips.cdp.dicommclient.cpp.CppController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
public class StartDcsRequestTest {

    @Mock
    CppController cppControllerMock;

    @Mock
    ResponseHandler responseHandlerMock;

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
                Thread.sleep(500L);
                Object lock = startDcsRequest.getLock();
                synchronized (lock) {
                    lock.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    @Test
    public void whenExecuteIsCalledThenDSCIsStarted() throws Exception {
        thread.start();
        startDcsRequest.execute();

        verify(cppControllerMock).startDCSService(anyObject());
    }

    @Test
    public void whenTheTreadWasNotifiedThenResponseMessageIsEmpty() throws Exception {
        thread.start();

        Response response = startDcsRequest.execute();

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
        startDcsRequest.setTimeOut(10L);

        when(cppControllerMock.getState()).thenReturn(CppController.ICP_CLIENT_DCS_STATE.STARTING);

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUESTFAILED, null);
    }


}