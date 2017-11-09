/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.request;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoteRequestTest {

    @Mock
    private CountDownLatch latchMock;
    @Mock
    private ResponseHandler handlerMock;
    @Mock
    private CloudController cloudControllerMock;

    private RemoteRequest subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DICommLog.disableLogging();
        //
        subject = new RemoteRequest("", "", 1, RemoteRequestType.GET_PROPS, null, handlerMock, cloudControllerMock) {
            @Override
            protected CountDownLatch createCountDownLatch() {
                return latchMock;
            }
        };
    }

    @Test
    public void givenRequestCreated_whenExecuted_andNoCallbacksReceived_thenShouldReturnResponse() throws Exception {
        Response result = this.subject.execute();
        assertNotNull(result);
    }

    @Test
    public void givenRequestCreated_whenExecuted_andNoCallbacksReceived_thenShouldReturnErrorResponse() throws Exception {
        Response response = this.subject.execute();
        Error result = response.getError();
        assertNotNull(result);
    }

    @Test
    public void givenRequestCreated_whenExecuted_andPublishCallbackReceived_thenShouldReturnResponse() throws Exception {
        final int messageId = 1337;
        final String conversationId = testUUID();
        when(cloudControllerMock.publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(messageId);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.onPublishEventReceived(RemoteRequest.SUCCESS, messageId, conversationId);
                return null;
            }
        }).when(latchMock).await(anyLong(), any(TimeUnit.class));

        Response response = this.subject.execute();
        assertNotNull(response);
    }

    @Test
    public void givenRequestCreated_whenExecuted_andPublishCallbackReceived_thenShouldReturnErrorResponse() throws Exception{
        final int messageId = 1337;
        final String conversationId = testUUID();
        when(cloudControllerMock.publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(messageId);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.onPublishEventReceived(RemoteRequest.SUCCESS, messageId, conversationId);
                return null;
            }
        }).when(latchMock).await(anyLong(), any(TimeUnit.class));


        Response response = this.subject.execute();
        Error result = response.getError();
        assertNotNull(result);
    }

    @Test
    public void givenRequestCreated_whenExecuted_andBothCallbacksReceived_thenShouldReturnResponse() throws Exception{
        final int messageId = 1337;
        final String conversationId = testUUID();
        final String dcsResponse = "{}";
        when(cloudControllerMock.publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(messageId);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.onPublishEventReceived(RemoteRequest.SUCCESS, messageId, conversationId);
                subject.onDCSResponseReceived(dcsResponse, conversationId);
                return null;
            }
        }).when(latchMock).await(anyLong(), any(TimeUnit.class));
        this.subject.onPublishEventReceived(RemoteRequest.SUCCESS, messageId, conversationId);

        Response response = this.subject.execute();
        assertNotNull(response);
    }

    @Test
    public void givenRequestCreated_whenExecuted_andBothCallbacksReceived_thenShouldReturnNoErrorResponse() throws Exception {
        final int messageId = 1337;
        final String conversationId = testUUID();
        final String dcsResponse = "{}";
        when(cloudControllerMock.publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(messageId);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.onPublishEventReceived(RemoteRequest.SUCCESS, messageId, conversationId);
                subject.onDCSResponseReceived(dcsResponse, conversationId);
                return null;
            }
        }).when(latchMock).await(anyLong(), any(TimeUnit.class));

        Response response = this.subject.execute();

        Error result = response.getError();
        assertNull(result);
    }

    private String testUUID() {
        return UUID.randomUUID().toString();
    }
}