/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDevice.SHNDeviceListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.StreamData;
import com.philips.pins.shinelib.capabilities.StreamIdentifier;
import com.philips.pins.shinelib.dicommsupport.DiCommByteStreamReader;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;
import com.philips.pins.shinelib.dicommsupport.MessageType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp.dicommclient.request.Error.NOT_AVAILABLE;
import static com.philips.cdp.dicommclient.request.Error.NOT_UNDERSTOOD;
import static com.philips.cdp.dicommclient.request.Error.NO_REQUEST_DATA;
import static com.philips.cdp.dicommclient.request.Error.PROTOCOL_VIOLATION;
import static com.philips.cdp.dicommclient.request.Error.TIMED_OUT;
import static com.philips.cdp.dicommclient.request.Error.UNKNOWN;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static com.philips.pins.shinelib.dicommsupport.StatusCode.NoError;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleRequestTest {
    private static final String PORT_NAME = "PoliticallyCorrectPiet";
    private static final int PRODUCT_ID = 1337;
    private static final String MOCK_BLUETOOTH_DEVICE_ADDRESS = "11:22:33:44:55:66";

    private BleRequest request;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private DiCommResponse mockDicommResponse;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private CapabilityDiComm mockCapability;

    @Mock
    private Timer timerMock;

    @Mock
    private CountDownLatch mockInProgressLatch;

    @Mock
    private Handler handlerMock;

    @Mock
    private DiCommByteStreamReader mockByteStreamReader;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<ResultListener<StreamData>> dataListenerCaptor;

    private int executeCounter;

    private SHNDeviceListener stateListener;

    @Before
    public void setUp() throws Exception {
        executeCounter = 0;
        initMocks(this);
        DICommLog.disableLogging();
        when(mockDevice.getCapabilityForType(SHNCapabilityType.DI_COMM)).thenReturn(mockCapability);
        when(mockDevice.getState()).thenReturn(Connected);
        when(mockDevice.getAddress()).thenReturn(MOCK_BLUETOOTH_DEVICE_ADDRESS);

        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                request.processDiCommResponse(mockDicommResponse);
                return null;
            }
        }).when(mockInProgressLatch).await();

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                stateListener = (SHNDeviceListener) invocation.getArguments()[0];
                return null;
            }
        }).when(mockDevice).registerSHNDeviceListener(any(SHNDeviceListener.class));

        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");

        doNothing().when(mockCapability).addDataListener(dataListenerCaptor.capture());

        request = new BleRequest(mockDevice, PORT_NAME, PRODUCT_ID, responseHandlerMock, handlerMock, new AtomicBoolean(true)) {
            @Override
            protected void execute(final CapabilityDiComm capability) {
                executeCounter++;
            }

            @NonNull
            @Override
            protected Timer createTimer() {
                return timerMock;
            }
        };

        request.inProgressLatch = mockInProgressLatch;
        request.diCommByteStreamReader = mockByteStreamReader;

        when(handlerMock.post(runnableCaptor.capture())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                runnableCaptor.getValue().run();
                return null;
            }
        });
    }

    @Test
    public void whenRequestIsCancelledAfterSuccessThenNoErrorIsReported() {
        request.run();

        request.cancel("timeout");

        verify(responseHandlerMock, times(0)).onError(any(Error.class), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenErrorIsReported() {

        request.cancel("timeout");

        verify(responseHandlerMock).onError(eq(TIMED_OUT), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenRequestIsNeverExecuted() {
        request.cancel("timeout");

        request.run();

        verify(responseHandlerMock, times(0)).onSuccess(anyString());
    }

    @Test
    public void callsOnSuccessOnHandlerAndDisconnects() {
        request.run();

        verify(responseHandlerMock).onSuccess(anyString());
        verify(mockDevice).disconnect();
    }

    @Test
    public void doesntCallDisconnectWhenStayingConnected() {
        request = new BleGetRequest(mockDevice, PORT_NAME, PRODUCT_ID, responseHandlerMock, handlerMock, new AtomicBoolean(false));
        request.inProgressLatch = mockInProgressLatch;

        request.run();

        verify(responseHandlerMock).onSuccess(anyString());
        verify(mockDevice, times(0)).disconnect();
    }

    @Test
    public void callsConnectWhenNotConnected() {
        when(mockDevice.getState()).thenReturn(Disconnected);

        request.run();

        verify(mockDevice).connect(eq(30000L));
    }

    @Test
    public void unregistersListenerAfterDisconnected() throws InterruptedException {
        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                request.processDiCommResponse(mockDicommResponse);
                when(mockDevice.getState()).thenReturn(Disconnected);
                stateListener.onStateUpdated(mockDevice);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(mockDevice).unregisterSHNDeviceListener(request.bleDeviceListener);
    }

    @Test
    public void givenRequestIsExecutingWhenDeviceDisconnectsThenOnErrorIsReported() throws InterruptedException {
        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                when(mockDevice.getState()).thenReturn(Disconnected);
                stateListener.onStateUpdated(mockDevice);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(Error.REQUEST_FAILED), anyString());
    }

    @Test
    public void givenRequestIsWaitingForConnectedState_whenConnectedStateIsReportedTwice_thenRequestIsOnlyExecutedOnce() throws Exception {
        when(mockDevice.getState()).thenReturn(Disconnected);

        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) {
                when(mockDevice.getState()).thenReturn(Connected);
                stateListener.onStateUpdated(mockDevice);
                stateListener.onStateUpdated(mockDevice);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        assertThat(executeCounter).isEqualTo(1);
    }

    @Test
    public void givenRequestIsRunning_whenItCompletes_thenTimeoutTimerIsStopped() {
        request.run();

        //completion is arranged by setup

        verify(timerMock).cancel();
    }

    @Test
    public void whenRequestIsRun_thenTimeoutTimerIsStarted() {

        request.run();

        verify(timerMock).schedule(isA(TimerTask.class), anyLong());
    }

    @Test
    public void givenRequestIsRunning_whenTimoutTimerExpires_thenRequestIsCancelledAndErrorIsReported() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                ArgumentCaptor<TimerTask> timerTaskCaptor = ArgumentCaptor.forClass(TimerTask.class);
                verify(timerMock).schedule(timerTaskCaptor.capture(), anyLong());
                timerTaskCaptor.getValue().run();
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(TIMED_OUT), anyString());
    }

    @Test
    public void givenRequestIsRunning_whenConnectingFails_thenRequestReportsAnError() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                request.bleDeviceListener.onFailedToConnect(mockDevice, SHNResult.SHNErrorBondLost);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(NOT_AVAILABLE), anyString());
    }

    @Test
    public void givenRequestIsRunning_whenAChunkOfDataIsReceived_thenItWillBePassedToTheByteStreamReader() throws Exception {
        byte[] rawData = "This is Test Data!".getBytes();
        final StreamData data = new StreamData(rawData, StreamIdentifier.STREAM_1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                dataListenerCaptor.getValue().onActionCompleted(data, SHNResult.SHNOk);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(mockByteStreamReader).onBytes(rawData);
    }

    @Test
    public void givenRequestIsRunning_whenTheStreamingLayerFails_thenRequestReportsAnError() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                dataListenerCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(NOT_UNDERSTOOD), anyString());
    }

    @Test
    public void givenRequestIsRunning_whenTheByteStreamReaderFails_thenRequestReportsAnError() throws InterruptedException {
        final String errorMsg = "Housten, we have a problem!";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                request.dicommMessageListener.onError(errorMsg);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();

        verify(responseHandlerMock).onError(eq(NO_REQUEST_DATA), eq(errorMsg));
    }

    @Test
    public void givenRequestIsRunning_whenADICommMessageIsReceivedThatIsNotAResponse_thenRequestReportsAnError() throws InterruptedException {
        DiCommMessage message = mock(DiCommMessage.class);
        when(message.getMessageType()).thenReturn(MessageType.GetPortsRequest);

        simulateRunWithDiCommMessage(message);

        verify(responseHandlerMock).onError(eq(PROTOCOL_VIOLATION), anyString());
    }

    @Test
    public void givenRequestIsRunning_whenADICommMessageIsReceivedThatHasAnInvalidStatusCode_thenRequestReportsAnError() throws InterruptedException {
        byte invalidStatusCode = 0x10;

        DiCommMessage message = mock(DiCommMessage.class);
        when(message.getMessageType()).thenReturn(MessageType.GenericResponse);
        when(message.getPayload()).thenReturn(new byte[] {invalidStatusCode, 0x00});

        simulateRunWithDiCommMessage(message);

        verify(responseHandlerMock).onError(eq(UNKNOWN), anyString());
    }

    @Test
    public void givenRequestIsRunning_whenASuccessfulDICommMessageIsReceived_thenRequestReportsSuccess() throws InterruptedException {
        String validJsonString = "{\"A valid key\" : \"A valid value\"}";
        DiCommMessage message = createSuccessfulResponseFromJson(validJsonString);

        simulateRunWithDiCommMessage(message);

        verify(responseHandlerMock).onSuccess(validJsonString);
    }

    @Test
    public void givenRequestIsCompleted_whenTimeoutOccurs_thenRequestFinishes() throws InterruptedException {
        String validJsonString = "{\"A valid key\" : \"A valid value\"}";
        DiCommMessage message = createSuccessfulResponseFromJson(validJsonString);

        simulateRunWithDiCommMessage(message);

        ArgumentCaptor<TimerTask> timerTaskCaptor = ArgumentCaptor.forClass(TimerTask.class);
        verify(timerMock).schedule(timerTaskCaptor.capture(), anyLong());
        timerTaskCaptor.getValue().run();

        verify(mockInProgressLatch).countDown();
    }

    @NonNull
    private DiCommMessage createSuccessfulResponseFromJson(String validJsonString) {
        byte[] payload = ("\0" + validJsonString + "\0").getBytes();

        DiCommMessage message = mock(DiCommMessage.class);
        when(message.getMessageType()).thenReturn(MessageType.GenericResponse);
        when(message.getPayload()).thenReturn(payload);
        return message;
    }

    private void simulateRunWithDiCommMessage(final DiCommMessage message) throws InterruptedException {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                request.dicommMessageListener.onMessage(message);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request.run();
    }
}
