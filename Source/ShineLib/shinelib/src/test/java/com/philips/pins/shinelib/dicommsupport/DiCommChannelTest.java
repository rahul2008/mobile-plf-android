package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Timer.class)
public class DiCommChannelTest {

    private static final int TIME_OUT = 1000;
    private static final String PORT_NAME = "port";
    private static final String PRODUCT_NAME = "1";

    private byte firstDataByte = (byte) 0xCA;
    private byte secondDataByte = (byte) 0xFE;
    private byte thirdByte = (byte) 0xFE;

    private byte[] validMessageData = {(byte) 0xFE, (byte) 0xFF, MessageType.GenericResponse.getDiCommMessageTypeCode(), (byte) 0, (byte) 2, firstDataByte, secondDataByte};

    @Mock
    private SHNProtocolMoonshineStreaming shnProtocolMoonshineStreamingMock;

    @Mock
    private DiCommPort diCommPortMock;

    @Mock
    private SHNMapResultListener<String, Object> resultListenerMock;

    @Mock
    private SHNMapResultListener<String, Object> resultListenerMock2;

    @Captor
    private ArgumentCaptor<byte[]> argumentCaptor;

    @Mock
    private DiCommRequest diCommRequestMock;

    @Mock
    private DiCommResponse diCommResponseMock;

    @Mock
    private DiCommMessage diCommMessageMock;

    private MockedHandler mockedHandler;

    private DiCommChannel diCommChannel;
    private Map<String, Object> properties;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedHandler = new MockedHandler();
        Timer.setHandler(mockedHandler.getMock());

        when(diCommRequestMock.putPropsRequestDataWithProduct(anyString(), anyString(), anyMap())).thenReturn(diCommMessageMock);
        when(diCommRequestMock.getPropsRequestDataWithProduct(anyString(), anyString())).thenReturn(diCommMessageMock);
        when(diCommResponseMock.getStatus()).thenReturn(StatusCode.NoError);

