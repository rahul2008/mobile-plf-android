/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.healththermometer;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class SHNServiceHealthThermometerTest {
    private SHNServiceHealthThermometer shnServiceHealthThermometer;
    private SHNFactory mockedSHNFactory;
    private SHNService mockedSHNService;
    private SHNServiceHealthThermometer.SHNServiceHealthThermometerListener mockedSHNServiceHealthThermometerListener;
    private SHNResultListener mockedSHNResultListener;
    private SHNCharacteristic mockedSHNCharacteristicTemperatureMeasurement;
    private SHNCharacteristic mockedSHNCharacteristicMeasurementInterval;
    private SHNCharacteristic mockedSHNCharacteristicIntermediateTemperature;
    private SHNCharacteristic mockedSHNCharacteristicTemperatureType;

    @Before
    public void setUp() {
        mockedSHNFactory = Utility.makeThrowingMock(SHNFactory.class);
        mockedSHNService = Utility.makeThrowingMock(SHNService.class);
        mockedSHNServiceHealthThermometerListener = Utility.makeThrowingMock(SHNServiceHealthThermometer.SHNServiceHealthThermometerListener.class);
        mockedSHNResultListener = Utility.makeThrowingMock(SHNResultListener.class);

        mockedSHNCharacteristicTemperatureMeasurement = Utility.makeThrowingMock(SHNCharacteristic.class);
        mockedSHNCharacteristicIntermediateTemperature = Utility.makeThrowingMock(SHNCharacteristic.class);
        mockedSHNCharacteristicTemperatureType = Utility.makeThrowingMock(SHNCharacteristic.class);
        mockedSHNCharacteristicMeasurementInterval = Utility.makeThrowingMock(SHNCharacteristic.class);

        doReturn(mockedSHNService).when(mockedSHNFactory).createNewSHNService(any(UUID.class), any(Set.class), any(Set.class));

        doReturn(true).when(mockedSHNService).registerSHNServiceListener(any(SHNService.SHNServiceListener.class));
        doNothing().when(mockedSHNService).transitionToReady();
        doReturn(mockedSHNCharacteristicTemperatureMeasurement).when(mockedSHNService).getSHNCharacteristic(SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID);
        doReturn(mockedSHNCharacteristicIntermediateTemperature).when(mockedSHNService).getSHNCharacteristic(SHNServiceHealthThermometer.CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID);
        doReturn(mockedSHNCharacteristicTemperatureType).when(mockedSHNService).getSHNCharacteristic(SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_TYPE_UUID);
        doReturn(mockedSHNCharacteristicMeasurementInterval).when(mockedSHNService).getSHNCharacteristic(SHNServiceHealthThermometer.CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID);

        doNothing().when(mockedSHNServiceHealthThermometerListener).onServiceStateChanged(any(SHNServiceHealthThermometer.class), any(SHNService.State.class));
        doNothing().when(mockedSHNServiceHealthThermometerListener).onTemperatureMeasurementReceived(any(SHNServiceHealthThermometer.class), any(SHNTemperatureMeasurement.class));
        doNothing().when(mockedSHNServiceHealthThermometerListener).onIntermediateTemperatureReceived(any(SHNServiceHealthThermometer.class), any(SHNTemperatureMeasurement.class));

        setupCharacteristicMock(mockedSHNCharacteristicTemperatureMeasurement, SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID);
        setupCharacteristicMock(mockedSHNCharacteristicIntermediateTemperature, SHNServiceHealthThermometer.CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID);
        setupCharacteristicMock(mockedSHNCharacteristicTemperatureType, SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_TYPE_UUID);
        setupCharacteristicMock(mockedSHNCharacteristicMeasurementInterval, SHNServiceHealthThermometer.CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID);
    }

    private void setupCharacteristicMock(SHNCharacteristic shnCharacteristic, UUID characteristicUUID) {
        doNothing().when(shnCharacteristic).setShnCharacteristicChangedListener(any(SHNCharacteristic.SHNCharacteristicChangedListener.class));
        doNothing().when(shnCharacteristic).setIndication(anyBoolean(), any(SHNCommandResultReporter.class));
        doReturn(characteristicUUID).when(shnCharacteristic).getUuid();
    }

    private void setupService() {
        shnServiceHealthThermometer = new SHNServiceHealthThermometer(mockedSHNFactory);
        shnServiceHealthThermometer.setSHNServiceHealthThermometerListener(mockedSHNServiceHealthThermometerListener);
    }

    @Test
    public void canCreateInstance() {
        setupService();

        assertNotNull(shnServiceHealthThermometer);
    }

    @Test
    public void whenCreatingTheThermometerServiceThenTheFactoryMethodIsCalledWithTheCorrectUUIDs() {
        // Replace the doReturn from the setUp to capture arguments
        ArgumentCaptor<UUID> serviceUUIDArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Set> requiredCharacteristicUUIDs = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalCharacteristicUUIDs = ArgumentCaptor.forClass(Set.class);
        doReturn(mockedSHNService).when(mockedSHNFactory).createNewSHNService(serviceUUIDArgumentCaptor.capture(), requiredCharacteristicUUIDs.capture(), optionalCharacteristicUUIDs.capture());

        setupService();

        assertEquals(SHNServiceHealthThermometer.SERVICE_HEALTH_THERMOMETER_UUID, serviceUUIDArgumentCaptor.getValue());
        assertEquals(1, requiredCharacteristicUUIDs.getValue().size());
        assertTrue(requiredCharacteristicUUIDs.getValue().contains(new SHNCharacteristicInfo(SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID, true)));
        assertEquals(3, optionalCharacteristicUUIDs.getValue().size());
        assertTrue(optionalCharacteristicUUIDs.getValue().contains(new SHNCharacteristicInfo(SHNServiceHealthThermometer.CHARACTERISTIC_TEMPERATURE_TYPE_UUID, true)));
        assertTrue(optionalCharacteristicUUIDs.getValue().contains(new SHNCharacteristicInfo(SHNServiceHealthThermometer.CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID, true)));
        assertTrue(optionalCharacteristicUUIDs.getValue().contains(new SHNCharacteristicInfo(SHNServiceHealthThermometer.CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID, true)));
    }

    @Test
    public void whenCreatingTheThermometerServiceThenItBecomesTheServiceListener() {
        setupService();
        verify(mockedSHNService).registerSHNServiceListener(shnServiceHealthThermometer);
    }

    private void setupServiceToStateAvailable() {
        setupService();
        shnServiceHealthThermometer.onServiceStateChanged(mockedSHNService, SHNService.State.Available);
    }

    @Test
    public void whenTheServiceBecomesAvailableThenTransitionToReadyMustBecalled() {
        setupServiceToStateAvailable();

        verify(mockedSHNService).transitionToReady();
    }

    @Test
    public void whenTheServiceInStateAvailableBecomesUnavailableThenOnServiceStateChangedMustBeCalled() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.onServiceStateChanged(mockedSHNService, SHNService.State.Unavailable);

        verify(mockedSHNServiceHealthThermometerListener).onServiceStateChanged(shnServiceHealthThermometer, SHNService.State.Unavailable);
    }

    @Test
    public void whenEnablingReceivingOfTemperatureMeasurementsThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(true, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicTemperatureMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(true, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenDisablingReceivingOfTemperatureMeasurementsThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(false, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicTemperatureMeasurement).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(false, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenEnablingReceivingIntermediateTemperatureMeasurementsThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveIntermediateTemperatures(true, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicIntermediateTemperature).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(true, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenDisablingReceivingIntermediateTemperatureMeasurementsThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveIntermediateTemperatures(false, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicIntermediateTemperature).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(false, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenEnablingReceivingMeasurementIntervalChangesThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveMeasurementIntervalChanges(true, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicMeasurementInterval).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(true, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenDisablingReceivingMeasurementIntervalChangesThenTheRequestMustBeForwardedToTheCorrectSHNCharacteristic() {
        setupServiceToStateAvailable();
        shnServiceHealthThermometer.setReceiveMeasurementIntervalChanges(false, mockedSHNResultListener);
        ArgumentCaptor<Boolean> enableArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedSHNCharacteristicMeasurementInterval).setIndication(enableArgumentCaptor.capture(), any(SHNCommandResultReporter.class));
        assertEquals(false, enableArgumentCaptor.getValue());
    }

    @Test
    public void whenAProperlyFormattedNewMeasurementArrivesThenTheOnTemperatureMeasurementReceivedMustBeCalled() {
        setupServiceToStateAvailable();
        ArgumentCaptor<SHNServiceHealthThermometer> shnServiceHealthThermometerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceHealthThermometer.class);
        ArgumentCaptor<SHNTemperatureMeasurement> shnTemperatureMeasurementArgumentCaptor = ArgumentCaptor.forClass(SHNTemperatureMeasurement.class);

        shnServiceHealthThermometer.onCharacteristicChanged(mockedSHNCharacteristicTemperatureMeasurement, new byte[]{0, 0, 0, 0, 0});
        verify(mockedSHNServiceHealthThermometerListener).onTemperatureMeasurementReceived(shnServiceHealthThermometerArgumentCaptor.capture(), shnTemperatureMeasurementArgumentCaptor.capture());
        assertNotNull(shnServiceHealthThermometerArgumentCaptor.getValue());
        assertEquals(shnServiceHealthThermometer, shnServiceHealthThermometerArgumentCaptor.getValue());
        assertNotNull(shnTemperatureMeasurementArgumentCaptor.getValue());
    }

    @Test
    public void whenAInproperlyFormattedNewMeasurementArrivesThenTheOnTemperatureMeasurementReceivedMustNOTBeCalled() {
        setupServiceToStateAvailable();
        ArgumentCaptor<SHNServiceHealthThermometer> shnServiceHealthThermometerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceHealthThermometer.class);
        ArgumentCaptor<SHNTemperatureMeasurement> shnTemperatureMeasurementArgumentCaptor = ArgumentCaptor.forClass(SHNTemperatureMeasurement.class);

        shnServiceHealthThermometer.onCharacteristicChanged(mockedSHNCharacteristicTemperatureMeasurement, new byte[]{0, 0, 0, 0});
        verify(mockedSHNServiceHealthThermometerListener, never()).onTemperatureMeasurementReceived(shnServiceHealthThermometerArgumentCaptor.capture(), shnTemperatureMeasurementArgumentCaptor.capture());
    }

    @Test
    public void whenAProperlyFormattedNewIntermediateMeasurementArrivesThenTheOnIntermediateTemperatureReceivedMustBeCalled() {
        setupServiceToStateAvailable();
        ArgumentCaptor<SHNServiceHealthThermometer> shnServiceHealthThermometerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceHealthThermometer.class);
        ArgumentCaptor<SHNTemperatureMeasurement> shnTemperatureMeasurementArgumentCaptor = ArgumentCaptor.forClass(SHNTemperatureMeasurement.class);

        shnServiceHealthThermometer.onCharacteristicChanged(mockedSHNCharacteristicIntermediateTemperature, new byte[]{0, 0, 0, 0, 0});
        verify(mockedSHNServiceHealthThermometerListener).onIntermediateTemperatureReceived(shnServiceHealthThermometerArgumentCaptor.capture(), shnTemperatureMeasurementArgumentCaptor.capture());
        assertNotNull(shnServiceHealthThermometerArgumentCaptor.getValue());
        assertEquals(shnServiceHealthThermometer, shnServiceHealthThermometerArgumentCaptor.getValue());
        assertNotNull(shnTemperatureMeasurementArgumentCaptor.getValue());
    }

    @Test
    public void whenAInproperlyFormattedNewIntermediateMeasurementArrivesThenTheOnIntermediateTemperatureReceivedMustNOTBeCalled() {
        setupServiceToStateAvailable();
        ArgumentCaptor<SHNServiceHealthThermometer> shnServiceHealthThermometerArgumentCaptor = ArgumentCaptor.forClass(SHNServiceHealthThermometer.class);
        ArgumentCaptor<SHNTemperatureMeasurement> shnTemperatureMeasurementArgumentCaptor = ArgumentCaptor.forClass(SHNTemperatureMeasurement.class);

        shnServiceHealthThermometer.onCharacteristicChanged(mockedSHNCharacteristicIntermediateTemperature, new byte[]{0, 0, 0, 0});
        verify(mockedSHNServiceHealthThermometerListener, never()).onIntermediateTemperatureReceived(shnServiceHealthThermometerArgumentCaptor.capture(), shnTemperatureMeasurementArgumentCaptor.capture());
    }
}