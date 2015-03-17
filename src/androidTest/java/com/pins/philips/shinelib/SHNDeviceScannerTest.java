package com.pins.philips.shinelib;

import android.test.AndroidTestCase;

import com.pins.philips.shinelib.framework.BleDeviceFoundInfo;
import com.pins.philips.shinelib.framework.LeScanCallbackProxy;
import com.pins.philips.shinelib.framework.SingleThreadEventDispatcher;
import com.pins.philips.shinelib.utilities.MockedBleUtilitiesBuilder;
import com.pins.philips.shinelib.utilities.UUIDListHelper;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SHNDeviceScannerTest extends AndroidTestCase {

    public static final int TEST_RSSI = -50;
    public static final byte[] TEST_SCAN_RECORD = new byte[]{0x00, 0x01, 0x02};
    public static final String BLE_DEVICE_1_MAC_ADDRESS = "00:01:02:03:04:05";
    public static final String BLE_DEVICE_2_MAC_ADDRESS = "00:01:02:03:04:06";

    public void test01CanCreateASHNDeviceScanner() {
        SHNCentral mockedShnCentral = new SetupDeviceScanner().invoke().getMockedShnCentral();
    }

    public void test02StartScanning() {
        MockedBleUtilitiesBuilder mockedBleUtilitiesBuilder = new MockedBleUtilitiesBuilder()
                .setStartLeScan(true);
        mockedBleUtilitiesBuilder.buildAndUse();

        SetupDeviceScanner setupDeviceScanner = new SetupDeviceScanner().invoke();
        SHNCentral mockedShnCentral = setupDeviceScanner.getMockedShnCentral();
        SHNDeviceScanner shnDeviceScanner = setupDeviceScanner.getShnDeviceScanner();

        List<SHNDeviceDefinitionInfo> shnDeviceDefinitionInfos = new ArrayList<>();
        when(mockedShnCentral.getRegisteredDeviceDefinitions()).thenReturn(shnDeviceDefinitionInfos);

        SHNDeviceDefinitionInfo mockedShnDeviceDefinitionInfo1 = mock(SHNDeviceDefinitionInfo.class);
        shnDeviceDefinitionInfos.add(mockedShnDeviceDefinitionInfo1);
        UUIDListHelper uuidListHelper1 = new UUIDListHelper(10);
        when(mockedShnDeviceDefinitionInfo1.getPrimaryServiceUUIDs()).thenReturn(uuidListHelper1.getSet());

        SHNDeviceDefinitionInfo mockedShnDeviceDefinitionInfo2 = mock(SHNDeviceDefinitionInfo.class);
        shnDeviceDefinitionInfos.add(mockedShnDeviceDefinitionInfo2);
        UUIDListHelper uuidListHelper2 = new UUIDListHelper(10);
        when(mockedShnDeviceDefinitionInfo2.getPrimaryServiceUUIDs()).thenReturn(uuidListHelper2.getSet());

        SHNDeviceScanner.SHNDeviceScannerListener mockedShnDeviceScannerListener = mock(SHNDeviceScanner.SHNDeviceScannerListener.class);
        assertTrue(shnDeviceScanner.startScanning(mockedShnDeviceScannerListener));
        verify(mockedShnCentral).getRegisteredDeviceDefinitions();

        ArgumentCaptor<UUID[]> uuidArrayCaptor = ArgumentCaptor.forClass(UUID[].class);
        ArgumentCaptor<LeScanCallbackProxy> leScanCallbackProxyCaptor = ArgumentCaptor.forClass(LeScanCallbackProxy.class);
        verify(mockedBleUtilitiesBuilder.getBleUtilities(), times(2)).startLeScan(uuidArrayCaptor.capture(), leScanCallbackProxyCaptor.capture());
    }

    public void test03OnLeScan() {
        SetupDeviceScanner setupDeviceScanner = new SetupDeviceScanner().invoke();
        SHNCentral mockedShnCentral = setupDeviceScanner.getMockedShnCentral();
        SHNDeviceScanner shnDeviceScanner = setupDeviceScanner.getShnDeviceScanner();
        SingleThreadEventDispatcher mockedEventDispatcher = setupDeviceScanner.getMockedEventDispatcher();

        BleDeviceFoundInfo bleDeviceFoundInfo = new BleDeviceFoundInfo(null, TEST_RSSI, TEST_SCAN_RECORD);
        shnDeviceScanner.onLeScanSub(bleDeviceFoundInfo);

        verify(mockedShnCentral).getEventDispatcher();
        ArgumentCaptor<SHNDeviceScanner.DeviceFoundEvent> deviceFoundEventCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.DeviceFoundEvent.class);
        verify(mockedEventDispatcher).queueEvent(deviceFoundEventCaptor.capture());
        assertEquals(null, deviceFoundEventCaptor.getValue().bleDeviceFoundInfo.bluetoothDevice);
        assertEquals(TEST_RSSI, deviceFoundEventCaptor.getValue().bleDeviceFoundInfo.rssi);
        assertEquals(TEST_SCAN_RECORD.length, deviceFoundEventCaptor.getValue().bleDeviceFoundInfo.scanRecord.length);
        assertNotSame(TEST_SCAN_RECORD, deviceFoundEventCaptor.getValue().bleDeviceFoundInfo.scanRecord);
        assertArrayEquals(TEST_SCAN_RECORD, deviceFoundEventCaptor.getValue().bleDeviceFoundInfo.scanRecord);
    }

    public void test04HandleDeviceFoundEvent() {
        // Setup
        SetupDeviceScanner setupDeviceScanner = new SetupDeviceScanner().invoke();
        SHNCentral mockedShnCentral = setupDeviceScanner.getMockedShnCentral();
        SHNDeviceScanner shnDeviceScanner = setupDeviceScanner.getShnDeviceScanner();
        SingleThreadEventDispatcher mockedEventDispatcher = setupDeviceScanner.getMockedEventDispatcher();

        // Verify that the listener is registered with the event dispatcher (white box)
        verify(mockedEventDispatcher).register(shnDeviceScanner, SHNDeviceScanner.DeviceFoundEvent.class);

        // Generate device one found
        BleDeviceFoundInfo mockedBleDeviceFoundInfo1 = mock(BleDeviceFoundInfo.class);
        when(mockedBleDeviceFoundInfo1.getDeviceAddress()).thenReturn(BLE_DEVICE_1_MAC_ADDRESS);
        shnDeviceScanner.handleDeviceFoundEvent(new SHNDeviceScanner.DeviceFoundEvent(mockedBleDeviceFoundInfo1));
        ArgumentCaptor<SHNDevice> shnDeviceCaptor = ArgumentCaptor.forClass(SHNDevice.class);
        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> shnDeviceScannerListenerCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedShnCentral).reportDeviceFound(shnDeviceScannerListenerCaptor.capture(), eq(shnDeviceScanner), shnDeviceCaptor.capture());
        assertEquals(BLE_DEVICE_1_MAC_ADDRESS, shnDeviceCaptor.getValue().getAddress());

        // Generate device two found
        BleDeviceFoundInfo mockedBleDeviceFoundInfo2 = mock(BleDeviceFoundInfo.class);
        when(mockedBleDeviceFoundInfo2.getDeviceAddress()).thenReturn(BLE_DEVICE_2_MAC_ADDRESS);
        shnDeviceScanner.handleDeviceFoundEvent(new SHNDeviceScanner.DeviceFoundEvent(mockedBleDeviceFoundInfo2));
        shnDeviceCaptor = ArgumentCaptor.forClass(SHNDevice.class);
        shnDeviceScannerListenerCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedShnCentral, times(2)).reportDeviceFound(shnDeviceScannerListenerCaptor.capture(), eq(shnDeviceScanner), shnDeviceCaptor.capture());
        assertEquals(BLE_DEVICE_2_MAC_ADDRESS, shnDeviceCaptor.getValue().getAddress());

        // Report device one again, but don't inform the caller of the duplicate (note the times(2) in the verify)
        shnDeviceScanner.handleDeviceFoundEvent(new SHNDeviceScanner.DeviceFoundEvent(mockedBleDeviceFoundInfo1));
        shnDeviceCaptor = ArgumentCaptor.forClass(SHNDevice.class);
        shnDeviceScannerListenerCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedShnCentral, times(2)).reportDeviceFound(shnDeviceScannerListenerCaptor.capture(), eq(shnDeviceScanner), shnDeviceCaptor.capture());

    }

    private class SetupDeviceScanner {
        private SHNCentral mockedShnCentral;
        private SHNDeviceScanner shnDeviceScanner;
        private SingleThreadEventDispatcher mockedEventDispatcher;

        public SHNCentral getMockedShnCentral() {
            return mockedShnCentral;
        }

        public SHNDeviceScanner getShnDeviceScanner() {
            return shnDeviceScanner;
        }

        public SingleThreadEventDispatcher getMockedEventDispatcher() {
            return mockedEventDispatcher;
        }

        public SetupDeviceScanner invoke() {
            mockedShnCentral = mock(SHNCentral.class);

            mockedEventDispatcher = mock(SingleThreadEventDispatcher.class);
            when(mockedShnCentral.getEventDispatcher()).thenReturn(mockedEventDispatcher);

            shnDeviceScanner = new SHNDeviceScanner(mockedShnCentral);
            assertNotNull(shnDeviceScanner);
            return this;
        }
    }
}