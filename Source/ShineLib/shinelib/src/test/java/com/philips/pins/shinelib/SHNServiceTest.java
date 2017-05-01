/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

public class SHNServiceTest {
    private SHNService shnService;
    private UUID mockedServiceUUID;
    private Set<SHNCharacteristicInfo> requiredCharacteristics;
    private Set<SHNCharacteristicInfo> optionalCharacteristics;
    private SHNCharacteristicInfo mockedRequiredCharacteristicInfo;
    private SHNCharacteristicInfo mockedOptionalCharacteristicInfo;
    private byte[] mockedCharacteristicValue;
    private SHNService.SHNServiceListener mockedSHNServiceListener;
    private SHNService.CharacteristicDiscoveryListener mockedCharacteristicDiscoveryListener;
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;
    private BTGatt mockedBTGatt;

    @Before
    public void setUp() {
        requiredCharacteristics = new HashSet<>();
        optionalCharacteristics = new HashSet<>();

        mockedServiceUUID = UUID.randomUUID();
        mockedRequiredCharacteristicInfo = new SHNCharacteristicInfo(UUID.randomUUID(), false);
        requiredCharacteristics.add(mockedRequiredCharacteristicInfo);
        mockedOptionalCharacteristicInfo = new SHNCharacteristicInfo(UUID.randomUUID(), false);
        optionalCharacteristics.add(mockedOptionalCharacteristicInfo);
        mockedSHNServiceListener = (SHNService.SHNServiceListener) Utility.makeThrowingMock(SHNService.SHNServiceListener.class);

        mockedCharacteristicValue = new byte[]{0x12};
        mockedCharacteristicDiscoveryListener = mock(SHNService.CharacteristicDiscoveryListener.class);

        shnService = new SHNService(mockedServiceUUID, requiredCharacteristics, optionalCharacteristics);
        shnService.registerSHNServiceListener(mockedSHNServiceListener);
        shnService.registerCharacteristicDiscoveryListener(mockedCharacteristicDiscoveryListener);
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
        shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicInfo.getUUID());
        assertNotNull(shnCharacteristic);
        assertEquals(mockedRequiredCharacteristicInfo.getUUID(), shnCharacteristic.getUuid());
        shnCharacteristic = shnService.getSHNCharacteristic(mockedOptionalCharacteristicInfo.getUUID());
        assertNotNull(shnCharacteristic);
        assertEquals(mockedOptionalCharacteristicInfo.getUUID(), shnCharacteristic.getUuid());
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
        doReturn(mockedRequiredCharacteristicInfo.getUUID()).when(mockedBluetoothGattCharacteristic).getUuid();
        doReturn(mockedCharacteristicValue).when(mockedBluetoothGattCharacteristic).getValue();
        doNothing().when(mockedSHNServiceListener).onServiceStateChanged(any(SHNService.class), any(SHNService.State.class));

        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);

        shnService.connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
    }

    @Test
    public void whenTheRequiredServicesAreNOTDiscoveredThenTheServiceRemainsInUnavailableState() {
        keepServiceInUnavailableStateThroughConnectToBLELayer(mockedOptionalCharacteristicInfo.getUUID());
        assertEquals(SHNService.State.Unavailable, shnService.getState());
    }

    private void keepServiceInUnavailableStateThroughConnectToBLELayer(UUID characteristicUUID) {
        BluetoothGattService mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        List<BluetoothGattCharacteristic> mockedBluetoothCharacteristics = new ArrayList<>();
        BluetoothGattCharacteristic mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        mockedBluetoothCharacteristics.add(mockedBluetoothGattCharacteristic);
        doReturn(mockedBluetoothCharacteristics).when(mockedBluetoothGattService).getCharacteristics();
        doReturn(mockedCharacteristicValue).when(mockedBluetoothGattCharacteristic).getValue();
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
        keepServiceInUnavailableStateThroughConnectToBLELayer(mockedOptionalCharacteristicInfo.getUUID());

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
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicInfo.getUUID());

        getServiceToAvailableStateThroughConnectToBLELayer();

        doNothing().when(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic, shnCharacteristic.isEncrypted());
        doNothing().when(mockedSHNCommandResultReporter).reportResult(any(SHNResult.class), any(byte[].class));

        shnCharacteristic.read(mockedSHNCommandResultReporter);
        byte[] data = {'d', 'a', 't', 'a'};
        shnService.onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, data);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, data);
    }

    @Test
    public void testOnCharacteristicWrite() {
        SHNCommandResultReporter mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicInfo.getUUID());

        getServiceToAvailableStateThroughConnectToBLELayer();

        byte[] data = {'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, shnCharacteristic.isEncrypted(), data);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(any(SHNResult.class), any(byte[].class));

        shnCharacteristic.write(data, mockedSHNCommandResultReporter);
        shnService.onCharacteristicWrite(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void testOnCharacteristicChangedWithData() {
        SHNCommandResultReporter mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(mockedRequiredCharacteristicInfo.getUUID());
        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);

        getServiceToAvailableStateThroughConnectToBLELayer();

        byte[] data = {'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, shnCharacteristic.isEncrypted(), data);
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

    @Test
    public void testRegisteringCharacteristicDiscoveryListener() throws Exception {
        SHNService.CharacteristicDiscoveryListener mock2 = mock(SHNService.CharacteristicDiscoveryListener.class);
        assertEquals(1, shnService.numberOfRegisteredDiscoveryListeners());
        shnService.registerCharacteristicDiscoveryListener(mock2);
        assertEquals(2, shnService.numberOfRegisteredDiscoveryListeners());
        shnService.unregisterCharacteristicDiscoverListener(mock2);
        assertEquals(1, shnService.numberOfRegisteredDiscoveryListeners());
    }

    @Test
    public void testConectingToBleShouldCallOnCharacteristicsDiscovered() throws Exception {
        getServiceToAvailableStateThroughConnectToBLELayer();
        verify(mockedCharacteristicDiscoveryListener).onCharacteristicDiscovered(eq(mockedRequiredCharacteristicInfo.getUUID()), eq(mockedCharacteristicValue), any(SHNCharacteristic.class));
    }

    @Test
    public void testConectingToBleShouldNotCallOnCharacteristicsDiscovered_whenDiscoveryListenerIsUnregistered() throws Exception {
        shnService.unregisterCharacteristicDiscoverListener(mockedCharacteristicDiscoveryListener);
        getServiceToAvailableStateThroughConnectToBLELayer();
        verify(mockedCharacteristicDiscoveryListener, never()).onCharacteristicDiscovered(any(UUID.class), any(byte[].class), any(SHNCharacteristic.class));
    }
}