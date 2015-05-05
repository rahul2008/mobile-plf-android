package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;

import com.pins.philips.shinelib.bluetoothwrapper.BTGatt;
import com.pins.philips.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class SHNCharacteristicTest {
    private SHNCharacteristic shnCharacteristic;
    private UUID uuid;
    private BTGatt mockedBTGatt;

    @Before
    public void setUp() {
        uuid = UUID.randomUUID();
        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);
        shnCharacteristic = new SHNCharacteristic(uuid);
    }

    @Test
    public void whenASHNCharacteristicIsCreatedThenItsStateIsInactive() {
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void testGetUuid() {
        assertEquals(uuid, shnCharacteristic.getUuid());
    }

    @Test
    public void whenConnectedToABLECharThenTheStateIsActive() {
        BluetoothGattCharacteristic mockedBluetoothGattCharacteristic = mock(BluetoothGattCharacteristic.class);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        assertEquals(SHNCharacteristic.State.Active, shnCharacteristic.getState());
    }

    @Test
    public void whenDisconnectedFromABLECharThenTheStateIsInactive() {
        shnCharacteristic.disconnectFromBLELayer();
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void whenInactiveThenGetValueReturnsNull() {
        assertNull(shnCharacteristic.getValue());
    }

    @Test
    public void whenActiveThenGetValueReturnsGetValueFromBLEChar() {
        BluetoothGattCharacteristic mockedBluetoothGattCharacteristic = mock(BluetoothGattCharacteristic.class);
        when(mockedBluetoothGattCharacteristic.getValue()).thenReturn(new byte[1]);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        assertNotNull(shnCharacteristic.getValue());
        verify(mockedBluetoothGattCharacteristic).getValue();
    }

    @Test
    public void whenInactiveThenReadCharacteristicReturnsFalse() {
        assertFalse(shnCharacteristic.read(null));
    }

    @Test
    public void whenActiveThenReadCharacteristicReturnsTrue() {
        BluetoothGattCharacteristic mockedBluetoothGattCharacteristic = mock(BluetoothGattCharacteristic.class);
        when(mockedBluetoothGattCharacteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_READ);
        doNothing().when(mockedBTGatt).readCharacteristic(any(BluetoothGattCharacteristic.class));
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        assertTrue(shnCharacteristic.read(null));
        verify(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
    }
}