/*
 * Copyright (c) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
        SHNCentral shnCentral = mock(SHNCentral.class);
        BTGatt.BTGattCallback callback = mock(BTGatt.BTGattCallback.class);

        BTGatt btGatt = btDevice.connectGatt(RuntimeEnvironment.application, false, shnCentral, callback);

        assertNotNull(btGatt);
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