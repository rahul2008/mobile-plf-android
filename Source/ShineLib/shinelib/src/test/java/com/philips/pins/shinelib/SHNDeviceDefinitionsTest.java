/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.framework.BleUUIDCreator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNDeviceDefinitionsTest {

    private static final String TEST_DEVICE_TYPE_NAME = "TEST";
    private static final String TEST_DEVICE_TYPE_NAME_1 = "TEST2";
    private SHNDeviceDefinitions shnDeviceDefinitions;
    private UUID testUUID;

    @Mock
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfoMock;

    @Mock
    private SHNDeviceDefinitionInfo shnDeviceDefinitionInfoMock1;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        testUUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x1809));

        shnDeviceDefinitions = new SHNDeviceDefinitions();
    }

    @Test
    public void canInitialize() throws Exception {
        new SHNDeviceDefinitions();
    }

    @Test
    public void canAddDeviceDefinition() throws Exception {
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);

        assertEquals(1, shnDeviceDefinitions.getRegisteredDeviceDefinitions().size());
    }

    @Test
    public void whenRegisteredThenAddedToTheListOfRegisteredDevices() throws Exception {
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);

        assertEquals(shnDeviceDefinitionInfoMock, shnDeviceDefinitions.getRegisteredDeviceDefinitions().get(0));
    }

    @Test
    public void whenGetSHNDeviceDefinitionInfoForDeviceTypeNameIsCalledThenRegisteredDefinitionIsReturned() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);

        assertEquals(shnDeviceDefinitionInfoMock, shnDeviceDefinitions.getSHNDeviceDefinitionInfoForDeviceTypeName(TEST_DEVICE_TYPE_NAME));
    }

    @Test
    public void whenDeviceDefinitionIsNotRegisteredThenNullIsReturned() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);

        assertNull(shnDeviceDefinitions.getSHNDeviceDefinitionInfoForDeviceTypeName(TEST_DEVICE_TYPE_NAME_1));
    }

    @Test(expected = IllegalStateException.class)
    public void whenRegisteredTwiceThenExceptionIsGenerated() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);

        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);
    }

    @Test
    public void canRegisterTwoDeviceDefinitionWithDifferentDeviceName() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);
        when(shnDeviceDefinitionInfoMock.useAdvertisedDataMatcher()).thenReturn(true);
        when(shnDeviceDefinitionInfoMock.getPrimaryServiceUUIDs()).thenReturn(new HashSet<UUID>());
        when(shnDeviceDefinitionInfoMock1.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME_1);
        when(shnDeviceDefinitionInfoMock1.useAdvertisedDataMatcher()).thenReturn(true);
        Set<UUID> uuid = new HashSet<>();
        uuid.add(testUUID);
        when(shnDeviceDefinitionInfoMock1.getPrimaryServiceUUIDs()).thenReturn(uuid);

        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock1);

        assertEquals(2, shnDeviceDefinitions.getRegisteredDeviceDefinitions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void whenRegisteredWithSameUUIDThenExceptionIsGenerated() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);
        when(shnDeviceDefinitionInfoMock.useAdvertisedDataMatcher()).thenReturn(true);
        when(shnDeviceDefinitionInfoMock1.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME_1);
        when(shnDeviceDefinitionInfoMock1.useAdvertisedDataMatcher()).thenReturn(true);

        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock1);
    }

    @Test
    public void whenUseAdvertisedDataMatcherIsFalseThenUUIDiSNotChecked() throws Exception {
        when(shnDeviceDefinitionInfoMock.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME);
        when(shnDeviceDefinitionInfoMock.useAdvertisedDataMatcher()).thenReturn(false);
        when(shnDeviceDefinitionInfoMock1.getDeviceTypeName()).thenReturn(TEST_DEVICE_TYPE_NAME_1);
        when(shnDeviceDefinitionInfoMock.useAdvertisedDataMatcher()).thenReturn(false);

        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock);
        shnDeviceDefinitions.add(shnDeviceDefinitionInfoMock1);

        assertEquals(2, shnDeviceDefinitions.getRegisteredDeviceDefinitions().size());
    }
}