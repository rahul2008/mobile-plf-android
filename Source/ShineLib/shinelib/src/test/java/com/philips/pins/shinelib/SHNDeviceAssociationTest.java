package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.helper.Utility;
import com.philips.pins.shinelib.utility.QuickTestConnection;
import com.philips.pins.shinelib.utility.SHNServiceRegistry;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by 310188215 on 27/05/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SHNDeviceAssociation.class})
public class SHNDeviceAssociationTest {
    public static final String DEVICE_TYPE_NAME = "Moonshine";
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNDeviceAssociation.SHNDeviceAssociationListener mockedSHNDeviceAssociationListener;
    private SHNCentral mockedSHNCentral;
    private SHNDeviceDefinitionInfo mockedSHNDeviceDefinitionInfo;
    private SHNDeviceDefinitionInfo.SHNDeviceDefinition mockedSHNDeviceDefinition;
    private SHNAssociationProcedurePlugin mockedSHNAssociationProcedure;
    private UUID mockedPrimaryServiceUUID;
    private SHNDeviceDefinitions mockedSHNDeviceDefinitions;
    private ShinePreferenceWrapper mockedShinePreferenceWrapper;
    private SHNDevice mockedSHNDevice;
    private MockedHandler mockedInternalHandler;
    private MockedHandler mockedUserHandler;

    @Mock
    private QuickTestConnection quickTestConnectionMock;

    @Captor
    private ArgumentCaptor<QuickTestConnection.Listener> quickTestConnectionListenerCaptor;
    private SHNDeviceScannerInternal mockedSHNDeviceScannerInternal;

    @Before
    public void setUp() {
        SHNServiceRegistry.releaseInstance();

        initMocks(this);

        mockedSHNDeviceAssociationListener = Utility.makeThrowingMock(SHNDeviceAssociation.SHNDeviceAssociationListener.class);
        mockedSHNAssociationProcedure = Utility.makeThrowingMock(SHNAssociationProcedurePlugin.class);
        mockedSHNCentral = Utility.makeThrowingMock(SHNCentral.class);
        mockedSHNDeviceScannerInternal = Utility.makeThrowingMock(SHNDeviceScannerInternal.class);
        mockedSHNDeviceDefinitionInfo = Utility.makeThrowingMock(SHNDeviceDefinitionInfo.class);
        mockedSHNDeviceDefinition = Utility.makeThrowingMock(SHNDeviceDefinitionInfo.SHNDeviceDefinition.class);
        mockedSHNDeviceDefinitions = Utility.makeThrowingMock(SHNDeviceDefinitions.class);
        mockedShinePreferenceWrapper = Utility.makeThrowingMock(ShinePreferenceWrapper.class);
        mockedSHNDevice = Utility.makeThrowingMock(SHNDevice.class);
        mockedPrimaryServiceUUID = UUID.randomUUID();
        mockedInternalHandler = new MockedHandler();
        mockedUserHandler = new MockedHandler();

        // mockedSHNDeviceAssociationListener
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationFailed(any(SHNResult.class));
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStarted(mockedSHNAssociationProcedure);
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationStopped();
        doNothing().when(mockedSHNDeviceAssociationListener).onAssociationSucceeded(any(SHNDevice.class));

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
        SHNServiceRegistry.getInstance().add(mockedShinePreferenceWrapper, ShinePreferenceWrapper.class);
        doReturn(mockedInternalHandler.getMock()).when(mockedSHNCentral).getInternalHandler();
        doReturn(mockedUserHandler.getMock()).when(mockedSHNCentral).getUserHandler();
        doReturn(mockedSHNDevice).when(mockedSHNCentral).createSHNDeviceForAddressAndDefinition(anyString(), any(SHNDeviceDefinitionInfo.class));
        SHNDeviceFoundInfo.setSHNCentral(mockedSHNCentral);

        // mockedSHNDeviceScanner
        doReturn(true).when(mockedSHNDeviceScannerInternal).startScanning(isA(SHNDeviceScanner.SHNDeviceScannerListener.class), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
        doNothing().when(mockedSHNDeviceScannerInternal).stopScanning();

        // mockedSHNAssociationProcedure
        doReturn(true).when(mockedSHNAssociationProcedure).getShouldScan();
        doNothing().when(mockedSHNAssociationProcedure).deviceDiscovered(any(SHNDevice.class), any(SHNDeviceFoundInfo.class));
        doReturn("mockedSHNAssociationProcedure").when(mockedSHNAssociationProcedure).toString();

        // mockedSHNDeviceDefinitions
        doReturn(shnDeviceDefinitionInfos).when(mockedSHNDeviceDefinitions).getRegisteredDeviceDefinitions();
        doReturn(null).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(anyString());
        doReturn(mockedSHNDeviceDefinitionInfo).when(mockedSHNDeviceDefinitions).getSHNDeviceDefinitionInfoForDeviceTypeName(DEVICE_TYPE_NAME);

        doReturn(mockedSHNDevice).when(mockedSHNDeviceDefinition).createDeviceFromDeviceAddress(anyString(), any(SHNDeviceDefinitionInfo.class), any(SHNCentral.class));

        doReturn(Collections.emptyList()).when(mockedShinePreferenceWrapper).readAssociatedDeviceInfos();
        doNothing().when(mockedShinePreferenceWrapper).storeAssociatedDeviceInfos(anyList());

        shnDeviceAssociation = new TestSHNDeviceAssociation(mockedSHNCentral, mockedSHNDeviceScannerInternal);

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
        verify(mockedShinePreferenceWrapper).readAssociatedDeviceInfos();
    }

    @Test
    public void whenCallingStartAssociationForAnUnregisteredDeviceTypeWhenAssociationNotInProcess_ThenOnAssociationFailedIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType("UnknownDeviceType");

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
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcess_ThenStartIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        verify(mockedSHNAssociationProcedure).start();
    }

