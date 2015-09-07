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
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
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
    private SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener mockedShnCapabilitySHNCapabilityLogSynchronizationListener;
    private Timer mockedTimeoutTimer;
    private ArgumentCaptor<Runnable> timeOutCaptor;

    @Before
    public void setUp() throws Exception {
        mockedShnCapabilitySHNCapabilityLogSynchronizationListener = mock(SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener.class);

        mockedTimeoutTimer = mock(Timer.class);
        mockStatic(Timer.class);
        timeOutCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(Timer.createTimer(timeOutCaptor.capture(), anyLong())).thenReturn(mockedTimeoutTimer);

        mockStatic(Log.class);
        when(Log.w(anyString(), anyString())).thenReturn(0);

        testSHNCapabilityLogSyncBase = new TestSHNCapabilityLogSyncBase();
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsInStateIdle() {
        assertNotNull(testSHNCapabilityLogSyncBase);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenCreatedThenLogIsNotCreated() {
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNLog.class), any(SHNResult.class));
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNResult.class));
    }

    @Test
    public void whenRequestingTheLastSynchronizationTokenThenAObjectIsReturned() {
        assertNotNull(testSHNCapabilityLogSyncBase.getLastSynchronizationToken());
    }

    @Test
    public void whenStartSynchronizationIsCalledWithNoListenerAttachedThanStateIsSynchronizingAndListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate(testSHNCapabilityLogSyncBase, 0f);
    }

    @Test
    public void whenMeasurementReceivedWithNoListenerAttachedThanStateIsSynchronizingAndListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        generateDataAndSendIt(new Date[]{new Date()});

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate(testSHNCapabilityLogSyncBase, 0f);
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
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate(shnCapabilityLogSynchronizationArgumentCaptor.capture(), floatArgumentCaptor.capture());

        assertEquals(0.0f, floatArgumentCaptor.getValue(), 0.0f);
        assertEquals(testSHNCapabilityLogSyncBase, shnCapabilityLogSynchronizationArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenListenerIsCalled() {
        startCapabilityWithResult(SHNResult.SHNOk);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultNotOkThenStateIsIdle() {
        startCapabilityWithResult(SHNResult.SHNBluetoothDisabledError);

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, times(2)).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultNotOkThenTimerIsStopped() {
        startCapabilityWithResult(SHNResult.SHNBluetoothDisabledError);

        verify(mockedTimeoutTimer).stop();
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultNotOkThenTimerIsNotRestarted() {
        startCapabilityWithResult(SHNResult.SHNBluetoothDisabledError);

        InOrder inOrder = inOrder(mockedTimeoutTimer);
        inOrder.verify(mockedTimeoutTimer).restart();
        inOrder.verify(mockedTimeoutTimer).stop();
        inOrder.verify(mockedTimeoutTimer, never()).restart();
    }

    @Test
    public void whenAbortSynchronizationIsCalledFirstTimeThenListenerIsNotified() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate(testSHNCapabilityLogSyncBase, 1f);
        verify(mockedTimeoutTimer).stop();
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithResultOkStateIsIdle() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceThenListenerIsNotNotifiedAgain() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenTheTimerTimesOutWithNoDataThenOnLogSynchronizedWithNoLogIsReported() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(testSHNCapabilityLogSyncBase, null, SHNResult.SHNOk);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenSynchronizationStartReturnsResultNotOkayAndLogIsEmptyThenFailedIsReported() {
        startCapabilityWithResult(SHNResult.SHNTimeoutError);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronizationFailed(testSHNCapabilityLogSyncBase, SHNResult.SHNTimeoutError);
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
    public void whenSynchronizationResultIsNotOkayAndLogIsNotEmptyThenFailedIsReported() {
        startCapabilityWithResult(SHNResult.SHNOk);

        generateDataAndSendIt(new Date[]{new Date()});

        testSHNCapabilityLogSyncBase.abortSynchronization();

        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSynchronization.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(SHNResult.SHNAborted, shnResultArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    @Test
    public void whenOnMeasurementReceivedInStateIdleThenProgressIsNotUpdated() {
        generateDataAndSendIt(new Date[]{new Date()});

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate(any(SHNCapabilityLogSyncHealthThermometer.class), anyFloat());
    }

    @Test
    public void whenOnMeasurementReceivedThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate(any(SHNCapabilityLogSyncHealthThermometer.class), floatArgumentCaptor.capture());

        assertEquals((float) 1 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer).restart();
    }

    @Test
    public void whenOnMeasurementReceivedMultipleTimesThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, times(3)).onProgressUpdate(any(SHNCapabilityLogSyncHealthThermometer.class), floatArgumentCaptor.capture());

        assertEquals((float) 3 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer, times(3)).restart();
    }

    @Test
    public void whenTheTimerTimesOutThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        generateDataAndSendIt(new Date[]{new Date()});

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate(testSHNCapabilityLogSyncBase, 1.0f);
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date()});

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNResult.class));

        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreatedWithMultipleItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNResult.class));

        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreatedWithSortedItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Mockito.reset(mockedTimeoutTimer);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
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
    public void whenLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        generateDataAndSendIt(new Date[]{new Date()});
        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNLog.class), any(SHNResult.class));
    }

    @Test
    public void whenNoLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed(any(SHNCapabilityLogSyncHealthThermometer.class), any(SHNResult.class));
    }

    @Test
    public void whenSynchronizationIsPerformedTwiceInARowThenSecondLogDoesNotContainItemsFromFirstLog() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), any(SHNResult.class));

        List<SHNLogItem> firstLog = shnLogArgumentCaptor.getValue().getLogItems();

        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Date[] dates2 = {new Date(100L)};
        generateDataAndSendIt(dates2);

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), any(SHNResult.class));

        List<SHNLogItem> secondLog = shnLogArgumentCaptor.getValue().getLogItems();

        boolean modified = secondLog.removeAll(firstLog);
        assertFalse(modified);
    }

    private class TestSHNCapabilityLogSyncBase extends SHNCapabilityLogSyncBase {

        private SHNResult result = SHNResult.SHNOk;

        @Override
        protected void setupToReceiveMeasurements() {
            handleResultOfMeasurementsSetup(result);
        }

        @Override
        protected void teardownReceivingMeasurements() {

        }

        public void setResult(SHNResult result) {
            this.result = result;
        }
    }
}