package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.pins.philips.shinelib.bluetoothwrapper.BTDevice;
import com.pins.philips.shinelib.bluetoothwrapper.BTGatt;
import com.pins.philips.shinelib.capabilities.SHNCapabilityNotifications;
import com.pins.philips.shinelib.helper.MockedHandler;
import com.pins.philips.shinelib.helper.Utility;
import com.pins.philips.shinelib.wrappers.SHNCapabilityNotificationsWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by 310188215 on 05/05/15.
 */
@RunWith(PowerMockRunner.class)
public class SHNDeviceTest {
    public static final String TEST_DEVICE_TYPE = "TEST_DEVICE_TYPE";
    private SHNDeviceImpl shnDevice;
    private BTDevice mockedBTDevice;
    private SHNCentral mockedSHNCentral;
    private Context mockedContext;
    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;
    private BTGatt mockedBTGatt;
    private BTGatt.BTGattCallback btGattCallback;
    private List<BluetoothGattService> discoveredServices;
    private UUID serviceUUID;
    private SHNService mockedSHNService;
    private SHNService.State mockedServiceState;
    private BluetoothGattService mockedBluetoothGattService;
    private SHNDeviceImpl.SHNDeviceListener mockedSHNDeviceListener;
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;
    private BluetoothGattDescriptor mockedBluetoothGattDescriptor;