    @Test
    public void whenCallingStartAssociationForARegisteredDeviceTypeWhenAssociationNotInProcessAndShouldScanReturnsTrue_ThenStartScanningIsCalled() {
        shnDeviceAssociation.startAssociationForDeviceType(DEVICE_TYPE_NAME);

        ArgumentCaptor<SHNDeviceScanner.ScannerSettingDuplicates> duplicatesArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.ScannerSettingDuplicates.class);
        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> scannerListenerArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedSHNDeviceScannerInternal).startScanning(scannerListenerArgumentCaptor.capture(), duplicatesArgumentCaptor.capture(), anyLong());
        assertEquals(SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, duplicatesArgumentCaptor.getValue());
        assertNotNull(scannerListenerArgumentCaptor.getValue());
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

        ArgumentCaptor<SHNDeviceScanner.SHNDeviceScannerListener> scannerListenerArgumentCaptor = ArgumentCaptor.forClass(SHNDeviceScanner.SHNDeviceScannerListener.class);
        verify(mockedSHNDeviceScannerInternal).startScanning(scannerListenerArgumentCaptor.capture(), isA(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());

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
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertTrue(shnDeviceAssociation.getAssociatedDevices().isEmpty());
    }

    @Test
    public void whenRemoveDeviceIsCalled_ThenProperDeviceIsRemoved() {
        String macAddress = "11:22:33:44:55:66";
        SHNDevice shnDevice = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress, shnDevice, 1);

        String macAddress2 = "11:22:33:44:55:77";
        SHNDevice shnDevice2 = mock(SHNDevice.class);
        startAssociationAndCompleteWithDevice(macAddress2, shnDevice2, 2);

        shnDeviceAssociation.removeAssociatedDevice(shnDevice);

        assertFalse(shnDeviceAssociation.getAssociatedDevices().isEmpty());
        assertEquals(1, shnDeviceAssociation.getAssociatedDevices().size());
        assertEquals(macAddress2, shnDeviceAssociation.getAssociatedDevices().get(0).getAddress());
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

        public TestSHNDeviceAssociation(final SHNCentral shnCentral, SHNDeviceScannerInternal mockedSHNDeviceScannerInternal) {
            super(shnCentral, mockedSHNDeviceScannerInternal);
        }

        @NonNull
        @Override
        QuickTestConnection createQuickTestConnection() {
            return quickTestConnectionMock;
        }
    }
}