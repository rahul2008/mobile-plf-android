package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by 310188215 on 06/05/15.
 */
@RunWith(PowerMockRunner.class)
public class SHNCharacteristicTest {
    private SHNCharacteristic shnCharacteristic;
    private UUID characteristicUUID;
    private BTGatt mockedBTGatt;
    private BluetoothGattCharacteristic mockedBluetoothGattCharacteristic;
    private SHNCommandResultReporter mockedSHNCommandResultReporter;

    @Before
    public void setUp() {
        mockedBTGatt = (BTGatt) Utility.makeThrowingMock(BTGatt.class);
        mockedBluetoothGattCharacteristic = (BluetoothGattCharacteristic) Utility.makeThrowingMock(BluetoothGattCharacteristic.class);
        mockedSHNCommandResultReporter = (SHNCommandResultReporter) Utility.makeThrowingMock(SHNCommandResultReporter.class);

        characteristicUUID = UUID.randomUUID();
        shnCharacteristic = new SHNCharacteristic(characteristicUUID);
    }

    @Test
    public void testGetState() {
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void testGetUuid() {
        assertEquals(characteristicUUID, shnCharacteristic.getUuid());
    }

    @Test
    public void testConnectToBLELayer() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        assertEquals(SHNCharacteristic.State.Active, shnCharacteristic.getState());
    }

