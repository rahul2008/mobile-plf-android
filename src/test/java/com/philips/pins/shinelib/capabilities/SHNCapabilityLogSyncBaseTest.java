package com.philips.pins.shinelib.capabilities;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.framework.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
public class SHNCapabilityLogSyncBaseTest {

    private TestSHNCapabilityLogSyncBase testSHNCapabilityLogSyncBase;
    private SHNCapabilityLogSynchronization.Listener mockedShnCapabilityListener;
    private Timer mockedTimeoutTimer;
    private ArgumentCaptor<Runnable> timeOutCaptor;

    @Before
    public void setUp() throws Exception {
        mockedShnCapabilityListener = mock(SHNCapabilityLogSynchronization.Listener.class);

        mockedTimeoutTimer = mock(Timer.class);
        mockStatic(Timer.class);
        timeOutCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(Timer.createTimer(timeOutCaptor.capture(), anyLong())).thenReturn(mockedTimeoutTimer);

        mockStatic(Log.class);
        when(Log.w(anyString(), anyString())).thenReturn(0);

        testSHNCapabilityLogSyncBase = new TestSHNCapabilityLogSyncBase();
        testSHNCapabilityLogSyncBase.setListener(mockedShnCapabilityListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsInStateIdle() {
        assertNotNull(testSHNCapabilityLogSyncBase);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenCreatedThenLogIsNotCreated() {
        verify(mockedShnCapabilityListener, never()).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNLog.class), any(SHNResult.class));
        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));
    }

    @Test
    public void whenRequestingTheLastSynchronizationTokenThenAObjectIsReturned() {
        assertNotNull(testSHNCapabilityLogSyncBase.getLastSynchronizationToken());
    }

    @Test
    public void whenStartSynchronizationIsCalledWithNoListenerAttachedThanStateIsSynchronizingAndListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilityListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilityListener, never()).onProgressUpdate(testSHNCapabilityLogSyncBase, 0f);
    }

    @Test
    public void whenMeasurementReceivedWithNoListenerAttachedThanStateIsSynchronizingAndListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        generateDataAndSendIt(new Date[]{new Date()});

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilityListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilityListener, never()).onProgressUpdate(testSHNCapabilityLogSyncBase, 0f);
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleStateIsIdle() {
        testSHNCapabilityLogSyncBase.abortSynchronization();

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenStateIsSynchronizing() {
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
    }

    private void startCapabilityWithResult(SHNResult result) {
        testSHNCapabilityLogSyncBase.setResult(result);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenStateIsSynchronizing() {
        startCapabilityWithResult(SHNResult.SHNOk);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenTimerIsStarted() {
        startCapabilityWithResult(SHNResult.SHNOk);

        verify(mockedTimeoutTimer).restart();
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<SHNCapabilityLogSynchronization> shnCapabilityLogSynchronizationArgumentCaptor = ArgumentCaptor.forClass(SHNCapabilityLogSynchronization.class);
        verify(mockedShnCapabilityListener).onProgressUpdate(shnCapabilityLogSynchronizationArgumentCaptor.capture(), floatArgumentCaptor.capture());

        assertEquals(0.0f, floatArgumentCaptor.getValue(), 0.0f);
        assertEquals(testSHNCapabilityLogSyncBase, shnCapabilityLogSynchronizationArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);

        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        verify(mockedShnCapabilityListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenListenerIsCalled() {
        startCapabilityWithResult(SHNResult.SHNOk);

        verify(mockedShnCapabilityListener).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultNotOkStateIsIdle() {
        startCapabilityWithResult(SHNResult.SHNBluetoothDisabledError);

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilityListener, times(2)).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenAbortSynchronizationIsCalledFirstTimeThenListenerIsNotified() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilityListener).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilityListener).onProgressUpdate(testSHNCapabilityLogSyncBase, 1f);
        verify(mockedTimeoutTimer).stop();
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithResultOkStateIsIdle() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceThenListenerIsNotNotifiedAgain() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();
        Mockito.reset(mockedShnCapabilityListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilityListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenTheTimerTimesOutWithNoDataThenOnLogSynchronizationFailedIsReported() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilityListener).onLogSynchronizationFailed(testSHNCapabilityLogSyncBase, SHNResult.SHNResponseIncompleteError);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    private SHNLogItem[] generateDataAndSendIt(Date[] dates) {
        SHNLogItem[] items = new SHNLogItem[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNLogItem mockedShnLogItem = Mockito.mock(SHNLogItem.class);
            when(mockedShnLogItem.getTimestamp()).thenReturn(dates[i]);
            items[i] = mockedShnLogItem;

            testSHNCapabilityLogSyncBase.onMeasurementReceived(mockedShnLogItem);
        }
        return items;
    }

    @Test
    public void whenOnMeasurementReceivedInStateIdleThenProgressIsNotUpdated() {
        generateDataAndSendIt(new Date[]{new Date()});

        verify(mockedShnCapabilityListener, never()).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), anyFloat());
    }

    @Test
    public void whenOnMeasurementReceivedThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilityListener).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), floatArgumentCaptor.capture());

        assertEquals((float) 1 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer).restart();
    }

    @Test
    public void whenOnMeasurementReceivedMultipleTimesThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilityListener, times(3)).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), floatArgumentCaptor.capture());

        assertEquals((float) 3 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer, times(3)).restart();
    }

    @Test
    public void whenTheTimerTimesOutThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);

        generateDataAndSendIt(new Date[]{new Date()});

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilityListener).onProgressUpdate(testSHNCapabilityLogSyncBase, 1.0f);
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date()});

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));

        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreatedWithMultipleItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));

        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreatedWithSortedItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
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
    public void whenLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        generateDataAndSendIt(new Date[]{new Date()});
        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilityListener, never()).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNLog.class), any(SHNResult.class));
    }

    @Test
    public void whenNoLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));
    }

    private class TestSHNCapabilityLogSyncBase extends SHNCapabilityLogSyncBase {

        private SHNResult result = SHNResult.SHNOk;

        @Override
        void setupToReceiveMeasurements() {
            handleResultOfMeasurementsSetup(result);
        }

        @Override
        void teardownReceivingMeasurements() {

        }

        public void setResult(SHNResult result) {
            this.result = result;
        }
    }
}