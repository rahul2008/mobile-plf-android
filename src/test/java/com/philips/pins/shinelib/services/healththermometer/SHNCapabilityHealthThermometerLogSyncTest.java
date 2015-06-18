package com.philips.pins.shinelib.services.healththermometer;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
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
 * Created by 310188215 on 17/06/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class, Log.class})
public class SHNCapabilityHealthThermometerLogSyncTest {

    private static final byte CELSIUS = 0x00;
    private static final byte TIMESTAMP = 0x02;
    private static final byte TEMPTYPE = 0x04;

    private SHNCapabilityHealthThermometerLogSync shnCapabilityHealthThermometerLogSync;
    private SHNServiceHealthThermometer mockedSHNServiceHealthThermometer;
    private SHNCapabilityLogSynchronization.Listener mockedShnCapabilityListener;
    private Timer mockedTimeoutTimer;
    private ArgumentCaptor<Runnable> timeOutCaptor;

    @Before
    public void setUp() {
        mockedSHNServiceHealthThermometer = mock(SHNServiceHealthThermometer.class);
        mockedShnCapabilityListener = mock(SHNCapabilityLogSynchronization.Listener.class);

        mockedTimeoutTimer = mock(Timer.class);
        mockStatic(Timer.class);
        timeOutCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(Timer.createTimer(timeOutCaptor.capture(), anyLong())).thenReturn(mockedTimeoutTimer);

        mockStatic(Log.class);
        when(Log.w(anyString(),anyString())).thenReturn(0);

        shnCapabilityHealthThermometerLogSync = new SHNCapabilityHealthThermometerLogSync(mockedSHNServiceHealthThermometer);
        shnCapabilityHealthThermometerLogSync.setListener(mockedShnCapabilityListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsInStateIdle() {
        assertNotNull(shnCapabilityHealthThermometerLogSync);
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenCreatedThenLogIsNotCreated() {
        verify(mockedShnCapabilityListener, never()).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNLog.class), any(SHNResult.class));
        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));
    }

    @Test
    public void whenCreatedServiceHealthThermometerListenerIsSet() {
        verify(mockedSHNServiceHealthThermometer).setSHNServiceHealthThermometerListener(shnCapabilityHealthThermometerLogSync);
    }

    @Test
    public void whenRequestingTheLastSynchronizationTokenThenAObjectIsReturned() {
        assertNotNull(shnCapabilityHealthThermometerLogSync.getLastSynchronizationToken());
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenStateIsSynchronizing() {
        shnCapabilityHealthThermometerLogSync.startSynchronizationFromToken(null);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenOnTheServiceTheNotificationsAreEnabled() {
        shnCapabilityHealthThermometerLogSync.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertTrue(booleanArgumentCaptor.getValue());
    }

    private void assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult result) {
        shnCapabilityHealthThermometerLogSync.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenStateIsSynchronizing() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        assertEquals(SHNCapabilityLogSynchronization.State.Synchronizing, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenTimerIsStarted() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        verify(mockedTimeoutTimer).restart();
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenListenerIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        verify(mockedShnCapabilityListener).onStateUpdated(shnCapabilityHealthThermometerLogSync);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultOkThenProgressIsUpdated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<SHNCapabilityHealthThermometerLogSync> shnCapabilityHealthThermometerLogSyncArgumentCaptor = ArgumentCaptor.forClass(SHNCapabilityHealthThermometerLogSync.class);
        verify(mockedShnCapabilityListener).onProgressUpdate(shnCapabilityHealthThermometerLogSyncArgumentCaptor.capture(), floatArgumentCaptor.capture());

        assertEquals(0.0f, floatArgumentCaptor.getValue(), 0.0f);
        assertEquals(shnCapabilityHealthThermometerLogSync, shnCapabilityHealthThermometerLogSyncArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);
        Mockito.reset(mockedShnCapabilityListener);

        shnCapabilityHealthThermometerLogSync.startSynchronizationFromToken(null);

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));
        verify(mockedShnCapabilityListener, never()).onStateUpdated(shnCapabilityHealthThermometerLogSync);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithResultNotOkStateIsIdle() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNBluetoothDisabledError);

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
        verify(mockedShnCapabilityListener, times(2)).onStateUpdated(shnCapabilityHealthThermometerLogSync);
    }

    @Test
    public void whenStartSynchronizationIsCalledWithNoListenerAttachedThan() {
        shnCapabilityHealthThermometerLogSync.setListener(null);
        shnCapabilityHealthThermometerLogSync.startSynchronizationFromToken(null);

    }

    private void assertAbortSynchronizationWithResultThenNotificationsAreDisabled() {
        shnCapabilityHealthThermometerLogSync.abortSynchronization();

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertFalse(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortSynchronizationIsCalledFirstTimeThenListenerIsNotified() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();

        verify(mockedShnCapabilityListener).onStateUpdated(shnCapabilityHealthThermometerLogSync);
        verify(mockedShnCapabilityListener).onProgressUpdate(shnCapabilityHealthThermometerLogSync, 1f);
        verify(mockedTimeoutTimer).stop();
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithResultOkStateIsIdle() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceTheNotificationsAreNotDisabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();
        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityHealthThermometerLogSync.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));
        verify(mockedShnCapabilityListener, never()).onStateUpdated(shnCapabilityHealthThermometerLogSync);
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleStateIsIdle() {
        shnCapabilityHealthThermometerLogSync.abortSynchronization();

        verify(mockedSHNServiceHealthThermometer, never()).setReceiveTemperatureMeasurements(anyBoolean(), any(SHNResultListener.class));

        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanAbortSynchronizationIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        Mockito.reset(mockedSHNServiceHealthThermometer);

        shnCapabilityHealthThermometerLogSync.onServiceStateChanged(mockedSHNServiceHealthThermometer, SHNService.State.Unavailable);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNServiceHealthThermometer).setReceiveTemperatureMeasurements(booleanArgumentCaptor.capture(), any(SHNResultListener.class));

        assertFalse(booleanArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityHealthThermometerLogSync.getState());
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedThenProgressIsUpdated() {
        startSynchronizingAndReceiveOneMeasurement();

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilityListener).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), floatArgumentCaptor.capture());

        assertEquals((float) 1 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer).restart();
    }

    private void startSynchronizingAndReceiveOneMeasurement() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date());
        when(mockedShnTemperatureMeasurement.getSHNDataType()).thenReturn(SHNDataType.BodyTemperature);

        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedMultipleTimesThenProgressIsUpdated() {
        startSynchronizingAndReceiveOneMeasurement();

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date());
        when(mockedShnTemperatureMeasurement.getSHNDataType()).thenReturn(SHNDataType.BodyTemperature);
        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);
        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        ArgumentCaptor<Float> floatArgumentCaptor = ArgumentCaptor.forClass(Float.class);
        verify(mockedShnCapabilityListener, times(3)).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), floatArgumentCaptor.capture());

        assertEquals((float) 3 / 50, floatArgumentCaptor.getValue(), 0.01f);
        verify(mockedTimeoutTimer, times(3)).restart();
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedWithNoTimeStampThenProgressIsNotUpdated(){
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        Mockito.reset(mockedShnCapabilityListener);
        Mockito.reset(mockedTimeoutTimer);

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(null);
        when(mockedShnTemperatureMeasurement.getSHNDataType()).thenReturn(SHNDataType.BodyTemperature);

        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        verify(mockedShnCapabilityListener, never()).onProgressUpdate(any(SHNCapabilityHealthThermometerLogSync.class), anyFloat());
        verify(mockedTimeoutTimer).restart();
    }

    @Test
    public void whenTheTimerTimesOutThenProgressIsUpdated(){
        startSynchronizingAndReceiveOneMeasurement();

        timeOutCaptor.getValue().run();

        verify(mockedShnCapabilityListener).onProgressUpdate(shnCapabilityHealthThermometerLogSync, 1.0f);
    }

    @Test
    public void whenTheTimerTimesOutThenLogIsCreated(){
        startSynchronizingAndReceiveOneMeasurement();

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
    public void whenTheTimerTimesOutThenLogIsCreatedWithMultipleItemsv(){
        startSynchronizingAndReceiveOneMeasurement();

        SHNTemperatureMeasurement mockedShnTemperatureMeasurement = Mockito.mock(SHNTemperatureMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date());
        when(mockedShnTemperatureMeasurement.getSHNDataType()).thenReturn(SHNDataType.BodyTemperature);
        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);
        shnCapabilityHealthThermometerLogSync.onTemperatureMeasurementReceived(mockedSHNServiceHealthThermometer, mockedShnTemperatureMeasurement);

        timeOutCaptor.getValue().run();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilityListener).onLogSynchronized(any(SHNCapabilityHealthThermometerLogSync.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());
        verify(mockedShnCapabilityListener, never()).onLogSynchronizationFailed(any(SHNCapabilityHealthThermometerLogSync.class), any(SHNResult.class));

        assertNotNull(shnLogArgumentCaptor.getValue());
        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }
}