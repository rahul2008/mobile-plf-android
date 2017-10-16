/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

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
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNServiceDeviceInformationTest {

    public static final SHNCapabilityDeviceInformation.SHNDeviceInformationType INFORMATION_TYPE = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
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
    public void setUp() throws ParseException {
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
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, stringResultListenerMock);

        assertThat(deviceInformation.characteristicUUID).isEqualTo(UUID.fromString(SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID));
    }

    @Test
    public void Legacy_shouldReadValueFromCharacteristic_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, stringResultListenerMock);

        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        assertThat(reporterArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    public void Legacy_shouldInformListenerOnError_whenReporterReportsError() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, stringResultListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNErrorBluetoothDisabled;
        resultReporter.reportResult(shnResult, null);

        verify(stringResultListenerMock).onActionCompleted(null, shnResult);
    }

    @Test
    public void Legacy_shouldInformListenerValue_whenReporterReportsValue() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, stringResultListenerMock);
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
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);

        assertThat(deviceInformation.characteristicUUID).isEqualTo(UUID.fromString(SHNServiceDeviceInformation.HARDWARE_REVISION_CHARACTERISTIC_UUID));
    }

    @Test
    public void shouldReadValueFromCharacteristic_whenInformationTypeIsProvided() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);

        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        assertThat(reporterArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    public void shouldInformListenerOnError_whenReporterReportsError() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNErrorBluetoothDisabled;
        resultReporter.reportResult(shnResult, null);

        verify(deviceInformationListenerMock).onError(INFORMATION_TYPE, shnResult);
    }

    @Test
    public void shouldInformListenerValue_whenReporterReportsValue() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        SHNResult shnResult = SHNResult.SHNOk;
        resultReporter.reportResult(shnResult, TEST_MESSAGE.getBytes(StandardCharsets.UTF_8));

        verify(deviceInformationListenerMock).onDeviceInformation(eq(INFORMATION_TYPE), eq(TEST_MESSAGE), isA(Date.class));
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
