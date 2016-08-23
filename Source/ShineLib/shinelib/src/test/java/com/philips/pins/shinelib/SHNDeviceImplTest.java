package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

public class SHNDeviceImplTest {
    public static final String TEST_DEVICE_TYPE = "TEST_DEVICE_TYPE";
    private SHNDeviceImpl shnDevice;

    @Mock private BTDevice mockedBTDevice;

    @Mock private SHNCentral mockedSHNCentral;

    @Mock private Context mockedContext;

    @Mock private Timer timerMock;

    @Mock private BTGatt mockedBTGatt;

    @Mock private SHNService mockedSHNService;

    @Mock private BluetoothGattService mockedBluetoothGattService;

    @Mock private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;

    @Mock private BluetoothGattDescriptor mockedBluetoothGattDescriptor;

    @Mock private SHNDeviceImpl.SHNDeviceListener mockedSHNDeviceListener;

    @Mock private BluetoothDevice mockedBluetoothDevice;

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

        Timer.setHandler(mockedInternalHandler.getMock());

        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();
        doReturn(true).when(mockedSHNCentral).isBluetoothAdapterEnabled();

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

        when(mockedBluetoothDevice.getAddress()).thenReturn(ADDRESS_STRING);
    }

    private void connectTillGATTConnected() {
        if (useTimeoutConnect) shnDevice.connect();
        else shnDevice.connect(false, -1L);
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

    // State Disconnected
    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
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
    public void whenInStateDisconnectedTheConnectMethodIsCalledThenTheStateChangesToConnecting() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    @Test
    public void whenInStateDisconnectedWhenTheStateChangesToConnectingThenTheConnectGattIsCalled() {
        shnDevice.connect();
        verify(mockedBTDevice).connectGatt(mockedContext, false, btGattCallback);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateDisconnectedConnectIsCalledThenRegisterBondStatusListenerForAddressIsCalled() {
        shnDevice.connect();

        verify(mockedSHNCentral).registerBondStatusListenerForAddress(shnDevice, ADDRESS_STRING);
    }

    // State Connecting
    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenTheOnFailedToConnectGetsCalled() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(SHNDevice.State.Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenConnectTimerIsStopped() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothProfile.STATE_DISCONNECTED);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenBtGattCloseIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateConnectingThenThereIsNoTimerRunning() {
        shnDevice.connect();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedThenDiscoverServicesIsCalled() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);
        verify(mockedBTGatt).discoverServices();

        assertEquals(SHNDevice.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectIsCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenCloseIsCalled() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);

        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(eq(shnDevice), any(SHNResult.class));
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectFromBLELayerIsNotCalled() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedSHNService, never()).disconnectFromBLELayer();
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithFailureAndThenGattIndicatesDisconnectedThenTheOnFailedToConnectIsCalled() {
        whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenCloseIsCalled();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorConnectionLost);
        assertEquals(SHNDevice.State.Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    @Test
    public void whenInStateConnectingDisconnectIsCalledThenStateIsDisconnecting() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.disconnect();

        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void a() {
        whenInStateConnectingDisconnectIsCalledThenStateIsDisconnecting();

        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        PowerMockito.verifyNoMoreInteractions(mockedSHNDeviceListener);

        reset(mockedBTGatt, mockedSHNDeviceListener);
        btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        PowerMockito.verifyNoMoreInteractions(mockedSHNDeviceListener);
    }

    @Test
    public void whenInStateConnectingAndDisconnectIsCalledThenTheStateBecomesDisconnecting() {
        shnDevice.connect();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        shnDevice.disconnect();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingDisconnectIsCalledAndThenDisconnectOnBTGattIsCalled() {
        whenInStateConnectingAndDisconnectIsCalledThenTheStateBecomesDisconnecting();

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void whenInStateConnectingConnectIsCalledAndThenCallIsIgnored() {
        shnDevice.connect();

        reset(mockedBTDevice, mockedSHNDeviceListener);
        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // State ConnectedDiscoveringServices
    @Test
    public void whenInStateDiscoveringServicesTheGattCallbackIndicatesServicesDiscoveredThenTheSHNServiceIsConnectedToTheBleService() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNService).connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
        verify(mockedSHNDeviceListener, never()).onStateUpdated(any(SHNDevice.class));
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateDiscoveringServicesDisconnectIsCalledThenStateIsDisconnecting() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenDiscoveringServicesConnectTimeoutOccursThenTheStateIsChangedToDisconnecting() {
        connectTillGATTConnected();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        reset(mockedSHNDeviceListener);

        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    @Test
    public void whenInStateDiscoveringServicesGATIndicatedDisconnectedThenStateIsDisconnected() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateDiscoveringConnectIsCalledAndThenCallIsIgnored() {
        connectTillGATTConnected();

        reset(mockedBTDevice, mockedSHNDeviceListener);
        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    // State ConnectedInitializingServices
    @Test
    public void whenServicesAreDiscoveredServicesTheGattCallbackIndicatesServicesDiscoveredThenGetServicesIsCalled() {
        connectTillGATTServicesDiscovered();
        verify(mockedBTGatt).getServices();
    }

    @Test
    public void whenServicesAreDiscoveredAndDirectlyBecomeReadyThenTheDeviceBecomesConnected() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDevice.State.Connected, shnDevice.getState());
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void whenServicesAreDiscoveredAndTheServiceIndicatesAvailableThenTheStateRemainsConnecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);
        mockedServiceState = SHNService.State.Available;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
    }

    @Test
    public void whenServicesAreDiscoveredAndGotoErrorStateThenTheDeviceBecomesDisconnecting() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Error;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenServicesAreDiscoveredAndBTGATTIndicatesDisconnectedThenStateIsDisconecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenServicesAreDiscoveredAndDisconnectIsCalledThenStateIsDisconnecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenServicesAreDiscoveredConnectIsCalledAndThenCallIsIgnored() {
        connectTillGATTServicesDiscovered();

        reset(mockedBTDevice, mockedSHNDeviceListener);
        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateInitializingServicesATimeoutOccursThenTheStateIsChangedToDisconnecting() {
        shnDevice.connect();
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        reset(mockedSHNDeviceListener);

        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
    }

    // State ConnectedReady
    @Test
    public void whenInStateConnectedThenThereIsNoTimerRunning() {
        getDeviceInConnectedState();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenTheStateIsChangedToDisconnecting() {
        getDeviceInConnectedState();

        shnDevice.disconnect();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenDisconnectOnBTGattIsCalled() {
        getDeviceInConnectedState();

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
    }

    @Test
    public void whenInStateConnectedAndBTGATTIndicatesDisconnectedThenStateIsDisconnected() {
        getDeviceInConnectedState();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectedConnectIsCalledAndThenCallIsIgnored() {
        getDeviceInConnectedState();
        reset(mockedBTDevice, mockedSHNDeviceListener);

        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // State Disconnecting
    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheStateIsChangedToDisconnected() {
        getDeviceInConnectedState();
        shnDevice.disconnect();

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
        verify(mockedSHNService).disconnectFromBLELayer();
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenBondStatusListenerIsUnregistered() {
        getDeviceInConnectedState();
        shnDevice.disconnect();

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNCentral).unregisterBondStatusListenerForAddress(shnDevice, ADDRESS_STRING);
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesUnavailableThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();

        mockedServiceState = SHNService.State.Unavailable;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesAvailableThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);

        mockedServiceState = SHNService.State.Available;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesReadyThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);

        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateConnectedAServicesGoesToErrorStateThenTheDeviceBecomesDisconnecting() {
        connectTillGATTConnected();
        btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        reset(mockedSHNDeviceListener);
        mockedServiceState = SHNService.State.Error;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(SHNDevice.State.Disconnecting, shnDevice.getState());
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenInStateDisconnectingTheDisconnectMethodIsCalledThenThenStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);
        shnDevice.disconnect();

        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateDisconnectingConnectMethodIsCalledThenThenCallIsIgnored() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedBTDevice, mockedSHNDeviceListener);

        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // Device that requires bonding
    @Test
    public void whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, true);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndStateIsBondingThenWaitingUntilBondingStartedTimerIsStopped() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);

        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsCreatedThenServicesAreDiscovered() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);
        shnDevice.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDED, BluetoothDevice.BOND_BONDING);

        assertEquals(2, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();

        verify(mockedBTGatt).discoverServices();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsNotCreatedThenServicesAreDiscovered() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);
        shnDevice.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_NONE, BluetoothDevice.BOND_BONDING);

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondTimerExpiresThenServicesAreDiscovered() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        mockedInternalHandler.executeFirstScheduledExecution();

        verify(mockedBTGatt).discoverServices();
        assertEquals(SHNDeviceImpl.State.Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndGATTIndicatesDisconnectedThenStateIsDisconnected() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(SHNDeviceImpl.State.Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndDisconnectIsCalledThenStateIsDisconnecting() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(SHNDeviceImpl.State.Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndConnectIsCalledThenCallIsIgnored() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedBTDevice, mockedSHNDeviceListener);

        shnDevice.connect();

        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
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
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenReadRSSIOnConnectedDeviceThenGattReadRSSIIsCalled() {
        getDeviceInConnectedState();

        shnDevice.readRSSI();

        verify(mockedBTGatt).readRSSI();
    }

    @Test
    public void whenOnReadRSSIIsCalledThenTheListsnerIsNotified() {
        getDeviceInConnectedState();
        btGattCallback.onReadRemoteRssi(mockedBTGatt, 10, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNDeviceListener).onReadRSSI(10);
    }
}