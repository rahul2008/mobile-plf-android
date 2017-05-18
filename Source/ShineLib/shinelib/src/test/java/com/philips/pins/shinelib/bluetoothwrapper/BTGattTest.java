/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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

    private ArgumentMatcher<byte[]> byteArrayArgumentMatcher(final byte[] bytes) {
        return new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(Object argument) {
                return argument.equals(bytes);
            }
        };
    }

    private ArgumentMatcher<byte[]> anyByteArray() {
        return new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(Object argument) {
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
}