package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformationCached;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceInformationCacheTest {
    public static final SHNCapabilityDeviceInformation.SHNDeviceInformationType INFORMATION_TYPE = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final String TEST_DATE_STRING = "TEST_DATE_STRING";
    public static final Date TEST_DATE = new Date();

    @Mock
    private Context contextMock;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor editorMock;

    @Mock
    private SimpleDateFormat simpleDateFormatMock;
    private DeviceInformationCache deviceInformationCache;

    @Before
    public void setUp() throws ParseException {
        initMocks(this);

        when(contextMock.getSharedPreferences(ShinePreferenceWrapper.SHINELIB_PREFERENCES_FILE_KEY + DeviceInformationCache.DEVICE_INFORMATION_CACHE, Context.MODE_PRIVATE)).thenReturn(sharedPreferencesMock);

        deviceInformationCache = new TestDeviceInformationCache(contextMock);
    }

    @Test
    public void ShouldReturnValueOfCachedType_WhenQueried() {
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name(), null)).thenReturn(TEST_MESSAGE);

        String value = deviceInformationCache.getValue(INFORMATION_TYPE);

        assertThat(value).isEqualTo(TEST_MESSAGE);
    }

    @Test
    public void ShouldReturnDateOfCachedType_WhenQueried() throws ParseException {
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name() + DeviceInformationCache.DATE_SUFFIX, null)).thenReturn(TEST_DATE_STRING);
        when(simpleDateFormatMock.parse(TEST_DATE_STRING)).thenReturn(TEST_DATE);

        Date date = deviceInformationCache.getDate(INFORMATION_TYPE);

        assertThat(date).isEqualTo(TEST_DATE);
    }

    @Test
    public void ShouldPersistValueAndDate_WhenSaveIsCalled() {
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(simpleDateFormatMock.format(any(Date.class), any(StringBuffer.class), any(FieldPosition.class))).thenReturn(new StringBuffer(TEST_DATE_STRING));

        deviceInformationCache.save(INFORMATION_TYPE, TEST_MESSAGE);

        verify(editorMock).putString(INFORMATION_TYPE.name(), TEST_MESSAGE);
        verify(editorMock).putString(INFORMATION_TYPE.name() + DeviceInformationCache.DATE_SUFFIX, TEST_DATE_STRING);
        verify(editorMock).commit();
    }

    // ---------

    private class TestDeviceInformationCache extends DeviceInformationCache {

        public TestDeviceInformationCache(@NonNull final Context context) {
            super(context);
        }

        @NonNull
        @Override
        SimpleDateFormat getSimpleDateFormat() {
            return simpleDateFormatMock;
        }
    }
}