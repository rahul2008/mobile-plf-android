package com.pins.philips.shinelib;

import com.pins.philips.shinelib.helper.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by 310188215 on 27/05/15.
 */
@RunWith(PowerMockRunner.class)
public class SHNDeviceAssociationTest {
    public static final String DEVICE_TYPE_NAME = "Moonshine";
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNDeviceAssociation.SHNDeviceAssociationListener mockedSHNDeviceAssociationListener;
    private SHNCentral mockedSHNCentral;
    private SHNDeviceDefinitionInfo mockedSHNDeviceDefinitionInfo;
    private SHNDeviceDefinitionInfo.SHNDeviceDefinition mockedSHNDeviceDefinition;
    private SHNAssociationProcedure mockedSHNAssociationProcedure;
    private UUID mockedPrimaryServiceUUID;

    @Before
    public void setUp() {
        mockedSHNDeviceAssociationListener = (SHNDeviceAssociation.SHNDeviceAssociationListener) Utility.makeThrowingMock(SHNDeviceAssociation.SHNDeviceAssociationListener.class);
        mockedSHNAssociationProcedure = (SHNAssociationProcedure) Utility.makeThrowingMock(SHNAssociationProcedure.class);
        mockedSHNCentral = (SHNCentral) Utility.makeThrowingMock(SHNCentral.class);
        mockedSHNDeviceDefinitionInfo = (SHNDeviceDefinitionInfo) Utility.makeThrowingMock(SHNDeviceDefinitionInfo.class);
        mockedSHNDeviceDefinition = (SHNDeviceDefinitionInfo.SHNDeviceDefinition) Utility.makeThrowingMock(SHNDeviceDefinitionInfo.SHNDeviceDefinition.class);
        mockedPrimaryServiceUUID = UUID.randomUUID();

        // mockedSHNDeviceAssociationListener
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationFailed(any(SHNResult.class));
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);

        // mockedSHNDeviceDefinitionInfo
        doReturn(DEVICE_TYPE_NAME).when(mockedSHNDeviceDefinitionInfo).getDeviceTypeName();
        doReturn(mockedSHNAssociationProcedure).when(mockedSHNDeviceDefinitionInfo).createSHNAssociationProcedure(any(SHNCentral.class), any(SHNAssociationProcedure.SHNAssociationProcedureListener.class));
        Set<UUID> primaryServiceUUIDs = new HashSet<>();
        primaryServiceUUIDs.add(mockedPrimaryServiceUUID);
        doReturn(primaryServiceUUIDs).when(mockedSHNDeviceDefinitionInfo).getPrimaryServiceUUIDs();

        // mockedSHNCentral
        List<SHNDeviceDefinitionInfo> shnDeviceDefinitionInfos = new ArrayList<>();
        shnDeviceDefinitionInfos.add(mockedSHNDeviceDefinitionInfo);
        doReturn(shnDeviceDefinitionInfos).when(mockedSHNCentral).getRegisteredDeviceDefinitions();
        doReturn(true).when(mockedSHNCentral).startScanningForDevices(any(Collection.class), any(SHNCentral.ScannerSettingDuplicates.class), any(SHNDeviceScanner.SHNDeviceScannerListener.class));

        // mockedSHNAssociationProcedure
        doReturn(true).when(mockedSHNAssociationProcedure).getShouldScan();


        shnDeviceAssociation = new SHNDeviceAssociation(mockedSHNCentral);

        shnDeviceAssociation.setShnDeviceAssociationListener(mockedSHNDeviceAssociationListener);
    }

    @Test
    public void canCreateInstance() {
        assertNotNull(shnDeviceAssociation);
    }

    @Test
    public void whenCallingStartAssociationForAnUnregisteredDeviceTypeWhenAssociationNotInProcessThenOnAssociationFailedIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType("UnknownDeviceType");

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNUnknownDeviceTypeError);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessThenDidStartWithProperAssocProcIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsTrueThenStartScanningIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        ArgumentCaptor<Collection> uuidCollectionArgumentCaptor = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<SHNCentral.ScannerSettingDuplicates> duplicatesArgumentCaptor = ArgumentCaptor.forClass(SHNCentral.ScannerSettingDuplicates.class);
        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> scannerListenerArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedSHNCentral).startScanningForDevices(uuidCollectionArgumentCaptor.capture(), duplicatesArgumentCaptor.capture(), scannerListenerArgumentCaptor.capture());
        assertEquals(1, uuidCollectionArgumentCaptor.getValue().size());
        assertTrue(uuidCollectionArgumentCaptor.getValue().contains(mockedPrimaryServiceUUID));
        assertEquals(SHNCentral.ScannerSettingDuplicates.DuplicatesAllowed, duplicatesArgumentCaptor.getValue());
        assertNotNull(scannerListenerArgumentCaptor.getValue());
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsFalseThenStartScanningIsNotCalled() {
        /* !!! */ doReturn(false).when(mockedSHNAssociationProcedure).getShouldScan();

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNCentral, never()).startScanningForDevices(any(Collection.class), any(SHNCentral.ScannerSettingDuplicates.class), any(SHNDeviceScanner.SHNDeviceScannerListener.class));
    }

    @Test
    public void testGetShnDeviceAssociationState() {

    }

    @Test
    public void testStartAssociationForDeviceType() {

    }

    @Test
    public void testStopAssociation() {

    }
}