package com.philips.pins.shinelib.protocols.moonshinestreaming;

import android.os.Handler;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceByteStreaming;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SHNProtocolByteStreamingVersionSwitcher.class)
public class SHNProtocolByteStreamingVersionSwitcherTest {

    @Mock
    private SHNServiceByteStreaming mockServiceByteStreaming;

    @Mock
    private Handler mockHandler;

    @Mock
    private SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener mockMoonshineStreamingListener;

    @Mock
    private SHNProtocolMoonshineStreamingV1 mockMoonshineStreamingV1;

    @Mock
    private SHNProtocolMoonshineStreamingV2 mockMoonshineStreamingV2;

    @Mock
    private SHNProtocolMoonshineStreamingV3 mockMoonshineStreamingV3;

    @Mock
    private SHNProtocolMoonshineStreamingV4 mockMoonshineStreamingV4;


    private SHNProtocolByteStreamingVersionSwitcher subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new SHNProtocolByteStreamingVersionSwitcher(mockServiceByteStreaming, mockHandler);

        PowerMockito.whenNew(SHNProtocolMoonshineStreamingV1.class).withArguments(mockServiceByteStreaming, mockHandler).thenReturn(mockMoonshineStreamingV1);
        PowerMockito.whenNew(SHNProtocolMoonshineStreamingV2.class).withArguments(mockServiceByteStreaming, mockHandler).thenReturn(mockMoonshineStreamingV2);
        PowerMockito.whenNew(SHNProtocolMoonshineStreamingV3.class).withArguments(mockServiceByteStreaming, mockHandler).thenReturn(mockMoonshineStreamingV3);
        PowerMockito.whenNew(SHNProtocolMoonshineStreamingV4.class).withArguments(mockServiceByteStreaming, mockHandler).thenReturn(mockMoonshineStreamingV4);
    }

    @Test
    public void whenSendingData_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.sendData(testData, MoonshineStreamIdentifier.STREAM_1);

        ArgumentCaptor<byte[]> dataCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockMoonshineStreamingV3).sendData(testData, MoonshineStreamIdentifier.STREAM_1);
    }

    @Test
    public void whenSendingData_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.sendData(testData, MoonshineStreamIdentifier.STREAM_1);

        verify(mockMoonshineStreamingV3, never()).sendData(testData, MoonshineStreamIdentifier.STREAM_1);
    }

    @Test
    public void whenTransitionToErrorReceived_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        SHNResult testResult = SHNResult.SHNErrorInvalidParameter;
        subject.transitionToError(testResult);

        verify(mockMoonshineStreamingV3).transitionToError(testResult);
    }

    @Test
    public void whenTransitionToErrorReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        SHNResult testResult = SHNResult.SHNErrorInvalidParameter;
        subject.transitionToError(testResult);

        verify(mockMoonshineStreamingV3, never()).transitionToError(testResult);
    }

    @Test
    public void whenTransitionToReadyReceived_thenItShouldRelayToProtocolVersionImplementation() {
        byte[] protocolData = new byte[] {(byte)3, 0x3F, 0x3F};
        subject.onReadProtocolInformation(protocolData);

        subject.transitionToReady();

        verify(mockMoonshineStreamingV3).transitionToReady();
    }

    @Test
    public void whenTransitionToReadyReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        subject.transitionToReady();

        verify(mockMoonshineStreamingV3, never()).transitionToReady();
    }


    @Test
    public void whenDataReceived_thenItShouldRelayToProtocolVersionImplementation() {
        setupStreamingWithVersion(3);

        subject.setShnProtocolMoonshineStreamingListener(mockMoonshineStreamingListener);

        byte[] testData = new byte[] {(byte)0x80, (byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.onReceiveData(testData);

        verify(mockMoonshineStreamingV3).onReceiveData(testData);
    }

    @Test
    public void whenDataReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        subject.setShnProtocolMoonshineStreamingListener(mockMoonshineStreamingListener);

        byte[] testData = new byte[] {(byte)0x10, (byte)0x20, (byte)0x30, (byte)0x40, (byte)0x50};
        subject.onReceiveData(testData);

        verify(mockMoonshineStreamingV3, never()).onReceiveData(testData);
    }

    @Test
    public void whenAckReceived_thenItShouldRelayToProtocolVersionImplementation() {
        byte[] protocolData = new byte[] {(byte)3, 0x3F, 0x3F};
        subject.onReadProtocolInformation(protocolData);
        subject.setShnProtocolMoonshineStreamingListener(mockMoonshineStreamingListener);

        byte[] testData = new byte[] {(byte)0x40};
        subject.onReceiveAck(testData);

        verify(mockMoonshineStreamingV3).onReceiveAck(testData);
    }

    @Test
    public void whenAckReceived_AndProtocolInfoNotReceived_thenDoNotRelay() {
        // do not setup streaming

        subject.setShnProtocolMoonshineStreamingListener(mockMoonshineStreamingListener);

        byte[] testData = new byte[] {(byte)0x40};
        subject.onReceiveAck(testData);

        verify(mockMoonshineStreamingV3, never()).onReceiveAck(testData);
    }

    @Test
    public void whenOnServiceAvailable_thenServiceByteStreamingShouldReadProtocolInformation() {
        setupStreamingWithVersion(3);

        subject.onServiceAvailable();

        verify(mockMoonshineStreamingV3).onServiceAvailable();
    }

    @Test
    public void whenOnServiceUnavailable_thenItShouldRelayToProtocolVersionImplementationAndDestroyIt() {
        setupStreamingWithVersion(3);

        subject.onServiceUnavailable();

        verify(mockMoonshineStreamingV3).onServiceUnavailable();
        reset(mockMoonshineStreamingV3);

        subject.onServiceUnavailable();
        verify(mockMoonshineStreamingV3, never()).onServiceUnavailable();
    }

    @Test
    public void whenOnReadProtocolInformationWithV1_thenItShouldSetupMoonshineStreamingV1() {
        setupStreamingWithVersion(1, false);

        verify(mockMoonshineStreamingV1).onReadProtocolInformation(any(byte[].class));
    }

    @Test
    public void whenOnReadProtocolInformationWithV2_thenItShouldSetupMoonshineStreamingV2() {
        setupStreamingWithVersion(2, false);

        verify(mockMoonshineStreamingV2).onReadProtocolInformation(any(byte[].class));
    }

    @Test
    public void whenOnReadProtocolInformationWithV3_thenItShouldSetupMoonshineStreamingV3() {
        setupStreamingWithVersion(3, false);

        verify(mockMoonshineStreamingV3).onReadProtocolInformation(any(byte[].class));
    }

    @Test
    public void whenOnReadProtocolInformationWithV4_thenItShouldSetupMoonshineStreamingV4() {
        setupStreamingWithVersion(4, false);

        verify(mockMoonshineStreamingV4).onReadProtocolInformation(any(byte[].class));
    }

    @Test
    public void whenOnReadProtocolInformationWithoutInfo_thenItShouldGoInErrorState() {
        subject.onReadProtocolInformation(null);

        assertEquals(SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingState.Error, subject.getState());
    }

    @Test
    public void whenOnReadProtocolInformationWithNonExistingFutureVersion_thenItShouldGoInErrorState() {
        setupStreamingWithVersion(5);

        assertEquals(SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingState.Error, subject.getState());
    }

    private void setupStreamingWithVersion(int version) {
        setupStreamingWithVersion(version, true);
    }

    private void setupStreamingWithVersion(int version, boolean resetMock) {
        byte[] protocolData = new byte[] {(byte)version, 0x3F, 0x3F};
        subject.onReadProtocolInformation(protocolData);
        subject.onReceiveAck(new byte[] {(byte)0x40});
        if (resetMock) {
            reset(mockServiceByteStreaming);
        }
    }
}