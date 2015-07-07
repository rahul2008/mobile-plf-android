package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.SHNFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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

    @Before
    public void setUp() {
        mockedSHNFactory = mock(SHNFactory.class);
        mockedSHNService = mock(SHNService.class);

        when(mockedSHNFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedSHNService);

        shnServiceCurrentTime = new SHNServiceCurrentTime(mockedSHNFactory);

        // Retrieve the SHNServiceListener by capturing it during creation. The test is seperate.
        ArgumentCaptor<SHNService.SHNServiceListener> shnServiceListenerArgumentCaptor = ArgumentCaptor.forClass(SHNService.SHNServiceListener.class);
        verify(mockedSHNService).registerSHNServiceListener(shnServiceListenerArgumentCaptor.capture());
        shnServiceListener = shnServiceListenerArgumentCaptor.getValue();
    }

    private void serviceSetupServiceStateChangedToAvailable() {
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
    public void whenTheSHNServiceReportsThatItIsAvailableThenTheServiceIsTransitionedToReady() {
        serviceSetupServiceStateChangedToAvailable();
        verify(mockedSHNService).transitionToReady();
    }
}