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

public class SHNServiceBodyCompositionTest {

    private SHNServiceBodyComposition shnServiceBodyComposition;
    private SHNService.SHNServiceListener shnServiceListener;
    private SHNFactory mockedSHNFactory;
    private SHNService mockedSHNService;
    private SHNServiceBodyComposition.SHNServiceBodyCompositionListener mockedSHNBodyCompositionListener;
    private SHNCharacteristic mockedSHNCharacteristicBodyCompositionMeasurement;
    private SHNCharacteristic mockedSHNCharacteristicBodyCompositionFeature;

    @Before
    public void setUp() {
        mockedSHNFactory = mock(SHNFactory.class);
        mockedSHNService = mock(SHNService.class);

        mockedSHNCharacteristicBodyCompositionMeasurement = mock(SHNCharacteristic.class);
        when(mockedSHNCharacteristicBodyCompositionMeasurement.getUuid()).thenReturn(SHNServiceBodyComposition.BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID);
        when(mockedSHNService.getSHNCharacteristic(SHNServiceBodyComposition.BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID)).thenReturn(mockedSHNCharacteristicBodyCompositionMeasurement);

        mockedSHNCharacteristicBodyCompositionFeature = mock(SHNCharacteristic.class);
        when(mockedSHNCharacteristicBodyCompositionFeature.getUuid()).thenReturn(SHNServiceBodyComposition.BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID);
        when(mockedSHNService.getSHNCharacteristic(SHNServiceBodyComposition.BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID)).thenReturn(mockedSHNCharacteristicBodyCompositionFeature);

        when(mockedSHNFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedSHNService);

        shnServiceBodyComposition = new SHNServiceBodyComposition(mockedSHNFactory);

        ArgumentCaptor<SHNService.SHNServiceListener> shnServiceListenerArgumentCaptor = ArgumentCaptor.forClass(SHNService.SHNServiceListener.class);
        verify(mockedSHNService).registerSHNServiceListener(shnServiceListenerArgumentCaptor.capture());
        shnServiceListener = shnServiceListenerArgumentCaptor.getValue();

        mockedSHNBodyCompositionListener = mock(SHNServiceBodyComposition.SHNServiceBodyCompositionListener.class);
        shnServiceBodyComposition.setSHNServiceBodyCompositionListener(mockedSHNBodyCompositionListener);
    }

    @Test
    public void anInstanceCanBeCreated() {
        assertNotNull(shnServiceBodyComposition);
    }

    @Test
    public void whenInstanceIsCreatedThanServiceIsInitialized() {
        assertNotNull(shnServiceBodyComposition.getShnService());
    }

    @Test
    public void whenTheServiceIsCreatedThenTheProperUUIDSArePassedIntoTheSHNFactory() {
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Set> mandatoryUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalUUIDSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(mockedSHNFactory).createNewSHNService(uuidArgumentCaptor.capture(), mandatoryUUIDSetArgumentCaptor.capture(), optionalUUIDSetArgumentCaptor.capture());

        assertEquals(SHNServiceBodyComposition.BODY_COMPOSITION_UUID, uuidArgumentCaptor.getValue());
        assertNotNull(mandatoryUUIDSetArgumentCaptor.getValue());
        assertEquals(2, mandatoryUUIDSetArgumentCaptor.getValue().size());
        assertTrue(mandatoryUUIDSetArgumentCaptor.getValue().contains(new SHNCharacteristicInfo(SHNServiceBodyComposition.BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID, true)));
        assertTrue(mandatoryUUIDSetArgumentCaptor.getValue().contains(new SHNCharacteristicInfo(SHNServiceBodyComposition.BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID, true)));
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
        verify(mockedSHNBodyCompositionListener).onServiceStateChanged(shnServiceBodyComposition, SHNService.State.Available);
    }

    @Test
    public void whenTheSHNServiceReportsThatItIsAvailableThenTheTransitionToReadyIsCalled() {
        serviceSetupServiceStateChangedToAvailable();

        verify(mockedSHNService).transitionToReady();
    }

    @Test
    public void whenTheServiceInStateAvailableBecomesUnavailableThenOnServiceStateChangedMustBeCalled() {
        serviceSetupServiceStateChangedToAvailable();
        shnServiceBodyComposition.onServiceStateChanged(mockedSHNService, SHNService.State.Unavailable);

        verify(mockedSHNBodyCompositionListener).onServiceStateChanged(shnServiceBodyComposition, SHNService.State.Unavailable);
    }

