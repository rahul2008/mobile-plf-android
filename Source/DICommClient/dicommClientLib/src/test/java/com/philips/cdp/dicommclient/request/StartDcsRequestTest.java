/*
 * © Koninklijke Philips N.V., 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;


import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;

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

public class StartDcsRequestTest {

    private final long TIME_OUT = 10L;

    @Mock
    CloudController cloudControllerMock;

    @Mock
    ResponseHandler responseHandlerMock;

    @Captor
    ArgumentCaptor<DefaultCloudController.DCSStartListener> dcsStartedListenerCaptor;

    private StartDcsRequest startDcsRequest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        startDcsRequest = new StartDcsRequest(cloudControllerMock, responseHandlerMock);
    }

    @Test
    public void canInit() throws Exception {
        new StartDcsRequest(cloudControllerMock, responseHandlerMock);
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(TIME_OUT);

                verify(cloudControllerMock).startDCSService(dcsStartedListenerCaptor.capture());
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

        verify(cloudControllerMock).startDCSService(any(DefaultCloudController.DCSStartListener.class));
    }

    @Test
    public void whenTheTreadWasNotifiedThenResponseMessageIsEmpty() throws Exception {
        thread.start();

        Response response = startDcsRequest.execute();
        verify(cloudControllerMock).startDCSService(dcsStartedListenerCaptor.capture());

        assertEquals(null, response.getResponseMessage());
    }

    @Test
    public void whenTheTreadWasNotifiedAndDSCIsStartedThenNoErrorResponseIsReturned() throws Exception {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTED);
        thread.start();

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onSuccess(null);
    }

    @Test
    public void whenTheTreadWasNotifiedButDSCIsNotStartedThenNoErrorResponseIsReturned() throws Exception {
        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTING);
        thread.start();

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUESTFAILED, null);
    }

    @Test
    public void whenTimeOutOccursAndDSCIsNotStartedThenErrorResponseIsReported() throws Exception {
        startDcsRequest.setTimeOut(TIME_OUT);

        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTING);

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onError(Error.REQUESTFAILED, null);
    }

    @Test
    public void whenTimeOutOccursAndDSCIsStartedThenNoResponseIsReported() throws Exception {
        startDcsRequest.setTimeOut(TIME_OUT);

        when(cloudControllerMock.getState()).thenReturn(CloudController.ICPClientDCSState.STARTED);

        Response response = startDcsRequest.execute();
        response.notifyResponseHandler();

        verify(responseHandlerMock).onSuccess(null);
    }
}
