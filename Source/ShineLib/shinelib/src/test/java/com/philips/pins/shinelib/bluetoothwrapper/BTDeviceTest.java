/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BTDeviceTest {

    @Mock
    private BluetoothDevice bluetoothDevice;

    private BTDevice btDevice;

    private MockedHandler mockedUserHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockedUserHandler = new MockedHandler();
        btDevice = new BTDevice(bluetoothDevice, mockedUserHandler.getMock());
    }

    @After
    public void validate() {
        bluetoothDevice = null;
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
    public void whenBluetoothDeviceConnectGattIsCalledThenReturnGatt() throws NoSuchFieldException, IllegalAccessException {
        BluetoothGatt gatt = Mockito.mock(BluetoothGatt.class);
        when(bluetoothDevice.connectGatt(Mockito.any(Context.class), Mockito.anyBoolean(), Mockito.any(BluetoothGattCallback.class))).thenReturn(gatt);

        BTGatt.BTGattCallback callback = Mockito.mock(BTGatt.BTGattCallback.class);
        BTGatt btGatt = btDevice.connectGatt(RuntimeEnvironment.application, false, callback);

        Field f = BTGatt.class.getDeclaredField("bluetoothGatt");
        f.setAccessible(true);
        BluetoothGatt bluetoothGatt = (BluetoothGatt) f.get(btGatt);

        assertEquals(bluetoothGatt, gatt);
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledThenReturnTrue() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(bluetoothDevice.createBond()).thenReturn(true);

        assertEquals(btDevice.createBond(), true);
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledAndFailsThenReturnFalse() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_NONE);
        when(bluetoothDevice.createBond()).thenReturn(false);

        assertEquals(btDevice.createBond(), false);
    }

    @Test
    public void whenBluetoothDeviceCreateBondIsCalledWhenBondAlreadyExistsThenReturnFalse() {
        when(bluetoothDevice.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);

        assertEquals(btDevice.createBond(), false);
    }
}