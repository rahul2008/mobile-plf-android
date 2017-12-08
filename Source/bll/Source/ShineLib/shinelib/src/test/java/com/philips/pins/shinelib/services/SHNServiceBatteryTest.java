/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.SHNFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SHNServiceBatteryTest {

    private SHNFactory mockedShnFactory;
    private SHNService mockedShnService;
    private SHNCharacteristic mockedShnCharacteristic;
    private SHNServiceBattery shnServiceBattery;
    private SHNIntegerResultListener mockedShnIntegerResultListener;

    @Before
    public void setUp() {
        mockedShnFactory = Mockito.mock(SHNFactory.class);
        mockedShnService = Mockito.mock(SHNService.class);
        mockedShnIntegerResultListener = Mockito.mock(SHNIntegerResultListener.class);

        when(mockedShnFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedShnService);

        mockedShnCharacteristic = Mockito.mock(SHNCharacteristic.class);
        when(mockedShnService.getSHNCharacteristic(any(UUID.class))).thenReturn(mockedShnCharacteristic);

        shnServiceBattery = new SHNServiceBattery(mockedShnFactory);
    }

    @Test
    public void initializeTest() {
        assertNotNull(shnServiceBattery);

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        ArgumentCaptor<Set> requiredSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(mockedShnFactory).createNewSHNService(uuidArgumentCaptor.capture(), requiredSetArgumentCaptor.capture(), optionalSetArgumentCaptor.capture());
        assertEquals(SHNServiceBattery.SERVICE_UUID, uuidArgumentCaptor.getValue());

        assertNotNull(requiredSetArgumentCaptor.getValue());
        assertEquals(1, requiredSetArgumentCaptor.getValue().size());
        assertTrue(requiredSetArgumentCaptor.getValue().contains(new SHNCharacteristicInfo(SHNServiceBattery.SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID, false)));

        assertNotNull(optionalSetArgumentCaptor.getValue());
        assertEquals(0, optionalSetArgumentCaptor.getValue().size());

        verify(mockedShnService).registerSHNServiceListener(shnServiceBattery);
    }

    @Test
    public void getServiceReturnsService() {
        assertEquals(mockedShnService, shnServiceBattery.getShnService());
    }

    @Test
    public void getBatteryWithResultOkTest() {
        shnServiceBattery.getBatteryLevel(mockedShnIntegerResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, new byte[]{97});

        verify(mockedShnIntegerResultListener).onActionCompleted(97, SHNResult.SHNOk);
    }

    @Test
    public void getBatteryWithTimeOutResultTest() {
        shnServiceBattery.getBatteryLevel(mockedShnIntegerResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();
        shnCommandResultReporter.reportResult(SHNResult.SHNErrorTimeout, null);

        verify(mockedShnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNErrorTimeout);
    }


    @Test
    public void getBatteryWithValueOutOfRangeTest() {
        shnServiceBattery.getBatteryLevel(mockedShnIntegerResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, new byte[]{127});

        verify(mockedShnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNErrorWhileParsing);
    }

    @Test
    public void setNotificationEnabled() {
        checkNotificationSetting(true);
    }

    @Test
    public void setNotificationDisabled() {
        checkNotificationSetting(false);
    }

    private void checkNotificationSetting(boolean enabled) {
        SHNResultListener mockedShnResultListener = Mockito.mock(SHNResultListener.class);

        shnServiceBattery.setBatteryLevelNotifications(enabled, mockedShnResultListener);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).setNotification(booleanArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();
        shnCommandResultReporter.reportResult(SHNResult.SHNOk, null);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
        assertEquals(enabled, booleanArgumentCaptor.getValue());

        if (enabled) {
            verify(mockedShnCharacteristic).setShnCharacteristicChangedListener(shnServiceBattery.shnCharacteristicChangedListener);
        } else {
            verify(mockedShnCharacteristic).setShnCharacteristicChangedListener(null);
        }

    }

    @Test
    public void stateChangedToAvailableTest() {
        shnServiceBattery.onServiceStateChanged(mockedShnService, SHNService.State.Available);
        verify(mockedShnService).transitionToReady();
    }

    @Test
    public void stateChangedToUnavailableTest() {
        shnServiceBattery.onServiceStateChanged(mockedShnService, SHNService.State.Unavailable);
        verify(mockedShnService, never()).transitionToReady();
    }
}
