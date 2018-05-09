/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Build;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
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
    private BluetoothGattDescriptor mockedDescriptor;

    @Mock
    private SHNCentral mockedSHNCentral;

    private static final byte[] byteArray = new byte[]{1, 2, 3};

    private BTGatt btGatt;

    private MockedHandler mockedUserHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockedUserHandler = new MockedHandler();

        btGatt = new BTGatt(mockedSHNCentral, mockedCallback, mockedUserHandler.getMock());
    }

    @After
    public void tearDown() throws Exception {
        ReflectionHelpers.setStaticField(Build.class, "MANUFACTURER", null);
        ReflectionHelpers.setStaticField(Build.class, "MODEL", null);
    }

    private ArgumentMatcher<byte[]> byteArrayArgumentMatcher(final byte[] bytes) {
        return new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(final byte[] argument) {
                return argument.equals(bytes);
            }
        };
    }

    private ArgumentMatcher<byte[]> anyByteArray() {
        return new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(final byte[] argument) {
                return true;
            }
        };
    }

    @Test
    public void whenBluetoothGattIsSetToNullAndDisconnectIsCalledThenNoExceptionISGenerated(){
        btGatt.setBluetoothGatt(null);

        btGatt.disconnect();
    }

    @Test
    public void whenDisconnectIsCalledAfterCloseThenNoExceptionISGenerated(){
        btGatt.setBluetoothGatt(mockedBluetoothGatt);
        btGatt.close();

        btGatt.disconnect();
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndDiscoverServicesIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.discoverServices();
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndReadCharacteristicIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.readCharacteristic(mockedCharacteristic, false);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndReadEncryptedCharacteristicIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.readCharacteristic(mockedCharacteristic, true);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndWriteCharacteristicIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);
        when(mockedCharacteristic.setValue(Matchers.argThat(anyByteArray()))).thenReturn(true);

        btGatt.writeCharacteristic(mockedCharacteristic, false, byteArray);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndWriteEncryptedCharacteristicIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);
        when(mockedCharacteristic.setValue(Matchers.argThat(anyByteArray()))).thenReturn(true);

        btGatt.writeCharacteristic(mockedCharacteristic, true, byteArray);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndReadDescriptorIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.readDescriptor(mockedDescriptor);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndWriteDescriptorIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);
        when(mockedDescriptor.setValue(Matchers.argThat(anyByteArray()))).thenReturn(true);

        btGatt.writeDescriptor(mockedDescriptor, byteArray);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndSetCharacteristicNotificationIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.setCharacteristicNotification(mockedCharacteristic, true);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndGetServicesIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);

        btGatt.getServices();
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

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndNoBondExists_ThenABondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device).createBond();
    }

    @Test
    public void whenReadCharacteristicIsEncrypted_AndBondAlreadyExists_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, true);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndBondAlreadyExists_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenReadCharacteristicIsNotEncrypted_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.readCharacteristic(mockedCharacteristic, false);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsNotEncrypted_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, false, new byte[0]);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenReadCharacteristicIsEncrypted_AndBluetoothGattIsNull_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(device.createBond()).thenReturn(true);
        btGatt.setBluetoothGatt(null);

        btGatt.readCharacteristic(mockedCharacteristic, true);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(device, never()).createBond();
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndBluetoothGattIsNull_ThenNoBondIsCreated() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(device.createBond()).thenReturn(true);
        btGatt.setBluetoothGatt(null);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral, never()).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
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

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String)any());
        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String)any());
    }

    @Test
    public void whenWriteCharacteristicIsEncrypted_AndCreateBondReturnsFalse_ThenTheBondListenerIsUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(device.createBond()).thenReturn(false);
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.writeCharacteristic(mockedCharacteristic, true, new byte[0]);

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
    }

    @Test
    public void whenBondStatusChanged_ThenTheBondListenerIsUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn("0.0.0.0");
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.onBondStatusChanged(device, BluetoothDevice.BOND_BONDED, 0);

        mockedUserHandler.executeFirstScheduledExecution();

        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
    }

    @Test
    public void whenBondStatusChanged_AndStatusIsBonding_ThenTheBondListenerIsNotUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn("0.0.0.0");
        btGatt.setBluetoothGatt(mockedBluetoothGatt);

        btGatt.onBondStatusChanged(device, BluetoothDevice.BOND_BONDING, 0);

        mockedUserHandler.executeFirstScheduledExecution();

        verify(mockedSHNCentral, never()).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
    }

    @Test
    public void whenBondStatusChanged_AndBluetoothGattIsNull_ThenTheBondListenerIsNotUnregistered() {
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(mockedBluetoothGatt.getDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn("0.0.0.0");
        btGatt.setBluetoothGatt(null);

        btGatt.onBondStatusChanged(device, BluetoothDevice.BOND_BONDED, 0);

        mockedUserHandler.executeFirstScheduledExecution();

        verify(mockedSHNCentral, never()).unregisterBondStatusListenerForAddress(any(SHNCentral.SHNBondStatusListener.class), (String) any());
    }

    @Test
    public void whenServicesAreDiscoveredThenCallbackIsNotified() {
        btGatt.onServicesDiscovered(mockedBluetoothGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(0, mockedUserHandler.getScheduledExecutionCount());
        verify(mockedCallback).onServicesDiscovered(btGatt, BluetoothGatt.GATT_SUCCESS);
    }

}