    @Test
    public void whenNotificationsForBodyCompositionIsEnabledThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceBodyComposition.setNotificationsEnabled(true, mockedSHNResultListener);

        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicBodyCompositionMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(true, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenNotificationsForBodyCompositionIsDisabledThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceBodyComposition.setNotificationsEnabled(false, mockedSHNResultListener);

        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicBodyCompositionMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(false, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenResultForNotificationsIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();

        SHNResultListener mockedSHNResultListener = mock(SHNResultListener.class);
        shnServiceBodyComposition.setNotificationsEnabled(true, mockedSHNResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicBodyCompositionMeasurement).setIndication(anyBoolean(), shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, null);
        verify(mockedSHNResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenAProperlyFormattedNewMeasurementArrivesThenTheOnBodyCompositionMeasurementReceivedMustBeCalled() {
        serviceSetupServiceStateChangedToAvailable();
        ArgumentCaptor<SHNServiceBodyComposition> serviceBodyCompositionArgumentCaptor = ArgumentCaptor.forClass(SHNServiceBodyComposition.class);
        ArgumentCaptor<SHNBodyCompositionMeasurement> compositionArgumentCaptor = ArgumentCaptor.forClass(SHNBodyCompositionMeasurement.class);

        shnServiceBodyComposition.onCharacteristicChanged(mockedSHNCharacteristicBodyCompositionMeasurement, new byte[]{0, 0, 0, 0});

        verify(mockedSHNBodyCompositionListener).onBodyCompositionMeasurementReceived(serviceBodyCompositionArgumentCaptor.capture(), compositionArgumentCaptor.capture());
        assertNotNull(serviceBodyCompositionArgumentCaptor.getValue());
        assertEquals(shnServiceBodyComposition, serviceBodyCompositionArgumentCaptor.getValue());
        assertNotNull(compositionArgumentCaptor.getValue());
    }

    @Test
    public void whenAInproperlyFormattedNewMeasurementArrivesThenTheOnTemperatureMeasurementReceivedMustNOTBeCalled() {
        serviceSetupServiceStateChangedToAvailable();

        shnServiceBodyComposition.onCharacteristicChanged(mockedSHNCharacteristicBodyCompositionMeasurement, new byte[]{0, 0, 0});

        verify(mockedSHNBodyCompositionListener, never()).onBodyCompositionMeasurementReceived(any(SHNServiceBodyComposition.class), any(SHNBodyCompositionMeasurement.class));
    }

    @Test
    public void whenGetFeaturesIsCalledThenProperCharacteristicIsRed() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceBodyComposition.getFeatures(mockedShnResultListener);

        verify(mockedSHNCharacteristicBodyCompositionFeature).read(any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenGetFeaturesResultIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceBodyComposition.getFeatures(mockedShnResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicBodyCompositionFeature).read(shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNOk, new byte[]{0, 0, 0, 0});

        ArgumentCaptor<SHNResult> shnResultArgumentCaptor = ArgumentCaptor.forClass(SHNResult.class);

        verify(mockedShnResultListener).onActionCompleted(any(SHNBodyCompositionFeatures.class), shnResultArgumentCaptor.capture());
        assertEquals(SHNResult.SHNOk, shnResultArgumentCaptor.getValue());
    }

    @Test
    public void whenGetFeaturesErrorResultIsReportedThenListenerIsNotified() {
        serviceSetupServiceStateChangedToAvailable();
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceBodyComposition.getFeatures(mockedShnResultListener);

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedSHNCharacteristicBodyCompositionFeature).read(shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(SHNResult.SHNErrorTimeout, null);
        verify(mockedShnResultListener).onActionCompleted(null, SHNResult.SHNErrorTimeout);
    }

    @Test
    public void whenGetFeaturesIsCalledWithServiceStateUnavailableThenResultIsReported() {
        SHNObjectResultListener mockedShnResultListener = mock(SHNObjectResultListener.class);
        shnServiceBodyComposition.getFeatures(mockedShnResultListener);

        verify(mockedShnResultListener).onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
    }
}