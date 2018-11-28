/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Build;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.util.ReflectionHelpers;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BTDeviceTest {

    @Mock
    private BluetoothDevice bluetoothDevice;

    private BTDevice btDevice;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        MockedHandler mockedUserHandler = new MockedHandler();
        btDevice = new BTDevice(bluetoothDevice, mockedUserHandler.getMock());
    }

    @Test
    public void whenBluetoothDeviceNameIsCalledThenReturnName() {
        when(bluetoothDevice.getName()).thenReturn("btName");

        assertEquals(btDevice.getName(), "btName");
    }

    @Test
    public void whenBluetoothDeviceAddressIsCalledThenReturnAddress() {
        when(bluetoothDevice.getAddress()).thenReturn("00:00:00:00");

        assertEquals(btDevice.getAddress(), "00:00:00:00");
    }

    @Test
    public void whenBluetoothDeviceBondStateIsCalledThenReturnBondState() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);

        assertEquals(btDevice.getBondState(), BluetoothDevice.BOND_BONDED);
    }

    @Test
    public void whenBluetoothDeviceConnectGattIsCalledThenReturnGatt() {
        SHNCentral shnCentral = mock(SHNCentral.class);
        Context context = mock(Context.class);
        BluetoothGatt mockBluetoothGatt = mock(BluetoothGatt.class);
        BTGatt.BTGattCallback callback = mock(BTGatt.BTGattCallback.class);
        when(bluetoothDevice.connectGatt(any(Context.class), eq(false), any(BTGatt.class))).thenReturn(mockBluetoothGatt);

        BTGatt btGatt = btDevice.connectGatt(context, false, shnCentral, callback, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        assertNotNull(btGatt);
    }

    @Test
    public void whenBluetoothDeviceConnectGattIsCalledOnDeviceThatNeedsWorkaroundThenReturnGatt() {
        ReflectionHelpers.setStaticField(Build.VERSION.class, "SDK_INT", Build.VERSION_CODES.N);
        whenBluetoothDeviceConnectGattIsCalledThenReturnGatt();
    }

    @Test
    public void whenBluetoothDeviceConnectGattIsCalledThenConnectionPriorityIsSet() {
        SHNCentral shnCentral = mock(SHNCentral.class);
        Context context = mock(Context.class);
        BluetoothGatt mockBluetoothGatt = mock(BluetoothGatt.class);
        BTGatt.BTGattCallback callback = mock(BTGatt.BTGattCallback.class);
        when(bluetoothDevice.connectGatt(any(Context.class), eq(false), any(BTGatt.class))).thenReturn(mockBluetoothGatt);

        btDevice.connectGatt(context, false, shnCentral, callback, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        verify(mockBluetoothGatt, times(1)).requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledThenReturnTrue() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(bluetoothDevice.createBond()).thenReturn(true);

        assertTrue(btDevice.createBond());
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledAndFailsThenReturnFalse() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(bluetoothDevice.createBond()).thenReturn(false);

        assertFalse(btDevice.createBond());
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledWhenBondAlreadyExistsThenReturnFalse() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);

        verify(bluetoothDevice, never()).createBond();
        assertFalse(btDevice.createBond());
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledWhenAlreadyBondingThenReturnFalse() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_BONDING);

        verify(bluetoothDevice, never()).createBond();
        assertFalse(btDevice.createBond());
    }
}
