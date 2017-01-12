/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp.pluginreferenceboard;

import com.philips.pins.shinelib.SHNAssociationProcedurePlugin;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.services.SHNServiceDiCommStreaming;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Set;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceDefinitionInfoReferenceBoardTest {

    @Mock
    SHNCentral centralMock;

    @Mock
    SHNAssociationProcedurePlugin.SHNAssociationProcedureListener associationProcedureListenerMock;

    DeviceDefinitionInfoReferenceBoard deviceDefinitionInfoReferenceBoard;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        deviceDefinitionInfoReferenceBoard = new DeviceDefinitionInfoReferenceBoard();
    }

    @Test
    public void shouldReturnReferenceBoardasDeviceTypeName() throws Exception {
        assertEquals("ReferenceBoard", deviceDefinitionInfoReferenceBoard.getDeviceTypeName());
    }

    @Test
    public void shouldSpecifyPrimaryUUID() throws Exception {
        UUID uuid = SHNServiceDiCommStreaming.SERVICE_UUID;

        Set<UUID> primaryServiceUUIDs = deviceDefinitionInfoReferenceBoard.getPrimaryServiceUUIDs();
        assertNotNull(primaryServiceUUIDs);
        assertTrue(primaryServiceUUIDs.contains(uuid));
    }

    @Test
    public void shouldSpecifyAssociationProcedure() throws Exception {
        assertNotNull(deviceDefinitionInfoReferenceBoard.createSHNAssociationProcedure(centralMock, associationProcedureListenerMock));
    }

    @Test
    public void shouldSpecifyDeviceDefinition() throws Exception {
        assertNotNull(deviceDefinitionInfoReferenceBoard.getSHNDeviceDefinition());
    }

    @Test
    public void shouldReturnFalseForUseAdvertisedDataMatcher() throws Exception {
        assertFalse(deviceDefinitionInfoReferenceBoard.useAdvertisedDataMatcher());
    }
}