package com.philips.pins.shinelib.bluetoothwrapper;

import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.helper.MockedHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class BTGattTest {

    @Mock
    private BTGatt.BTGattCallback mockedCallback;

    @Mock
    private BluetoothGatt mockedBluetoothGatt;

    private BTGatt btGatt;

    private MockedHandler mockedUserHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockedUserHandler = new MockedHandler();

        btGatt = new BTGatt(mockedCallback, mockedUserHandler.getMock());
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
}