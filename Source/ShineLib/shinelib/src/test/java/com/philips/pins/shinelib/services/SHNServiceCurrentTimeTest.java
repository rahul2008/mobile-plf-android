package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ExactTime256WithAdjustReason;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by 310188215 on 07/07/15.
 */
public class SHNServiceCurrentTimeTest {
    private SHNServiceCurrentTime shnServiceCurrentTime;
    private SHNService.SHNServiceListener shnServiceListener;
    private SHNFactory mockedSHNFactory;
    private SHNService mockedSHNService;
    private SHNObjectResultListener mockedSHNObjectResultListener;
    private ArgumentCaptor<SHNResult> shnResultArgumentCaptor;
    private SHNCharacteristic mockedSHNCharacteristicCurrentTime;
    private ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor;
    private ArgumentCaptor<SHNObjectResultListener> shnObjectResultListenerArgumentCaptor;
    private SimpleDateFormat simpleDateFormat;
    private SHNServiceCurrentTime.SHNServiceCurrentTimeListener mockedSHNServiceCurrentTimeListener;

    @Before
    public void setUp() {
        simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS", Locale.US);
        mockedSHNFactory = mock(SHNFactory.class);
        mockedSHNService = mock(SHNService.class);
        mockedSHNObjectResultListener = mock(SHNObjectResultListener.class);
        mockedSHNCharacteristicCurrentTime = mock(SHNCharacteristic.class);
        mockedSHNServiceCurrentTimeListener = mock(SHNServiceCurrentTime.SHNServiceCurrentTimeListener.class);

        when(mockedSHNFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedSHNService);

        when(mockedSHNService.getState()).thenReturn(SHNService.State.Unavailable);
        when(mockedSHNService.getSHNCharacteristic(SHNServiceCurrentTime.CURRENT_TIME_CHARACTERISTIC_UUID)).thenReturn(mockedSHNCharacteristicCurrentTime);

        shnServiceCurrentTime = new SHNServiceCurrentTime(mockedSHNFactory);

        // Retrieve the SHNServiceListener by capturing it during creation. The test is seperate.
        ArgumentCaptor<SHNService.SHNServiceListener> shnServiceListenerArgumentCaptor = ArgumentCaptor.forClass(SHNService.SHNServiceListener.class);
        verify(mockedSHNService).registerSHNServiceListener(shnServiceListenerArgumentCaptor.capture());
        shnServiceListener = shnServiceListenerArgumentCaptor.getValue();
        shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        shnObjectResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNObjectResultListener.class);

