package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.utility.PersistentStorageCleaner;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.QuickTestConnection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNDeviceAssociationTest {
    
    public static final String DEVICE_TYPE_NAME = "Moonshine";
    public static final String DEVICE_MAC_ADDRESS = "11:11:11:11:11:11";
    public static final String DEVICE_TYPE_UNKNOWN = "UnknownDeviceType";
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNDeviceAssociation.SHNDeviceAssociationListener mockedSHNDeviceAssociationListener;
    private SHNCentral mockedSHNCentral;
    private SHNDeviceDefinitionInfo mockedSHNDeviceDefinitionInfo;
    private SHNDeviceDefinitionInfo.SHNDeviceDefinition mockedSHNDeviceDefinition;
    private SHNAssociationProcedurePlugin mockedSHNAssociationProcedure;
    private UUID mockedPrimaryServiceUUID;
    private SHNDeviceDefinitions mockedSHNDeviceDefinitions;
    private SHNDevice mockedSHNDevice;
    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;

    @Mock
    private QuickTestConnection quickTestConnectionMock;

    @Mock
    private SHNDeviceAssociationHelper deviceAssociationHelperMock;

    @Mock
    private SHNDeviceAssociation.DeviceRemovedListener deviceRemovedListenerMock;

    @Mock
    private PersistentStorageFactory persistentStorageFactoryMock;

    @Mock
    private PersistentStorageCleaner persistentStorageCleanerMock;

    @Mock
    private BleScanRecord bleScanRecordMock;

    @Mock
    private SHNResultListener mockedSHNResultListener;

    @Captor
    private ArgumentCaptor<QuickTestConnection.Listener> quickTestConnectionListenerCaptor;

    @Captor
    private ArgumentCaptor<SHNDevice.SHNDeviceListener> deviceListenerCaptor;

    private SHNDeviceScannerInternal mockedSHNDeviceScannerInternal;

    @Before
    public void setUp() {
        initMocks(this);

        mockedSHNDeviceAssociationListener = Utility.makeThrowingMock(SHNDeviceAssociation.SHNDeviceAssociationListener.class);
        mockedSHNAssociationProcedure = Utility.makeThrowingMock(SHNAssociationProcedurePlugin.class);
        mockedSHNCentral = Utility.makeThrowingMock(SHNCentral.class);
        mockedSHNDeviceScannerInternal = Utility.makeThrowingMock(SHNDeviceScannerInternal.class);
        mockedSHNDeviceDefinitionInfo = Utility.makeThrowingMock(SHNDeviceDefinitionInfo.class);
        mockedSHNDeviceDefinition = Utility.makeThrowingMock(SHNDeviceDefinitionInfo.SHNDeviceDefinition.class);
        mockedSHNDeviceDefinitions = Utility.makeThrowingMock(SHNDeviceDefinitions.class);
        mockedSHNDevice = Utility.makeThrowingMock(SHNDevice.class);
        mockedPrimaryServiceUUID = UUID.randomUUID();
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();

        // mockedSHNDeviceAssociationListener
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationFailed(any(SHNResult.class));
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStopped();
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationSucceeded(any(SHNDevice.class));
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociatedDevicesUpdated();

        doReturn(SHNResult.SHNOk).when(mockedSHNAssociationProcedure).start();
        doNothing().when(mockedSHNAssociationProcedure).stop();
        doNothing().when(mockedSHNAssociationProcedure).setShnAssociationProcedureListener(any(SHNAssociationProcedurePlugin.SHNAssociationProcedureListener.class));

        // mockedSHNDeviceDefinitionInfo
        doReturn(DEVICE_TYPE_NAME).when(mockedSHNDeviceDefinitionInfo).getDeviceTypeName();
        doReturn(mockedSHNAssociationProcedure).when(mockedSHNDeviceDefinitionInfo).createSHNAssociationProcedure(any(SHNCentral.class), any(SHNAssociationProcedurePlugin.SHNAssociationProcedureListener.class));
        Set<UUID> primaryServiceUUIDs = new HashSet<>();
        primaryServiceUUIDs.add(mockedPrimaryServiceUUID);
        doReturn(primaryServiceUUIDs).when(mockedSHNDeviceDefinitionInfo).getPrimaryServiceUUIDs();
        doReturn(mockedSHNDeviceDefinition).when(mockedSHNDeviceDefinitionInfo).getSHNDeviceDefinition();

        // mockedSHNCentral
        List<SHNDeviceDefinitionInfo> shnDeviceDefinitionInfos = new ArrayList<>();
        shnDeviceDefinitionInfos.add(mockedSHNDeviceDefinitionInfo);
        doReturn(mockedSHNDeviceDefinitions).when(mockedSHNCentral).getSHNDeviceDefinitions();
        doReturn(SHNCentral.State.SHNCentralStateReady).when(mockedSHNCentral).getShnCentralState();
        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();
        doReturn(mockedSHNDevice).when(mockedSHNCentral).createSHNDeviceForAddressAndDefinition(anyString(), any(SHNDeviceDefinitionInfo.class));
        doNothing().when(mockedSHNCentral).removeDeviceFromDeviceCache(isA(SHNDevice.class));
        SHNDeviceFoundInfo.setSHNCentral(mockedSHNCentral);

        // mockedSHNDeviceScanner
        doReturn(true).when(mockedSHNDeviceScannerInternal).startScanning(isA(SHNInternalScanRequest.class));
        doNothing().when(mockedSHNDeviceScannerInternal).stopScanning(isA(SHNInternalScanRequest.class));

        // mockedSHNAssociationProcedure
        doReturn(true).when(mockedSHNAssociationProcedure).getShouldScan();
        doNothing().when(mockedSHNAssociationProcedure).deviceDiscovered(any(SHNDevice.class), any(SHNDeviceFoundInfo.class));
        doReturn("mockedSHNAssociationProcedure").when(mockedSHNAssociationProcedure).toString();

        // mockedSHNDeviceDefinitions
        doReturn(shnDeviceDefinitionInfos).when(mockedSHNDeviceDefinitions).getRegisteredDeviceDefinitions();
        doReturn(null).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(anyString());
        doReturn(mockedSHNDeviceDefinitionInfo).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(DEVICE_TYPE_NAME);

        doReturn(mockedSHNDevice).when(mockedSHNDeviceDefinition).createDeviceFromDeviceAddress(anyString(), any(SHNDeviceDefinitionInfo.class), any(SHNCentral.class));

        // mockedSHNDevice
        doReturn(DEVICE_MAC_ADDRESS).when(mockedSHNDevice).getAddress();
        doReturn(DEVICE_TYPE_NAME).when(mockedSHNDevice).getDeviceTypeName();
        doReturn(SHNDevice.State.Disconnected).when(mockedSHNDevice).getState();

        doReturn(Collections.emptyList()).when(deviceAssociationHelperMock).readAssociatedDeviceInfos();
        doNothing().when(deviceAssociationHelperMock).storeAssociatedDeviceInfos(anyList());

        PowerMockito.when(persistentStorageFactoryMock.getPersistentStorageCleaner()).thenReturn(persistentStorageCleanerMock);

        shnDeviceAssociation = new TestSHNDeviceAssociation(mockedSHNCentral, mockedSHNDeviceScannerInternal, persistentStorageFactoryMock);

        shnDeviceAssociation.setShnDeviceAssociationListener(mockedSHNDeviceAssociationListener);
    }

    @Test
    public void whenCreated_ThenTheInstanceIsInIdleState() {
        assertNotNull(shnDeviceAssociation);
        assertEquals(SHNDeviceAssociation.State.Idle, shnDeviceAssociation.getState());
    }

    @Test
    public void whenCreated_ThenTheAssociationsAreRead() {
        assertNotNull(shnDeviceAssociation);
        verify(deviceAssociationHelperMock).readAssociatedDeviceInfos();
    }

    @Test
    public void whenCallingStartAssociationForAnUnregisteredDeviceTypeWhenAssociationNotInProcess_ThenOnAssociationFailedIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_UNKNOWN);

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNErrorUnknownDeviceType);
    }

    @Test
    public void whenStartReturnsAnError_ThenAssociationFailedIsCalled() {
        doReturn(SHNResult.SHNErrorInvalidParameter).when(mockedSHNAssociationProcedure).start();

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcess_ThenDidStartWithProperAssocProcIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
        verify(mockedSHNDeviceAssociationListener, never()).onAssociationFailed(any(SHNResult.class));
    }

    @Test
    public void whenAssociationIsStartedAndNoListenerIsAttachedThenNoExceptionIsGenerated() {
        shnDeviceAssociation.setShnDeviceAssociationListener(null);
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcess_ThenStartIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNAssociationProcedure).start();
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsTrue_ThenStartScanningIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        ArgumentCaptor<SHNInternalScanRequest> scanRequestCaptor = ArgumentCaptor.forClass(SHNInternalScanRequest.class);
        verify(mockedSHNDeviceScannerInternal).startScanning(scanRequestCaptor.capture());
        assertNotNull(scanRequestCaptor.getValue().shnDeviceScannerListener);
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsFalse_ThenStartScanningIsNotCalled() {
        /* !!! */
        doReturn(false).when(mockedSHNAssociationProcedure).getShouldScan();

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceScannerInternal, never()).startScanning(isA(SHNDeviceScanner.SHNDeviceScannerListener.class), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProgres_ThenTheStateChangesToAssociating() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        assertEquals(SHNDeviceAssociation.State.Associating, shnDeviceAssociation.getState());
    }

    @Test
    public void whenCallingStopAssociationWhenIdle_ThenAssociationStoppedShouldNotBeCalled() {
        shnDeviceAssociation.stopAssociation();

        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStopped();
    }

    @Test
    public void whenAssociationIsInProgress_ThenAdditionalCallsToStartAssociationShouldBeIgnored() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
        reset(mockedSHNDeviceAssociationListener); // clears the doReturn functions

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStarted(mockedSHNAssociationProcedure);
    }

    @Test
    public void whenAssociationIsInProgressAndStopAssociationIsCalled_ThenOnAssociationStopIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        shnDeviceAssociation.stopAssociation();

        verify(mockedSHNDeviceAssociationListener).onAssociationStopped();
    }

    @Test
    public void whenAssociationIsStopAssociationAndNoListenerIsAttachedThenNoExceptionIsGenerated() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
        shnDeviceAssociation.setShnDeviceAssociationListener(null);
        shnDeviceAssociation.stopAssociation();
    }

    @Test
    public void whenAssociationIsInProgressAndStopAssociationIsCalled_ThenAssociationProcedureStopIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        shnDeviceAssociation.stopAssociation();

        verify(mockedSHNAssociationProcedure).stop();
    }

    @Test
    public void whenAssociationIsInProgressAndStopAssociationIsCalled_ThenListenerIsSetToNull() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        shnDeviceAssociation.stopAssociation();

        verify(mockedSHNAssociationProcedure).setShnAssociationProcedureListener(null);
    }

    @Test
    public void whenAssociationIsInProgressAndADeviceIsDiscovered_ThenOnDeviceDiscoveredIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);
