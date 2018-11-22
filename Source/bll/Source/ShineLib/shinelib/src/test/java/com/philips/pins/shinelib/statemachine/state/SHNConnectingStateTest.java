/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothProfile;

import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;
import com.philips.pins.shinelib.tagging.SHNTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class SHNConnectingStateTest {

    private SHNConnectingState state;

    @Mock
    private SHNDeviceStateMachine stateMachineMock;

    @Mock
    private SHNDeviceResources deviceResourcesMock;

    @Before
    public void setUp()  {
        initMocks(this);
        mockStatic(SHNTagger.class);
        when(stateMachineMock.getSharedResources()).thenReturn(deviceResourcesMock);
        state = new SHNConnectingState(stateMachineMock, "SHNGattConnectingStateTest", 1000) {};
    }

    @Test
    public void givenCentralNoticesBluetoothIsOff_whenThisIsReported_thenStateBecomesDisconnected()  {

        state.onStateUpdated(SHNCentralStateNotReady);

        verify(stateMachineMock).setState(isA(SHNDisconnectingState.class));
    }

    @Test
    public void givenCentralNoticesBluetoothIsOff_whenThisIsReported_thenTagIsSentWithProperData()  {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        state.onStateUpdated(SHNCentralStateNotReady);

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Not ready for connection to the peripheral.", captor.getValue());
    }

    @Test
    public void whenConnectionStateChangesToDisconnected_thenTagIsSentWithProperData()  {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        state.onConnectionStateChange(null, -133, BluetoothProfile.STATE_DISCONNECTED);

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Connection state changed to disconnected, status [-133], newState [0]", captor.getValue());
    }
}