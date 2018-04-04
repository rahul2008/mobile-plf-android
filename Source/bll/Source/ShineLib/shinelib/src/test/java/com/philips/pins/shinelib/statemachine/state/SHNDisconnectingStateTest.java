package com.philips.pins.shinelib.statemachine.state;

import android.content.Context;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SHNDisconnectingState.class, Timer.class})
public class SHNDisconnectingStateTest {

    private SHNDisconnectingState state;

    @Mock
    private Timer timerMock;

    @Mock
    private SHNDeviceStateMachine statemachineMock;

    @Mock
    private Context mockedContext;

    @Mock
    private BTDevice mockedBTDevice;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private BTGatt.BTGattCallback mockedBtGattCallback;

    @Mock
    private SHNDeviceResources sharedResources;

    @Mock
    private SHNCentral.SHNCentralListener mockedSHNCentralListener;

    @Mock
    private BTGatt btGatt;

    @Captor
    private ArgumentCaptor<Runnable> timerRunnable;

    @Before
    public void setUp() {
        initMocks(this);

        PowerMockito.mockStatic(Timer.class);
        PowerMockito.when(Timer.createTimer(timerRunnable.capture(), anyLong())).thenReturn(timerMock);

        doReturn(mockedBTDevice).when(sharedResources).getBtDevice();
        doReturn(mockedSHNCentral).when(sharedResources).getShnCentral();
        doReturn(mockedBtGattCallback).when(sharedResources).getBTGattCallback();
        doReturn(sharedResources).when(statemachineMock).getSharedResources();
        doReturn(btGatt).when(sharedResources).getBtGatt();
        state = new SHNDisconnectingState(statemachineMock);
    }

    @Test
    public void whenStateIsEntered_thenDisconnectTimerIsStarted() throws Exception {

        state.onEnter();

        verify(timerMock).restart();
    }

    @Test
    public void givenStateIsEntered_whenTimerExpires_thenStateIsSetToDisconnected() throws Exception {
        state.onEnter();

        timerRunnable.getValue().run();

        verify(statemachineMock).setState(isA(SHNDisconnectedState.class));
    }
}