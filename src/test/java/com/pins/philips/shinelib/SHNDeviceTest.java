package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.pins.philips.shinelib.bletestsupport.BTDevice;
import com.pins.philips.shinelib.bletestsupport.BTGatt;
import com.pins.philips.shinelib.framework.BleUUIDCreator;
import com.pins.philips.shinelib.helper.MockedScheduledThreadPoolExecutor;
import com.pins.philips.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
//@PrepareForTest(Log.class) // This does not work on the buildserver. TODO find out why...
public class SHNDeviceTest {
    public static final UUID SERVICE_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180D));
    public static final UUID CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A37));
    private static final byte[] CHARACTERISTIC_DATA = new byte[] {'T', 'e', 's', 't'};
    private SHNDevice shnDevice;
    private BTDevice mockedBTDevice;
    private SHNCentral mockedSHNCentral;
    private Context mockedContext;
    private BTGatt mockedBTGatt;
    private ArgumentCaptor<BTGatt.BTGattCallback> bluetoothGattCallbackArgumentCaptor;
    private List<BluetoothGattService> bluetoothGattServices;
    private List<BluetoothGattCharacteristic> bluetoothGattCharacteristics;
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;
    private BluetoothGattService mockedBluetoothGattService;
    private SHNService shnService;
    private SHNCharacteristic shnCharacteristic;
    private MockedScheduledThreadPoolExecutor mockedScheduledThreadPoolExecutor;

    @Before
    public void setUp() {
//        mockStatic(Log.class);

        mockedBTDevice = (BTDevice) Utility.makeThrowingMock(BTDevice.class);
        mockedSHNCentral = (SHNCentral) Utility.makeThrowingMock(SHNCentral.class);
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Exception e = (Exception) invocation.getArguments()[0];
                throw e;
            }
        }).when(mockedSHNCentral).reportExceptionOnAppMainThread(any(Exception.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = (Runnable) invocation.getArguments()[0];
                runnable.run();
                return null;
            }
        }).when(mockedSHNCentral).runOnHandlerThread(any(Runnable.class));
        mockedContext = (Context) Utility.makeThrowingMock(Context.class);
        mockedScheduledThreadPoolExecutor = new MockedScheduledThreadPoolExecutor();
        doReturn(mockedScheduledThreadPoolExecutor.getMock()).when(mockedSHNCentral).getScheduledThreadPoolExecutor();
        doReturn(mockedContext).when(mockedSHNCentral).getApplicationContext();
        shnDevice = new SHNDevice(mockedBTDevice, mockedSHNCentral);
        Set<UUID> requiredUUIDs = new HashSet<>();
        requiredUUIDs.add(CHARACTERISTIC_UUID);
        shnService = new SHNService(SERVICE_UUID, requiredUUIDs, new HashSet<UUID>());
        shnDevice.registerService(shnService);
        shnCharacteristic = new SHNCharacteristic(CHARACTERISTIC_UUID);
        shnService.addRequiredSHNCharacteristic(shnCharacteristic);
        shnService.registerSHNServiceListener(shnDevice);

        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);
        bluetoothGattCallbackArgumentCaptor = ArgumentCaptor.forClass(BTGatt.BTGattCallback.class);

        mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        doReturn(SERVICE_UUID).when(mockedBluetoothGattService).getUuid();
        bluetoothGattServices = new ArrayList<>();
        bluetoothGattServices.add(mockedBluetoothGattService);

        mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        doReturn(CHARACTERISTIC_UUID).when(mockedBluetoothGattCharacteristic).getUuid();
        doReturn(CHARACTERISTIC_DATA).when(mockedBluetoothGattCharacteristic).getValue();
        bluetoothGattCharacteristics = new ArrayList<>();
        bluetoothGattCharacteristics.add(mockedBluetoothGattCharacteristic);

        doReturn(bluetoothGattServices).when(mockedBTGatt).getServices();
        doReturn(bluetoothGattCharacteristics).when(mockedBluetoothGattService).getCharacteristics();

    }

    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateDisconnected, shnDevice.getState());
    }

    @Test
    public void theGetAddressFunctionReturnsTheAddressOfTheBluetoothDevice() {
        String address = "11:22:33:44:55:66";
        doReturn(address).when(mockedBTDevice).getAddress();
        assertEquals(address, shnDevice.getAddress());
        verify(mockedBTDevice).getAddress();
    }

    @Test
    public void theGetNameFunctionReturnsTheNameOfTheBluetoothDevice() {
        String name = "Moonshine";
        doReturn(name).when(mockedBTDevice).getName();
        assertEquals(name, shnDevice.getName());
        verify(mockedBTDevice).getName();
    }

    @Test
    public void whenOnConnectionStateChaneIndicatesConnectedThenTheSHNDeviceStateIsConnecting() {
        doReturn(mockedBTGatt).when(mockedBTDevice).connectGatt(any(Context.class), anyBoolean(), bluetoothGattCallbackArgumentCaptor.capture());
        doNothing().when(mockedBTGatt).discoverServices();

        shnDevice.connect();
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateConnecting, shnDevice.getState());
        bluetoothGattCallbackArgumentCaptor.getValue().onConnectionStateChange(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);
        verify(mockedBTGatt).discoverServices();
    }

    @Test
    public void whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected() {
        // Chain...
        whenOnConnectionStateChaneIndicatesConnectedThenTheSHNDeviceStateIsConnecting();

        bluetoothGattCallbackArgumentCaptor.getValue().onServicesDiscovered(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateConnecting, shnDevice.getState());

        shnService.transitionToReady();
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateConnected, shnDevice.getState());
    }

    @Test
    public void testHandleConnectTimeout() {

    }

    @Test
    public void testDisconnect() {

    }

    @Test
    public void testSetShnDeviceListener() {
    }

    @Test
    public void testGetSupportedCapabilityTypes() {

    }

    @Test
    public void testGetCapabilityForType() {

    }

    @Test
    public void testRegisterCapability() {

    }

    @Test
    public void testRegisterService() {

    }

