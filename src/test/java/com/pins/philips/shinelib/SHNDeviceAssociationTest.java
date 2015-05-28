package com.pins.philips.shinelib;

import android.util.Log;

import com.pins.philips.shinelib.helper.Utility;
import com.pins.philips.shinelib.utility.ShinePreferenceWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by 310188215 on 27/05/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, SHNDeviceAssociation.class})
public class SHNDeviceAssociationTest {
    public static final String DEVICE_TYPE_NAME = "Moonshine";
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNDeviceAssociation.SHNDeviceAssociationListener mockedSHNDeviceAssociationListener;
    private SHNCentral mockedSHNCentral;
    private SHNDeviceDefinitionInfo mockedSHNDeviceDefinitionInfo;
    private SHNDeviceDefinitionInfo.SHNDeviceDefinition mockedSHNDeviceDefinition;
    private SHNAssociationProcedure mockedSHNAssociationProcedure;
    private UUID mockedPrimaryServiceUUID;
    private SHNDeviceDefinitions mockedSHNDeviceDefinitions;
    private ShinePreferenceWrapper mockedShinePreferenceWrapper;

    @Before
    public void setUp() {
        mockStatic(Log.class);
        mockedSHNDeviceAssociationListener = (SHNDeviceAssociation.SHNDeviceAssociationListener) Utility.makeThrowingMock(SHNDeviceAssociation.SHNDeviceAssociationListener.class);
        mockedSHNAssociationProcedure = (SHNAssociationProcedure) Utility.makeThrowingMock(SHNAssociationProcedure.class);
        mockedSHNCentral = (SHNCentral) Utility.makeThrowingMock(SHNCentral.class);
        mockedSHNDeviceDefinitionInfo = (SHNDeviceDefinitionInfo) Utility.makeThrowingMock(SHNDeviceDefinitionInfo.class);
        mockedSHNDeviceDefinition = (SHNDeviceDefinitionInfo.SHNDeviceDefinition) Utility.makeThrowingMock(SHNDeviceDefinitionInfo.SHNDeviceDefinition.class);
        mockedSHNDeviceDefinitions = (SHNDeviceDefinitions) Utility.makeThrowingMock(SHNDeviceDefinitions.class);
        mockedShinePreferenceWrapper = (ShinePreferenceWrapper) Utility.makeThrowingMock(ShinePreferenceWrapper.class);
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
        doReturn(mockedSHNDeviceDefinitions).when(mockedSHNCentral).getSHNDeviceDefinitions();
        doReturn(true).when(mockedSHNCentral).startScanningForDevices(any(Collection.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), any(SHNDeviceScanner.SHNDeviceScannerListener.class));
        doReturn(mockedShinePreferenceWrapper).when(mockedSHNCentral).getShinePreferenceWrapper();

        // mockedSHNAssociationProcedure
        doReturn(true).when(mockedSHNAssociationProcedure).getShouldScan();
        doReturn("mockedSHNAssociationProcedure").when(mockedSHNAssociationProcedure).toString();

        // mockedSHNDeviceDefinitions
        doReturn(shnDeviceDefinitionInfos).when(mockedSHNDeviceDefinitions).getRegisteredDeviceDefinitions();
        doReturn(null).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(anyString());
        doReturn(mockedSHNDeviceDefinitionInfo).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(DEVICE_TYPE_NAME);

        // mockedShinePreferenceWrapper
        doReturn(Collections.emptyList()).when(mockedShinePreferenceWrapper).readAssociatedDeviceInfos();

        shnDeviceAssociation = new SHNDeviceAssociation(mockedSHNCentral);

        shnDeviceAssociation.setShnDeviceAssociationListener(mockedSHNDeviceAssociationListener);
    }

    @Test
    public void whenCreatedThenTheInstanceIsInIdleState() {
        assertNotNull(shnDeviceAssociation);
        assertEquals(SHNDeviceAssociation.State.Idle, shnDeviceAssociation.getState());
    }

    @Test
    public void whenCreatedThenTheAssociationsAreRead() {
        assertNotNull(shnDeviceAssociation);
        verify(mockedShinePreferenceWrapper).readAssociatedDeviceInfos();
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
        ArgumentCaptor<SHNDeviceScanner.ScannerSettingDuplicates> duplicatesArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.ScannerSettingDuplicates.class);
        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> scannerListenerArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedSHNCentral).startScanningForDevices(uuidCollectionArgumentCaptor.capture(), duplicatesArgumentCaptor.capture(), scannerListenerArgumentCaptor.capture());
        assertEquals(1, uuidCollectionArgumentCaptor.getValue().size());
        assertTrue(uuidCollectionArgumentCaptor.getValue().contains(mockedPrimaryServiceUUID));
        assertEquals(SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, duplicatesArgumentCaptor.getValue());
        assertNotNull(scannerListenerArgumentCaptor.getValue());
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsFalseThenStartScanningIsNotCalled() {
        /* !!! */ doReturn(false).when(mockedSHNAssociationProcedure).getShouldScan();

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNCentral, never()).startScanningForDevices(any(Collection.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), any(SHNDeviceScanner.SHNDeviceScannerListener.class));
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProgresThenTheStateChangesToAssociating() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        assertEquals(SHNDeviceAssociation.State.Associating, shnDeviceAssociation.getState());
    }

    @Test
    public void whenCallingStopAssociationWhenIdleThenAssociationStoppedShouldNotBeCalled() {
        shnDeviceAssociation.stopAssociation();

        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStopped();
    }

    @Test
    public void testStartAssociationForDeviceType() {

    }

    @Test
    public void testStopAssociation() {

    }
}