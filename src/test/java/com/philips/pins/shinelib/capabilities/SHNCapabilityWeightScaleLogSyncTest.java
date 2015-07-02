package com.philips.pins.shinelib.capabilities;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;
import com.philips.pins.shinelib.datatypes.SHNDataWeightMeasurement;

import org.junit.Assert;
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
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class, Log.class})
public class SHNCapabilityWeightScaleLogSyncTest{

    private SHNCapabilityWeightScaleLogSync shnCapabilityWeightScaleLogSync;
    private SHNServiceWeightScale mockedShnServiceWeightScale;

    private SHNCapabilityLogSynchronization.Listener mockedShnCapabilityListener;
    private ArgumentCaptor<Runnable> timeOutCaptor;

    @Before
    public void setUp() {
        mockedShnServiceWeightScale = mock(SHNServiceWeightScale.class);
        mockedShnCapabilityListener = mock(SHNCapabilityLogSynchronization.Listener.class);

        Timer mockedTimeoutTimer = mock(Timer.class);
        mockStatic(Timer.class);
        timeOutCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(Timer.createTimer(timeOutCaptor.capture(), anyLong())).thenReturn(mockedTimeoutTimer);

        mockStatic(Log.class);
        when(Log.w(anyString(), anyString())).thenReturn(0);

        shnCapabilityWeightScaleLogSync = new SHNCapabilityWeightScaleLogSync(mockedShnServiceWeightScale);
        shnCapabilityWeightScaleLogSync.setListener(mockedShnCapabilityListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsNotNull() {
        Assert.assertNotNull(shnCapabilityWeightScaleLogSync);
    }

    @Test
    public void whenCreatedServiceWeightScaleListenerIsSet() {
        verify(mockedShnServiceWeightScale).setShnWeightServiceListener(shnCapabilityWeightScaleLogSync);
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenOnTheServiceTheNotificationsAreEnabled() {
        shnCapabilityWeightScaleLogSync.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertTrue(booleanArgumentCaptor.getValue());
    }

    private void assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult result) {
        shnCapabilityWeightScaleLogSync.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        shnCapabilityWeightScaleLogSync.startSynchronizationFromToken(null);

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    private void assertAbortSynchronizationWithResultThenNotificationsAreDisabled() {
        shnCapabilityWeightScaleLogSync.abortSynchronization();

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertFalse(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceTheNotificationsAreNotDisabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();
        Mockito.reset(mockedShnServiceWeightScale);

        shnCapabilityWeightScaleLogSync.abortSynchronization();

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleThanListenerIsNotSet() {
        shnCapabilityWeightScaleLogSync.abortSynchronization();

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanAbortSynchronizationIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        shnCapabilityWeightScaleLogSync.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));

        assertFalse(booleanArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityWeightScaleLogSync.getState());
    }

    private SHNDataWeightMeasurement[] generateDataAndSendIt(Date[] dates) {
        SHNDataWeightMeasurement[] measurements = new SHNDataWeightMeasurement[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNDataWeightMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNDataWeightMeasurement.class);
            when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(dates[i]);
            when(mockedShnTemperatureMeasurement.getSHNDataType()).thenReturn(SHNDataType.WeightMeasurement);
            measurements[i] = mockedShnTemperatureMeasurement;
            shnCapabilityWeightScaleLogSync.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedShnTemperatureMeasurement);
        }
        return measurements;
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedWithNoTimeStampThenProgressIsNotUpdated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);

        SHNDataWeightMeasurement mockedMeasurement = Mockito.mock(SHNDataWeightMeasurement.class);
        when(mockedMeasurement.getTimestamp()).thenReturn(null);
        when(mockedMeasurement.getSHNDataType()).thenReturn(SHNDataType.WeightMeasurement);

        shnCapabilityWeightScaleLogSync.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedMeasurement);

        verify(mockedShnCapabilityListener, never()).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), anyFloat());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityWeightScaleLogSync.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenOnServiceStateChangedWithStateErrorWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L), new Date(115L)};
        generateDataAndSendIt(dates);

        shnCapabilityWeightScaleLogSync.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Error);

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(4, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenMeasurementReceivedWhileNotSynchronizingThenItIsIgnored() {
        Date[] dates = {new Date(100L), new Date(80L), new Date(110L), new Date(115L)};
        generateDataAndSendIt(dates);

        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);

        Date[] dates2 = {new Date()};
        generateDataAndSendIt(dates2);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenThereIsAnTimelessMeasurementThenItIsSkipped() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());

        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(0).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp()));
        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(2).getTimestamp()));
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingWithNoDataThanListenerIsNotified() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnServiceWeightScale);

        shnCapabilityWeightScaleLogSync.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        verify(mockedShnCapabilityListener).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));
    }

    @Test
    public void whenLogIsCreatedThenMeasurementsAreOfTypeWeightMeasurement() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().contains(SHNDataType.WeightMeasurement));
        shnLogArgumentCaptor.getValue().getContainedDataTypes().remove(SHNDataType.WeightMeasurement);
        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().isEmpty());
    }
}