// We are not testing the properties of the characteristic anymore. It is assumed that the OS layer does this.....
//    @Test
//    public void whenTheCharacteristicIsNotReadableThenQueueingAReadFails() {
//        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
//        doReturn(0).when(mockedBluetoothGattCharacteristic).getProperties();
//        doNothing().when(mockedBTGatt).readCharacteristic(any(BluetoothGattCharacteristic.class));
//
//        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);
//
//        assertFalse(shnCharacteristic.read(mockedShnGattCommandResultReporter));
//    }

//    This test does not belong here
//    @Test
//    public void whenTheCharacteristicIsReadableButNotAvailableThenQueueingSucceedsButAStateErrorIsReported() {
//        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
//        doReturn(BluetoothGattCharacteristic.PROPERTY_READ).when(mockedBluetoothGattCharacteristic).getProperties();
//
//        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);
//        doNothing().when(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNInvalidStateError);
//
//        doNothing().when(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
//
//        assertTrue(shnCharacteristic.read(mockedShnGattCommandResultReporter));
//        verify(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNInvalidStateError);
//    }

//    This test does not belong here
//    @Test
//    public void whenTheCharacteristicIsReadableAndAvailableThenQueueingSucceedsAndSuccessIsReported() {
//        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
//        doReturn(BluetoothGattCharacteristic.PROPERTY_READ).when(mockedBluetoothGattCharacteristic).getProperties();
//
//        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);
//        doNothing().when(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNOk);
//
//        doNothing().when(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
//
//        verify(mockedSHNCentral, times(3)).getScheduledThreadPoolExecutor();
//        assertTrue(shnCharacteristic.read(mockedShnGattCommandResultReporter));
//
//        bluetoothGattCallbackArgumentCaptor.getValue().onCharacteristicReadWithData(mockedBTGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS, any(byte[].class));
//        verify(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNOk);
//
//        verify(mockedSHNCentral, times(4)).getScheduledThreadPoolExecutor();
//    }

    @Test
    public void testHandleOnCharacteristicRead() {

    }

    @Test
    public void testOnServiceStateChanged() {

    }

    @Test
    public void testToString() {

    }
}