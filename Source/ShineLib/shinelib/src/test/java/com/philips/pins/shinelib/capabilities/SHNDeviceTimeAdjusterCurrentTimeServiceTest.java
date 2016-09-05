/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.services.SHNServiceCurrentTime;
import com.philips.pins.shinelib.utility.ExactTime256WithAdjustReason;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SHNDeviceTimeAdjusterCurrentTimeServiceTest {
    private SHNDeviceTimeAdjusterCurrentTimeService shnDeviceTimeAdjusterCurrentTimeService;
    private SHNServiceCurrentTime mockedSHNServiceCurrentTime;
    private SHNServiceCurrentTime.SHNServiceCurrentTimeListener shnServiceCurrentTimeListener;
    private byte[] TEST_DEVICE_TIME;

    @Before
    public void setUp() {
        mockedSHNServiceCurrentTime = mock(SHNServiceCurrentTime.class);

        shnDeviceTimeAdjusterCurrentTimeService = new SHNDeviceTimeAdjusterCurrentTimeService(mockedSHNServiceCurrentTime);

        ArgumentCaptor<SHNServiceCurrentTime.SHNServiceCurrentTimeListener> shnServiceCurrentTimeListenerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceCurrentTime.SHNServiceCurrentTimeListener.class);
        verify(mockedSHNServiceCurrentTime).setSHNServiceCurrentTimeListener(shnServiceCurrentTimeListenerArgumentCaptor.capture());
        shnServiceCurrentTimeListener = shnServiceCurrentTimeListenerArgumentCaptor.getValue();
    }

    @Test
    public void anInstanceCanBeCreated() {
        assertNotNull(shnDeviceTimeAdjusterCurrentTimeService);
    }

    @Test
    public void whenAnInstanceIsCreatedItRegistersAListener() {
        assertNotNull(shnServiceCurrentTimeListener);
    }

    @Test
    public void whenStateIndicatesAvailableThenGetTheCurrentTimeIsCalled() {
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        verify(mockedSHNServiceCurrentTime).getCurrentTime(any(SHNObjectResultListener.class));
    }

    @Test
    public void whenGetCurrentTimeResultIsOKThenTransitionToReadyIsCalled() {
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        ArgumentCaptor<SHNObjectResultListener> shnObjectResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNObjectResultListener.class);
        verify(mockedSHNServiceCurrentTime).getCurrentTime(shnObjectResultListenerArgumentCaptor.capture());
        TEST_DEVICE_TIME = new byte[]{
                (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
                , 6                          // month june = 6
                , 8                          // day 8th
                , 8                          // hour 8
                , 34                         // minutes 34
                , 45                         // seconds 45
                , 1                          // Day Of Week Monday (Not checked to be correct for the date)
                , (byte) 0x80                // Fraction256 128
                , 0b00000001                 // Adjust reason
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(TEST_DEVICE_TIME);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        shnObjectResultListenerArgumentCaptor.getValue().onActionCompleted(new ExactTime256WithAdjustReason(byteBuffer), SHNResult.SHNOk);
        verify(mockedSHNServiceCurrentTime).transitionToReady();
    }

    @Test
    public void whenAdjustToHostTimeIsCalledTheTimeIsAdjusted() {
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        ArgumentCaptor<SHNObjectResultListener> shnObjectResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNObjectResultListener.class);
        verify(mockedSHNServiceCurrentTime).getCurrentTime(shnObjectResultListenerArgumentCaptor.capture());
        TEST_DEVICE_TIME = new byte[]{
                (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
                , 6                          // month june = 6
                , 8                          // day 8th
                , 8                          // hour 8
                , 34                         // minutes 34
                , 45                         // seconds 45
                , 1                          // Day Of Week Monday (Not checked to be correct for the date)
                , (byte) 0x80                // Fraction256 128
                , 0b00000001                 // Adjust reason
        };
        ByteBuffer byteBuffer = ByteBuffer.wrap(TEST_DEVICE_TIME);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ExactTime256WithAdjustReason exactTime256WithAdjustReason = new ExactTime256WithAdjustReason(byteBuffer);
        shnObjectResultListenerArgumentCaptor.getValue().onActionCompleted(exactTime256WithAdjustReason, SHNResult.SHNOk);
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - shnDeviceTimeAdjusterCurrentTimeService.adjustTimestampToHostTime(exactTime256WithAdjustReason.exactTime256.exactTime256Date.getTime());
        assertTrue(-2 < diff && diff < 2);
    }

    @Test
    public void whenStateIndicatesAvailableTwiceInARowThenGetTheCurrentTimeIsCalledOnlyOnce() {
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        verify(mockedSHNServiceCurrentTime).getCurrentTime(any(SHNObjectResultListener.class));
    }

    @Test
    public void whenTheStateWasUnavailableStateIndicatesAvailableThenGetTheCurrentTimeIsCalledOnlyOnce() {
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Unavailable);
        shnServiceCurrentTimeListener.onServiceStateChanged(mockedSHNServiceCurrentTime, SHNService.State.Available);
        verify(mockedSHNServiceCurrentTime, times(2)).getCurrentTime(any(SHNObjectResultListener.class));
    }
}
