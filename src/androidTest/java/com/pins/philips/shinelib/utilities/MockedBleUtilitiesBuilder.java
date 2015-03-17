package com.pins.philips.shinelib.utilities;

import android.bluetooth.BluetoothAdapter;

import com.pins.philips.shinelib.bletestsupport.BleUtilities;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by 310188215 on 09/03/15.
 */
public class MockedBleUtilitiesBuilder {
    private final BleUtilities mockedBleUtilities;

    public MockedBleUtilitiesBuilder() {
        mockedBleUtilities = mock(BleUtilities.class);
    }
    public MockedBleUtilitiesBuilder setDeviceHasBle(boolean deviceHasBle) {
        when(mockedBleUtilities._deviceHasBle()).thenReturn(deviceHasBle);
        return this;
    }
    public MockedBleUtilitiesBuilder setBluetoothEnabled(boolean bluetoothEnabled) {
        when(mockedBleUtilities._isBluetoothAdapterEnabled()).thenReturn(bluetoothEnabled);
        return this;
    }
    public MockedBleUtilitiesBuilder setStartLeScan(boolean startLeScan) {
        when(mockedBleUtilities._startLeScan(any(UUID[].class), any(BluetoothAdapter.LeScanCallback.class))).thenReturn(startLeScan);
        return this;
    }
    public void buildAndUse() {
        BleUtilities.setInstance(mockedBleUtilities);
    }

    public BleUtilities getBleUtilities() {
        return mockedBleUtilities;
    }

}
