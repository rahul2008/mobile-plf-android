package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityLogSyncBaseTest {

    private TestSHNCapabilityLogSyncBase testSHNCapabilityLogSyncBase;
    private SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener mockedShnCapabilitySHNCapabilityLogSynchronizationListener;

    @Captor
    ArgumentCaptor<SHNLog> logCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedShnCapabilitySHNCapabilityLogSynchronizationListener = mock(SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener.class);

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
        startCapabilityWithResult(SHNResult.SHNErrorBluetoothDisabled);

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, times(2)).onStateUpdated(testSHNCapabilityLogSyncBase);
    }

    @Test
    public void whenAbortSynchronizationIsCalledFirstTimeThenListenerIsNotified() {
        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onStateUpdated(testSHNCapabilityLogSyncBase);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate(testSHNCapabilityLogSyncBase, 1f);
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
    public void whenSynchronizationStartReturnsResultNotOkayAndLogIsEmptyThenFailedIsReported() {
        startCapabilityWithResult(SHNResult.SHNErrorTimeout);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronizationFailed(testSHNCapabilityLogSyncBase, SHNResult.SHNErrorTimeout);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, testSHNCapabilityLogSyncBase.getState());
    }

    private SHNLogItem[] generateDataAndSendIt(Date[] dates) {
        SHNLogItem[] items = new SHNLogItem[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNLogItem mockedShnLogItem = mock(SHNLogItem.class);
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

        testSHNCapabilityLogSyncBase.stop(SHNResult.SHNErrorConnectionLost);

        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSynchronization.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(SHNResult.SHNErrorConnectionLost, shnResultArgumentCaptor.getValue());
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

        generateDataAndSendIt(new Date[]{new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onProgressUpdate((SHNCapabilityLogSynchronization) any(), floatArgumentCaptor.capture());

        assertEquals((float) 1 / 50, floatArgumentCaptor.getValue(), 0.01f);
    }

    @Test
    public void whenOnMeasurementReceived_ThenIntermediateLogIsReported() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        generateDataAndSendIt(new Date[]{new Date()});

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onIntermediateLogSynchronized((SHNCapabilityLogSynchronization) any(), logCaptor.capture());

        assertShineLog(logCaptor.getValue(), 1);
    }

    @Test
    public void whenOnMeasurementReceivedMultipleTimesThenProgressIsUpdated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, times(3)).onProgressUpdate((SHNCapabilityLogSynchronization) any(), floatArgumentCaptor.capture());

        assertEquals((float) 3 / 50, floatArgumentCaptor.getValue(), 0.01f);
    }

    @Test
    public void whenAbortIsCalled_ThenLogIsCreated() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        generateDataAndSendIt(new Date[]{new Date()});

        testSHNCapabilityLogSyncBase.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed((SHNCapabilityLogSynchronization) any(), (SHNResult) any());

        assertShineLog(shnLogArgumentCaptor.getValue(), 1);
        assertEquals(SHNResult.SHNAborted, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortIsCalled_ThenLogIsCreatedWithMultipleItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        generateDataAndSendIt(new Date[]{new Date(), new Date(), new Date()});

        testSHNCapabilityLogSyncBase.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed((SHNCapabilityLogSynchronization) any(), (SHNResult) any());

        assertShineLog(shnLogArgumentCaptor.getValue(), 3);
        assertEquals(SHNResult.SHNAborted, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortIsCalled_ThenLogIsCreatedWithSortedItems() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());

        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(0).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp()));
        assertTrue(shnLogArgumentCaptor.getValue().getLogItems().get(1).getTimestamp().before(shnLogArgumentCaptor.getValue().getLogItems().get(2).getTimestamp()));
    }

    @Test
    public void whenLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        generateDataAndSendIt(new Date[]{new Date()});
        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronized((SHNCapabilityLogSynchronization) any(), (SHNLog) any(), (SHNResult) any());
    }

    @Test
    public void whenNoLogIsCreatedWithNoListenerAttachedThanListenerIsNotUpdated() {
        testSHNCapabilityLogSyncBase.setSHNCapabilityLogSynchronizationListener(null);
        testSHNCapabilityLogSyncBase.startSynchronizationFromToken(null);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onLogSynchronizationFailed((SHNCapabilityLogSynchronization) any(), (SHNResult) any());
    }

    @Test
    public void whenSynchronizationIsPerformedTwiceInARowThenSecondLogDoesNotContainItemsFromFirstLog() {
        startCapabilityWithResult(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), (SHNResult) any());

        List<SHNLogItem> firstLog = shnLogArgumentCaptor.getValue().getLogItems();

        startCapabilityWithResult(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
        Date[] dates2 = {new Date(100L)};
        generateDataAndSendIt(dates2);

        testSHNCapabilityLogSyncBase.abortSynchronization();

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized((SHNCapabilityLogSynchronization) any(), shnLogArgumentCaptor.capture(), (SHNResult) any());

        List<SHNLogItem> secondLog = shnLogArgumentCaptor.getValue().getLogItems();

        boolean containsAll = secondLog.containsAll(firstLog);
        assertFalse(containsAll);
    }

    //--------------------

    private void assertShineLog(final SHNLog shnLog, final int expectedNrLogItems) {
        assertNotNull(shnLog);
        assertEquals(expectedNrLogItems, shnLog.getLogItems().size());
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

    @Test
    public void whenSetValueForOptionIsCalledThenUnsupportedResultIsReturned() {
        SHNResultListener shnResultListener = mock(SHNResultListener.class);
        testSHNCapabilityLogSyncBase.setValueForOption(0, SHNCapabilityLogSynchronization.Option.AutomaticSynchronizationEnabled, shnResultListener);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNErrorUnsupportedOperation);
    }

    @Test
    public void whenGetValueForOptionIsCalledThenUnsupportedResultIsReturned() {
        SHNIntegerResultListener shnResultListener = mock(SHNIntegerResultListener.class);
        testSHNCapabilityLogSyncBase.getValueForOption(SHNCapabilityLogSynchronization.Option.AutomaticSynchronizationEnabled, shnResultListener);

        verify(shnResultListener).onActionCompleted(0, SHNResult.SHNErrorUnsupportedOperation);
    }
}