//        reset(mockedSHNDeviceAssociationListener); // clears the doReturn functions

        ArgumentCaptor<SHNInternalScanRequest> scanRequestCaptor = ArgumentCaptor.forClass(SHNInternalScanRequest.class);
        verify(mockedSHNDeviceScannerInternal).startScanning(scanRequestCaptor.capture());

        BluetoothDevice mockedBluetoothDevice = (BluetoothDevice) Utility.makeThrowingMock(BluetoothDevice.class);
        doReturn("11:22:33:44:55:66").when(mockedBluetoothDevice).getAddress();
        doReturn("MoonshineTest").when(mockedBluetoothDevice).getName();

        byte[] mockedScanRecord = new byte[]{0x00, 0x0A};
        SHNDeviceFoundInfo shnDeviceFoundInfo = new SHNDeviceFoundInfo(mockedBluetoothDevice, 321, mockedScanRecord, mockedSHNDeviceDefinitionInfo, bleScanRecordMock);

        // Call the device scanner listener
        scanRequestCaptor.getValue().shnDeviceScannerListener.deviceFound(null, shnDeviceFoundInfo);

        ArgumentCaptor<SHNDeviceFoundInfo> shnDeviceFoundInfoArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceFoundInfo.class);
        ArgumentCaptor<SHNDevice> shnDeviceArgumentCaptor = ArgumentCaptor.forClass(SHNDevice.class);
        verify(mockedSHNAssociationProcedure).deviceDiscovered(shnDeviceArgumentCaptor.capture(), shnDeviceFoundInfoArgumentCaptor.capture());

        assertNotNull(shnDeviceFoundInfoArgumentCaptor.getValue());
        assertEquals(shnDeviceFoundInfo, shnDeviceFoundInfoArgumentCaptor.getValue());
        assertEquals("11:22:33:44:55:66", shnDeviceFoundInfoArgumentCaptor.getValue().getDeviceAddress());
        assertEquals("MoonshineTest", shnDeviceFoundInfoArgumentCaptor.getValue().getDeviceName());

        assertEquals(mockedSHNDevice, shnDeviceArgumentCaptor.getValue());
    }

    @Ignore
    @Test
    public void whenAssociationHasSucceeded_ThenDeviceWillBeQuickConnected() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        verify(quickTestConnectionMock).execute(eq(shnDevice), isA(QuickTestConnection.Listener.class));
    }

    @Ignore
    @Test
    public void whenQuickConnectedHasSucceeded_AssociationListerIsInformedOfSuccess() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        verify(quickTestConnectionMock).execute(eq(shnDevice), quickTestConnectionListenerCaptor.capture());

        QuickTestConnection.Listener listener = quickTestConnectionListenerCaptor.getValue();
        listener.onSuccess();

        verify(mockedSHNDeviceAssociationListener).onAssociationSucceeded(shnDevice);
    }

    @Ignore
    @Test
    public void whenQuickConnectedHasFailed_AssociationListerIsInformedOfSuccessAnyway() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        verify(quickTestConnectionMock).execute(eq(shnDevice), quickTestConnectionListenerCaptor.capture());

        QuickTestConnection.Listener listener = quickTestConnectionListenerCaptor.getValue();
        listener.onFailure();

        verify(mockedSHNDeviceAssociationListener).onAssociationSucceeded(shnDevice);
    }

    @Test
    public void whenRemoveDeviceIsCalled_ThenDeviceIsRemoved() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertTrue(shnDeviceAssociation.getAssociatedDevices().isEmpty());
    }

    @Test
    public void whenRemoveAllDevicesIsCalled_ThenAllDeviceAreRemoved() {
        String macAddress1 = "11:11:11:11:11:11";
        String macAddress2 = "22:22:22:22:22:22";
        SHNDevice shnDevice1 = createMockedDisconnectedSHNDevice();
        SHNDevice shnDevice2 = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress1, shnDevice1, 1);
        startAssociationAndCompleteWithDevice(macAddress2, shnDevice2, 2);

        shnDeviceAssociation.removeAllAssociatedDevices();

        assertThat(shnDeviceAssociation.getAssociatedDevices()).isEmpty();
    }

    private SHNDevice createMockedDisconnectedSHNDevice() {
        SHNDevice shnDevice1 = mock(SHNDevice.class);
        doReturn(SHNDevice.State.Disconnected).when(shnDevice1).getState();
        return shnDevice1;
    }

    @Test
    public void whenRemoveDeviceIsCalled_ThenProperDeviceIsRemoved() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        String macAddress2 = "11:22:33:44:55:77";
        SHNDevice shnDevice2 = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress2, shnDevice2, 2);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertFalse(shnDeviceAssociation.getAssociatedDevices().isEmpty());
        assertEquals(1, shnDeviceAssociation.getAssociatedDevices().size());
        assertEquals(macAddress2, shnDeviceAssociation.getAssociatedDevices().get(0).getAddress());
    }

    @Test
    public void whenRemoveDeviceIsCalledWhenTheDeviceIsConnected_ThenDataIsClearedWhenTheDeviceStartDisconnecting() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        doReturn(SHNDevice.State.Connected).when(shnDevice).getState();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);
        mockedInternalHandler.executeFirstPostedExecution();

        verify(shnDevice).registerSHNDeviceListener(deviceListenerCaptor.capture());
        when(shnDevice.getState()).thenReturn(SHNDevice.State.Disconnecting);
        deviceListenerCaptor.getValue().onStateUpdated(shnDevice);
        mockedInternalHandler.executeFirstPostedExecution();

        verify(persistentStorageCleanerMock).clearDeviceData(shnDevice);
    }

    @Test
    public void whenRemoveDeviceIsCalledWhenTheDeviceIsConnected_ThenTheDeviceIsToldToDisconnect() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        doReturn(SHNDevice.State.Connected).when(shnDevice).getState();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        verify(shnDevice).disconnect();
    }

    @Test
    public void whenRemoveDeviceIsCalledWhenTheDeviceIsDisconnected_ThenDataIsCleared() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(shnDevice, never()).registerSHNDeviceListener(deviceListenerCaptor.capture());
        verify(persistentStorageCleanerMock).clearDeviceData(shnDevice);
    }

    @Test
    public void whenRemoveDeviceIsCalledWhenTheDeviceIsDisconnecting_ThenDataIsCleared() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        doReturn(SHNDevice.State.Disconnecting).when(shnDevice).getState();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertEquals(0, mockedInternalHandler.getPostedExecutionCount());
        verify(shnDevice, never()).registerSHNDeviceListener(deviceListenerCaptor.capture());
        verify(persistentStorageCleanerMock).clearDeviceData(shnDevice);
    }

    @Test
    public void whenDeviceIsNotAssociatedAndRemovedIsCalled_ThenAssociationDeviceListDoesNotChange() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        String macAddress2 = "11:22:33:44:55:77";
        SHNDevice shnDevice2 = mock(SHNDevice.class);
        when(shnDevice2.getAddress()).thenReturn(macAddress2);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice2);

        assertFalse(shnDeviceAssociation.getAssociatedDevices().isEmpty());
        assertEquals(1, shnDeviceAssociation.getAssociatedDevices().size());
        assertEquals(macAddress, shnDeviceAssociation.getAssociatedDevices().get(0).getAddress());
    }

    @Test
    public void whenAssociationHasSucceeded_ThenDeviceIsAddedToTheListOfAssociatedDevices() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        assertNotNull(shnDeviceAssociation.getAssociatedDevices());
        assertEquals(1, shnDeviceAssociation.getAssociatedDevices().size());
        assertEquals(macAddress, shnDeviceAssociation.getAssociatedDevices().get(0).getAddress());
        assertEquals(DEVICE_TYPE_NAME, shnDeviceAssociation.getAssociatedDevices().get(0).getDeviceTypeName());
    }

    @Test
    public void whenSHNCentralStateIsNotReadyAndAssociationIsStarted_ThenAssociationOnAssociationFailedIsCalled() {
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateNotReady);

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNErrorBluetoothDisabled);
        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStarted(any(SHNAssociationProcedure.class));
    }

    @Test
    public void whenSHNCentralStateIsNotReadyAndAssociationIsStartedForUnknownType_ThenAssociationOnAssociationFailedIsCalled() {
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateNotReady);

        shnDeviceAssociation.startAssociationForDeviceType("UnknownType");

        verify(mockedSHNDeviceAssociationListener).onAssociationFailed(SHNResult.SHNErrorBluetoothDisabled);
    }

    @Test
    public void whenSHNCentralStateIsNotReadyAndAssociationIsStartedAgain_ThenTheSecondStartIsSilentlyIgnored() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        reset(mockedSHNDeviceAssociationListener);
        when(mockedSHNCentral.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateNotReady);
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNDeviceAssociationListener, never()).onAssociationFailed(any(SHNResult.class));
        verify(mockedSHNDeviceAssociationListener, never()).onAssociationStarted(any(SHNAssociationProcedure.class));
    }

    // ---------

    private void startAssociationAndCompleteWithDevice(String macAddress, SHNDevice shnDevice, int number) {
        when(shnDevice.getAddress()).thenReturn(macAddress);
        when(shnDevice.getDeviceTypeName()).thenReturn(DEVICE_TYPE_NAME);

        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        ArgumentCaptor<SHNAssociationProcedurePlugin.SHNAssociationProcedureListener> shnAssociationProcedureListenerArgumentCaptor = ArgumentCaptor.forClass(SHNAssociationProcedurePlugin.SHNAssociationProcedureListener.class);
        verify(mockedSHNDeviceDefinitionInfo, times(number)).createSHNAssociationProcedure(any(SHNCentral.class), shnAssociationProcedureListenerArgumentCaptor.capture());
        shnAssociationProcedureListenerArgumentCaptor.getValue().onAssociationSuccess(shnDevice);
    }

    private class TestSHNDeviceAssociation extends SHNDeviceAssociation {

        public TestSHNDeviceAssociation(final SHNCentral shnCentral, SHNDeviceScannerInternal mockedSHNDeviceScannerInternal, final PersistentStorageFactory persistentStorageFactory) {
            super(shnCentral, mockedSHNDeviceScannerInternal, persistentStorageFactory);
            initAssociatedDevicesListOnInternalThread();
        }

        @NonNull
        @Override
        QuickTestConnection createQuickTestConnection() {
            return quickTestConnectionMock;
        }

        @NonNull
        @Override
        SHNDeviceAssociationHelper getShnDeviceAssociationHelper() {
            return deviceAssociationHelperMock;
        }

        @Override
        boolean isRunningOnTheInternalThread() {
            return true;
        }
    }

    @Test
    public void whenADeviceForAPluginIsPersisted_ButThePluginIsNotRegistered_ThenItShouldNotListTheDevice() {
        List<SHNDeviceAssociationHelper.AssociatedDeviceInfo> associatedDeviceInfoList = new ArrayList<>();
        associatedDeviceInfoList.add(new SHNDeviceAssociationHelper.AssociatedDeviceInfo("11:22:33:44:55:66", DEVICE_TYPE_NAME));
        associatedDeviceInfoList.add(new SHNDeviceAssociationHelper.AssociatedDeviceInfo("22:22:33:44:55:66", "NOT_KNOWN"));
        associatedDeviceInfoList.add(new SHNDeviceAssociationHelper.AssociatedDeviceInfo("33:22:33:44:55:66", DEVICE_TYPE_NAME));

        doReturn(associatedDeviceInfoList).when(deviceAssociationHelperMock).readAssociatedDeviceInfos();

        shnDeviceAssociation = new TestSHNDeviceAssociation(mockedSHNCentral, mockedSHNDeviceScannerInternal, persistentStorageFactoryMock);

        assertEquals(2, shnDeviceAssociation.getAssociatedDevices().size());
    }

    @Test
    public void ShouldInformGenericDeviceRemovedListener_WhenDeviceIsRemoved() {
        String macAddress1 = "11:11:11:11:11:11";
        SHNDevice shnDevice1 = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress1, shnDevice1, 1);

        shnDeviceAssociation.addDeviceRemovedListeners(deviceRemovedListenerMock);
        shnDeviceAssociation.removeAllAssociatedDevices();

        verify(deviceRemovedListenerMock).onAssociatedDeviceRemoved(shnDevice1);
    }

    @Test
    public void ShouldInformSpecificDeviceRemovedListener_WhenDeviceIsRemoved() {
        String macAddress1 = "11:11:11:11:11:11";
        SHNDevice shnDevice1 = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress1, shnDevice1, 1);

        shnDeviceAssociation.removeAllAssociatedDevices(deviceRemovedListenerMock);

        verify(deviceRemovedListenerMock).onAssociatedDeviceRemoved(shnDevice1);
    }

    @Test
    public void ShouldNotInformDeviceRemovedListener_WhenDeviceIsRemovedAndTheListenerHasBeenRemoved() {
        String macAddress1 = "11:11:11:11:11:11";
        SHNDevice shnDevice1 = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress1, shnDevice1, 1);

        shnDeviceAssociation.addDeviceRemovedListeners(deviceRemovedListenerMock);
        shnDeviceAssociation.removeDeviceRemovedListeners(deviceRemovedListenerMock);
        shnDeviceAssociation.removeAllAssociatedDevices();

        verify(deviceRemovedListenerMock, never()).onAssociatedDeviceRemoved(shnDevice1);
    }

    // Association Injection Tests
    private void verifyNoAssociationAddedWithErrorResult(int nrExpectedAssociatedDevices, SHNResult error) {
        assertNotNull(shnDeviceAssociation.getAssociatedDevices());
        assertEquals(nrExpectedAssociatedDevices, shnDeviceAssociation.getAssociatedDevices().size());
        verify(deviceAssociationHelperMock, never()).storeAssociatedDeviceInfos(isA(List.class));
        verify(mockedSHNDeviceAssociationListener, never()).onAssociatedDevicesUpdated();
        verify(mockedSHNResultListener).onActionCompleted(error);
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_ItIsAddedToTheAssociatedDevicesList() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        assertNotNull(shnDeviceAssociation.getAssociatedDevices());
        assertEquals(1, shnDeviceAssociation.getAssociatedDevices().size());
        assertEquals(DEVICE_MAC_ADDRESS, shnDeviceAssociation.getAssociatedDevices().get(0).getAddress());
        assertEquals(DEVICE_TYPE_NAME, shnDeviceAssociation.getAssociatedDevices().get(0).getDeviceTypeName());
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_TheAssociatedDevicesListIsPersisted() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verify(deviceAssociationHelperMock).storeAssociatedDeviceInfos(isA(List.class));
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_TheResultListenerShouldIndicateSuccess() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verify(mockedSHNResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_SHNCentralIsUsedToCreateTheDeviceInstance() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verify(mockedSHNCentral).createSHNDeviceForAddressAndDefinition(DEVICE_MAC_ADDRESS, mockedSHNDeviceDefinitionInfo);
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_SHNCentralIsUsedToCreateTheDeviceInstanceWithUpperCaseMacAddress() {
        String macAddress = "1c:87:74:00:00:f0";
        shnDeviceAssociation.injectAssociatedDevice(macAddress, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verify(mockedSHNCentral).createSHNDeviceForAddressAndDefinition(macAddress.toUpperCase(), mockedSHNDeviceDefinitionInfo);
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceType_Then_TheAssociationListenerIsCalled() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verify(mockedSHNDeviceAssociationListener).onAssociatedDevicesUpdated();
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredUnknowDeviceType_Then_ItIsNotAddedToTheAssociatedDevicesList() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_UNKNOWN, mockedSHNResultListener);

        verifyNoAssociationAddedWithErrorResult(0, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenANewAssociationIsInjectedForAUnregisteredKnowDeviceTypeWithAnIllegalMACAddress_Then_ItIsNotAddedToTheAssociatedDevicesList() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS + "0", DEVICE_TYPE_NAME, mockedSHNResultListener);

        verifyNoAssociationAddedWithErrorResult(0, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenANewAssociationIsInjectedForARegisteredKnowDeviceType_Then_TheResultListenerShouldIndicateSHNErrorInvalidParameter() {
        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);
        reset(mockedSHNResultListener);
        reset(deviceAssociationHelperMock);
        reset(mockedSHNDeviceAssociationListener);

        shnDeviceAssociation.injectAssociatedDevice(DEVICE_MAC_ADDRESS, DEVICE_TYPE_NAME, mockedSHNResultListener);

        verifyNoAssociationAddedWithErrorResult(1, SHNResult.SHNErrorOperationFailed);
    }

    @Test
    public void whenRemoveDeviceIsCalled_ThenDeviceIsRemovedFromCacheInSHNCentral() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = createMockedDisconnectedSHNDevice();
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        verify(mockedSHNCentral).removeDeviceFromDeviceCache(shnDevice);
    }
}