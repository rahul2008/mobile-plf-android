package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.pins.philips.shinelib.framework.BleUUIDCreator;
import com.pins.philips.shinelib.helper.MockedScheduledThreadPoolExecutor;
import com.pins.philips.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private BluetoothDevice mockedBluetoothDevice;
    private SHNCentral mockedSHNCentral;
    private Context mockedContext;
    private BluetoothGatt mockedBluetoothGatt;
    private ArgumentCaptor<BluetoothGattCallback> bluetoothGattCallbackArgumentCaptor;
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

        mockedBluetoothDevice = (BluetoothDevice) Utility.makeThrowingMock(BluetoothDevice.class);
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
        shnDevice = new SHNDevice(mockedBluetoothDevice, mockedSHNCentral);
        shnService = new SHNService(shnDevice, SERVICE_UUID);
        shnDevice.registerService(shnService);
        shnCharacteristic = new SHNCharacteristic(shnDevice, CHARACTERISTIC_UUID);
        shnService.addRequiredSHNCharacteristic(shnCharacteristic);
        shnService.registerSHNServiceListener(shnDevice);

        mockedBluetoothGatt = (BluetoothGatt) Utility.makeThrowingMock(BluetoothGatt.class);
        bluetoothGattCallbackArgumentCaptor = ArgumentCaptor.forClass(BluetoothGattCallback.class);

        mockedBluetoothGattService = (BluetoothGattService) Utility.makeThrowingMock(BluetoothGattService.class);
        doReturn(SERVICE_UUID).when(mockedBluetoothGattService).getUuid();
        bluetoothGattServices = new ArrayList<>();
        bluetoothGattServices.add(mockedBluetoothGattService);

        mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        doReturn(CHARACTERISTIC_UUID).when(mockedBluetoothGattCharacteristic).getUuid();
        doReturn(CHARACTERISTIC_DATA).when(mockedBluetoothGattCharacteristic).getValue();
        bluetoothGattCharacteristics = new ArrayList<>();
        bluetoothGattCharacteristics.add(mockedBluetoothGattCharacteristic);

        doReturn(bluetoothGattServices).when(mockedBluetoothGatt).getServices();
        doReturn(bluetoothGattCharacteristics).when(mockedBluetoothGattService).getCharacteristics();

    }

    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateDisconnected, shnDevice.getState());
    }

    @Test
    public void theGetAddressFunctionReturnsTheAddressOfTheBluetoothDevice() {
        String address = "11:22:33:44:55:66";
        doReturn(address).when(mockedBluetoothDevice).getAddress();
        assertEquals(address, shnDevice.getAddress());
        verify(mockedBluetoothDevice).getAddress();
    }

    @Test
    public void theGetNameFunctionReturnsTheNameOfTheBluetoothDevice() {
        String name = "Moonshine";
        doReturn(name).when(mockedBluetoothDevice).getName();
        assertEquals(name, shnDevice.getName());
        verify(mockedBluetoothDevice).getName();
    }

    @Test
    public void whenOnConnectionStateChaneIndicatesConnectedThenTheSHNDeviceStateIsConnecting() {
        doReturn(mockedBluetoothGatt).when(mockedBluetoothDevice).connectGatt(any(Context.class), anyBoolean(), bluetoothGattCallbackArgumentCaptor.capture());
        doReturn(true).when(mockedBluetoothGatt).discoverServices();

        shnDevice.connect();
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateConnecting, shnDevice.getState());
        bluetoothGattCallbackArgumentCaptor.getValue().onConnectionStateChange(mockedBluetoothGatt, BluetoothGatt.GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);
        verify(mockedBluetoothGatt).discoverServices();
    }

    @Test
    public void whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected() {
        // Chain...
        whenOnConnectionStateChaneIndicatesConnectedThenTheSHNDeviceStateIsConnecting();

        bluetoothGattCallbackArgumentCaptor.getValue().onServicesDiscovered(mockedBluetoothGatt, BluetoothGatt.GATT_SUCCESS);
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateConnecting, shnDevice.getState());

        shnService.upperLayerReady();
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

    @Test
    public void whenTheCharacteristicIsNotReadableThenQueueingAReadFails() {
        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
        doReturn(0).when(mockedBluetoothGattCharacteristic).getProperties();

        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);

        assertFalse(shnCharacteristic.readCharacteristic(mockedShnGattCommandResultReporter));
    }

    @Test
    public void whenTheCharacteristicIsReadableButNotAvailableThenQueueingSucceedsButAStateErrorIsReported() {
        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
        doReturn(BluetoothGattCharacteristic.PROPERTY_READ).when(mockedBluetoothGattCharacteristic).getProperties();

        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);
        doNothing().when(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNInvalidStateError);

        doReturn(false).when(mockedBluetoothGatt).readCharacteristic(mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.readCharacteristic(mockedShnGattCommandResultReporter));
        verify(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNInvalidStateError);
    }

    @Test
    public void whenTheCharacteristicIsReadableAndAvailableThenQueueingSucceedsAndSuccessIsReported() {
        whenOnConnectionStateChangeAndOnServicesDiscoveredIndicateSuccessAndContainTheExpectedServicesThenTheSHNDeviceStateIsConnected();
        doReturn(BluetoothGattCharacteristic.PROPERTY_READ).when(mockedBluetoothGattCharacteristic).getProperties();

        SHNDevice.SHNGattCommandResultReporter mockedShnGattCommandResultReporter = (SHNDevice.SHNGattCommandResultReporter) Utility.makeThrowingMock(SHNDevice.SHNGattCommandResultReporter.class);
        doNothing().when(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNOk);

        doReturn(true).when(mockedBluetoothGatt).readCharacteristic(mockedBluetoothGattCharacteristic);

        verify(mockedSHNCentral, times(3)).getScheduledThreadPoolExecutor();
        assertTrue(shnCharacteristic.readCharacteristic(mockedShnGattCommandResultReporter));

        bluetoothGattCallbackArgumentCaptor.getValue().onCharacteristicRead(mockedBluetoothGatt, mockedBluetoothGattCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mockedShnGattCommandResultReporter).reportResult(SHNResult.SHNOk);

        verify(mockedSHNCentral, times(4)).getScheduledThreadPoolExecutor();
        verify(mockedSHNCentral).runOnHandlerThread(any(Runnable.class));
    }

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