        // set the mockedListener
        shnServiceCurrentTime.setSHNServiceCurrentTimeListener(mockedSHNServiceCurrentTimeListener);
    }

    private void serviceSetupServiceStateChangedToAvailable() { // Make sure the mock returns the proper state before calling the change handler
        when(mockedSHNService.getState()).thenReturn(SHNService.State.Available);
        shnServiceListener.onServiceStateChanged(mockedSHNService, SHNService.State.Available);
    }

    @Test
    public void anInstanceCanBeCreated() {
        assertNotNull(shnServiceCurrentTime);
    }

    @Test
    public void whenTheCTServiceIsCreatedThenTheProperUUIDSArePassedIntoTheSHNFactory() {
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Set> mandatoryUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(mockedSHNFactory).createNewSHNService(uuidArgumentCaptor.capture(), mandatoryUUIDSetArgumentCaptor.capture(), optionalUUIDSetArgumentCaptor.capture());

        assertEquals(SHNServiceCurrentTime.SERVICE_UUID, uuidArgumentCaptor.getValue());
        assertNotNull(mandatoryUUIDSetArgumentCaptor.getValue());
        assertEquals(1, mandatoryUUIDSetArgumentCaptor.getValue().size());
        assertTrue(mandatoryUUIDSetArgumentCaptor.getValue().contains(SHNServiceCurrentTime.CURRENT_TIME_CHARACTERISTIC_UUID));
        assertNotNull(optionalUUIDSetArgumentCaptor.getValue());
        assertEquals(2, optionalUUIDSetArgumentCaptor.getValue().size());
        assertTrue(optionalUUIDSetArgumentCaptor.getValue().contains(SHNServiceCurrentTime.REFERENCE_TIME_INFO_CHARACTERISTIC_UUID));
        assertTrue(optionalUUIDSetArgumentCaptor.getValue().contains(SHNServiceCurrentTime.LOCAL_TIME_INFO_CHARACTERISTIC_UUID));
    }

    @Test
    public void whenTheCTServiceIsCreatedThenAOnServiceStateChangedListenerIsRegistered() {
        assertNotNull(shnServiceListener);
    }

    @Test
    public void whenTheSHNServiceReportsThatItIsAvailableThenTheStateChangeListenerIsCalled() {
        serviceSetupServiceStateChangedToAvailable();
        verify(mockedSHNServiceCurrentTimeListener).onServiceStateChanged(shnServiceCurrentTime, SHNService.State.Available);
    }

    @Test
    public void whenTransitionToReadyIsCalledThenItIsRelayedToSHNService() {
        shnServiceCurrentTime.transitionToReady();
        verify(mockedSHNService).transitionToReady();
    }

    @Test
    public void whenTransitionToErrorIsCalledThenItIsRelayedToSHNService() {
        shnServiceCurrentTime.transitionToError();
        verify(mockedSHNService).transitionToError();
    }

    @Test
    public void whenTheServiceIsUnavailableThenGetCurrentTimeReturnsAnError() {
        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);

        verify(mockedSHNObjectResultListener).onActionCompleted(anyObject(), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNErrorServiceUnavailable, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenTheServiceIsAvailableThenGetCurrentTimeReadsFromTheCurrentTimeCharacteristic() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);

        verify(mockedSHNCharacteristicCurrentTime).read(any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenTheCurrentTimeCharacteristicProvidesValidDataThenTheResultListenerIndicatesSuccess() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);
        verify(mockedSHNCharacteristicCurrentTime).read(shnCommandResultReporterArgumentCaptor.capture()); // Just to capture the ResultReporter
        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, new byte[]{
                (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
                , 6                          // month june = 6
                , 8                          // day 8th
                , 8                          // hour 8
                , 34                         // minutes 34
                , 45                         // seconds 45
                , 1                          // Day Of Week Monday (Not checked to be correct for the date)
                , (byte) 0x80                // Fraction256 128
                , 0b00000001                 // Adjust reason
        });
        verify(mockedSHNObjectResultListener).onActionCompleted(shnObjectResultListenerArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
        assertNotNull(shnObjectResultListenerArgumentCaptor.getValue());
        ExactTime256WithAdjustReason exactTime256WithAdjustReason = (ExactTime256WithAdjustReason) shnObjectResultListenerArgumentCaptor.getValue();
        assertTrue(exactTime256WithAdjustReason.adjustReason.manualTimeUpdate);
        assertEquals("2015-06-08 08:34:45:500", simpleDateFormat.format(exactTime256WithAdjustReason.exactTime256.exactTime256Date.getTime()));
    }

    @Test
    public void whenTheCurrentTimeCharacteristicProvidesToFewBytesThenTheResultListenerIndicatesResponseIncomplete() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);
        verify(mockedSHNCharacteristicCurrentTime).read(shnCommandResultReporterArgumentCaptor.capture()); // Just to capture the ResultReporter
        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, new byte[]{
                (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
                , 6                          // month june = 6
                , 8                          // day 8th
                , 8                          // hour 8
                , 34                         // minutes 34
                , 45                         // seconds 45
                , 1                          // Day Of Week Monday (Not checked to be correct for the date)
                , (byte) 0x80                // Fraction256 128
                //, 0b00000001                 // Adjust reason
        });

        verify(mockedSHNObjectResultListener).onActionCompleted(shnObjectResultListenerArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNErrorResponseIncomplete, shnResultArgumentCaptor.getValue());
        assertNull(shnObjectResultListenerArgumentCaptor.getValue());
    }

    @Test
    public void whenTheCurrentTimeCharacteristicProvidesToFewBytesThenTheResultListenerIndicatesParseError() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);
        verify(mockedSHNCharacteristicCurrentTime).read(shnCommandResultReporterArgumentCaptor.capture()); // Just to capture the ResultReporter
        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, new byte[]{
                (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
                , 13                          // month june = 6
                , 8                          // day 8th
                , 8                          // hour 8
                , 34                         // minutes 34
                , 45                         // seconds 45
                , 1                          // Day Of Week Monday (Not checked to be correct for the date)
                , (byte) 0x80                // Fraction256 128
                , 0b00000001                 // Adjust reason
        });

        verify(mockedSHNObjectResultListener).onActionCompleted(shnObjectResultListenerArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNErrorWhileParsing, shnResultArgumentCaptor.getValue());
        assertNull(shnObjectResultListenerArgumentCaptor.getValue());
    }

    @Test
    public void whenTheCharacteristicReadReturnsAnErrorThenTheResultListenerIndicatesThatError() {
        serviceSetupServiceStateChangedToAvailable();

        shnServiceCurrentTime.getCurrentTime(mockedSHNObjectResultListener);
        verify(mockedSHNCharacteristicCurrentTime).read(shnCommandResultReporterArgumentCaptor.capture()); // Just to capture the ResultReporter
        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNErrorConnectionLost, null);

        verify(mockedSHNObjectResultListener).onActionCompleted(shnObjectResultListenerArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNErrorConnectionLost, shnResultArgumentCaptor.getValue());
        assertNull(shnObjectResultListenerArgumentCaptor.getValue());
    }
}