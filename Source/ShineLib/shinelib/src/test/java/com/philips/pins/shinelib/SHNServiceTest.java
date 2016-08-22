/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

public class SHNServiceTest {
    private SHNService shnService;
    private UUID mockedServiceUUID;
    private Set<UUID> requiredCharacteristics;
    private Set<UUID> optionalCharacteristics;
    private UUID mockedRequiredCharacteristicUUID;
    private UUID mockedOptionalCharacteristicUUID;
    private SHNService.SHNServiceListener mockedSHNServiceListener;
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;
    private BTGatt mockedBTGatt;

    @Before
    public void setUp() {
        requiredCharacteristics = new HashSet<>();
        optionalCharacteristics = new HashSet<>();

        mockedServiceUUID = UUID.randomUUID();
        mockedRequiredCharacteristicUUID = UUID.randomUUID();
        requiredCharacteristics.add(mockedRequiredCharacteristicUUID);
        mockedOptionalCharacteristicUUID = UUID.randomUUID();
        optionalCharacteristics.add(mockedOptionalCharacteristicUUID);
        mockedSHNServiceListener = (SHNService.SHNServiceListener) Utility.makeThrowingMock(SHNService.SHNServiceListener.class);

        shnService = new SHNService(mockedServiceUUID, requiredCharacteristics, optionalCharacteristics);
        shnService.registerSHNServiceListener(mockedSHNServiceListener);
    }

    @Test
    public void whenASHNServiceIsCreatedThenTheStateIsUnavailable() {
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    @Test
    public void testGetUuid() {
        assertEquals(mockedServiceUUID, shnService.getUuid());
    }

    @Test
    public void testGetSHNCharacteristic() {
        // Test that no characteristic can be found for a unknown UUID
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(UUID.randomUUID());
        assertNull(shnCharacteristic);

        // Test that both the required and optional characteristics have been created and can be retrieved
        shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicUUID);
        assertNotNull(shnCharacteristic);
        assertEquals(mockedRequiredCharacteristicUUID, shnCharacteristic.getUuid());
        shnCharacteristic = shnService.getSHNCharacteristic(mockedOptionalCharacteristicUUID);
        assertNotNull(shnCharacteristic);
        assertEquals(mockedOptionalCharacteristicUUID, shnCharacteristic.getUuid());
    }

