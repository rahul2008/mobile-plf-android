package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNServiceDeviceInformationTest {

    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    
    @Mock
    private SHNStringResultListener stringResultListenerMock;
    
    @Mock
    private SHNCapabilityDeviceInformation.Listener deviceInformationListenerMock;

    @Mock
    private SHNCharacteristic characteristicMock;

    @Captor
    private ArgumentCaptor<SHNCommandResultReporter> reporterArgumentCaptor;

    private TestSHNServiceDeviceInformation deviceInformation;

    @Before
    public void setUp() {
        initMocks(this);

        deviceInformation = new TestSHNServiceDeviceInformation();
    }

    @Test
    public void Legacy_shouldInformListenerOfUnsupportedOperation_whenInformationTypeIsNull() {
        deviceInformation.readDeviceInformation(null, stringResultListenerMock);

        verify(stringResultListenerMock).onActionCompleted(null, SHNResult.SHNErrorUnsupportedOperation);
    }

    @Test
    public void Legacy_shouldQueryForCorrectUUID_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, stringResultListenerMock);

        assertThat(deviceInformation.characteristicUUID).isEqualTo(UUID.fromString(SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID));
    }

    @Test
    public void Legacy_shouldReadValueFromCharacteristic_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, stringResultListenerMock);

        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        assertThat(reporterArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    public void Legacy_shouldInformListenerOnError_whenReporterReportsError() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, stringResultListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNErrorBluetoothDisabled;
        resultReporter.reportResult(shnResult, null);

        verify(stringResultListenerMock).onActionCompleted(null, shnResult);
    }

    @Test
    public void Legacy_shouldInformListenerValue_whenReporterReportsValue() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, stringResultListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNOk;
        resultReporter.reportResult(shnResult, TEST_MESSAGE.getBytes(StandardCharsets.UTF_8));

        verify(stringResultListenerMock).onActionCompleted(TEST_MESSAGE, shnResult);
    }

    @Test
    public void shouldInformListenerOfUnsupportedOperation_whenInformationTypeIsNull() {
        deviceInformation.readDeviceInformation(null, deviceInformationListenerMock);

        verify(deviceInformationListenerMock).onError(null, SHNResult.SHNErrorUnsupportedOperation);
    }

    @Test
    public void shouldQueryForCorrectUUID_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, deviceInformationListenerMock);

        assertThat(deviceInformation.characteristicUUID).isEqualTo(UUID.fromString(SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID));
    }

    @Test
    public void shouldReadValueFromCharacteristic_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision, deviceInformationListenerMock);

        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        assertThat(reporterArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    public void shouldInformListenerOnError_whenReporterReportsError() {
        SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
        deviceInformation.readDeviceInformation(informationType, deviceInformationListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNErrorBluetoothDisabled;
        resultReporter.reportResult(shnResult, null);

        verify(deviceInformationListenerMock).onError(informationType, shnResult);
    }

    @Test
    public void shouldInformListenerValue_whenReporterReportsValue() {
        SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
        deviceInformation.readDeviceInformation(informationType, deviceInformationListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNOk;
        resultReporter.reportResult(shnResult, TEST_MESSAGE.getBytes(StandardCharsets.UTF_8));

        verify(deviceInformationListenerMock).onDeviceInformation(eq(informationType), eq(TEST_MESSAGE), isA(Date.class));
    }

    private class TestSHNServiceDeviceInformation extends SHNServiceDeviceInformation {

        UUID characteristicUUID;

        @Override
        public SHNCharacteristic getSHNCharacteristic(final UUID characteristicUUID) {
            if (characteristicUUID == null) {
                return super.getSHNCharacteristic(characteristicUUID);
            } else {
                this.characteristicUUID = characteristicUUID;
                return characteristicMock;
            }
        }
    }
}