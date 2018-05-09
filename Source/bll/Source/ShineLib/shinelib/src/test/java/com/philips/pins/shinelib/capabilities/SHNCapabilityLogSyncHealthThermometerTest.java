package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.services.healththermometer.SHNServiceHealthThermometer;
import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SHNCapabilityLogSyncHealthThermometerTest {

    private SHNCapabilityLogSyncHealthThermometer shnCapabilityLogSyncHealthThermometer;
    private SHNServiceHealthThermometer mockedSHNServiceHealthThermometer;
    private SHNDeviceTimeAdjuster mockedSHNDeviceTimeAdjuster;
    private SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener mockedShnCapabilitySHNCapabilityLogSynchronizationListener;

    @Before
    public void setUp() {
        mockedSHNServiceHealthThermometer = mock(SHNServiceHealthThermometer.class);
        mockedSHNDeviceTimeAdjuster = mock(SHNDeviceTimeAdjuster.class);
        mockedShnCapabilitySHNCapabilityLogSynchronizationListener = mock(SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener.class);

        shnCapabilityLogSyncHealthThermometer = new SHNCapabilityLogSyncHealthThermometer(mockedSHNServiceHealthThermometer, mockedSHNDeviceTimeAdjuster);
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
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), (SHNResultListener) any());
        assertFalse(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceTheNotificationsAreNotDisabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), (SHNResultListener) any());
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleThanListenerIsNotSet() {
        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), (SHNResultListener) any());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanAbortSynchronizationIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), (SHNResultListener) any());

        assertFalse(booleanArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityLogSyncHealthThermometer.getState());
    }

    private SHNTemperatureMeasurement[] generateDataAndSendIt(Date[] dates) {
        SHNTemperatureMeasurement[] measurements = new SHNTemperatureMeasurement[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNTemperatureMeasurement mockedShnTemperatureMeasurement = mock(SHNTemperatureMeasurement.class);
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

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(null);

        shnCapabilityLogSyncHealthThermometer.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate((SHNCapabilityLogSynchronization) any(), anyFloat());
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
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

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
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

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

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    private void setUpTimeAdjuster(final long delta) {
        doAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                return (Long) invocation.getArguments()[0] + delta;
            }
        }).when(mockedSHNDeviceTimeAdjuster).adjustTimestampToHostTime(anyLong());
    }

    @Test
    public void whenThereIsAnTimelessMeasurementThenItIsSkipped() {
        setUpTimeAdjuster(10000);
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());

        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(0).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp()));
        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(2).getTimestamp()));
    }

    @Test
    public void whenMeasurementIsReceivedWithATimeStampThenItTimeIsAdjusted() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date date = new Date(100L);
        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(date);
        shnCapabilityLogSyncHealthThermometer.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        verify(mockedSHNDeviceTimeAdjuster).adjustTimestampToHostTime(date.getTime());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingWithNoDataThanListenerIsNotified() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityLogSyncHealthThermometer.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(
                isA(SHNCapabilityLogSyncHealthThermometer.class),
                isA(SHNLog.class),
                isA(SHNResult.class));
    }

    @Test
    public void whenLogIsCreatedThenMeasurementsAreOfTypeBodyTemperature() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().contains(SHNDataType.BodyTemperature));
        assertEquals(1, shnLogArgumentCaptor.getValue().getContainedDataTypes().size());
    }

    @Test
    public void whenLogIsCreatedThanLofContainsAdjustedTime() {
        long delta = 100000;
        setUpTimeAdjuster(delta);
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date date = new Date(100L);
        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(date);
        shnCapabilityLogSyncHealthThermometer.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        shnCapabilityLogSyncHealthThermometer.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());

        long receivedDate = shnLogArgumentCaptor.getValue().getLogItems().get(0).getTimestamp().getTime();
        assertEquals(receivedDate, date.getTime() + delta);
    }
}