    @Test
    public void testDisconnectFromBLELayer() {
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);
        shnCharacteristic.disconnectFromBLELayer();
        assertEquals(SHNCharacteristic.State.Inactive, shnCharacteristic.getState());
    }

    @Test
    public void testGetValue() {
        byte[] mockedData = new byte[]{'d', 'a', 't', 'a'};
        doReturn(mockedData).when(mockedBluetoothGattCharacteristic).getValue();

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        byte[] data = shnCharacteristic.getValue();
        assertEquals(mockedData, data);
    }

    @Test
    public void whenWriteIsCalledThenWriteCharacteristicOnBTGattIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(anyBluetoothGattCharacteristic(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.write(data, null));
        verify(mockedBTGatt).writeCharacteristic(mockedBluetoothGattCharacteristic, data);
    }

    @Test
    public void whenReadIsCalledThenReadCharacteristicOnBTGattIsCalled() {
        doNothing().when(mockedBTGatt).readCharacteristic(anyBluetoothGattCharacteristic());
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.read(null));
        verify(mockedBTGatt).readCharacteristic(mockedBluetoothGattCharacteristic);
    }

    @Test
    public void whenSetNotificationIsCalledThenSetCharacteristicNotificationOnBTGattIsCalled() {
        BluetoothGattDescriptor mockedDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(anyUUID());
        doReturn(true).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());
        doNothing().when(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.setNotification(true, null);
        verify(mockedBTGatt).setCharacteristicNotification(mockedBluetoothGattCharacteristic, true);
        verify(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    }

    @Test
    public void whenAReadRequestCompletesThenTheResultReporterIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).readCharacteristic(anyBluetoothGattCharacteristic());
        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.read(mockedSHNCommandResultReporter));

        shnCharacteristic.onReadWithData(mockedBTGatt, BluetoothGatt.GATT_SUCCESS, data);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, data);
    }

    @Test
    public void whenAWriteRequestCompletesThenTheResultReporterIsCalled() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        doNothing().when(mockedBTGatt).writeCharacteristic(anyBluetoothGattCharacteristic(), anyByteArray());
        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.write(data, mockedSHNCommandResultReporter));

        shnCharacteristic.onWrite(mockedBTGatt, BluetoothGatt.GATT_SUCCESS);

        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void testOnChanged() {
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        doNothing().when(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(anySHNCharacteristic(), anyByteArray());

        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.onChanged(mockedBTGatt, data);
        verify(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(shnCharacteristic, data);
    }

    @Test
    public void testOnDescriptorReadWithData() {
        boolean exceptionCaught = false;
        try {
            shnCharacteristic.onDescriptorReadWithData(null, null, BluetoothGatt.GATT_SUCCESS, null);
        } catch (Exception e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    @Test
    public void whenSetingNotificationsTrueOnACharacteristicThenWriteDescriptorIsCalled() {
        BluetoothGattDescriptor mockedDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(anyUUID());
        doReturn(true).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());
        doNothing().when(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.setNotification(true, mockedSHNCommandResultReporter));

        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, null);
    }

    @Test
    public void whenSetingNotificationsFalseOnACharacteristicThenWriteDescriptorIsCalled() {
        BluetoothGattDescriptor mockedDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(anyUUID());
        doReturn(true).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());
        doNothing().when(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.setNotification(false, mockedSHNCommandResultReporter));

        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNOk, null);
    }

    // Error paths
    @Test
    public void whenInactiveSetingNotificationsOnACharacteristicThenSetNotificationIs_NOT_Accepted() {
        assertFalse(shnCharacteristic.setNotification(false, mockedSHNCommandResultReporter));
    }

    @Test
    public void testOnChangedWithoutListener() {
        final int[] characteristicChangedListenerInvocationCount = {0};
        byte[] data = new byte[]{'d', 'a', 't', 'a'};
        SHNCharacteristic.SHNCharacteristicChangedListener mockedSHNCharacteristicChangedListener = (SHNCharacteristic.SHNCharacteristicChangedListener) Utility.makeThrowingMock(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        doAnswer(new Answer<Void>(){
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                characteristicChangedListenerInvocationCount[0]++;
                return null;
            }
        }).when(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(anySHNCharacteristic(), anyByteArray());

        shnCharacteristic.setShnCharacteristicChangedListener(mockedSHNCharacteristicChangedListener);
        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        shnCharacteristic.onChanged(mockedBTGatt, data);
        verify(mockedSHNCharacteristicChangedListener).onCharacteristicChanged(shnCharacteristic, data);
        assertEquals(1, characteristicChangedListenerInvocationCount[0]); // Added to prove that the doAnswer on the listener works

        shnCharacteristic.setShnCharacteristicChangedListener(null);
        shnCharacteristic.onChanged(mockedBTGatt, data);
        assertEquals(1, characteristicChangedListenerInvocationCount[0]);
    }

    @Test
    public void whenSetingNotificationsTrueOnACharacteristicThatDoesNOTSupportThatThenSetNotificationFails() {
        doReturn(false).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertFalse(shnCharacteristic.setNotification(true, mockedSHNCommandResultReporter));
    }

    @Test
    public void whenReadIsCalledWhenNotActiveThenReadIsNotAccepted() {
        assertFalse(shnCharacteristic.read(null));
    }

    @Test
    public void whenGetValueIsCalledWhenNotActiveThenNullIsReturned() {
        assertNull(shnCharacteristic.getValue());
    }

    @Test
    public void whenWriteIsCalledWhenNotActiveThenWriteIsNotAccepted() {
        assertFalse(shnCharacteristic.write(null, mockedSHNCommandResultReporter));
    }

    @Test
    public void whenOnDescriptorWriteIndicatesAnErrorOnACharacteristicThenTheErrorIsReported() {
        BluetoothGattDescriptor mockedDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(anyUUID());
        doReturn(true).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());
        doNothing().when(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.setNotification(true, mockedSHNCommandResultReporter));

        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNUnknownDeviceTypeError, null);
    }

    @Test
    public void whenSetNotificationIsCalledWithoutResultReporterThenNothingIsReported() {
        BluetoothGattDescriptor mockedDescriptor = (BluetoothGattDescriptor) Utility.makeThrowingMock(BluetoothGattDescriptor.class);
        doReturn(mockedDescriptor).when(mockedBluetoothGattCharacteristic).getDescriptor(anyUUID());
        doReturn(true).when(mockedBTGatt).setCharacteristicNotification(anyBluetoothGattCharacteristic(), anyBoolean());
        doNothing().when(mockedBTGatt).writeDescriptor(mockedDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        doNothing().when(mockedSHNCommandResultReporter).reportResult(anySHNResult(), anyByteArray());

        shnCharacteristic.connectToBLELayer(mockedBTGatt, mockedBluetoothGattCharacteristic);

        assertTrue(shnCharacteristic.setNotification(true, null));

        shnCharacteristic.onDescriptorWrite(mockedBTGatt, mockedDescriptor, BluetoothGatt.GATT_FAILURE);
//        verify(mockedSHNCommandResultReporter).reportResult(SHNResult.SHNUnknownDeviceTypeError, null);
    }

    // Helper functions
    private SHNCharacteristic anySHNCharacteristic() {
        return any(SHNCharacteristic.class);
    }

    private SHNResult anySHNResult() {
        return any(SHNResult.class);
    }

    private BluetoothGattCharacteristic anyBluetoothGattCharacteristic() {
        return any(BluetoothGattCharacteristic.class);
    }

    private byte[] anyByteArray() {
        return any(byte[].class);
    }

    private UUID anyUUID() {
        return any(UUID.class);
    }
}