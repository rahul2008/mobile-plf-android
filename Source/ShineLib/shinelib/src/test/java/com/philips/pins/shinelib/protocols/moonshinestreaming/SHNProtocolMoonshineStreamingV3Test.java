/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.protocols.moonshinestreaming;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.services.SHNServiceMoonshineStreaming;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SHNProtocolMoonshineStreamingV3Test {
    private static final int TEST_TX_WINDOW_SIZE = 2;
    private static final int TEST_RX_WINDOW_SIZE = 3;
    public static final byte[] OK_PROTOCOL_INFO_DATA = new byte[]{SHNProtocolMoonshineStreamingV3.PROTOCOL_VERSION, TEST_TX_WINDOW_SIZE, TEST_RX_WINDOW_SIZE};
    public static final byte[] OK_PROTOCOL_INFO_DATA_VPLUS = new byte[]{SHNProtocolMoonshineStreamingV3.PROTOCOL_VERSION + 1, TEST_TX_WINDOW_SIZE, TEST_RX_WINDOW_SIZE};
    private SHNProtocolMoonshineStreamingV3 shnProtocolMoonshineStreamingV3;
    private SHNServiceMoonshineStreaming mockedShnServiceMoonshineStreaming;
    private SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingListener mockedShnProtocolMoonshineStreamingListener;
    private MockedHandler mockedHandler;
    public static final byte[] TWO_BYTES_TEST_DATA = new byte[]{(byte) 0xC0, (byte) 0xDE};

    private void getProtocolToReadyStateWithTestWindowsSize() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{SHNProtocolMoonshineStreamingV3.HEADER_PACKET_TYPE_START});
    }

    @Before
    public void setUp() {
        mockedShnServiceMoonshineStreaming = mock(SHNServiceMoonshineStreaming.class);
        doNothing().when(mockedShnServiceMoonshineStreaming).readProtocolConfiguration();
        doNothing().when(mockedShnServiceMoonshineStreaming).sendData(any(byte[].class));
        doNothing().when(mockedShnServiceMoonshineStreaming).transitionToReady();
        doNothing().when(mockedShnServiceMoonshineStreaming).sendData(any(byte[].class));

        // Make a normal mock object. We will verify the calls being made.
        mockedShnProtocolMoonshineStreamingListener = mock(SHNProtocolByteStreamingVersionSwitcher.SHNProtocolMoonshineStreamingListener.class);

        mockedHandler = new MockedHandler();

        shnProtocolMoonshineStreamingV3 = new SHNProtocolMoonshineStreamingV3(mockedShnServiceMoonshineStreaming, mockedHandler.getMock());
        shnProtocolMoonshineStreamingV3.setShnProtocolMoonshineStreamingListener(mockedShnProtocolMoonshineStreamingListener);
    }

    // Start with test for the initialization phase of the protocol
    @Test
    public void canCreateAProtocolObject() {
        assertNotNull(shnProtocolMoonshineStreamingV3);
    }

    @Test
    public void whenTheProtocolIsCreatedThenItsStateIsInitializing() {
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Initializing, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenTransitionToErrorIsCalledThenItsStateIsError() {
        shnProtocolMoonshineStreamingV3.transitionToError(SHNResult.SHNErrorTimeout);
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Error, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenTransitionToErrorIsCalledThenTransitionToErrorOnTheService() {
        shnProtocolMoonshineStreamingV3.transitionToError(SHNResult.SHNErrorTimeout);
        verify(mockedShnServiceMoonshineStreaming).transitionToError();
    }

    @Test
    public void whenOnServiceAvailableThenReadProtocolConfigurationIsNotCalled() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        verify(mockedShnServiceMoonshineStreaming, never()).readProtocolConfiguration();
    }

    @Test
    public void whenOnServiceAvailableThenTheStateIsChangedToAcquiringConfiguration() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.AcquiringConfiguration, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnServiceUnavailableInStateAcquiringConfigurationThenTheStateIsChangedToInitializing() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onServiceUnavailable();
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Initializing, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReadProtocolInformationCouldNotBeReadThenTheStateIsChangedToWaitinfForHandshakeAck() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(null);
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnServiceUnavailableInStateWaitingForHandshakeAckThenTheStateIsChangedToInitializing() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(null);
        shnProtocolMoonshineStreamingV3.onServiceUnavailable();
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Initializing, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReadProtocolInformationCouldNotBeReadThenTheWindowSizeIsSetToDefault() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(null);
        assertEquals(SHNProtocolMoonshineStreamingV3.DEFAULT_TX_WINDOW_SIZE, shnProtocolMoonshineStreamingV3.getTxWindowSize());
        assertEquals(SHNProtocolMoonshineStreamingV3.DEFAULT_TX_WINDOW_SIZE, shnProtocolMoonshineStreamingV3.getRxWindowSize());
    }

    @Test
    public void whenOnReadProtocolInformationCouldNotBeReadThenTheStartMsgIsSent() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(null);
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendData(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(1, argumentCaptor.getValue().length);
        assertEquals(SHNProtocolMoonshineStreamingV3.HEADER_PACKET_TYPE_START, argumentCaptor.getValue()[0]); // b7b6b5b4b3b2b1b0 b7-b6: type   b5-b0:seq nr
    }

    @Test
    public void whenOnReadProtocolInformationCouldBeReadThenTheStateIsChangedToWaitinfForHandshakeAck() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReadProtocolInformationWithVersionErrorCouldBeReadThenTheStateIsChangedToError() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA_VPLUS);
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Error, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReadProtocolInformationWithVersionErrorCouldBeReadThenNoDataIsSend() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA_VPLUS);
        verify(mockedShnServiceMoonshineStreaming, times(0)).sendData(any(byte[].class));
    }

    @Test
    public void whenOnReadProtocolInformationCouldBeReadThenTheWindowSizeIsSetToTheTestWindowSize() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        assertEquals(TEST_TX_WINDOW_SIZE, shnProtocolMoonshineStreamingV3.getTxWindowSize());
        assertEquals(TEST_RX_WINDOW_SIZE, shnProtocolMoonshineStreamingV3.getRxWindowSize());
    }

    @Test
    public void whenOnReadProtocolInformationCouldBeReadThenTheStartMsgIsSent() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendData(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(1, argumentCaptor.getValue().length);
        assertEquals(SHNProtocolMoonshineStreamingV3.HEADER_PACKET_TYPE_START, argumentCaptor.getValue()[0]); // b7b6b5b4b3b2b1b0 b7-b6: type   b5-b0:seq nr
    }

    @Test
    public void whenTheStartMsgIsSentThenATimeoutTriggersResendingOfTheStartMsg() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        mockedHandler.executeFirstScheduledExecution(); // The ack timeout
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(1, argumentCaptor.getValue().length);
        assertEquals(SHNProtocolMoonshineStreamingV3.HEADER_PACKET_TYPE_START, argumentCaptor.getValue()[0]); // b7b6b5b4b3b2b1b0 b7-b6: type   b5-b0:seq nr
    }

    @Test
    public void whenOnReceiveAckIsCalledWithoutTheStartIndicationTheStateRemains() {
        shnProtocolMoonshineStreamingV3.onServiceAvailable();
        shnProtocolMoonshineStreamingV3.onReadProtocolInformation(OK_PROTOCOL_INFO_DATA);
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x00});
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.WaitingForHandshakeAck, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReceiveAckIsCalledWithTheCorrectSNThenTheStateIsChangedToReady() {
        getProtocolToReadyStateWithTestWindowsSize();
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Ready, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenOnReceiveAckIsCalledWithTheCorrectSNThenTheAckTimeoutTimerIsStopped() {
        getProtocolToReadyStateWithTestWindowsSize();
        mockedHandler.executeFirstScheduledExecution(); // should trigger a timeout
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Ready, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenTransitionToReadyIsCalledThenTransitionToReadyIsCalledOnService() {
        shnProtocolMoonshineStreamingV3.transitionToReady();

        verify(mockedShnServiceMoonshineStreaming).transitionToReady();
    }

    @Test
    public void whenOnReceiveAckIsCalledWithTheCorrectSNThenOnProtocolAvailableIsCalled() {
        getProtocolToReadyStateWithTestWindowsSize();
        verify(mockedShnProtocolMoonshineStreamingListener).onProtocolAvailable();
    }

    // Sending data tests
    @Test
    public void whenReadySendDateWith2BytesPacketThenSendDataIsRelayedToTheServiceWithASequenceNr() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.sendData(TWO_BYTES_TEST_DATA);
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendData(argumentCaptor.capture());
        assertEquals(3, argumentCaptor.getValue().length);
        assertEquals((byte) 0x01, argumentCaptor.getValue()[0]);     // SEQNr
        assertEquals(TWO_BYTES_TEST_DATA[0], argumentCaptor.getValue()[1]);    // byte[0]
        assertEquals(TWO_BYTES_TEST_DATA[1], argumentCaptor.getValue()[2]);    // byte[1]
    }

    @Test
    public void whenReadySendDateWith2BytesPacketAndReceiveAckTimeoutThenTheUnacknowledgedPacketsAreResend() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.sendData(TWO_BYTES_TEST_DATA);
        mockedHandler.executeFirstScheduledExecution();
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture()); // one packet send twice (initial and resend after ack timeout).
        assertEquals(3, argumentCaptor.getValue().length);
        assertEquals((byte) 0x01, argumentCaptor.getValue()[0]);     // SEQNr
        assertEquals(TWO_BYTES_TEST_DATA[0], argumentCaptor.getValue()[1]);    // byte[0]
        assertEquals(TWO_BYTES_TEST_DATA[1], argumentCaptor.getValue()[2]);    // byte[1]
    }

    @Test
    public void whenReadySendDateWith2BytesPacketAndAckIsReceivedThenTimeoutIsCancelled() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.sendData(TWO_BYTES_TEST_DATA);
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x01}); // SeqNr

        reset(mockedShnServiceMoonshineStreaming);
        mockedHandler.executeFirstScheduledExecution();
        verify(mockedShnServiceMoonshineStreaming, never()).sendData(any(byte[].class));
    }

    @Test
    public void whenReadySendDateWith2BytesPacketAndEarlierAckIsReceivedThenTimeoutIsStillActive() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.sendData(TWO_BYTES_TEST_DATA);
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x00}); // Wrong SeqNr

        reset(mockedShnServiceMoonshineStreaming);
        mockedHandler.executeFirstScheduledExecution(); // Trigger timeout
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendData(argumentCaptor.capture());
        assertEquals(3, argumentCaptor.getValue().length);
        assertEquals((byte) 0x01, argumentCaptor.getValue()[0]);     // SEQNr
        assertEquals(TWO_BYTES_TEST_DATA[0], argumentCaptor.getValue()[1]);    // byte[0]
        assertEquals(TWO_BYTES_TEST_DATA[1], argumentCaptor.getValue()[2]);    // byte[1]
    }

    @Test
    public void whenReceivingTheSameAckThreeTimesInARowThenTheStateIsChangedToError() {
        getProtocolToReadyStateWithTestWindowsSize();

        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Ready, shnProtocolMoonshineStreamingV3.getState());
        shnProtocolMoonshineStreamingV3.sendData(TWO_BYTES_TEST_DATA);

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x00}); // Wrong SeqNr
        mockedHandler.executeFirstScheduledExecution(); // Trigger timeout and resend data
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Ready, shnProtocolMoonshineStreamingV3.getState());
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x00}); // Wrong SeqNr
        mockedHandler.executeFirstScheduledExecution(); // Trigger timeout and resend data
        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Ready, shnProtocolMoonshineStreamingV3.getState());
        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x00}); // Wrong SeqNr
        mockedHandler.executeFirstScheduledExecution(); // Trigger timeout and resend data

        assertEquals(SHNProtocolMoonshineStreamingV3.SHNProtocolMoonshineStreamingState.Error, shnProtocolMoonshineStreamingV3.getState());
    }

    @Test
    public void whenSendingMorePacketsThanWindowSizeThenOnlyWindowSizePacketsAreSend() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x10}); // Has SeqNr 1
        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x11}); // Has SeqNr 2
        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x12}); // Has SeqNr 3
        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x13}); // Has SeqNr 4

        verify(mockedShnServiceMoonshineStreaming, times(TEST_TX_WINDOW_SIZE)).sendData(any(byte[].class));
    }

    @Test
    public void whenSendingMorePacketsThanWindowSizeThenPacketsAreSendInTheOrderThatTheyAreSubmitted() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x10}); // Has SeqNr 1
        verify(mockedShnServiceMoonshineStreaming, times(1)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(1, argumentCaptor.getValue()[0]);
        assertEquals(0x10, argumentCaptor.getValue()[1]);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x11}); // Has SeqNr 2
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(2, argumentCaptor.getValue()[0]);
        assertEquals(0x11, argumentCaptor.getValue()[1]);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x12}); // Has SeqNr 3
        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x13}); // Has SeqNr 4

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x01}); // Ack First Packet
        verify(mockedShnServiceMoonshineStreaming, times(3)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(3, argumentCaptor.getValue()[0]);
        assertEquals(0x12, argumentCaptor.getValue()[1]);

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x02}); // Ack Second Packet
        verify(mockedShnServiceMoonshineStreaming, times(4)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(4, argumentCaptor.getValue()[0]);
        assertEquals(0x13, argumentCaptor.getValue()[1]);
    }

    @Test
    public void whenSendingAPacketOf19BytesThenOnePacketIsSent() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        byte[] packet = new byte[19];
        Arrays.fill(packet, (byte) 0x4E);

        shnProtocolMoonshineStreamingV3.sendData(packet); // Has SeqNr 1
        verify(mockedShnServiceMoonshineStreaming, times(1)).sendData(argumentCaptor.capture());
        assertEquals(20, argumentCaptor.getValue().length);
        assertEquals(1, argumentCaptor.getValue()[0]);
        for (int index = 0; index < 19; index++) {
            assertEquals(0x4E, argumentCaptor.getValue()[index + 1]);
        }
    }

    @Test
    public void whenSendingAPacketOf20BytesThenTwoPacketsAreSent() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        byte[] packet = new byte[20];
        Arrays.fill(packet, (byte) 0x4E);

        shnProtocolMoonshineStreamingV3.sendData(packet); // Has SeqNr 1 and 2

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{(byte) 0x01});
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(2, argumentCaptor.getValue()[0]);
        for (int index = 0; index < 1; index++) {
            assertEquals(0x4E, argumentCaptor.getValue()[index + 1]);
        }
    }

    @Test
    public void whenSendingAPacketOf38BytesThenTwoPacketsAreSent() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        byte[] packet = new byte[38];
        Arrays.fill(packet, (byte) 0x4E);

        shnProtocolMoonshineStreamingV3.sendData(packet); // Has SeqNr 1 and 2

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{(byte) 0x01});
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture());
        assertEquals(20, argumentCaptor.getValue().length);
        assertEquals(2, argumentCaptor.getValue()[0]);
        for (int index = 0; index < 19; index++) {
            assertEquals(0x4E, argumentCaptor.getValue()[index + 1]);
        }
    }

    @Test
    public void whenSendingAPacketOf39BytesThenThreePacketsAreSent() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        byte[] packet = new byte[39];
        Arrays.fill(packet, (byte) 0x4E);

        shnProtocolMoonshineStreamingV3.sendData(packet); // Has SeqNr 1 and 2

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{(byte) 0x01});
        verify(mockedShnServiceMoonshineStreaming, times(3)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(3, argumentCaptor.getValue()[0]);
        for (int index = 0; index < 1; index++) {
            assertEquals(0x4E, argumentCaptor.getValue()[index + 1]);
        }
    }

    @Test
    public void whenSendingMorePacketsThanWindowSizeAndTheAckAcknowledgesMultiplePacketsThenMultiplePacketsAreSendInTheOrderThatTheyAreSubmitted() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x10}); // Has SeqNr 1
        verify(mockedShnServiceMoonshineStreaming, times(1)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(1, argumentCaptor.getValue()[0]);
        assertEquals(0x10, argumentCaptor.getValue()[1]);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x11}); // Has SeqNr 2
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(2, argumentCaptor.getValue()[0]);
        assertEquals(0x11, argumentCaptor.getValue()[1]);

        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x12}); // Has SeqNr 3
        shnProtocolMoonshineStreamingV3.sendData(new byte[]{0x13}); // Has SeqNr 4

        shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{0x02}); // Ack Second Packet
        verify(mockedShnServiceMoonshineStreaming, times(4)).sendData(argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().length);
        assertEquals(4, argumentCaptor.getValue()[0]);
        assertEquals(0x13, argumentCaptor.getValue()[1]);
    }

    @Test
    public void whenSendingMoreThanMaxSeqNrPacketsThenTheSeqNrWrapAround() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        for (int seqNr = 1; seqNr < 255; seqNr++) {
            byte[] packet = new byte[]{(byte) seqNr};
            shnProtocolMoonshineStreamingV3.sendData(packet);
            verify(mockedShnServiceMoonshineStreaming, times(seqNr)).sendData(argumentCaptor.capture());
            assertEquals(seqNr % SHNProtocolMoonshineStreamingV3.MAX_SEQUENCE_NR, argumentCaptor.getValue()[0] & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK);
            shnProtocolMoonshineStreamingV3.onReceiveAck(packet);
        }
    }

    @Test
    public void whenSendingMoreThanMaxSeqNrPacketsWithWindowOf1ThenTheSeqNrWrapAround() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        byte[] packet = new byte[]{(byte) 1};
        shnProtocolMoonshineStreamingV3.sendData(packet);
        verify(mockedShnServiceMoonshineStreaming).sendData(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue()[0] & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK);
        shnProtocolMoonshineStreamingV3.onReceiveAck(packet);

        for (int seqNr = 2; seqNr < 255; seqNr++) {
            byte[] previousPacket = packet;
            packet = new byte[]{(byte) seqNr};
            shnProtocolMoonshineStreamingV3.sendData(packet);
            verify(mockedShnServiceMoonshineStreaming, times(seqNr)).sendData(argumentCaptor.capture());
            assertEquals(seqNr % SHNProtocolMoonshineStreamingV3.MAX_SEQUENCE_NR, argumentCaptor.getValue()[0] & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK);
            shnProtocolMoonshineStreamingV3.onReceiveAck(previousPacket);
        }
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrThenOnDataReceivedIsCalled() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{0x00, 0x00});
        verify(mockedShnProtocolMoonshineStreamingListener).onDataReceived(any(byte[].class));
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrThenAfterTimeoutAnAcknowledgeIsSent() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{0x00, 0x00});
        mockedHandler.executeFirstScheduledExecution();
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(0, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrWith2PacketsThenAfterTimeoutAnAcknowledgeIsSentForLastPacket() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{0x00, 0x00});
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{0x01, 0x00});
        mockedHandler.executeFirstScheduledExecution();
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrWithWindowSizePacketsThenAnAcknowledgeIsSentForLastPacket() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        for (int seqNr = 0; seqNr < TEST_RX_WINDOW_SIZE; seqNr++) {
            shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) seqNr, 0x00});
        }
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(TEST_RX_WINDOW_SIZE - 1, argumentCaptor.getValue()[0]);

        mockedHandler.executeFirstScheduledExecution(); // The timeout has no effect.
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(TEST_RX_WINDOW_SIZE - 1, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrWith2TimesWindowSizePacketsThenAnAcknowledgeIsSentForLastPacket() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        for (int seqNr = 0; seqNr < (2 * TEST_RX_WINDOW_SIZE); seqNr++) {
            shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) seqNr, 0x00});
        }
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendAck(argumentCaptor.capture());
        assertEquals((2 * TEST_RX_WINDOW_SIZE) - 1, argumentCaptor.getValue()[0]);

        mockedHandler.executeFirstScheduledExecution(); // The timeout has no effect.
        verify(mockedShnServiceMoonshineStreaming, times(2)).sendAck(argumentCaptor.capture());
        assertEquals((2 * TEST_RX_WINDOW_SIZE) - 1, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithExpectedSeqNrWithMaxSeqNrSizePacketsThenAnAcknowledgeIsSentForLastPacket() {
        getProtocolToReadyStateWithTestWindowsSize();
        reset(mockedShnServiceMoonshineStreaming);

        int packetCount = ((((SHNProtocolMoonshineStreamingV3.MAX_SEQUENCE_NR / TEST_RX_WINDOW_SIZE) + 1) * TEST_RX_WINDOW_SIZE) * 2) + 1;
        for (int packetNr = 0; packetNr < packetCount; packetNr++) {
            shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (packetNr & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), 0x00});

            if (((packetNr + 1) % TEST_RX_WINDOW_SIZE) == 0) {
                ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
                verify(mockedShnServiceMoonshineStreaming, times((packetNr + 1) / TEST_RX_WINDOW_SIZE)).sendAck(argumentCaptor.capture());
                assertEquals(packetNr % SHNProtocolMoonshineStreamingV3.MAX_SEQUENCE_NR, argumentCaptor.getValue()[0]);
            }
        }

        // The loop above emits x * TEST_RX_WINDOW + 1 packets. packetCount is one past the last packet sent! This explains the -2 and -1 in the next two tests.
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnServiceMoonshineStreaming, atLeastOnce()).sendAck(argumentCaptor.capture());
        assertEquals((packetCount - 2) & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);

        mockedHandler.executeFirstScheduledExecution(); // There should be one packet left that needs a confirm.
        verify(mockedShnServiceMoonshineStreaming, atLeastOnce()).sendAck(argumentCaptor.capture());
        assertEquals((packetCount - 1) & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithUnexpectedSeqNrThenThePacketIsNotForwardedToTheStreamingListener() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x00}); // receive packet with SeqNo = 0;

        reset(mockedShnProtocolMoonshineStreamingListener);
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!
        verify(mockedShnProtocolMoonshineStreamingListener, never()).onDataReceived(any(byte[].class));
    }

    @Test
    public void whenOnReceiveDataWithUnexpectedSeqNrThenThePacketIsNotAcknowledged() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x00}); // receive packet with SeqNo = 0;
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        mockedHandler.executeFirstScheduledExecution(); // There should be one packet that needs a confirm.
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithReSendDataThenThePacketIsForwardedToTheStreamingListener() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x00}); // receive packet with SeqNo = 0;
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!
        mockedHandler.executeFirstScheduledExecution(); // There should be one packet that needs a confirm.

        reset(mockedShnProtocolMoonshineStreamingListener);
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (1 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x01}); // receive packet with SeqNo = 1;
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnProtocolMoonshineStreamingListener).onDataReceived(argumentCaptor.capture());
        assertEquals(1 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithReSendDataThenThePacketIsAcknowledged() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x00}); // receive packet with SeqNo = 0;
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!
        mockedHandler.executeFirstScheduledExecution(); // There should be one packet that needs a confirm.

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (1 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x01}); // receive packet with SeqNo = 1;
        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!

        reset(mockedShnServiceMoonshineStreaming);
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        mockedHandler.executeFirstScheduledExecution();
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenOnReceiveDataWithPacketOutOfSequenceThenAfterTimeoutTheLastAckIsResend() {
        getProtocolToReadyStateWithTestWindowsSize();

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x00}); // receive packet with SeqNo = 0;
        mockedHandler.executeFirstScheduledExecution(); // Nothing to ack. No timer active

        shnProtocolMoonshineStreamingV3.onReceiveData(new byte[]{(byte) (2 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK), (byte) 0x02}); // receive packet with SeqNo = 2; dropped!

        reset(mockedShnServiceMoonshineStreaming);
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        mockedHandler.executeFirstScheduledExecution();
        verify(mockedShnServiceMoonshineStreaming).sendAck(argumentCaptor.capture());
        assertEquals(0 & SHNProtocolMoonshineStreamingV3.HEADER_SEQNR_MASK, argumentCaptor.getValue()[0]);
    }

    @Test
    public void whenAckIsReceivedNextPacketSInTheWindowAreSend() {
        getProtocolToReadyStateWithTestWindowsSize();

        for (int i = 0; i < 256; i++) {
            shnProtocolMoonshineStreamingV3.sendData(new byte[]{(byte) i});
        }
        verify(mockedShnServiceMoonshineStreaming, times(TEST_RX_WINDOW_SIZE)).sendData(isA(byte[].class));

        for (int i = 1; i < 240; i++) {
            shnProtocolMoonshineStreamingV3.onReceiveAck(new byte[]{(byte) (i % SHNProtocolMoonshineStreamingV3.MAX_SEQUENCE_NR)});
            verify(mockedShnServiceMoonshineStreaming, times(TEST_RX_WINDOW_SIZE + i)).sendData(isA(byte[].class));
        }
    }
}