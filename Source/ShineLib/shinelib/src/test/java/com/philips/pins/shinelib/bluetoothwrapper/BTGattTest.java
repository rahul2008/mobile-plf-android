package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;

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

    private static final byte[] byteArray = new byte[]{1, 2, 3};

    private BTGatt btGatt;

    private MockedHandler mockedUserHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockedUserHandler = new MockedHandler();

        btGatt = new BTGatt(mockedCallback, mockedUserHandler.getMock());
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

        btGatt.readCharacteristic(mockedCharacteristic);
    }

    @Test
    public void whenBluetoothGattIsSetToNull_AndWriteCharacteristicIsCalled_ThenNoExceptionISGenerated() {
        btGatt.setBluetoothGatt(null);
        when(mockedCharacteristic.setValue(Matchers.argThat(anyByteArray()))).thenReturn(true);

        btGatt.writeCharacteristic(mockedCharacteristic, byteArray);
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
}