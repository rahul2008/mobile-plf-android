package com.philips.pins.shinelib.statemachine.state;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNConnectingStateTest {

    private SHNConnectingState state;

    @Mock
    private SHNDeviceStateMachine stateMachineMock;

    @Mock
    private SHNDeviceResources deviceResourcesMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(stateMachineMock.getSharedResources()).thenReturn(deviceResourcesMock);
        state = new SHNConnectingState(stateMachineMock, 1000) {};
    }

    @Test
    public void givenCentralNoticesBluetoothIsOff_whenThisIsReported_thenStateBecomesDisconnected() throws Exception {
        SHNCentral centralMock = mock(SHNCentral.class);
        when(centralMock.getShnCentralState()).thenReturn(SHNCentralStateNotReady);

        state.onStateUpdated(centralMock);

        verify(stateMachineMock).setState(isA(SHNDisconnectingState.class));
    }
}