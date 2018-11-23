/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SHNDisconnectingState.class, Timer.class, SHNTagger.class})
public class SHNDisconnectingStateTest {

    private SHNDisconnectingState state;

    @Mock
    private Timer timerMock;

    @Mock
    private SHNDeviceStateMachine mockedStatemachine;

    @Mock
    private BTDevice mockedBTDevice;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private SHNDeviceResources sharedResources;

    @Mock
    private BTGatt mockedBtGatt;

    @Captor
    private ArgumentCaptor<Runnable> timerRunnable;

    @Before
    public void setUp() {
        initMocks(this);

        PowerMockito.mockStatic(Timer.class);
        mockStatic(SHNTagger.class);
        PowerMockito.when(Timer.createTimer(timerRunnable.capture(), anyLong())).thenReturn(timerMock);

        doReturn(mockedBTDevice).when(sharedResources).getBtDevice();
        doReturn(mockedSHNCentral).when(sharedResources).getShnCentral();
        doReturn(mockedBtGatt).when(sharedResources).getBtGatt();

        doReturn(sharedResources).when(mockedStatemachine).getSharedResources();

        state = new SHNDisconnectingState(mockedStatemachine);
    }

    @Test
    public void whenStateIsEntered_thenDisconnectTimerIsStarted() {

        state.onEnter();

        verify(timerMock).restart();
    }

    @Test
    public void givenStateIsEntered_whenTimerExpires_thenStateIsSetToDisconnected() {
        state.onEnter();

        timerRunnable.getValue().run();

        verify(mockedStatemachine).setState(isA(SHNDisconnectedState.class));
    }

    @Test
    public void whenStateIsChangedToNotReady_thenTagIsSentWithProperData() {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        state.onStateUpdated(SHNCentralStateNotReady);

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Not ready for connection to the peripheral.", captor.getValue());
    }
}