package com.philips.pins.shinelib.capabilities;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.services.healththermometer.SHNServiceHealthThermometer;
import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by 310188215 on 17/06/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class, Log.class})
public class SHNCapabilityLogSyncHealthThermometerTest {

    private SHNCapabilityLogSyncHealthThermometer shnCapabilityLogSyncHealthThermometer;
    private SHNServiceHealthThermometer mockedSHNServiceHealthThermometer;
    private SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener mockedShnCapabilitySHNCapabilityLogSynchronizationListener;
    private ArgumentCaptor<Runnable> timeOutCaptor;

    @Before
    public void setUp() {
        mockedSHNServiceHealthThermometer = mock(SHNServiceHealthThermometer.class);
        mockedShnCapabilitySHNCapabilityLogSynchronizationListener = mock(SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener.class);

        Timer mockedTimeoutTimer = mock(Timer.class);
        mockStatic(Timer.class);
        timeOutCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(Timer.createTimer(timeOutCaptor.capture(), anyLong())).thenReturn(mockedTimeoutTimer);

        mockStatic(Log.class);
        when(Log.w(anyString(), anyString())).thenReturn(0);

        shnCapabilityLogSyncHealthThermometer = new SHNCapabilityLogSyncHealthThermometer(mockedSHNServiceHealthThermometer);
        shnCapabilityLogSyncHealthThermometer.setSHNCapabilityLogSynchronizationListener(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsNotNull() {
        assertNotNull(shnCapabilityLogSyncHealthThermometer);
    }

    @Test
    public void whenCreatedServiceHealthThermometerListenerIsSet() {
        verify(mockedSHNServiceHealthThermometer).setSHNServiceHealthThermometerListener(shnCapabilityLogSyncHealthThermometer);
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenOnTheServiceTheNotificationsAreEnabled() {
        shnCapabilityLogSyncHealthThermometer.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertTrue(booleanArgumentCaptor.getValue());
    }

    private void assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult result) {
        shnCapabilityLogSyncHealthThermometer.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.startSynchronizationFromToken(null);

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));
    }

    private void assertAbortSynchronizationWithResultThenNotificationsAreDisabled() {
        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertFalse(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceTheNotificationsAreNotDisabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleThanListenerIsNotSet() {
        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanAbortSynchronizationIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));

        assertFalse(booleanArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityLogSyncHealthThermometer.getState());
    }

    private SHNTemperatureMeasurement[] generateDataAndSendIt(Date[] dates) {
        SHNTemperatureMeasurement[] measurements = new SHNTemperatureMeasurement[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
            when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(dates[i]);
            measurements[i] = mockedShnTemperatureMeasurement;
            shnCapabilityLogSyncHealthThermometer.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);
        }
        return measurements;
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedWithNoTimeStampThenProgressIsNotUpdated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(null);

        shnCapabilityLogSyncHealthThermometer.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate(any(SHNCapabilityLogSyncHealthThermometer.class), anyFloat());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenOnServiceStateChangedWithStateErrorWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L), new Date(115L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Error);

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(4, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenMeasurementReceivedWhileNotSynchronizingThenItIsIgnored() {
        Date[] dates = {new Date(100L), new Date(80L), new Date(110L), new Date(115L)};
        generateDataAndSendIt(dates);

        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates2 = {new Date()};
        generateDataAndSendIt(dates2);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenThereIsAnTimelessMeasurementThenItIsSkipped() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());

        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(0).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp()));
        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(2).getTimestamp()));
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingWithNoDataThanListenerIsNotified() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronizationFailed(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNResult.class));
    }

    @Test
    public void whenLogIsCreatedThenMeasurementsAreOfTypeBodyTemperature() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().contains(SHNDataType.BodyTemperature));
        shnLogArgumentCaptor.getValue().getContainedDataTypes().remove(SHNDataType.BodyTemperature);
        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().isEmpty());
    }
}