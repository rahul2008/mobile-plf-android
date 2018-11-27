/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.util.ReflectionHelpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BTGattTest {

    @Mock
    private BTGatt.BTGattCallback mockedCallback;

    @Mock
    private BluetoothGatt mockedBluetoothGatt;

    @Mock
    private BluetoothGattCharacteristic mockedCharacteristic;

    @Mock
    private SHNCentral mockedSHNCentral;

    private BTGatt btGatt;

    private MockedHandler mockedUserHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockedUserHandler = new MockedHandler();

        btGatt = new BTGatt(mockedSHNCentral, mockedCallback, mockedUserHandler.getMock());
        btGatt.setBluetoothGatt(mockedBluetoothGatt);
    }

    @After
    public void tearDown() throws Exception {
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        ReflectionHelpers.setStaticField(Build.class, "MANUFACTURER", null);
        ReflectionHelpers.setStaticField(Build.class, "MODEL", null);
    }

    @Test
    public void whenDisconnectIsCalledAfterCloseThenNoExceptionIsGenerated() {
        btGatt.close();

        btGatt.disconnect();
    }

    @Test
    public void whenBluetoothGattIsClosed_AndAStateUpdateOccurs_ThenItWillNotForwardIt() {
        btGatt.setBluetoothGatt(mockedBluetoothGatt);
        btGatt.close();

        btGatt.onConnectionStateChange(mockedBluetoothGatt, 8, 9);

        verify(mockedCallback, never()).onConnectionStateChange(any(BTGatt.class), anyInt(), anyInt());
    }

    @Test
    public void whenReadCharacteristicIsEncrypted_AndNoBondExists_ThenABondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, true);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndNoBondExists_ThenABondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device).createBond();
    }

    @Test
    public void whenReadCharacteristicIsEncrypted_AndBondAlreadyExists_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, true);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndBondAlreadyExists_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenReadCharacteristicIsNotEncrypted_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, false);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsNotEncrypted_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, false, new byte[0]);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenReadCharacteristicIsEncrypted_AndCreateBondReturnsFalse_ThenTheBondListenerIsUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(device.createBond()).thenReturn(false);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, true);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndCreateBondReturnsFalse_ThenTheBondListenerIsUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(device.createBond()).thenReturn(false);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
    }

    @Test
    public void whenBondStatusChanged_ThenTheBondListenerIsUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn("0.0.0.0");
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.onBondStatusChanged(device, BluetoothDevice.BOND_BONDED, 0);

        mockedUserHandler.executeFirstScheduledExecution();

        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
    }

    @Test
    public void whenBondStatusChanged_AndStatusIsBonding_ThenTheBondListenerIsNotUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn("0.0.0.0");
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.onBondStatusChanged(device, BluetoothDevice.BOND_BONDING, 0);

        mockedUserHandler.executeFirstScheduledExecution();

        verify(mockedSHNCentral, never()).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), any());
    }

    @Test
    public void whenServicesAreDiscoveredThenCallbackIsNotified() {
        btGatt.onServicesDiscovered(mockedBluetoothGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(0, mockedUserHandler.getScheduledExecutionCount());
        verify(mockedCallback).onServicesDiscovered(btGatt, BluetoothGatt.GATT_SUCCESS);
    }
}