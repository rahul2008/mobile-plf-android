package com.philips.pins.shinelib.statemachine.state;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class SHNReadyStateTest {

    @Mock
    private SHNDeviceStateMachine stateMachineMock;

    @Mock
    private SHNCentral centralMock;

    private SHNReadyState readyState;

    @Mock
    private SHNDeviceResources sharedResourcesMock;

    @Mock
    private BTGatt btGattMock;

    private Set<SHNCharacteristicInfo> requiredCharacteristics;
    private Set<SHNCharacteristicInfo> optionalCharacteristics;
    private SHNCharacteristicInfo mockedRequiredCharacteristicInfo;
    private SHNCharacteristicInfo mockedOptionalCharacteristicInfo;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(SHNTagger.class);

        when(stateMachineMock.getSharedResources()).thenReturn(sharedResourcesMock);
        when(sharedResourcesMock.getBtGatt()).thenReturn(btGattMock);
        readyState = new SHNReadyState(stateMachineMock);
    }

    @Test
    public void whenCentralNotifiesNotReady_thenStateTransitionsToDisconnecting() throws Exception {
        when(centralMock.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateNotReady);

        readyState.onStateUpdated(centralMock);

        verify(stateMachineMock).setState(isA(SHNDisconnectingState.class));
    }

    @Test
    public void whenCentralNotifiesNotReady_thenTagIsSentWithProperData() throws Exception {
        when(centralMock.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateNotReady);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        readyState.onStateUpdated(centralMock);

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Not ready for connection to the peripheral.", captor.getValue());
    }

    @Test
    public void whenServiceStateChangedToError_thenTagIsSentWithProperData() throws Exception {

        UUID serviceUUID = UUID.randomUUID();
        requiredCharacteristics = new HashSet<>();
        optionalCharacteristics = new HashSet<>();
        mockedRequiredCharacteristicInfo = new SHNCharacteristicInfo(UUID.randomUUID(), false);
        requiredCharacteristics.add(mockedRequiredCharacteristicInfo);
        mockedOptionalCharacteristicInfo = new SHNCharacteristicInfo(UUID.randomUUID(), false);
        optionalCharacteristics.add(mockedOptionalCharacteristicInfo);
        SHNService shnService = new SHNService(serviceUUID, requiredCharacteristics, optionalCharacteristics);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        readyState.onServiceStateChanged(shnService, SHNService.State.Error);

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        String result = String.format("Service [%s] state changed to error in SHNReadyState, moving to SHNDisconnectingState", serviceUUID.toString());
        assertEquals(result, captor.getValue());
    }
}