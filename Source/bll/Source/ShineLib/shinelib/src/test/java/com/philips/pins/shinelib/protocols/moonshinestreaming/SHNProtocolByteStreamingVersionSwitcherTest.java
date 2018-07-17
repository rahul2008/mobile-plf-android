package com.philips.pins.shinelib.protocols.moonshinestreaming;

import android.os.Handler;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceByteStreaming;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNProtocolByteStreamingVersionSwitcherTest {

    @Mock
    private SHNServiceByteStreaming mockServiceByteStreaming;

    @Mock
    private Handler mockHandler;

    private SHNProtocolByteStreamingVersionSwitcher subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new SHNProtocolByteStreamingVersionSwitcher(mockServiceByteStreaming, mockHandler);
    }

    @Test
    public void whenSendingData_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.sendData(testData, MoonshineStreamIdentifier.STREAM_1);

        ArgumentCaptor<byte[]> dataCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockServiceByteStreaming, atLeast(1)).sendData(dataCaptor.capture());

        assertEquals(testData.length + 1, dataCaptor.getValue().length);
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], dataCaptor.getValue()[i+1]);
        }
    }

    @Test
    public void whenSendingData_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.sendData(testData, MoonshineStreamIdentifier.STREAM_1);

        verify(mockServiceByteStreaming, never()).sendData((byte[]) any());
    }

    @Test
    public void whenTransitionToErrorReceived_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        subject.transitionToError(SHNResult.SHNErrorInvalidParameter);

        verify(mockServiceByteStreaming).transitionToError();
    }

    @Test
    public void whenTransitionToErrorReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        subject.transitionToError(SHNResult.SHNErrorInvalidParameter);

        verify(mockServiceByteStreaming, never()).transitionToError();
    }

    @Test
    public void whenTransitionToReadyReceived_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        subject.transitionToReady();

        verify(mockServiceByteStreaming).transitionToReady();
    }

    @Test
    public void whenTransitionToReadyReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        subject.transitionToReady();

        verify(mockServiceByteStreaming, never()).transitionToReady();
    }




    private void setupStreamingWithVersion(int version) {
        byte[] protocolData = new byte[] {(byte)version, 0x3F, 0x3F};
        subject.onReadProtocolInformation(protocolData);
    }

    // things to test

    // onReadProtocolInformation - extensive testing!

    // onReceiveData
    // onReceiveAck
    // onServiceAvailable
    // onServiceUnavailable




}