        diCommChannel = new DiCommChannelForTest(shnProtocolMoonshineStreamingMock, TIME_OUT);
        properties = new HashMap<>();
        properties.put("data", 5);
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommChannel(shnProtocolMoonshineStreamingMock, TIME_OUT);
    }

    @Test
    public void whenCreatedThenNotAvailable() throws Exception {
        assertFalse(diCommChannel.isAvailable());
    }

    @Test
    public void whenCreatedThenListenerToMoonshineStreamingProtocolIsSet() throws Exception {
        verify(shnProtocolMoonshineStreamingMock).setShnProtocolMoonshineStreamingListener(any(SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener.class));
    }

    @Test
    public void whenMoonshineProtocolBecomesAvailableThenChannelIsAvailable() throws Exception {
        diCommChannel.onProtocolAvailable();

        assertTrue(diCommChannel.isAvailable());
    }

    @Test
    public void whenMoonshineProtocolBecomesUnavailableThenChannelIsNotAvailable() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.onProtocolUnavailable();

        assertFalse(diCommChannel.isAvailable());
    }

    @Test
    public void whenPortIsAddedThenSetChannelIsCalled() throws Exception {
        diCommChannel.addPort(diCommPortMock);

        verify(diCommPortMock).setDiCommChannel(diCommChannel);
    }

    @Test
    public void whenMoonshineProtocolBecomesAvailableThenNotifiesPort() throws Exception {
        diCommChannel.addPort(diCommPortMock);

        diCommChannel.onProtocolAvailable();

        verify(diCommPortMock).onChannelAvailabilityChanged(true);
    }

    @Test
    public void whenPortIsAddedTwiceThenItIsNotNotifiedTwice() throws Exception {
        diCommChannel.addPort(diCommPortMock);
        diCommChannel.addPort(diCommPortMock);

        diCommChannel.onProtocolAvailable();

        verify(diCommPortMock, times(1)).onChannelAvailabilityChanged(true);
    }

    @Test
    public void whenMoonshineProtocolSetAvailableTwiceThenPortIsNotNotifiedAgain() throws Exception {
        diCommChannel.addPort(diCommPortMock);

        diCommChannel.onProtocolAvailable();
        diCommChannel.onProtocolAvailable();

        verify(diCommPortMock, times(1)).onChannelAvailabilityChanged(true);
    }

    @Test
    public void whenMoonshineProtocolBecomesUnavailableThenNotifiesPort() throws Exception {
        diCommChannel.addPort(diCommPortMock);
        diCommChannel.onProtocolAvailable();
        reset(diCommPortMock);

        diCommChannel.onProtocolUnavailable();

        verify(diCommPortMock).onChannelAvailabilityChanged(false);
    }

    @Test
    public void whenMoonshineProtocolSetUnavailableTwiceThenPortIsNotNotifiedAgain() throws Exception {
        diCommChannel.addPort(diCommPortMock);
        diCommChannel.onProtocolAvailable();
        reset(diCommPortMock);

        diCommChannel.onProtocolUnavailable();
        diCommChannel.onProtocolUnavailable();

        verify(diCommPortMock, times(1)).onChannelAvailabilityChanged(false);
    }

    @Test
    public void whenMoonshineProtocolSetUnavailableAfterInitializationThenPortIsNotNotified() throws Exception {
        diCommChannel.addPort(diCommPortMock);
        reset(diCommPortMock);

        diCommChannel.onProtocolUnavailable();

        verify(diCommPortMock, never()).onChannelAvailabilityChanged(false);
    }

    @Test
    public void whenPortsAreAddedThenStateIsUpdated() throws Exception {
        diCommChannel.addPort(diCommPortMock);

        verify(diCommPortMock).onChannelAvailabilityChanged(false);
    }

    @Test
    public void whenSendPropertiesIsCalledWhileUnavailableThenErrorIsReported() throws Exception {
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        verify(shnProtocolMoonshineStreamingMock, never()).sendData(any(byte[].class));
        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenSendPropertiesIsExecutedThenMessageIsSentViaMoonshineProtocol() throws Exception {
        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        verify(shnProtocolMoonshineStreamingMock).sendData(any(byte[].class));
    }

    @Test
    public void whenPropertiesHaveNullAsAKeyThenInvalidParameterResultIsReported() throws Exception {
        when(diCommRequestMock.putPropsRequestDataWithProduct(anyString(), anyString(), anyMap())).thenReturn(null);
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenSecondRequestIsSentThenDataIsNotSendViaMoonshineStreaming() throws Exception {
        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        verify(shnProtocolMoonshineStreamingMock, times(1)).sendData(any(byte[].class));
    }

    @Test
    public void whenReceivedDataDoesNotContainAMessageThenDataIsIgnored() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        byte[] data = {(byte) 0xFE, (byte) 0xFF};

        diCommChannel.onDataReceived(data);

        verify(resultListenerMock, never()).onActionCompleted(anyMap(), any(SHNResult.class));
    }

    @Test
    public void whenReceivedDataDoesContainValidMessageResponseThenListenerIsNotified() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        byte[] data = {(byte) 0xFE, (byte) 0xFF};
        byte[] data2 = {MessageType.GenericResponse.getDiCommMessageTypeCode(), (byte) 0, (byte) 2, firstDataByte, secondDataByte, thirdByte};

        diCommChannel.onDataReceived(data);
        diCommChannel.onDataReceived(data2);

        verify(resultListenerMock).onActionCompleted(anyMap(), any(SHNResult.class));
    }

    @Test
    public void whenReceivedDataContainValidMessageResponseThenTheNextRequestIsSent() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock2);

        reset(shnProtocolMoonshineStreamingMock);
        diCommChannel.onDataReceived(validMessageData);

        verify(shnProtocolMoonshineStreamingMock).sendData(any(byte[].class));
    }

    @Test
    public void whenReceivedDataContainValidMessageResponseThenListenerIsNotified() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock2);

        diCommChannel.onDataReceived(validMessageData);
        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(anyMap(), any(SHNResult.class));
        verify(resultListenerMock2).onActionCompleted(anyMap(), any(SHNResult.class));
    }

    @Test
    public void whenResponseDataIsInvalidThenListenerIsNotifiedWithInvalidParameter() throws Exception {
        DiCommChannel diCommChannel = new DiCommChannelTrowingForTest(shnProtocolMoonshineStreamingMock, TIME_OUT);
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenUnexpectedResponseWithInvalidDataIsReceivedThenItIsIgnored() throws Exception {
        DiCommChannel diCommChannel = new DiCommChannelTrowingForTest(shnProtocolMoonshineStreamingMock, TIME_OUT);
        diCommChannel.onProtocolAvailable();
        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        diCommChannel.onDataReceived(validMessageData);
        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
    }

    //   
    @Test
    public void whenReloadPropertiesIsCalledWhileUnavailableThenErrorIsReported() throws Exception {
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        verify(shnProtocolMoonshineStreamingMock, never()).sendData(any(byte[].class));
        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
    }

    @Test
    public void whenReloadPropertiesIsExecutedThenMessageIsSentViaMoonshineProtocol() throws Exception {
        diCommChannel.onProtocolAvailable();

        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        verify(diCommRequestMock).getPropsRequestDataWithProduct(PRODUCT_NAME, PORT_NAME);
        verify(shnProtocolMoonshineStreamingMock).sendData(any(byte[].class));
    }

    @Test
    public void whenSecondRequestIsSentThenDataIsNotReloadViaMoonshineStreaming() throws Exception {
        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        verify(shnProtocolMoonshineStreamingMock, times(1)).sendData(any(byte[].class));
    }

    @Test
    public void whenReloadPropertiesIsRequestedAndReceivedDataDoesNotContainAMessageThenDataIsIgnored() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        byte[] data = {(byte) 0xFE, (byte) 0xFF};

        diCommChannel.onDataReceived(data);

        verify(resultListenerMock, never()).onActionCompleted(anyMap(), any(SHNResult.class));
    }

    @Test
    public void whenReloadPropertiesIsRequestedAndReceivedDataDoesContainValidMessageResponseThenListenerIsNotified() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        byte[] data = {(byte) 0xFE, (byte) 0xFF};
        byte[] data2 = {MessageType.GenericResponse.getDiCommMessageTypeCode(), (byte) 0, (byte) 2, firstDataByte, secondDataByte, thirdByte};

        diCommChannel.onDataReceived(data);
        diCommChannel.onDataReceived(data2);

        verify(resultListenerMock).onActionCompleted(anyMap(), any(SHNResult.class));
    }

    @Test
    public void whenReloadPropertiesIsRequestedAndReceivedDataContainValidMessageResponseThenTheNextRequestIsSent() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        reset(shnProtocolMoonshineStreamingMock);
        diCommChannel.onDataReceived(validMessageData);

        verify(shnProtocolMoonshineStreamingMock).sendData(any(byte[].class));
        assertEquals(1, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenReloadPropertiesIsRequestedAndReceivedDataContainValidMessageResponseThenListenerIsNotified() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock2);

        diCommChannel.onDataReceived(validMessageData);
        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(anyMap(), any(SHNResult.class));
        verify(resultListenerMock2).onActionCompleted(anyMap(), any(SHNResult.class));
        assertEquals(0, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenReloadPropertiesIsRequestedAndResponseDataIsInvalidThenListenerIsNotifiedWithInvalidParameter() throws Exception {
        DiCommChannel diCommChannel = new DiCommChannelTrowingForTest(shnProtocolMoonshineStreamingMock, TIME_OUT);
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorInvalidParameter);
        assertEquals(0, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenProtocolBecomesUnavailableThenPendingRequestsAreNotified() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock2);

        diCommChannel.onProtocolUnavailable();

        verify(resultListenerMock).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
        verify(resultListenerMock2).onActionCompleted(null, SHNResult.SHNErrorConnectionLost);
        assertEquals(0, mockedHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenASendPropertiesRequestTimesOutThenTheStreamingProtocolIsTransitionedToErrorState() {
        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);
        assertEquals(1, mockedHandler.getScheduledExecutionCount());
        mockedHandler.executeFirstScheduledExecution();

        verify(shnProtocolMoonshineStreamingMock).transitionToError(SHNResult.SHNErrorTimeout);
    }

    @Test
    public void whenAReloadPropertiesRequestTimesOutThenTheStreamingProtocolIsTransitionedToErrorState() {
        diCommChannel.onProtocolAvailable();

        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);
        assertEquals(1, mockedHandler.getScheduledExecutionCount());
        mockedHandler.executeFirstScheduledExecution();

        verify(shnProtocolMoonshineStreamingMock).transitionToError(SHNResult.SHNErrorTimeout);
    }

    private void receiveResponseWithStatus(StatusCode statusCode) {
        when(diCommResponseMock.getStatus()).thenReturn(statusCode);
        byte[] data = {(byte) 0xFE, (byte) 0xFF, MessageType.GenericResponse.getDiCommMessageTypeCode(), (byte) 0, (byte) 0};

        diCommChannel.onDataReceived(data);
    }

    @Test
    public void whenResponseStatusIsOutOfMemoryThenListenerIsNotifiedWithOperationFailed() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        receiveResponseWithStatus(StatusCode.OutOfMemory);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNErrorOperationFailed));
    }

    @Test
    public void whenResponseStatusIsNotImplementedThenListenerIsNotifiedWithUnsupportedOperation() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        receiveResponseWithStatus(StatusCode.NotImplemented);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNErrorUnsupportedOperation));
    }

    @Test
    public void whenResponseStatusIsInvalidParameterThenListenerIsNotifiedWithInvalidParameter() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        receiveResponseWithStatus(StatusCode.InvalidParameter);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNErrorInvalidParameter));
    }

    @Test
    public void whenResponseStatusIsProtocolViolationThenListenerIsNotifiedWithInvalidState() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        receiveResponseWithStatus(StatusCode.ProtocolViolation);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNErrorInvalidState));
    }

    @Test
    public void whenUnexpectedResponseIsReceivedThenItIsIgnored() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        receiveResponseWithStatus(StatusCode.NoError);
        receiveResponseWithStatus(StatusCode.NoError);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNOk));
    }

    @Test
    public void whenMessageIsTransmittedAndAvailabilityChangesThenNextMessageIsParsedProperly() throws Exception {
        diCommChannel.onProtocolAvailable();
        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);

        when(diCommResponseMock.getStatus()).thenReturn(StatusCode.NoError);
        byte[] data = {(byte) 0xFE, (byte) 0xFF, MessageType.GenericResponse.getDiCommMessageTypeCode()};
        diCommChannel.onDataReceived(data);

        diCommChannel.onProtocolUnavailable();
        reset(resultListenerMock);
        diCommChannel.onProtocolAvailable();

        diCommChannel.reloadProperties(PORT_NAME, resultListenerMock);
        diCommChannel.onDataReceived(validMessageData);

        verify(resultListenerMock).onActionCompleted(anyMap(), eq(SHNResult.SHNOk));
    }

    @Test
    public void whenSendPropertiesIsCalledForNonFirmwarePortThenPropertiesArePutForProduct1() throws Exception {
        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, PORT_NAME, resultListenerMock);

        verify(diCommRequestMock).putPropsRequestDataWithProduct(PRODUCT_NAME, PORT_NAME, properties);
    }

    @Test
    public void whenSendPropertiesIsCalledForFirmwarePortThenPropertiesArePutForProduct0() throws Exception {
        String FIRMWARE_PORT = "firmware";

        diCommChannel.onProtocolAvailable();

        diCommChannel.sendProperties(properties, FIRMWARE_PORT, resultListenerMock);

        verify(diCommRequestMock).putPropsRequestDataWithProduct("0", FIRMWARE_PORT, properties);
    }

    private class DiCommChannelForTest extends DiCommChannel {

        public DiCommChannelForTest(SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming, int timeOut) {
            super(shnProtocolMoonshineStreaming, timeOut);
        }

        @NonNull
        @Override
        protected DiCommRequest getDiCommRequest() {
            assertNotNull(super.getDiCommRequest());

            return diCommRequestMock;
        }

        @NonNull
        @Override
        protected DiCommResponse getDiCommResponse(DiCommMessage diCommMessage) {
            return diCommResponseMock;
        }
    }

    private class DiCommChannelTrowingForTest extends DiCommChannel {

        public DiCommChannelTrowingForTest(SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming, int timeOut) {
            super(shnProtocolMoonshineStreaming, timeOut);
        }

        @NonNull
        @Override
        protected DiCommRequest getDiCommRequest() {
            assertNotNull(super.getDiCommRequest());

            return diCommRequestMock;
        }

        @NonNull
        @Override
        protected DiCommResponse getDiCommResponse(DiCommMessage diCommMessage) {
            throw new InvalidParameterException("Mocked Exception");
        }
    }
}