package com.philips.pins.shinelib.statemachine.state;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.statemachine.SHNDeviceStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNConnectingStateTest {

    private SHNConnectingState state;

    @Mock
    private SHNDeviceStateMachine stateMachineMock;

    @Mock
    private SHNDeviceResources sharedResourcesMock;

    @Mock
    private BTGatt gattMock;

    @Mock
    private BTDevice btDeviceMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(sharedResourcesMock.getBtDevice()).thenReturn(btDeviceMock);
        when(btDeviceMock.getBondState()).thenReturn(BluetoothDevice.BOND_BONDED);
        when(stateMachineMock.getSharedResources()).thenReturn(sharedResourcesMock);
        state = new SHNGattConnectingState(stateMachineMock);
    }

    @Test
    public void whenGattCallbackIndicatesConnectedThenDiscoverServicesIsCalled() {
        state.onConnectionStateChange(gattMock, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        verify(stateMachineMock).setState(any(state.getClass()), isA(SHNDiscoveringServicesState.class));
    }
}