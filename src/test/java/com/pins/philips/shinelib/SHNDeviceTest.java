package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
//@PrepareForTest(Log.class) // This does not work on the buildserver. TODO find out why...
public class SHNDeviceTest {
    private SHNDevice shnDevice;
    private BluetoothDevice mockedBluetoothDevice;
    private SHNCentral mockedSHNCentral;

    @Before
    public void setUp() {
//        mockStatic(Log.class);

        mockedBluetoothDevice = mock(BluetoothDevice.class);
        mockedSHNCentral = mock(SHNCentral.class);
        when(mockedSHNCentral.getScheduledThreadPoolExecutor()).thenReturn(new CallthroughExecutor(1));
        shnDevice = new SHNDevice(mockedBluetoothDevice, mockedSHNCentral);
    }

    @Test
    public void whenASHNDeviceIsCreatedThenItsStateIsDisconnected() {
        assertEquals(SHNDevice.SHNDeviceState.SHNDeviceStateDisconnected, shnDevice.getState());
    }

    @Test
    public void theGetAddressFunctionReturnsTheAddressOfTheBluetoothDevice() {
        String address = "11:22:33:44:55:66";
        when(mockedBluetoothDevice.getAddress()).thenReturn(address);
        assertEquals(address, shnDevice.getAddress());
        verify(mockedBluetoothDevice).getAddress();
    }

    @Test
    public void theGetNameFunctionReturnsTheNameOfTheBluetoothDevice() {
        String name = "Moonshine";
        when(mockedBluetoothDevice.getName()).thenReturn(name);
        assertEquals(name, shnDevice.getName());
        verify(mockedBluetoothDevice).getName();
    }

    @Test
    public void testConnect() {
        when(mockedBluetoothDevice.connectGatt(any(Context.class), anyBoolean(), any(BluetoothGattCallback.class))).thenReturn(null);
        shnDevice.connect();
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
    public void testReadCharacteristic() {

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

    private static class CallthroughExecutor extends ScheduledThreadPoolExecutor {
        public CallthroughExecutor(int corePoolSize) {
            super(corePoolSize);
        }

        @Override
        public void execute(Runnable command) {
            command.run();
        }

        @Override
        public ScheduledFuture<?> schedule(Runnable command,
                                           long delay,
                                           TimeUnit unit) {
            command.run();
            return new ScheduledFuture<Object>() {
                @Override
                public long getDelay(TimeUnit unit) {
                    return 0;
                }

                @Override
                public int compareTo(Delayed another) {
                    return 0;
                }

                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public Object get() throws InterruptedException, ExecutionException {
                    return null;
                }

                @Override
                public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return null;
                }
            };
        }
    }
}