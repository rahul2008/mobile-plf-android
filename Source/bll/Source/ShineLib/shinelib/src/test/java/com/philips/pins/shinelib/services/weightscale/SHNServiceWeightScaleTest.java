/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.SHNFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class SHNServiceWeightScaleTest {

    private SHNServiceWeightScale shnServiceWeightScale;
    private SHNService.SHNServiceListener shnServiceListener;
    private SHNFactory mockedSHNFactory;
    private SHNService mockedSHNService;
    private SHNServiceWeightScale.SHNServiceWeightScaleListener mockedSHNWeightScaleListener;
    private SHNCharacteristic mockedSHNCharacteristicWeightScaleMeasurement;
    private SHNCharacteristic mockedSHNCharacteristicWeightScaleFeature;

    @Before
    public void setUp() {
        mockedSHNFactory = mock(SHNFactory.class);
        mockedSHNService = mock(SHNService.class);

        mockedSHNCharacteristicWeightScaleMeasurement = mock(SHNCharacteristic.class);
        when(mockedSHNCharacteristicWeightScaleMeasurement.getUuid()).thenReturn(SHNServiceWeightScale.WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID);
        when(mockedSHNService.getSHNCharacteristic(SHNServiceWeightScale.WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID)).thenReturn(mockedSHNCharacteristicWeightScaleMeasurement);

        mockedSHNCharacteristicWeightScaleFeature = mock(SHNCharacteristic.class);
        when(mockedSHNCharacteristicWeightScaleFeature.getUuid()).thenReturn(SHNServiceWeightScale.WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID);
        when(mockedSHNService.getSHNCharacteristic(SHNServiceWeightScale.WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID)).thenReturn(mockedSHNCharacteristicWeightScaleFeature);

        when(mockedSHNFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedSHNService);

        shnServiceWeightScale = new SHNServiceWeightScale(mockedSHNFactory);

        ArgumentCaptor<SHNService.SHNServiceListener> shnServiceListenerArgumentCaptor = ArgumentCaptor.forClass(SHNService.SHNServiceListener.class);
        verify(mockedSHNService).registerSHNServiceListener(shnServiceListenerArgumentCaptor.capture());
        shnServiceListener = shnServiceListenerArgumentCaptor.getValue();

        mockedSHNWeightScaleListener = mock(SHNServiceWeightScale.SHNServiceWeightScaleListener.class);
        shnServiceWeightScale.setSHNServiceWeightScaleListener(mockedSHNWeightScaleListener);
    }

    @Test
    public void anInstanceCanBeCreated() {
        assertNotNull(shnServiceWeightScale);
    }

    @Test
    public void whenInstanceIsCreatedThanServiceIsInitialized() {
        assertNotNull(shnServiceWeightScale.getShnService());
    }

    @Test
    public void whenTheServiceIsCreatedThenTheProperUUIDSArePassedIntoTheSHNFactory() {
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Set> mandatoryUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(mockedSHNFactory).createNewSHNService(uuidArgumentCaptor.capture(), mandatoryUUIDSetArgumentCaptor.capture(), optionalUUIDSetArgumentCaptor.capture());

        assertEquals(SHNServiceWeightScale.WEIGHT_SCALE_UUID, uuidArgumentCaptor.getValue());
        assertNotNull(mandatoryUUIDSetArgumentCaptor.getValue());
        assertEquals(2, mandatoryUUIDSetArgumentCaptor.getValue().size());
        assertTrue(mandatoryUUIDSetArgumentCaptor.getValue().contains(new SHNCharacteristicInfo(SHNServiceWeightScale.WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID, true)));
        assertTrue(mandatoryUUIDSetArgumentCaptor.getValue().contains(new SHNCharacteristicInfo(SHNServiceWeightScale.WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID, true)));
        assertNotNull(optionalUUIDSetArgumentCaptor.getValue());
        assertEquals(0, optionalUUIDSetArgumentCaptor.getValue().size());
    }

    @Test
    public void whenTheServiceIsCreatedThenAOnServiceStateChangedListenerIsRegistered() {
        assertNotNull(shnServiceListener);
    }

    private void serviceSetupServiceStateChangedToAvailable() {
        when(mockedSHNService.getState()).thenReturn(SHNService.State.Available);
        shnServiceListener.onServiceStateChanged(mockedSHNService, SHNService.State.Available);
    }

    @Test
    public void whenTheSHNServiceReportsThatItIsAvailableThenTheStateChangeListenerIsCalled() {
        serviceSetupServiceStateChangedToAvailable();
        verify(mockedSHNWeightScaleListener).onServiceStateChanged(shnServiceWeightScale, SHNService.State.Available);
    }

    @Test
    public void whenTheSHNServiceReportsThatItIsAvailableThenTheTransitionToReadyIsCalled() {
        serviceSetupServiceStateChangedToAvailable();

        verify(mockedSHNService).transitionToReady();
    }

    @Test
    public void whenTheServiceInStateAvailableBecomesUnavailableThenOnServiceStateChangedMustBeCalled() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceWeightScale.onServiceStateChanged(mockedSHNService, SHNService.State.Unavailable);

        verify(mockedSHNWeightScaleListener).onServiceStateChanged(shnServiceWeightScale, SHNService.State.Unavailable);
    }

    @Test
    public void whenNotificationsForWeightScaleIsEnabledThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceWeightScale.setNotificationsEnabled(true, mockedSHNResultListener);

        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicWeightScaleMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(true, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenNotificationsForWeightScaleIsDisabledThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceWeightScale.setNotificationsEnabled(false, mockedSHNResultListener);

        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicWeightScaleMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(false, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenResultForNotificationsIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceWeightScale.setNotificationsEnabled(true, mockedSHNResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicWeightScaleMeasurement).setIndication(anyBoolean(), shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, null);
        verify(mockedSHNResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenAProperlyFormattedNewMeasurementArrivesThenTheOnWeightScaleMeasurementReceivedMustBeCalled() {
        serviceSetupServiceStateChangedToAvailable();
        ArgumentCaptor<SHNServiceWeightScale> serviceWeightScaleArgumentCaptor = ArgumentCaptor.forClass(SHNServiceWeightScale.class);
        ArgumentCaptor<SHNWeightMeasurement> compositionArgumentCaptor = ArgumentCaptor.forClass(SHNWeightMeasurement.class);

        shnServiceWeightScale.onCharacteristicChanged(mockedSHNCharacteristicWeightScaleMeasurement, new byte[]{0, 0, 0});

        verify(mockedSHNWeightScaleListener).onWeightMeasurementReceived(serviceWeightScaleArgumentCaptor.capture(), compositionArgumentCaptor.capture());
        assertNotNull(serviceWeightScaleArgumentCaptor.getValue());
        assertEquals(shnServiceWeightScale, serviceWeightScaleArgumentCaptor.getValue());
        assertNotNull(compositionArgumentCaptor.getValue());
    }

    @Test
    public void whenAnInproperlyFormattedNewMeasurementArrivesThenTheOnTemperatureMeasurementReceivedMustNOTBeCalled() {
        serviceSetupServiceStateChangedToAvailable();

        shnServiceWeightScale.onCharacteristicChanged(mockedSHNCharacteristicWeightScaleMeasurement, new byte[]{0, 0});

        verify(mockedSHNWeightScaleListener, never()).onWeightMeasurementReceived(any(SHNServiceWeightScale.class), any(SHNWeightMeasurement.class));
    }

    @Test
    public void whenGetFeaturesIsCalledThenProperCharacteristicIsRed() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceWeightScale.getFeatures(mockedShnResultListener);

        verify(mockedSHNCharacteristicWeightScaleFeature).read(any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenGetFeaturesResultIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceWeightScale.getFeatures(mockedShnResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicWeightScaleFeature).read(shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, new byte[]{0, 0, 0, 0});

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);

        verify(mockedShnResultListener).onActionCompleted(any(SHNBodyCompositionFeatures.class), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenGetFeaturesErrorResultIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceWeightScale.getFeatures(mockedShnResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicWeightScaleFeature).read(shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNErrorTimeout, null);
        verify(mockedShnResultListener).onActionCompleted(null, SHNResult.SHNErrorTimeout);
    }

    @Test
    public void whenGetFeaturesIsCalledWithServiceStateUnavailableThenResultIsReported() {
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceWeightScale.getFeatures(mockedShnResultListener);

        verify(mockedShnResultListener).onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
    }
}