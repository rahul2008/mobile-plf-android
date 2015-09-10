package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

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
    private SHNDevice mockedSHNDevice;
    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;

    private List<ShinePreferenceWrapper.AssociatedDeviceInfo> associatedDeviceInfos;

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
        mockedSHNDevice = (SHNDevice) Utility.makeThrowingMock(SHNDevice.class);
        mockedPrimaryServiceUUID = UUID.randomUUID();
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();

        // mockedSHNDeviceAssociationListener
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationFailed(any(SHNResult.class));
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStopped();
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationSucceeded(any(SHNDevice.class));

        doReturn(SHNResult.SHNOk).when(mockedSHNAssociationProcedure).start();

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
        doNothing().when(mockedSHNCentral).stopScanning();
        doReturn(mockedSHNDevice).when(mockedSHNCentral).createSHNDeviceForAddress(anyString(), any(SHNDeviceDefinitionInfo.class));
        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();

        // mockedSHNAssociationProcedure
        doReturn(true).when(mockedSHNAssociationProcedure).getShouldScan();
        doNothing().when(mockedSHNAssociationProcedure).deviceDiscovered(any(SHNDevice.class), any(SHNDeviceFoundInfo.class));
        doReturn("mockedSHNAssociationProcedure").when(mockedSHNAssociationProcedure).toString();

        // mockedSHNDeviceDefinitions
        doReturn(shnDeviceDefinitionInfos).when(mockedSHNDeviceDefinitions).getRegisteredDeviceDefinitions();
        doReturn(null).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(anyString());
        doReturn(mockedSHNDeviceDefinitionInfo).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(DEVICE_TYPE_NAME);

        associatedDeviceInfos = new ArrayList<>();
        doReturn(associatedDeviceInfos).when(mockedShinePreferenceWrapper).readAssociatedDeviceInfos();
        doNothing().when(mockedShinePreferenceWrapper).storeAssociatedDeviceInfos(anyList());

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
    public void whenStartReturnsAnErrorThenAssociationFailedIsCalled() {
        doReturn(SHNResult.SHNInvalidParameterError).when(mockedSHNAssociationProcedure).start();

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessThenDidStartWithProperAssocProcIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessThenStartIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNAssociationProcedure).start();
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
        /* !!! */
        doReturn(false).when(mockedSHNAssociationProcedure).getShouldScan();

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
    public void whenAssociationIsInProgressThenAdditionalCallsToStartAssociationShouldBeIgnored() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
        reset(mockedSHNDeviceAssociationListener); // clears the doReturn functions

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStarted(mockedSHNAssociationProcedure);
    }

    @Test
    public void whenAssociationIsInProgressAndStopAssociationIsCalledThenOnAssociationStopIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
//        reset(mockedSHNDeviceAssociationListener); // clears the doReturn functions

        shnDeviceAssociation.stopAssociation();
        verify(mockedSHNDeviceAssociationListener).onAssociationStopped();
    }

    @Test
    public void whenAssociationIsInProgressAndADeviceIsDiscoveredThenOnDeviceDiscoveredIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
//        reset(mockedSHNDeviceAssociationListener); // clears the doReturn functions

        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> scannerListenerArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedSHNCentral).startScanningForDevices(anyCollection(), any(SHNDeviceScanner.ScannerSettingDuplicates.class), scannerListenerArgumentCaptor.capture());

        BluetoothDevice mockedBluetoothDevice = (BluetoothDevice) Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("11:22:33:44:55:66").when(mockedBluetoothDevice).getAddress();
        doReturn("MoonshineTest").when(mockedBluetoothDevice).getName();

        byte[] mockedScanRecord = new byte[]{0x00, 0x0A};
        SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(mockedBluetoothDevice, 321, mockedScanRecord, mockedSHNDeviceDefinitionInfo);

        // Call the device scanner listener
        scannerListenerArgumentCaptor.getValue().deviceFound(null, shnDeviceFoundInfo);

        ArgumentCaptor<SHNDeviceFoundInfo> shnDeviceFoundInfoArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceFoundInfo.class);
        ArgumentCaptor<SHNDevice> shnDeviceArgumentCaptor = ArgumentCaptor.forClass(SHNDevice.class);
        verify(mockedSHNAssociationProcedure).deviceDiscovered(shnDeviceArgumentCaptor.capture(), shnDeviceFoundInfoArgumentCaptor.capture());

        assertNotNull(shnDeviceFoundInfoArgumentCaptor.getValue());
        assertEquals(shnDeviceFoundInfo, shnDeviceFoundInfoArgumentCaptor.getValue());
        assertEquals("11:22:33:44:55:66", shnDeviceFoundInfoArgumentCaptor.getValue().deviceAddress);
        assertEquals("MoonshineTest", shnDeviceFoundInfoArgumentCaptor.getValue().deviceName);

        assertEquals(mockedSHNDevice, shnDeviceArgumentCaptor.getValue());
    }

    private void startAssociationAndCompleteWithDevice(String macAddress, SHNDevice shnDevice, int number) {
        when(shnDevice.getAddress()).thenReturn(macAddress);
        when(shnDevice.getDeviceTypeName()).thenReturn(DEVICE_TYPE_NAME);

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        ArgumentCaptor<SHNAssociationProcedure.SHNAssociationProcedureListener> shnAssociationProcedureListenerArgumentCaptor = ArgumentCaptor.forClass(SHNAssociationProcedure.SHNAssociationProcedureListener.class);
        verify(mockedSHNDeviceDefinitionInfo, times(number)).createSHNAssociationProcedure(any(SHNCentral.class), shnAssociationProcedureListenerArgumentCaptor.capture());
        shnAssociationProcedureListenerArgumentCaptor.getValue().onAssociationSuccess(shnDevice);
    }

    @Test
    public void whenAssociationHasSucceededThenDeviceIsAddedToTheListOfAssociatedDevices() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        assertFalse(associatedDeviceInfos.isEmpty());
        assertEquals(1, associatedDeviceInfos.size());
        assertEquals(macAddress, associatedDeviceInfos.get(0).macAddress);
        assertEquals(DEVICE_TYPE_NAME, associatedDeviceInfos.get(0).deviceTypeName);
    }

    @Test
    public void whenRemoveDeviceIsCalledThenDeviceIsRemoved() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertTrue(associatedDeviceInfos.isEmpty());
    }

    @Test
    public void whenRemoveDeviceIsCalledThenProperDeviceIsRemoved() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        String macAddress2 = "11:22:33:44:55:77";
        SHNDevice shnDevice2 = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress2, shnDevice2, 2);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertFalse(associatedDeviceInfos.isEmpty());
        assertEquals(1, associatedDeviceInfos.size());
        assertEquals(macAddress2, associatedDeviceInfos.get(0).macAddress);
    }

    @Test
    public void whenDeviceIsNotAssociatedAndRemovedIsCalledThen() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        String macAddress2 = "11:22:33:44:55:77";
        SHNDevice shnDevice2 = mock(SHNDevice.class);
        when(shnDevice2.getAddress()).thenReturn(macAddress2);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice2);

        assertFalse(associatedDeviceInfos.isEmpty());
        assertEquals(1, associatedDeviceInfos.size());
        assertEquals(macAddress, associatedDeviceInfos.get(0).macAddress);
    }
}