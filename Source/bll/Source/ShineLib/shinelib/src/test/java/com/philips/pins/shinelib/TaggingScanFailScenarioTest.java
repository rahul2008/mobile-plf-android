package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.LeScanCallbackProxy;
import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.philips.pins.shinelib.SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class TaggingScanFailScenarioTest {

    private static final long STOP_SCANNING_AFTER_10_SECONDS = 10_000L;
    private static final String MOCKED_BLUETOOTH_DEVICE_NAME = "Mocked Bluetooth Device";

    @Mock
    private SHNDevice deviceMock;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private LeScanCallbackProxy leScanCallbackProxyMock;

    @Mock
    private SHNDeviceScanner.SHNDeviceScannerListener mockedSHNDeviceScannerListener;

    private MockedHandler mockedHandler;
    private SHNDeviceScannerInternal shnDeviceScannerInternal;

    @Captor
    private ArgumentCaptor<LeScanCallbackProxy.LeScanCallback> leScanCallbackCaptor;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(SHNTagger.class);

        mockedHandler = new MockedHandler();

        when(shnCentralMock.getInternalHandler()).thenReturn(mockedHandler.getMock());
        doReturn(deviceMock).when(shnCentralMock).createSHNDeviceForAddressAndDefinition(anyString(), any(SHNDeviceDefinitionInfo.class));

        when(deviceMock.getDeviceTypeName()).thenReturn(MOCKED_BLUETOOTH_DEVICE_NAME);

        List<SHNDeviceDefinitionInfo> testDeviceDefinitionInfos = new ArrayList<>();
        testDeviceDefinitionInfos.add(new SHNDeviceDefinitionInfo() {
            @Override
            public String getDeviceTypeName() {
                return MOCKED_BLUETOOTH_DEVICE_NAME;
            }

            @Override
            public Set<UUID> getPrimaryServiceUUIDs() {
                Set<UUID> primaryServiceUUIDs = new HashSet<>();
                primaryServiceUUIDs.add(UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A)));
                return primaryServiceUUIDs;
            }

            @Override
            public SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener) {
                return null;
            }

            @Override
            public SHNDeviceDefinition getSHNDeviceDefinition() {
                return null;
            }

            @Override
            public boolean useAdvertisedDataMatcher() {
                return false;
            }

            @Override
            public boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi) {
                return false;
            }
        });

        shnDeviceScannerInternal = new TaggingScanFailScenarioTest.TestSHNDeviceScannerInternal(shnCentralMock, testDeviceDefinitionInfos);
    }


    @Test
    public void whenScanningFailureOccurs_ThenTagIsSentWithProperData() {

        shnDeviceScannerInternal.startScanning(mockedSHNDeviceScannerListener, DuplicatesNotAllowed, STOP_SCANNING_AFTER_10_SECONDS);
        verify(leScanCallbackProxyMock).startLeScan(leScanCallbackCaptor.capture());
        int randomFailureCode = -666;
        leScanCallbackCaptor.getValue().onScanFailed(-666);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.sendTechnicalError(captor.capture());
        final String result = String.format("Error starting scanning, errorCode: %s", randomFailureCode);
        assertEquals(result, captor.getValue());
    }

    private class TestSHNDeviceScannerInternal extends SHNDeviceScannerInternal {

        TestSHNDeviceScannerInternal(@NonNull final SHNCentral shnCentral, @NonNull final List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions) {
            super(shnCentral, registeredDeviceDefinitions);
        }

        @Override
        LeScanCallbackProxy createLeScanCallbackProxy() {
            return leScanCallbackProxyMock;
        }
    }

}
