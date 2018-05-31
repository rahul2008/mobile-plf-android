package com.philips.pins.shinelib.statemachine.state;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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

    @Before
    public void setUp() throws Exception {
        initMocks(this);
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
}