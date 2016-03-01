package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNDataBodyWeight;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;
import com.philips.pins.shinelib.services.weightscale.SHNWeightMeasurement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityLogSyncWeightScaleTest {

    public static final byte[] TEST_ENCODED_CURRENT_TIME = new byte[]{
            (byte) 0xDF, (byte) 0x07      // year 2015 = 0x07DF
            , 6                          // month june = 6
            , 8                          // day 8th
            , 8                          // hour 8
            , 34                         // minutes 34
            , 45                         // seconds 45
            , 1                          // Day Of Week Monday (Not checked to be correct for the date)
            , (byte) 0x80                // Fraction256 128
            , 0b00000001                 // Adjust reason
    };
    private SHNCapabilityLogSyncWeightScale shnCapabilityLogSyncWeightScale;
    private SHNServiceWeightScale mockedShnServiceWeightScale;
    private SHNDeviceTimeAdjuster mockedSHNDeviceTimeAdjuster;

    private SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener mockedShnCapabilitySHNCapabilityLogSynchronizationListener;
    private SHNServiceWeightScale.SHNServiceWeightScaleListener shnServiceWeightScaleListener;

    @Before
    public void setUp() {
        mockedShnServiceWeightScale = mock(SHNServiceWeightScale.class);
        mockedSHNDeviceTimeAdjuster = mock(SHNDeviceTimeAdjuster.class);
        mockedShnCapabilitySHNCapabilityLogSynchronizationListener = mock(SHNCapabilityLogSynchronization.SHNCapabilityLogSynchronizationListener.class);

        shnCapabilityLogSyncWeightScale = new SHNCapabilityLogSyncWeightScale(mockedShnServiceWeightScale, mockedSHNDeviceTimeAdjuster);
        captureServiceWeightScaleListener();

        doAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                return (Long) invocation.getArguments()[0] + 100000;
            }
        }).when(mockedSHNDeviceTimeAdjuster).adjustTimestampToHostTime(anyLong());

        shnCapabilityLogSyncWeightScale.setSHNCapabilityLogSynchronizationListener(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);
    }

    private void captureServiceWeightScaleListener() {
        ArgumentCaptor<SHNServiceWeightScale.SHNServiceWeightScaleListener> shnServiceWeightScaleListenerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceWeightScale.SHNServiceWeightScaleListener.class);
        verify(mockedShnServiceWeightScale).setSHNServiceWeightScaleListener(shnServiceWeightScaleListenerArgumentCaptor.capture());
        shnServiceWeightScaleListener = shnServiceWeightScaleListenerArgumentCaptor.getValue();
    }

    @Test
    public void whenCreatedThenTheInstanceIsNotNull() {
        Assert.assertNotNull(shnCapabilityLogSyncWeightScale);
    }

    @Test
    public void whenCreatedThenStateIsIdle() {
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityLogSyncWeightScale.getState());
    }

    @Test
    public void whenCreatedServiceWeightScaleListenerIsSet() {
        assertNotNull(shnServiceWeightScaleListener);
    }

    @Test
    public void whenInStateIdleStartSynchronizationIsCalledThenTheStateChangesToSynchronizing() {
        shnCapabilityLogSyncWeightScale.startSynchronizationFromToken(null);
        assertEquals(SHNCapabilityLogSyncWeightScale.State.Synchronizing, shnCapabilityLogSyncWeightScale.getState());
    }

    @Test
    public void whenStartSynchronizationIsCalledFirstTimeThenOnTheServiceTheNotificationsAreEnabled() {
        shnCapabilityLogSyncWeightScale.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertTrue(booleanArgumentCaptor.getValue());
    }

    private void assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult result) {
        shnCapabilityLogSyncWeightScale.startSynchronizationFromToken(null);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNResultListener> shnResultListenerArgumentCaptor = ArgumentCaptor.forClass(SHNResultListener.class);

        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), shnResultListenerArgumentCaptor.capture());
        shnResultListenerArgumentCaptor.getValue().onActionCompleted(result);
        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenStartSynchronizationIsCalledInStateSynchronizingThenOnTheServiceTheNotificationsAreNotEnabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        reset(mockedShnServiceWeightScale);

        shnCapabilityLogSyncWeightScale.startSynchronizationFromToken(null);

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    private void assertAbortSynchronizationWithResultThenNotificationsAreDisabled() {
        shnCapabilityLogSyncWeightScale.abortSynchronization();

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));
        assertFalse(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenAbortSynchronizationIsCalledInStateIdleThenOnTheServiceTheNotificationsAreNotDisabledAgain() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        reset(mockedShnServiceWeightScale);

        assertAbortSynchronizationWithResultThenNotificationsAreDisabled();
        reset(mockedShnServiceWeightScale);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenAbortSynchronizationIsCalledWithStateIdleThanListenerIsNotSet() {
        shnCapabilityLogSyncWeightScale.abortSynchronization();

        verify(mockedShnServiceWeightScale, never()).setNotificationsEnabled(anyBoolean(), any(SHNResultListener.class));
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanAbortSynchronizationIsCalled() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        reset(mockedShnServiceWeightScale);

        shnServiceWeightScaleListener.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedShnServiceWeightScale).setNotificationsEnabled(booleanArgumentCaptor.capture(), any(SHNResultListener.class));

        assertFalse(booleanArgumentCaptor.getValue());
        assertEquals(SHNCapabilityLogSynchronization.State.Idle, shnCapabilityLogSyncWeightScale.getState());
    }

    private SHNWeightMeasurement[] generateDataAndSendIt(Date[] dates) {
        SHNWeightMeasurement[] measurements = new SHNWeightMeasurement[dates.length];
        for (int i = 0; i < dates.length; i++) {
            SHNWeightMeasurement mockedShnTemperatureMeasurement = mock(SHNWeightMeasurement.class);
            when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(dates[i]);

            SHNWeightMeasurement.Flags mockedFlags = Mockito.mock(SHNWeightMeasurement.Flags.class);
            when(mockedFlags.hasUserId()).thenReturn(false);
            when(mockedFlags.hasBmiAndHeight()).thenReturn(false);
            when(mockedShnTemperatureMeasurement.getFlags()).thenReturn(mockedFlags);

            measurements[i] = mockedShnTemperatureMeasurement;
            shnServiceWeightScaleListener.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedShnTemperatureMeasurement);
        }
        return measurements;
    }

    @Test
    public void whenOnTemperatureMeasurementReceivedWithNoTimeStampThenProgressIsNotUpdated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        SHNWeightMeasurement mockedMeasurement = mock(SHNWeightMeasurement.class);
        when(mockedMeasurement.getTimestamp()).thenReturn(null);

        shnServiceWeightScaleListener.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedMeasurement);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener, never()).onProgressUpdate(any(SHNCapabilityLogSyncHealthThermometer.class), anyFloat());
    }

    @Test
    public void whenOnServiceStateChangedWithStateUnavailableWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        reset(mockedShnServiceWeightScale);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L)};
        generateDataAndSendIt(dates);

        shnServiceWeightScaleListener.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(3, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenOnServiceStateChangedWithStateErrorWhileSyncingThanLogIsCreated() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);
        reset(mockedShnServiceWeightScale);

        Date[] dates = {new Date(100L), new Date(80L), new Date(110L), new Date(115L)};
        generateDataAndSendIt(dates);

        shnServiceWeightScaleListener.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Error);

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
        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates2 = {new Date()};
        generateDataAndSendIt(dates2);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertEquals(1, shnLogArgumentCaptor.getValue().getLogItems().size());
    }

    @Test
    public void whenThereIsAnTimelessMeasurementThenItIsSkipped() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

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
        reset(mockedShnServiceWeightScale);

        shnServiceWeightScaleListener.onServiceStateChanged(mockedShnServiceWeightScale, SHNService.State.Unavailable);

        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(
                isA(SHNCapabilityLogSyncWeightScale.class),
                isA(SHNLog.class),
                isA(SHNResult.class));
    }

    @Test
    public void whenLogIsCreatedThenMeasurementsAreOfTypeWeightMeasurement() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        Date[] dates = {new Date(100L), new Date(80L), null, new Date(110L)};
        generateDataAndSendIt(dates);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        assertTrue(shnLogArgumentCaptor.getValue().getContainedDataTypes().contains(SHNDataType.BodyWeight));
        assertEquals(1, shnLogArgumentCaptor.getValue().getContainedDataTypes().size());
    }

    @Test
    public void whenMeasurementHasUserIdAndBmiWithHeightThanValuesAreSet() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        SHNWeightMeasurement mockedShnTemperatureMeasurement = mock(SHNWeightMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date(100L));

        SHNWeightMeasurement.Flags mockedFlags = Mockito.mock(SHNWeightMeasurement.Flags.class);
        when(mockedFlags.hasUserId()).thenReturn(true);
        when(mockedFlags.hasBmiAndHeight()).thenReturn(true);
        when(mockedShnTemperatureMeasurement.getFlags()).thenReturn(mockedFlags);

        shnServiceWeightScaleListener.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedShnTemperatureMeasurement);

        verify(mockedShnTemperatureMeasurement).getUserId();
        verify(mockedShnTemperatureMeasurement).getBMI();
        verify(mockedShnTemperatureMeasurement).getHeight();
    }

    @Test
    public void whenMeasurementHasUserIdThanLogItemContainsValue() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        SHNWeightMeasurement mockedShnTemperatureMeasurement = mock(SHNWeightMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date(100L));

        SHNWeightMeasurement.Flags mockedFlags = Mockito.mock(SHNWeightMeasurement.Flags.class);
        when(mockedFlags.hasUserId()).thenReturn(true);
        when(mockedFlags.hasBmiAndHeight()).thenReturn(false);
        when(mockedShnTemperatureMeasurement.getFlags()).thenReturn(mockedFlags);
        int userId = 3;
        when(mockedShnTemperatureMeasurement.getUserId()).thenReturn(userId);

        shnServiceWeightScaleListener.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedShnTemperatureMeasurement);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        List<SHNLogItem> items = shnLogArgumentCaptor.getValue().getLogItems();
        SHNDataBodyWeight shnDataBodyTemperature = (SHNDataBodyWeight) items.get(0).getDataByDataTypeMap().get(SHNDataType.BodyWeight);

        assertEquals(userId, shnDataBodyTemperature.getUserId());
    }

    @Test
    public void whenMeasurementHasBmiANDHeightThanLogItemContainsValues() {
        assertStartSynchronizationWithResultThenNotificationsAreEnabled(SHNResult.SHNOk);

        reset(mockedShnCapabilitySHNCapabilityLogSynchronizationListener);

        SHNWeightMeasurement mockedShnTemperatureMeasurement = mock(SHNWeightMeasurement.class);
        when(mockedShnTemperatureMeasurement.getTimestamp()).thenReturn(new Date(100L));

        SHNWeightMeasurement.Flags mockedFlags = Mockito.mock(SHNWeightMeasurement.Flags.class);
        when(mockedFlags.hasUserId()).thenReturn(false);
        when(mockedFlags.hasBmiAndHeight()).thenReturn(true);
        when(mockedShnTemperatureMeasurement.getFlags()).thenReturn(mockedFlags);

        float bmi = 26.3f;
        when(mockedShnTemperatureMeasurement.getBMI()).thenReturn(bmi);

        float height = 123.5f;
        when(mockedShnTemperatureMeasurement.getHeight()).thenReturn(height);

        shnServiceWeightScaleListener.onWeightMeasurementReceived(mockedShnServiceWeightScale, mockedShnTemperatureMeasurement);

        shnCapabilityLogSyncWeightScale.abortSynchronization();

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);
        ArgumentCaptor<SHNLog> shnLogArgumentCaptor = ArgumentCaptor.forClass(SHNLog.class);
        verify(mockedShnCapabilitySHNCapabilityLogSynchronizationListener).onLogSynchronized(any(SHNCapabilityLogSyncHealthThermometer.class), shnLogArgumentCaptor.capture(), shnResultArgumentCaptor.capture());

        List<SHNLogItem> items = shnLogArgumentCaptor.getValue().getLogItems();
        SHNDataBodyWeight shnDataBodyTemperature = (SHNDataBodyWeight) items.get(0).getDataByDataTypeMap().get(SHNDataType.BodyWeight);

        assertEquals(bmi, shnDataBodyTemperature.getBmi(), 0.001f);
        assertEquals(height, shnDataBodyTemperature.getHeightInMeters(), 0.001f);
    }
}