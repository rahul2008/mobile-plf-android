/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

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
import com.philips.pins.shinelib.statemachine.SHNDeviceResources;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.wrappers.SHNCapabilityNotificationsWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.philips.pins.shinelib.SHNCentral.State.SHNCentralStateNotReady;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Connecting;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnecting;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored"})
@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class SHNDeviceImplTest {

    @Mock
    private BTDevice mockedBTDevice;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private Context mockedContext;

    @Mock
    private BTGatt mockedBTGatt;

    @Mock
    private SHNService mockedSHNService;

    @Mock
    private SHNCharacteristic mockedSHNCharacteristic;

    @Mock
    private BluetoothGattService mockedBluetoothGattService;

    @Mock
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;

    @Mock
    private BluetoothGattDescriptor mockedBluetoothGattDescriptor;

    @Mock
    private SHNDeviceImpl.SHNDeviceListener mockedSHNDeviceListener;

    @Mock
    private SHNDevice.DiscoveryListener mockedDiscoveryListener;

    @Mock
    private BluetoothDevice mockedBluetoothDevice;

    @Mock
    private SHNDeviceResources mockedDeviceResources;

    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;

    private SHNCentral.SHNBondStatusListener bondStatusListener;
    private SHNCentral.SHNCentralListener centralStateListener;
    private List<BluetoothGattService> discoveredServices;
    private List<BluetoothGattCharacteristic> discoveredCharacteristics;
    private SHNService.State mockedServiceState;

    private static final String ADDRESS_STRING = "DE:AD:CO:DE:12:34";
    private static final String NAME_STRING = "TestDevice";

    private static final String TEST_DEVICE_TYPE = "TEST_DEVICE_TYPE";
    private static final byte[] MOCK_BYTES = new byte[]{0x42};
    private static final UUID MOCK_UUID = UUID.randomUUID();

    private SHNDeviceImpl shnDevice;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(SHNTagger.class);

        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();

        Timer.setHandler(mockedInternalHandler.getMock());

        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();
        doReturn(SHNCentral.State.SHNCentralStateReady).when(mockedSHNCentral).getShnCentralState();

        doAnswer((Answer<BTGatt>) invocation -> mockedBTGatt).when(mockedBTDevice).connectGatt(isA(Context.class), anyBoolean(), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), anyInt());

        when(mockedBTDevice.createBond()).thenReturn(true);

        doReturn(ADDRESS_STRING).when(mockedBTDevice).getAddress();

        discoveredServices = new ArrayList<>();
        discoveredServices.add(mockedBluetoothGattService);
        doReturn(discoveredServices).when(mockedBTGatt).getServices();

        discoveredCharacteristics = new ArrayList<>();
        discoveredCharacteristics.add(mockedBluetoothGattCharacteristic);
        doReturn(discoveredCharacteristics).when(mockedBluetoothGattService).getCharacteristics();

        mockedServiceState = SHNService.State.Ready;
        doAnswer((Answer<SHNService.State>) invocation -> mockedServiceState).when(mockedSHNService).getState();
        doReturn(mockedContext).when(mockedSHNCentral).getApplicationContext();

        doReturn(mockedBluetoothGattService).when(mockedBluetoothGattCharacteristic).getService();
        doReturn(mockedBluetoothGattCharacteristic).when(mockedBluetoothGattDescriptor).getCharacteristic();

        // Mock Characteristic for DiscoveryListener
        doReturn(MOCK_UUID).when(mockedBluetoothGattCharacteristic).getUuid();
        doReturn(MOCK_BYTES).when(mockedBluetoothGattCharacteristic).getValue();

        doReturn(NAME_STRING).when(mockedBTDevice).getName();
        doReturn(ADDRESS_STRING).when(mockedBTDevice).getAddress();

        doAnswer((Answer<Void>) invocation -> {
            bondStatusListener = (SHNCentral.SHNBondStatusListener) invocation.getArguments()[0];
            return null;
        }).when(mockedSHNCentral).registerBondStatusListenerForAddress(isA(SHNCentral.SHNBondStatusListener.class), anyString());

        doAnswer((Answer<Void>) invocation -> {
            centralStateListener = (SHNCentral.SHNCentralListener) invocation.getArguments()[0];
            return null;
        }).when(mockedSHNCentral).addInternalListener(isA(SHNCentral.SHNCentralListener.class));

        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, new SHNDeviceResources(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.NONE, BluetoothGatt.CONNECTION_PRIORITY_BALANCED));
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);
        shnDevice.registerDiscoveryListener(mockedDiscoveryListener);
        shnDevice.registerService(mockedSHNService);

        when(mockedBluetoothDevice.getAddress()).thenReturn(ADDRESS_STRING);
    }

    private void connectTillGATTConnected() {
        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
    }

    private void connectTillGATTServicesDiscovered() {
        connectTillGATTConnected();
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
    }

    private void getDeviceInConnectedState() {
        connectTillGATTServicesDiscovered();
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);
    }

    @Test
    public void whenSHNDeviceIsBuiltThenItsConnectionPriorityIsInitialisedAsBalanced() {

        connectTillGATTConnected();

        verify(mockedBTDevice).connectGatt(mockedSHNCentral.getApplicationContext(), false, mockedSHNCentral, shnDevice.btGattCallback, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
    }

    @Test
    public void whenSHNDeviceIsBuiltAndAnInvalidConnectionPriorityIsSetTheTheConnectionPriorityIsInitialisedAsBalanced() {
        int veryInvalidConnectionPriority = 1000;
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, veryInvalidConnectionPriority);

        connectTillGATTConnected();

        verify(mockedBTDevice).connectGatt(mockedSHNCentral.getApplicationContext(), false, mockedSHNCentral, shnDevice.btGattCallback, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
    }

    @Test
    public void whenSHNDeviceIsBuiltAndAValidConnectionPriorityIsSetTheTheConnectionPriorityIsInitialisedAsProvided() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, BluetoothGatt.CONNECTION_PRIORITY_HIGH);

        connectTillGATTConnected();

        verify(mockedBTDevice).connectGatt(mockedSHNCentral.getApplicationContext(), false, mockedSHNCentral, shnDevice.btGattCallback, BluetoothGatt.CONNECTION_PRIORITY_HIGH);
    }

    // State Disconnected
    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        assertEquals(Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectedTheDisconnectMethodIsCalledThenTheStateIsDisconnected() {
        shnDevice.disconnect();

        assertEquals(Disconnected, shnDevice.getState());
    }

    @Test
    public void givenInStateDisconnectedWhenTheDisconnectMethodIsCalledThenListenerIsNotified() {

        shnDevice.disconnect();

        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
    }

    @Test
    public void givenInStateDisconnectedWhenTheConnectMethodIsCalledThenTheStateChangesToConnecting() {

        shnDevice.connect();

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Connecting);
    }

    @Test
    public void whenInStateDisconnectedWhenTheStateChangesToConnectingThenTheConnectGattIsCalled() {
        shnDevice.connect();
        verify(mockedBTDevice).connectGatt(mockedContext, false, mockedSHNCentral, shnDevice.btGattCallback, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // State GattConnecting
    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenTheOnFailedToConnectGetsCalled() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(Disconnected, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenConnectTimerIsStopped() {
        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_DISCONNECTED);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingGATTCallbackIndicatedDisconnectedThenBtGattCloseIsCalled() {
        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateConnectingThenThereIsNoTimerRunning() {
        shnDevice.connect();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void givenInStateConnectingWhenTheGattCallbackIndicatesConnectedThenDiscoverServicesIsCalled() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);
        verify(mockedBTGatt).discoverServices();

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectIsCalledAndTimerStarted() {
        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenCloseIsCalled() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedBTGatt).disconnect();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();
        verify(mockedSHNDeviceListener).onFailedToConnect(eq(shnDevice), any(SHNResult.class));
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesConnectedWithStatusFailureThenDisconnectFromBLELayerIsNotCalled() {
        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mockedSHNService, never()).disconnectFromBLELayer();
    }

    @Test
    public void whenInStateConnectingTheGattIndicatesDisconnectedThenTheOnFailedToConnectIsCalled() {
        shnDevice.connect();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
    }

    @Test
    public void whenInStateConnectingDisconnectIsCalledThenStateIsDisconnecting() {
        shnDevice.connect();
        assertEquals(Connecting, shnDevice.getState());
        reset(mockedSHNDeviceListener);
        shnDevice.disconnect();

        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void disconnectIsDeferredTillConnectCallbackIsReceived() {
        whenInStateConnectingDisconnectIsCalledThenStateIsDisconnecting();
        reset(mockedBTGatt);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothGatt.STATE_CONNECTED);

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        PowerMockito.verifyNoMoreInteractions(mockedSHNDeviceListener);
    }

    @Test
    public void whenDisconnectedBleCallbackIsReceivedThenCloseIsCalled() {
        disconnectIsDeferredTillConnectCallbackIsReceived();
        reset(mockedBTGatt, mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, 0, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        PowerMockito.verifyNoMoreInteractions(mockedSHNDeviceListener);
    }

    @Test
    public void whenInStateConnectingAndDisconnectIsCalledThenTheStateBecomesDisconnecting() {
        shnDevice.connect();
        assertEquals(Connecting, shnDevice.getState());
        shnDevice.disconnect();
        assertEquals(Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingDisconnectIsCalledAndThenDisconnectOnBTGattIsCalled() {
        whenInStateConnectingAndDisconnectIsCalledThenTheStateBecomesDisconnecting();

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
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

    // State DiscoveringServices
    @Test
    public void whenInStateDiscoveringServicesTheGattCallbackIndicatesServicesDiscoveredThenTheSHNServiceIsConnectedToTheBleService() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNService).connectToBLELayer(mockedBTGatt, mockedBluetoothGattService);
        verify(mockedSHNDeviceListener, never()).onStateUpdated(any(SHNDevice.class), any(SHNDevice.State.class));
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
        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenDiscoveringServicesConnectTimeoutOccursThenTheStateIsChangedToDisconnecting() {
        connectTillGATTConnected();
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        reset(mockedSHNDeviceListener);

        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
    }

    @Test
    public void whenInStateDiscoveringServicesGATIndicatedDisconnectedThenStateIsDisconnected() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        assertEquals(Disconnected, shnDevice.getState());
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

    // State InitializingServices
    @Test
    public void whenServicesAreDiscoveredServicesTheGattCallbackIndicatesServicesDiscoveredThenGetServicesIsCalled() {
        connectTillGATTServicesDiscovered();
        verify(mockedBTGatt, times(2)).getServices();
    }

    @Test
    public void whenServicesAreDiscoveredAndNoServiceAreFoundThenReconnectWithTheDevice() {
        connectTillGATTConnected();
        reset(mockedBTDevice);

        List emptyServices = new ArrayList<>();
        doReturn(emptyServices).when(mockedBTGatt).getServices();

        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        verify(mockedBTGatt).disconnect();

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_DISCONNECTED);

        verify(mockedBTDevice).connectGatt(isA(Context.class), isA(Boolean.class), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), isA(Integer.class));
    }

    @Test
    public void whenServicesAreDiscoveredAndNoServiceAreFoundThenTagIsSentWithProperData() {

        connectTillGATTConnected();
        reset(mockedBTDevice);
        List emptyServices = new ArrayList<>();
        doReturn(emptyServices).when(mockedBTGatt).getServices();
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("No services found.", captor.getValue());
    }

    @Test
    public void whenServicesAreDiscoveredAndDirectlyBecomeReadyThenTheDeviceBecomesConnected() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Connected, shnDevice.getState());
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Connected);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
    }

    @Test
    public void whenServicesAreDiscoveredAndTheServiceIndicatesAvailableThenTheStateRemainsConnecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);
        mockedServiceState = SHNService.State.Available;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
    }

    @Test
    public void whenServicesAreDiscoveredAndGotoErrorStateThenTheDeviceBecomesDisconnecting() {
        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Error;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Disconnecting, shnDevice.getState());
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenServicesAreDiscoveredAndGotoErrorStateThenTagIsSentWithProperData() {

        connectTillGATTConnected();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Error;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        String result = String.format("Service [%s] state changed to error, state [%s]", null, SHNService.State.Error);
        assertEquals(result, captor.getValue());
    }

    @Test
    public void whenServicesAreDiscoveredAndBTGATTIndicatesDisconnectedThenStateIsDisconecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenServicesAreDiscoveredAndDisconnectIsCalledThenStateIsDisconnecting() {
        connectTillGATTServicesDiscovered();
        reset(mockedSHNDeviceListener);

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
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
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        reset(mockedSHNDeviceListener);

        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();

        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
    }

    @Test
    public void whenInStateInitializingServicesATimeoutOccursThenTagIsSentWithProperData() {

        shnDevice.connect();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        reset(mockedSHNDeviceListener);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("connect timeout in SHNConnectingState", captor.getValue());
    }

    // State Ready
    @Test
    public void whenInStateConnectedThenThereIsNoTimerRunning() {
        getDeviceInConnectedState();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectedDisconnectIsCalledThenTheStateIsChangedToDisconnecting() {
        getDeviceInConnectedState();

        shnDevice.disconnect();

        assertEquals(Disconnecting, shnDevice.getState());
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

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenInStateConnectedConnectIsCalledAndThenListenerIsNotified() {
        getDeviceInConnectedState();
        reset(mockedBTDevice, mockedSHNDeviceListener);

        shnDevice.connect();

        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Connected);
        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    // State Disconnecting
    @Test
    public void whenInStateDisconnectingTheCallbackIndicatesDisconnectedThenTheStateIsChangedToDisconnected() {
        getDeviceInConnectedState();
        shnDevice.disconnect();

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedSHNService).disconnectFromBLELayer();
        verify(mockedBTGatt).close();
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesUnavailableThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();

        mockedServiceState = SHNService.State.Unavailable;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesAvailableThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);

        mockedServiceState = SHNService.State.Available;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateDisconnectingTheServiceIndicatesReadyThenTheStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);

        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateConnectedAServicesGoesToErrorStateThenTheDeviceBecomesDisconnecting() {
        connectTillGATTConnected();
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        mockedServiceState = SHNService.State.Ready;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        reset(mockedSHNDeviceListener);
        mockedServiceState = SHNService.State.Error;
        shnDevice.onServiceStateChanged(mockedSHNService, mockedServiceState);

        assertEquals(Disconnecting, shnDevice.getState());
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener, never()).onFailedToConnect(any(SHNDevice.class), any(SHNResult.class));
        verify(mockedBTGatt).disconnect();
    }

    @Test
    public void whenInStateDisconnectingTheDisconnectMethodIsCalledThenThenStateIsDisconnecting() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener, mockedBTGatt);
        shnDevice.disconnect();

        assertEquals(Disconnecting, shnDevice.getState());
        verifyZeroInteractions(mockedSHNDeviceListener, mockedBTGatt);
    }

    @Test
    public void whenInStateDisconnectingConnectMethodIsCalledThenListenerIsNotifiedWithFailedToConnect() {
        getDeviceInConnectedState();
        shnDevice.disconnect();
        reset(mockedBTDevice, mockedSHNDeviceListener);

        shnDevice.connect();

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verifyNoMoreInteractions(mockedBTDevice, mockedSHNDeviceListener);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    // Device that requires bonding
    @Test
    public void whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.PERIPHERAL, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingNoneSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.NONE, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedBTGatt).discoverServices();
        verify(mockedBTDevice, never()).createBond();
    }

    @Test
    public void whenBondingPeripheralSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.PERIPHERAL, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedBTGatt, never()).discoverServices();
        verify(mockedBTDevice, never()).createBond();
    }

    @Test
    public void whenBondingAppSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.APP, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedBTGatt, never()).discoverServices();
        verify(mockedBTDevice).createBond();
    }

    @Test
    public void whenBondingAppSHNDeviceInStateConnectingCreataeBondReturnsFalseGATTCallbackIndicatedConnectedThenStateIsConnecting() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.APP, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);

        when(mockedBTDevice.createBond()).thenReturn(false);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedBTGatt).discoverServices();
        verify(mockedBTDevice).createBond();
    }

    @Test
    public void whenBondingAppSHNDeviceInStateConnectingCreateBondReturnsTrueGATTCallbackIndicatedConnectedThenTagIsSentWithProperData() {
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.APP, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        shnDevice.registerSHNDeviceListener(mockedSHNDeviceListener);
        when(mockedBTDevice.createBond()).thenReturn(false);

        shnDevice.connect();
        reset(mockedSHNDeviceListener);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Already bonded, bonding or bond creation failed.", captor.getValue());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndStateIsBondingThenWaitingUntilBondingStartedTimerIsStopped() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);

        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsCreatedThenServicesAreDiscovered() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);
        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDED, BluetoothDevice.BOND_BONDING);

        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
        mockedInternalHandler.executeFirstScheduledExecution();

        verify(mockedBTGatt).discoverServices();
        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsNotCreatedThenDeviceIsDisonnected() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);
        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_NONE, BluetoothDevice.BOND_BONDING);

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsNotCreatedThenTagIsSentWithProperData() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_NONE, BluetoothDevice.BOND_BONDING);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        final String result = String.format("Bond lost; currentBondState [%s], previousBondState [%s]", BluetoothDevice.BOND_NONE, BluetoothDevice.BOND_BONDING);
        assertEquals(result, captor.getValue());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondTimerExpiresThenServicesAreDiscovered() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        mockedInternalHandler.executeFirstScheduledExecution();

        verify(mockedBTGatt).discoverServices();
        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondTimerExpiresThenTagIsSentWithProperData() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        mockedInternalHandler.executeFirstScheduledExecution();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        assertEquals("Timed out waiting until bonded; trying service discovery", captor.getValue());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndGATTIndicatesDisconnectedThenStateIsDisconnected() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt, never()).disconnect();
        verify(mockedBTGatt).close();
        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndBondIsNotCreatedAndDeviceIsDisdconnectenThenFailedErrorIsGiven() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_BONDING, BluetoothDevice.BOND_NONE);
        bondStatusListener.onBondStatusChanged(mockedBluetoothDevice, BluetoothDevice.BOND_NONE, BluetoothDevice.BOND_BONDING);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt).disconnect();
        assertEquals(Disconnected, shnDevice.getState());
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorBondLost);
    }

    @Test
    public void whenBondingSHNDeviceInStateConnectingAndDisconnectIsCalledThenStateIsDisconnecting() {
        whenBondingSHNDeviceInStateConnectingGATTCallbackIndicatedConnectedThenStateIsConnecting();
        reset(mockedSHNDeviceListener);

        shnDevice.disconnect();

        verify(mockedBTGatt).disconnect();
        verify(mockedBTGatt, never()).close();
        assertEquals(Disconnecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        assertEquals(1, mockedInternalHandler.getScheduledExecutionCount());
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

        shnDevice.btGattCallback.onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicReadWithData(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), anyInt(), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnCharacteristicWriteThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        shnDevice.btGattCallback.onCharacteristicWrite(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNService).onCharacteristicWrite(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), anyInt());
    }

    @Test
    public void whenInStateConnectedOnCharacteristicChangedWithDataThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        shnDevice.btGattCallback.onCharacteristicChangedWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onCharacteristicChangedWithData(isA(BTGatt.class), isA(BluetoothGattCharacteristic.class), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorReadWithDataThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        shnDevice.btGattCallback.onDescriptorReadWithData(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS, new byte[]{'d', 'a', 't', 'a'});
        verify(mockedSHNService).onDescriptorReadWithData(isA(BTGatt.class), isA(BluetoothGattDescriptor.class), anyInt(), isA(byte[].class));
    }

    @Test
    public void whenInStateConnectedOnDescriptorWriteThenTheServiceIsCalled() {
        getDeviceInConnectedState();

        shnDevice.btGattCallback.onDescriptorWrite(mockedBTGatt, mockedBluetoothGattDescriptor, BluetoothGatt.GATT_SUCCESS);
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

    // Error conditions
    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredErrorThenADisconnectIsInitiated() {
        connectTillGATTConnected();
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_FAILURE);
        verify(mockedBTGatt).disconnect();
        assertEquals(Disconnecting, shnDevice.getState());
    }

    @Test
    public void whenInStateConnectingTheGattCallbackIndicatesServicesDiscoveredErrorThenTagIsSentWithProperData() {

        connectTillGATTConnected();
        shnDevice.btGattCallback.onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_FAILURE);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        String result = String.format("onServicedDiscovered: error discovering services, status [%s]; disconnecting.", BluetoothGatt.GATT_FAILURE);
        assertEquals(result, captor.getValue());
    }

    @Test
    public void whenConnectWithoutTimeoutThenNoTimeoutIsSet() {
        shnDevice.connect();
        assertEquals(0, mockedInternalHandler.getScheduledExecutionCount());
    }

    @Test
    public void givenConnected_whenRemoteDisconnects_thenCloseGattIsCalled() {
        getDeviceInConnectedState();
        assertEquals(Connected, shnDevice.getState());

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt).close();
    }

    @Test
    public void whenConnectWithTimeOutCalledThenConnectGattIsCalled() {
        shnDevice.connect(1L);

        verify(mockedBTDevice).connectGatt(isA(Context.class), eq(false), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), eq(0));
    }

    @Test
    public void whenConnectWithTimeOutCalledThenStateIsConnecting() {
        shnDevice.connect(1L);

        assertEquals(Connecting, shnDevice.getState());
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Connecting);
    }

    @Test
    public void whenConnectWithTimeOutCalledInStateConnectingThenGattConnectIsNotCalledAgain() {
        shnDevice.connect(1L);
        shnDevice.connect(1L);

        verify(mockedBTDevice, times(1)).connectGatt(isA(Context.class), eq(false), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), eq(0));
    }

    @Test
    public void whenConnectWithTimeOutCalledThenRegisterInternalSHNCentralListenerIsCalled() {
        shnDevice.connect(1L);

        verify(mockedSHNCentral).addInternalListener(shnDevice.centralListener);
    }

    @Test
    public void whenGattErrorIsReceivedAndTimeOutNotElapsedThenRetryIsIssuedSilently() {
        shnDevice.connect(1000L);

        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, SHNDeviceImpl.GATT_ERROR, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener, never()).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(Connecting, shnDevice.getState());
        verify(mockedBTDevice, times(2)).connectGatt(isA(Context.class), eq(false), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), eq(0));
    }

    @Test
    public void whenGattErrorIsReceivedAndTimeOutNotElapsedThenThePreviousConnectionIsClosed() {
        shnDevice.connect(1000L);

        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, SHNDeviceImpl.GATT_ERROR, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedBTGatt).close();
    }

    @Test
    public void whenGattErrorIsReceivedAndTimeOutNotElapsedThenRetryIsIssuedSilentlyMultipleTimes() {
        shnDevice.connect(100L);
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, SHNDeviceImpl.GATT_ERROR, BluetoothGatt.STATE_DISCONNECTED);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, SHNDeviceImpl.GATT_ERROR, BluetoothGatt.STATE_DISCONNECTED);
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, SHNDeviceImpl.GATT_ERROR, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener, never()).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener, never()).onStateUpdated(shnDevice, Connecting);
        assertEquals(Connecting, shnDevice.getState());
        verify(mockedBTDevice, times(4)).connectGatt(isA(Context.class), eq(false), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), eq(0));
    }

    @Test
    public void whenDisconnectIsIssuedThenRetryIsNotPerformed() {
        shnDevice.connect(100L);
        shnDevice.disconnect();
        reset(mockedSHNDeviceListener);

        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);

        verify(mockedSHNDeviceListener, never()).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(Disconnected, shnDevice.getState());
        verify(mockedBTDevice, times(1)).connectGatt(isA(Context.class), eq(false), isA(SHNCentral.class), isA(BTGatt.BTGattCallback.class), eq(0));
    }

    @Test(expected = InvalidParameterException.class)
    public void whenNegativeTimeOutItProvidedThenExceptionIsGenerated() {
        shnDevice.connect(-100);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenZeroTimeOutItProvidedThenExceptionIsGenerated() {
        shnDevice.connect(0);
    }

    @Test
    public void whenBluetoothIsSwitchedOffDuringReconnectCycleThenFailedToConnectIsReported() {
        shnDevice.connect(100L);
        reset(mockedSHNDeviceListener);

        shnDevice.centralListener.onStateUpdated(mockedSHNCentral, SHNCentralStateNotReady);

        verify(mockedSHNDeviceListener).onFailedToConnect(shnDevice, SHNResult.SHNErrorInvalidState);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnecting);
        verify(mockedSHNDeviceListener).onStateUpdated(shnDevice, Disconnected);
        assertEquals(Disconnected, shnDevice.getState());
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

        shnDevice.btGattCallback.onReadRemoteRssi(mockedBTGatt, 10, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNDeviceListener).onReadRSSI(10);
    }

    /**
     * DiscoveryListener Tests
     */

    @Test
    public void whenDeviceHasDiscoveryListenerItCalls_onServiceDiscovered() {
        connectTillGATTServicesDiscovered();
        verify(mockedDiscoveryListener, times(discoveredServices.size())).onServiceDiscovered(mockedBluetoothGattService.getUuid(), mockedSHNService);
    }

    @Test
    public void whenDeviceHasNoDiscoveryListenerItNeverCalls_onServiceDiscovered() {
        // Creating new SHNDeviceImpl => will have no DiscoveryListener set
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.PERIPHERAL, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);
        connectTillGATTServicesDiscovered();
        verify(mockedDiscoveryListener, never()).onServiceDiscovered(any(UUID.class), any(SHNService.class));
    }

    @Test
    public void whenDeviceHasDiscoveryListenerItCalls_onCharacteristicDiscovered() {
        shnDevice.onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
        verify(mockedDiscoveryListener).onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
    }

    @Test
    public void whenDeviceHasNoDiscoveryListenerItNeverCalls_onCharacteristicDiscovered() {
        // Creating new SHNDeviceImpl => will have no DiscoveryListener set
        shnDevice = new SHNDeviceImpl(mockedBTDevice, mockedSHNCentral, TEST_DEVICE_TYPE, SHNDeviceImpl.SHNBondInitiator.PERIPHERAL, BluetoothGatt.CONNECTION_PRIORITY_BALANCED);

        shnDevice.onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
        verify(mockedDiscoveryListener, never()).onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
    }

    @Test
    public void whenMultipleDiscoveryListenersAreRegisteredOnlyTheLastOneIsCalled() {
        SHNDevice.DiscoveryListener mock2 = mock(SHNDevice.DiscoveryListener.class);
        shnDevice.registerDiscoveryListener(mock2);

        shnDevice.onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
        verify(mock2, times(1)).onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
        verify(mockedDiscoveryListener, never()).onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
    }

    @Test
    public void whenDiscoveryListenerIsUnregisteredNoCallsWillBeForwarded() {
        shnDevice.unregisterDiscoveryListener(mockedDiscoveryListener);
        shnDevice.onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
        verify(mockedDiscoveryListener, never()).onCharacteristicDiscovered(MOCK_UUID, MOCK_BYTES, mockedSHNCharacteristic);
    }

    @Test
    public void shouldBeRobustAgainstServiceStateChangingToErrorWhileGattStateIsDisconnected() {
        connectTillGATTServicesDiscovered();
        doAnswer((Answer<Void>) invocation -> {
            shnDevice.onServiceStateChanged(mockedSHNService, SHNService.State.Error);
            return null;
        }).when(mockedSHNService).disconnectFromBLELayer();
        shnDevice.btGattCallback.onConnectionStateChange(mockedBTGatt, 0 /* don't care */, BluetoothProfile.STATE_DISCONNECTED);
    }

    @Test
    public void itRegistersItselfForCentralStateUpdates() {

        assertNotNull(centralStateListener);
    }

    @Test
    public void givenDeviceResourcesHasABtGatt_whenRefreshCacheIsCalled_thenTheCallIsForwardedToThatBtGatt() {
        when(mockedDeviceResources.getBtGatt()).thenReturn(mockedBTGatt);

        // TODO
    }
}
