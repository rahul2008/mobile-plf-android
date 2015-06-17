package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by 310188215 on 17/06/15.
 */
@RunWith(PowerMockRunner.class)
public class SHNCapabilityHealthThermometerLogSyncTest {
    private SHNCapabilityHealthThermometerLogSync shnCapabilityHealthThermometerLogSync;
    private SHNServiceHealthThermometer mockedSHNServiceHealthThermometer;

    @Before
    public void setUp() {
        mockedSHNServiceHealthThermometer = mock(SHNServiceHealthThermometer.class);
        shnCapabilityHealthThermometerLogSync = new SHNCapabilityHealthThermometerLogSync(mockedSHNServiceHealthThermometer);
    }

    @Test
    public void whenCreatedThenTheInstanceIsInStateIdle() {
        assertNotNull(shnCapabilityHealthThermometerLogSync);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenRequestingTheLastSynchronizationTokenThenAObjectIsReturned() {
        assertNotNull(shnCapabilityHealthThermometerLogSync.getLastSynchronizationToken());
        assertTrue(shnCapabilityHealthThermometerLogSync.getLastSynchronizationToken() instanceof Object);
    }

    @Test
    public void whenStartSynchronizationIsCalledThenOnTheServiceTheNotificationsAreEnabled() {

    }

    @Test
    public void testStartSynchronizationFromToken() {

    }

    @Test
    public void testAbortSynchronization() {

    }
}