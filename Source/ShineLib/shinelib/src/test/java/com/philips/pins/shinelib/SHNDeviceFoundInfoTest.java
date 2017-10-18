/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNDeviceFoundInfoTest {
    private SHNDeviceFoundInfo shnDeviceFoundInfo;

    @Mock
    private SHNCentral mockedSHNCentral;

    @Mock
    private BluetoothDevice mockedBluetoothDevice;

    @Mock
    private SHNDeviceDefinitionInfo mockedShnDeviceDefinitionInfo;

    @Mock
    private BleScanRecord mockedBleScanRecord;

    @Mock
    private SHNDevice mockedSHNDevice;


    @Before
    public void setUp() {
        initMocks(this);

        SHNDeviceFoundInfo.setSHNCentral(mockedSHNCentral);

        doReturn(mockedSHNDevice).when(mockedSHNCentral).createSHNDeviceForAddressAndDefinition(anyString(), any(SHNDeviceDefinitionInfo.class));
    }

    @Test
    public void whenBluetoothDeviceHasNameThenThatNameIsUsed() {
        doReturn("name").when(mockedBluetoothDevice).getName();

        shnDeviceFoundInfo = new SHNDeviceFoundInfo(mockedBluetoothDevice, 0,
                new byte[0], mockedShnDeviceDefinitionInfo, mockedBleScanRecord);

        assertEquals(shnDeviceFoundInfo.getDeviceName(), "name");
    }

    @Test
    public void whenOnlyScanRecordHasNameThenThatNameIsUsed() {
        doReturn("scanname").when(mockedBleScanRecord).getLocalName();

        shnDeviceFoundInfo = new SHNDeviceFoundInfo(mockedBluetoothDevice, 0,
                new byte[0], mockedShnDeviceDefinitionInfo, mockedBleScanRecord);

        assertEquals(shnDeviceFoundInfo.getDeviceName(), "scanname");
    }

    @Test
    public void whenBothDeviceAndScanRecordDontHaveANameThenTheSHNDeviceNameIsUsed() {
        doReturn("shnname").when(mockedSHNDevice).getName();

        shnDeviceFoundInfo = new SHNDeviceFoundInfo(mockedBluetoothDevice, 0,
                new byte[0], mockedShnDeviceDefinitionInfo, mockedBleScanRecord);

        assertEquals(shnDeviceFoundInfo.getDeviceName(), "shnname");
    }
}
