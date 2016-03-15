package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.capabilities.SHNCapabilityNotifications;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.wrappers.SHNCapabilityNotificationsWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;

public class SHNDeviceImplTest {
    public static final String TEST_DEVICE_TYPE = "TEST_DEVICE_TYPE";
    private SHNDeviceImpl shnDevice;

    @Mock
    private BTDevice mockedBTDevice;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private Context mockedContext;

    @Mock
    private Timer timerMock;

    @Mock
    private BTGatt mockedBTGatt;

    @Mock
    private SHNService mockedSHNService;

    @Mock
    private BluetoothGattService mockedBluetoothGattService;

    @Mock
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;

    @Mock
    private BluetoothGattDescriptor mockedBluetoothGattDescriptor;

    @Mock
    private SHNDeviceImpl.SHNDeviceListener mockedSHNDeviceListener;

    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;
    private BTGatt.BTGattCallback btGattCallback;
    private List<BluetoothGattService> discoveredServices;
    private SHNService.State mockedServiceState;
    public static final String ADDRESS_STRING = "DE:AD:CO:DE:12:34";
    private boolean useTimeoutConnect = true;

    @Before
    public void setUp() {
        initMocks(this);

        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();

        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();

        doAnswer(new Answer<BTGatt>() {
            @Override
            public BTGatt answer(InvocationOnMock invocation) throws Throwable {
                btGattCallback = (BTGatt.BTGattCallback) invocation.getArguments()[2];
                return mockedBTGatt;
            }
        }).when(mockedBTDevice).connectGatt(isA(Context.class), anyBoolean(), isA(BTGatt.BTGattCallback.class));

        doReturn(ADDRESS_STRING).when(mockedBTDevice).getAddress();

        discoveredServices = new ArrayList<>();
        discoveredServices.add(mockedBluetoothGattService);
        doReturn(discoveredServices).when(mockedBTGatt).getServices();

        mockedServiceState = SHNService.State.Ready;
        doAnswer(new Answer<SHNService.State>() {
            @Override
            public SHNService.State answer(InvocationOnMock invocation) throws Throwable {
                return mockedServiceState;
            }
        }).when(mockedSHNService).getState();
        doReturn(mockedContext).when(mockedSHNCentral).getApplicationContext();

        doReturn(mockedBluetoothGattService).when(mockedBluetoothGattCharacteristic).getService();
        doReturn(mockedBluetoothGattCharacteristic).when(mockedBluetoothGattDescriptor).getCharacteristic();

        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, false);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);
        shnDevice.registerService(mockedSHNService);
    }

    private void connectTillGATTConnected() {
        if (useTimeoutConnect)
            shnDevice.connect();
        else
            shnDevice.connect(false, -1L);
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
    }

    private void connectTillGATTServicesDiscovered() {
        connectTillGATTConnected();
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
    }

    private void getDeviceInConnectedState() {
        connectTillGATTServicesDiscovered();
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
    }

    private void getDeviceInDisconnectingState() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
    }

    // Start with the normal operations tests
    @Test
    public void aSHNDeviceObjectCanBeCreated() {
        assertNotNull(shnDevice);
        assertEquals(TEST_DEVICE_TYPE, shnDevice.getDeviceTypeName());
        verify(mockedSHNCentral).getApplicationContext();
        verify(mockedSHNCentral, times(2)).getInternalHandler(); // for the creation of both timer objects
    }

    @Test
    public void whenInStateDisconnectedTheDisconnectMethodIsCalledThenTheStateIsDisconnected() {
        shnDevice.disconnect();
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectedTheDisconnectMethodIsCalledThenTheOnStateUpdatedDoesNOTGetCalled() {
        shnDevice.disconnect();
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
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
    public void whenInStateDisconnectedThenTheStateChangesToConnectingThenTheOnStateUpdatedGetsCalled() {
        shnDevice.connect();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    @Test
    public void whenInStateConnectingThenTheStateChangesToDisconnectedThenTheOnFailedToConnectGetsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(null, 0, BluetoothProfile.STATE_DISCONNECTED);
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
    }
    @Test
    public void whenInStateConnectingAfterConnectingATimeoutOccursThenTheOnFailedToConnectGetsCalled() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorTimeout);
    }

    @Test
    public void whenInStateDisconnectedTheConnectMethodIsCalledThenConnectGattOnBTDeviceIsCalled() {
        shnDevice.connect();
        verify(mockedBTDevice).connectGatt(isA(Context.class), isA(Boolean.class), isA(BTGatt.BTGattCallback.class));
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedThenDiscoverServicesIsCalled() {
        connectTillGATTConnected();
        verify(mockedBTGatt).discoverServices();
    }

    @Test
    public void whenServicesAreDiscoveredAndDirectlyBecomeReadyThenTheDeviceBecomesConnected() {
        Mockito.doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        mockedServiceState = SHNService.State.Ready;
                        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
                        return null;
                    }
                }
        ).when(mockedSHNService).connectToBLELayer(any(BTGatt.class), any(BluetoothGattService.class));
        connectTillGATTServicesDiscovered();

        assertEquals(SHNDevice.State.Connected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenCloseIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectFromBLELayerIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedSHNService).disconnectFromBLELayer();
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenOnFailedToConnectIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenTheStateIsDisconnected() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredThenGetServicesIsCalled() {
        connectTillGATTServicesDiscovered();
        verify(mockedBTGatt).getServices();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredThenTheSHNServiceIsConnectedToTheBleService() {
        connectTillGATTServicesDiscovered();
        verify(mockedSHNService).connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
    }

    @Test
    public void whenInStateConnectingTheServiceIndicatesReadyThenTheStateIsChangedToConnected() {
        getDeviceInConnectedState();
        assertEquals(SHNDeviceImpl.State.Connected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheServiceIndicatesAvailableThenTheStateRemainsConnecting() {
        connectTillGATTServicesDiscovered();
        mockedServiceState = SHNService.State.Available;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingAndDisconnectIsCalledThenTheStateBecomesDisconnecting() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        shnDevice.disconnect();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenTheStateIsChangedToDisconnecting() {
        getDeviceInDisconnectingState();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenDisconnectOnBTGattIsCalled() {
        getDeviceInDisconnectingState();
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheStateIsChangedToDisconnected() {
        getDeviceInDisconnectingState();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenBondStatusListenerIsUnregistered() {
        getDeviceInDisconnectingState();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(shnDevice, ADDRESS_STRING);
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheGattServerIsClosed() {
        getDeviceInDisconnectingState();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenSHNServiceDisconnectFromBleLayerIsCalled() {
        getDeviceInDisconnectingState();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedSHNService).disconnectFromBLELayer();
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesUnavailableThenTheStateIsDisconnecting() {
        getDeviceInDisconnectingState();
        mockedServiceState = SHNService.State.Unavailable;
        shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Unavailable);
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheDisconnectMethodIsCalledThenThenStateIsDisconnecting() {
        getDeviceInDisconnectingState();
        shnDevice.disconnect();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    // Test the timeouts during connecting
    @Test
    public void whenInStateConnectingAfterConnectingATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingAndWaitingTillServicesAfterConnectingATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        connectTillGATTConnected();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenATimeoutOccursReachingTheConnectedStateThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingAfterServicesDiscoveredATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedThenThereIsNoTimerRunning() {
        getDeviceInConnectedState();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // Receiving responses tot requests
    @Test
    public void whenInStateConnectedOnCharacteristicReadWithDataThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        btGattCallback.onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicReadWithData(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), anyInt(), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnCharacteristicWriteThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        btGattCallback.onCharacteristicWrite(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNService).onCharacteristicWrite(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), anyInt());
    }

    @Test
    public void whenInStateConnectedOnCharacteristicChangedWithDataThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        btGattCallback.onCharacteristicChangedWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicChangedWithData(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorReadWithDataThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        btGattCallback.onDescriptorReadWithData(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onDescriptorReadWithData(isA(BTGatt.class), isA(BluetoothGattDescriptor.class), anyInt(), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorWriteThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        btGattCallback.onDescriptorWrite(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNService).onDescriptorWrite(isA(BTGatt.class), isA(BluetoothGattDescriptor.class), anyInt());
    }

    // Test toString()
    @Test
    public void whenToStringIscalledThenAStringWithReadableInfoAboutTheDeviceIsReturned() {
        final String nameString = "TestDevice";
        doReturn(nameString).when(mockedBTDevice).getName();
        doReturn(ADDRESS_STRING).when(mockedBTDevice).getAddress();
        assertEquals("SHNDevice - " + nameString + " [" + ADDRESS_STRING + "]", shnDevice.toString());
    }

    // Test Capability functions
    @Test
    public void whenNoCapabilitiesAreRegisteredThenGetSupportedCapabilityTypesIsEmpty() {
        assertTrue(shnDevice.getSupportedCapabilityTypes().isEmpty());
        assertNull(shnDevice.getCapabilityForType(SHNCapabilityType.NOTIFICATIONS));
    }

    @Test
    public void whenRegisteringACapabilityThenGetSupportedCapabilityTypesReturnsThatTypeAndTheDeprecatedType() {
        SHNCapabilityNotifications mockedSHNCapabilityNotifications = Utility.makeThrowingMock(SHNCapabilityNotifications.class);
        shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.NOTIFICATIONS);
        assertEquals(2, shnDevice.getSupportedCapabilityTypes().size());

        assertTrue(shnDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.NOTIFICATIONS));
        assertNotNull(shnDevice.getCapabilityForType(SHNCapabilityType.NOTIFICATIONS));
        assertTrue(shnDevice.getCapabilityForType(SHNCapabilityType.NOTIFICATIONS) instanceof SHNCapabilityNotificationsWrapper);

        assertTrue(shnDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.Notifications));
        assertNotNull(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications));
        assertTrue(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications) instanceof SHNCapabilityNotificationsWrapper);
    }

    @Test
    public void whenRegisteringADeprecatedCapabilityThenGetSupportedCapabilityTypesReturnsThatTypeAndTheDeprecatedType() {
        SHNCapabilityNotifications mockedSHNCapabilityNotifications = Utility.makeThrowingMock(SHNCapabilityNotifications.class);
        shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.Notifications);
        assertEquals(2, shnDevice.getSupportedCapabilityTypes().size());

        assertTrue(shnDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.NOTIFICATIONS));
        assertNotNull(shnDevice.getCapabilityForType(SHNCapabilityType.NOTIFICATIONS));
        assertTrue(shnDevice.getCapabilityForType(SHNCapabilityType.NOTIFICATIONS) instanceof SHNCapabilityNotificationsWrapper);

        assertTrue(shnDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.Notifications));
        assertNotNull(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications));
        assertTrue(shnDevice.getCapabilityForType(SHNCapabilityType.Notifications) instanceof SHNCapabilityNotificationsWrapper);
    }

    @Test
    public void whenRegisteringACapabilityMoreThanOnceThenAnExceptionIsThrown() {
        SHNCapabilityNotifications mockedSHNCapabilityNotifications = Utility.makeThrowingMock(SHNCapabilityNotifications.class);
        shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.NOTIFICATIONS);
        boolean exceptionCaught = false;
        try {
            shnDevice.registerCapability(mockedSHNCapabilityNotifications, SHNCapabilityType.NOTIFICATIONS);
        } catch (Exception e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    // Error conditions
    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredErrorThenADisconnectIsInitiated() {
        connectTillGATTConnected();
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_FAILURE);
        verify(mockedBTGatt).disconnect();
        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
    }

    // Tests for the connect without timeout
    @Test
    public void whenConnectWithoutTimeoutThenNoTimeoutIsSet() {
        shnDevice.connect(false, -1L);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenConnectWithoutTimeoutThenConnectGattIsCalledWithAutoConnect() {
        shnDevice.connect(false, -1L);
        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(mockedBTDevice).connectGatt(isA(Context.class), booleanArgumentCaptor.capture(), isA(BTGatt.BTGattCallback.class));
        assertEquals(true, booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenConnectWithoutTimeoutAndRemoteDisconnectsThenDisconnectGattIsCalledWithAutoConnect() {
        useTimeoutConnect = false;
        getDeviceInConnectedState();
        assertEquals(SHNDevice.State.Connected, shnDevice.getState());

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt).close();
    }
}