    @Test
    public void whenTheRequiredServicesAreDiscoveredThenTheServiceChangesToAvailableState() {
        getServiceToAvailableStateThroughConnectToBLELayer();
        ArgumentCaptor<SHNService.State> stateCaptor = ArgumentCaptor.forClass(SHNService.State.class);
        verify(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), stateCaptor.capture());
        assertEquals(SHNService.State.Available, stateCaptor.getValue());
        assertEquals(SHNService.State.Available, shnService.getState());
    }

    private void getServiceToAvailableStateThroughConnectToBLELayer() {
        BluetoothGattService mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        List<BluetoothGattCharacteristic> mockedBluetoothCharacteristics = new ArrayList<>();
        mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        mockedBluetoothCharacteristics.add(mockedBluetoothGattCharacteristic);
        doReturn(mockedBluetoothCharacteristics).when(mockedBluetoothGattService).getCharacteristics();
        doReturn(mockedRequiredCharacteristicUUID).when(mockedBluetoothGattCharacteristic).getUuid();
        doNothing().when(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), any(SHNService.State.class));

        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);

        shnService.connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
    }

    @Test
    public void whenTheRequiredServicesAreNOTDiscoveredThenTheServiceRemainsInUnavailableState() {
        keepServiceInUnavailableStateThroughConnectToBLELayer(mockedOptionalCharacteristicUUID);
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    private void keepServiceInUnavailableStateThroughConnectToBLELayer(UUID characteristicUUID) {
        BluetoothGattService mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        List<BluetoothGattCharacteristic> mockedBluetoothCharacteristics = new ArrayList<>();
        BluetoothGattCharacteristic mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        mockedBluetoothCharacteristics.add(mockedBluetoothGattCharacteristic);
        doReturn(mockedBluetoothCharacteristics).when(mockedBluetoothGattService).getCharacteristics();
        doReturn(characteristicUUID).when(mockedBluetoothGattCharacteristic).getUuid();

        BTGatt mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);

        shnService.connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
    }

    @Test
    public void whenTheServiceIsAvaiableAndDisconnectFromBLELayerIsCalledThenTheServiceTransitionsToUnavalable() {
        // First set the service in Available state
        shnService.unregisterSHNServiceListener(mockedSHNServiceListener);
        getServiceToAvailableStateThroughConnectToBLELayer();
        shnService.registerSHNServiceListener(mockedSHNServiceListener);

        shnService.disconnectFromBLELayer();
        ArgumentCaptor<SHNService.State> stateCaptor = ArgumentCaptor.forClass(SHNService.State.class);
        verify(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), stateCaptor.capture());
        assertEquals(SHNService.State.Unavailable, stateCaptor.getValue());
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    @Test
    public void whenTheServiceIsUnavailableAndDisconnectFromBLELayerIsCalledThenTheServiceRemainsUnavalable() {
        // First set the service in Unavailable state
        keepServiceInUnavailableStateThroughConnectToBLELayer(mockedOptionalCharacteristicUUID);

        assertEquals(SHNService.State.Unavailable, shnService.getState());
        shnService.disconnectFromBLELayer();
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    @Test
    public void whenTheServiceIsUnavailableAndNoConnectToBLELayerAndDisconnectFromBLELayerIsCalledThenTheServiceRemainsUnavalable() {
        assertEquals(SHNService.State.Unavailable, shnService.getState());
        shnService.disconnectFromBLELayer();
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    @Test
    public void whenTheServiceIsUnavailableThroughUnknownCharacteristicUUIDAndDisconnectFromBLELayerIsCalledThenTheServiceRemainsUnavalable() {
        // First set the service in Unavailable state
        keepServiceInUnavailableStateThroughConnectToBLELayer(UUID.randomUUID());

        assertEquals(SHNService.State.Unavailable, shnService.getState());
        shnService.disconnectFromBLELayer();
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    @Test
    public void testTransitionToReady() {
        doNothing().when(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), any(SHNService.State.class));

        shnService.transitionToReady();
        ArgumentCaptor<SHNService.State> stateCaptor = ArgumentCaptor.forClass(SHNService.State.class);
        verify(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), stateCaptor.capture());
        assertEquals(SHNService.State.Ready, stateCaptor.getValue());
        assertEquals(SHNService.State.Ready, shnService.getState());
    }

    @Test
    public void testTransitionToError() {
        doNothing().when(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), any(SHNService.State.class));

        shnService.transitionToError();
        ArgumentCaptor<SHNService.State> stateCaptor = ArgumentCaptor.forClass(SHNService.State.class);
        verify(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), stateCaptor.capture());
        assertEquals(SHNService.State.Error, stateCaptor.getValue());
        assertEquals(SHNService.State.Error, shnService.getState());
    }

    @Test
    public void testUnregisterSHNServiceListener() {
        shnService.unregisterSHNServiceListener(mockedSHNServiceListener);

        shnService.transitionToReady();
        assertEquals(SHNService.State.Ready, shnService.getState());
    }

    @Test
    public void testOnCharacteristicReadWithData() {
        SHNCommandResultReporter mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicUUID);

        getServiceToAvailableStateThroughConnectToBLELayer();

        doNothing().when(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(any(SHNResult.class), any(byte[].class));

        shnCharacteristic.read(mockedSHNCommandResultReporter);
        byte[] data = {'d', 'a', 't', 'a'};
        shnService.onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, data);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, data);
    }

    @Test
    public void testOnCharacteristicWrite() {
        SHNCommandResultReporter mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicUUID);

        getServiceToAvailableStateThroughConnectToBLELayer();

        byte[] data = {'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, data);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(any(SHNResult.class), any(byte[].class));

        shnCharacteristic.write(data, mockedSHNCommandResultReporter);
        shnService.onCharacteristicWrite(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void testOnCharacteristicChangedWithData() {
        SHNCommandResultReporter mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicUUID);
        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);

        getServiceToAvailableStateThroughConnectToBLELayer();

        byte[] data = {'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, data);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(any(SHNResult.class), any(byte[].class));
        doNothing().when(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(any(SHNCharacteristic.class), any(byte[].class));

        shnService.onCharacteristicChangedWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, data);
        verify(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(shnCharacteristic, data);
    }

    @Test
    public void testOnDescriptorReadWithData() {
        // No test for the descriptor since there is no SHNDescriptor concept
    }

    @Test
    public void testOnDescriptorWrite() {
        // No test for the descriptor since there is no SHNDescriptor concept
    }
}