    @Before
    public void setUp() {
        mockedBTDevice = (BTDevice) Utility.makeThrowingMock(BTDevice.class);
        mockedSHNCentral = (SHNCentral) Utility.makeThrowingMock(SHNCentral.class);
        mockedContext = (Context) Utility.makeThrowingMock(Context.class);
        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();
        mockedSHNService = (SHNService) Utility.makeThrowingMock(SHNService.class);
        mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        mockedBluetoothGattDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        mockedSHNDeviceListener = (SHNDeviceImpl.SHNDeviceListener) Utility.makeThrowingMock(SHNDeviceImpl.SHNDeviceListener.class);

        doReturn(mockedContext).when(mockedSHNCentral).getApplicationContext();
        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();
        doReturn(mockedBTGatt).when(mockedBTDevice).connectGatt(any(Context.class), anyBoolean(), any(BTGatt.BTGattCallback.class));
        doNothing().when(mockedBTGatt).discoverServices();
        doNothing().when(mockedBTGatt).disconnect();
        doNothing().when(mockedBTGatt).close();

        doAnswer(new Answer<BTGatt>() {
            @Override
            public BTGatt answer(InvocationOnMock invocation) throws Throwable {
                btGattCallback = (BTGatt.BTGattCallback) invocation.getArguments()[2];
                return mockedBTGatt;
            }
        }).when(mockedBTDevice).connectGatt(any(Context.class), anyBoolean(), any(BTGatt.BTGattCallback.class));

        serviceUUID = UUID.randomUUID();
        doReturn(serviceUUID).when(mockedBluetoothGattService).getUuid();
        discoveredServices = new ArrayList<>();
        discoveredServices.add(mockedBluetoothGattService);
        doReturn(discoveredServices).when(mockedBTGatt).getServices();

        doReturn(serviceUUID).when(mockedSHNService).getUuid();
        doReturn(true).when(mockedSHNService).registerSHNServiceListener(any(SHNService.SHNServiceListener.class));
        doNothing().when(mockedSHNService).connectToBLELayer(any(BTGatt.class), any(BluetoothGattService.class));
        doNothing().when(mockedSHNService).disconnectFromBLELayer();
        mockedServiceState = SHNService.State.Ready;
        doReturn(mockedServiceState).when(mockedSHNService).getState();
        doAnswer(new Answer<SHNService.State>() {
            @Override
            public SHNService.State answer(InvocationOnMock invocation) throws Throwable {
                return mockedServiceState;
            }
        }).when(mockedSHNService).getState();
        doNothing().when(mockedSHNService).onCharacteristicReadWithData(any(BTGatt.class), any(BluetoothGattCharacteristic.class), anyInt(), any(byte[].class));
        doNothing().when(mockedSHNService).onCharacteristicWrite(any(BTGatt.class), any(BluetoothGattCharacteristic.class), anyInt());
        doNothing().when(mockedSHNService).onCharacteristicChangedWithData(any(BTGatt.class), any(BluetoothGattCharacteristic.class), any(byte[].class));
        doNothing().when(mockedSHNService).onDescriptorReadWithData(any(BTGatt.class), any(BluetoothGattDescriptor.class), anyInt(), any(byte[].class));
        doNothing().when(mockedSHNService).onDescriptorWrite(any(BTGatt.class), any(BluetoothGattDescriptor.class), anyInt());


        doReturn(mockedBluetoothGattService).when(mockedBluetoothGattCharacteristic).getService();
        doReturn(mockedBluetoothGattCharacteristic).when(mockedBluetoothGattDescriptor).getCharacteristic();

        doNothing().when(mockedSHNDeviceListener).onStateUpdated(any(SHNDevice.class));

        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);
        shnDevice.registerService(mockedSHNService);
    }

    // Start with the normal operations tests
    @Test
    public void aSHNDeviceObjectCanBeCreated() {
        assertNotNull(shnDevice);
        verify(mockedSHNCentral).getApplicationContext();
        assertEquals(TEST_DEVICE_TYPE, shnDevice.getDeviceTypeName());
    }

    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectedTheConnectMethodIsCalledThenTheStateChangesToConnecting() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectedTheCTheStateChangesToConnectingThenTheOnStateUpdatedGetsCalled() {
        shnDevice.connect();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    @Test
    public void whenInStateDisconnectedTheConnectMethodIsCalledThenTheActualConnectIsExecutedOnTheInternalHandlerThread() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNCentral).getInternalHandler(); // for the creation of the timer object
    }

    @Test
    public void whenInStateDisconnectedTheConnectMethodIsCalledThenConnectGattOnBTDeviceIsCalled() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedBTDevice).connectGatt(any(Context.class), anyBoolean(), any(BTGatt.BTGattCallback.class));
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedThenDiscoverServicesIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).discoverServices();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredThenGetServicesIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        verify(mockedBTGatt).getServices();
        verify(mockedSHNService).connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
    }

    @Test
    public void whenInStateConnectingTheServiceIndicatesReadyThenTheStateIsChangedToConnected() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
        assertEquals(SHNDeviceImpl.State.Connected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheServiceIndicatesAVailableThenTheStateRemainsConnecting() {
        mockedServiceState = SHNService.State.Available;
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenDisconnectOnBTGattIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        shnDevice.disconnect();
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        shnDevice.disconnect();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheStateIsChangedToDisconnected() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        shnDevice.disconnect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheGattServerIsClosed() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        shnDevice.disconnect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenSHNServiceDisconnectFromBleLayerIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        shnDevice.disconnect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedSHNService).disconnectFromBLELayer();
    }

    // Test the timeouts during connecting
    @Test
    public void whenInStateConnectingAfterConnectigATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());

        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingAfterConnectionStateChangeATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());

        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingAfterServicesDiscoveredATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());

        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedThenThereIsNoTimerRunning() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // Receiving responses tot requests
    @Test
    public void whenInStateConnectedOnCharacteristicReadWithDataThenTheServiceIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        btGattCallback.onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicReadWithData(any(BTGatt.class), any(BluetoothGattCharacteristic.class), anyInt(), any(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnCharacteristicWriteThenTheServiceIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        btGattCallback.onCharacteristicWrite(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNService).onCharacteristicWrite(any(BTGatt.class), any(BluetoothGattCharacteristic.class), anyInt());
    }

    @Test
    public void whenInStateConnectedOnCharacteristicChangedWithDataThenTheServiceIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        btGattCallback.onCharacteristicChangedWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicChangedWithData(any(BTGatt.class), any(BluetoothGattCharacteristic.class), any(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorReadWithDataThenTheServiceIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        btGattCallback.onDescriptorReadWithData(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onDescriptorReadWithData(any(BTGatt.class), any(BluetoothGattDescriptor.class), anyInt(), any(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorWriteThenTheServiceIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Ready);
        // Now the SHNDevice is connected

        btGattCallback.onDescriptorWrite(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNService).onDescriptorWrite(any(BTGatt.class), any(BluetoothGattDescriptor.class), anyInt());
    }

    // Test toString()
    @Test
    public void whenToStringIscalledThenAStringWithReadableInfoAboutTheDeviceIsReturned() {
        final String addressString = "DE:AD:CO:DE:12:34";
        final String nameString = "TestDevice";
        doReturn(nameString).when(mockedBTDevice).getName();
        doReturn(addressString).when(mockedBTDevice).getAddress();
        assertEquals("SHNDevice - " + nameString + " [" + addressString + "]", shnDevice.toString());
    }

    // Test Capability functions
    @Test
    public void whenNoCapabilitiesAreRegisteredThenGetSupportedCapabilityTypesIsEmpty() {
        assertTrue(shnDevice.getSupportedCapabilityTypes().isEmpty());
        assertNull(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications));
    }

    @Test
    public void whenRegisteringACapabilityThenGetSupportedCapabilityTypesReturnsThatType() {
        SHNCapabilityNotifications mockedSHNCapabilityNotifications = (SHNCapabilityNotifications) Utility.makeThrowingMock(SHNCapabilityNotifications.class);
        shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.Notifications);
        assertEquals(1, shnDevice.getSupportedCapabilityTypes().size());
        assertTrue(shnDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.Notifications));
        assertNotNull(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications));
        assertTrue(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications) instanceof SHNCapabilityNotificationsWrapper);
    }

    @Test
    public void whenRegisteringACapabilityMorehanOnceThenAnExceptionIsThrown() {
        SHNCapabilityNotifications mockedSHNCapabilityNotifications = (SHNCapabilityNotifications) Utility.makeThrowingMock(SHNCapabilityNotifications.class);
        shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.Notifications);
        boolean exceptionCaught = false;
        try {
            shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.Notifications);
        } catch (Exception e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    // Error conditions
    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredErrorThenADisconnectIsInitiated() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_FAILURE);
        verify(mockedBTGatt).disconnect();
        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
    }

}