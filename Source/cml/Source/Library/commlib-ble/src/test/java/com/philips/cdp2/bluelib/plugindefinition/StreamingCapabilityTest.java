package com.philips.cdp2.bluelib.plugindefinition;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import com.philips.pins.shinelib.protocols.moonshinestreaming.MoonshineStreamIdentifier;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static com.philips.pins.shinelib.SHNResult.SHNErrorServiceUnavailable;
import static com.philips.pins.shinelib.SHNResult.SHNOk;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class StreamingCapabilityTest {

    private StreamingCapability subject;

    @Mock
    private SHNProtocolMoonshineStreaming mockMoonshineStreaming;

    @Mock
    private ResultListener<SHNDataRaw> mockResultListener;

    private ArgumentCaptor<byte[]> dataCaptor;
    private ArgumentCaptor<SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener> listenerCaptor;


    @Before
    public void setUp() throws Exception {
        initMocks(this);

        dataCaptor = ArgumentCaptor.forClass(byte[].class);
        listenerCaptor = ArgumentCaptor.forClass(SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener.class);

        subject = new StreamingCapability(mockMoonshineStreaming);
        verify(mockMoonshineStreaming).setShnProtocolMoonshineStreamingListener(listenerCaptor.capture());
    }

    @Test
    public void itShouldRegisterListenerWhenConstructed() {
        assertNotNull(listenerCaptor.getValue());
    }

    @Test
    public void whenWritingData_thenItShouldSendDataOnMoonshineStreamingProtocol() {
        byte[] testData = new byte[]{0x00, 0x00};
        subject.writeData(testData);

        verify(mockMoonshineStreaming).sendData(eq(testData), eq(MoonshineStreamIdentifier.STREAM_1));
    }

    @Test
    public void whenOnDataReceivedFromMoonshineStreaming_thenListenersWillBeNotifiedWithActionCompleted() {
        subject.addDataListener(mockResultListener);

        byte[] testData = new byte[]{(byte)0x21, (byte)0x43};
        listenerCaptor.getValue().onDataReceived(testData, MoonshineStreamIdentifier.STREAM_1);

        ArgumentCaptor<SHNDataRaw> dataRawArgumentCaptor = ArgumentCaptor.forClass(SHNDataRaw.class);
        verify(mockResultListener).onActionCompleted(dataRawArgumentCaptor.capture(), eq(SHNOk));
        assertEquals(testData[0], dataRawArgumentCaptor.getValue().getRawData()[0]);
        assertEquals(testData[1], dataRawArgumentCaptor.getValue().getRawData()[1]);
    }

    @Test
    public void whenOnProtocolAvailableFromMoonshineStreaming_thenMoonshineStreamingProtocolWillBeTransitionedToReady() {
        listenerCaptor.getValue().onProtocolAvailable();

        verify(mockMoonshineStreaming).transitionToReady();
    }

    @Test
    public void whenOnProtocolUnavailable_thenMoonshineStreamingProtocolWillBePutToErrorState() {
        listenerCaptor.getValue().onProtocolUnavailable();

        verify(mockMoonshineStreaming).transitionToError(eq(SHNErrorServiceUnavailable));
    }
}