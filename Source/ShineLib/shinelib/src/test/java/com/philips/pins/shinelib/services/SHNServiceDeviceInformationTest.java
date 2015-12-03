package com.philips.pins.shinelib.services;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNServiceDeviceInformationTest {

    public static final SHNCapabilityDeviceInformation.SHNDeviceInformationType INFORMATION_TYPE = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final String TEST_DATE_STRING = "TEST_DATE_STRING";
    public static final Date TEST_DATE = new Date();

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor editorMock;

    @Mock
    private SimpleDateFormat simpleDateFormatMock;

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

        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(simpleDateFormatMock.format(any(Date.class), any(StringBuffer.class), any(FieldPosition.class))).thenReturn(new StringBuffer(TEST_DATE_STRING));
        when(simpleDateFormatMock.parse(TEST_DATE_STRING)).thenReturn(TEST_DATE);

        deviceInformation = new TestSHNServiceDeviceInformation(sharedPreferencesMock);
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

        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name(), null)).thenReturn(TEST_MESSAGE);
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name() + SHNServiceDeviceInformation.DATE_SUFFIX, null)).thenReturn(TEST_DATE_STRING);

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

        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name(), null)).thenReturn(TEST_MESSAGE);
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name() + SHNServiceDeviceInformation.DATE_SUFFIX, null)).thenReturn(TEST_DATE_STRING);

        SHNResult shnResult = SHNResult.SHNOk;
        resultReporter.reportResult(shnResult, TEST_MESSAGE.getBytes(StandardCharsets.UTF_8));

        verify(deviceInformationListenerMock).onDeviceInformation(eq(INFORMATION_TYPE), eq(TEST_MESSAGE), isA(Date.class));
    }

    @Test
    public void shouldInformListenerValue_whenDataHasBeenCached_AndReporterReportsError() {
        deviceInformation.readDeviceInformation(INFORMATION_TYPE, deviceInformationListenerMock);
        verify(characteristicMock).read(reporterArgumentCaptor.capture());
        SHNCommandResultReporter resultReporter = reporterArgumentCaptor.getValue();

        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name(), null)).thenReturn(TEST_MESSAGE);
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name() + SHNServiceDeviceInformation.DATE_SUFFIX, null)).thenReturn(TEST_DATE_STRING);

        SHNResult shnResult = SHNResult.SHNErrorAssociationFailed;
        resultReporter.reportResult(shnResult, null);

        verify(deviceInformationListenerMock).onDeviceInformation(eq(INFORMATION_TYPE), eq(TEST_MESSAGE), isA(Date.class));
    }

    private class TestSHNServiceDeviceInformation extends SHNServiceDeviceInformation {

        UUID characteristicUUID;

        public TestSHNServiceDeviceInformation(@NonNull final SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        @Override
        public SHNCharacteristic getSHNCharacteristic(final UUID characteristicUUID) {
            if (characteristicUUID == null) {
                return super.getSHNCharacteristic(characteristicUUID);
            } else {
                this.characteristicUUID = characteristicUUID;
                return characteristicMock;
            }
        }

        @NonNull
        @Override
        SimpleDateFormat getSimpleDateFormat() {
            return simpleDateFormatMock;
        }
    }
}