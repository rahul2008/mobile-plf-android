package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.longThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DeviceInformationCacheTest {
    public static final SHNCapabilityDeviceInformation.SHNDeviceInformationType INFORMATION_TYPE = SHNCapabilityDeviceInformation.SHNDeviceInformationType.HardwareRevision;
    public static final String TEST_MESSAGE = "TEST_MESSAGE";
    public static final Date TEST_DATE = new Date();

    @Mock
    private Context contextMock;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor editorMock;

    private DeviceInformationCache deviceInformationCache;

    @Before
    public void setUp() throws ParseException {
        initMocks(this);

        when(contextMock.getSharedPreferences(ShinePreferenceWrapper.SHINELIB_PREFERENCES_FILE_KEY + DeviceInformationCache.DEVICE_INFORMATION_CACHE, Context.MODE_PRIVATE)).thenReturn(sharedPreferencesMock);

        deviceInformationCache = new DeviceInformationCache(contextMock);
    }

    @Test
    public void ShouldReturnValueOfCachedType_WhenQueried() {
        when(sharedPreferencesMock.getString(INFORMATION_TYPE.name(), null)).thenReturn(TEST_MESSAGE);

        String value = deviceInformationCache.getValue(INFORMATION_TYPE);

        assertThat(value).isEqualTo(TEST_MESSAGE);
    }

    @Test
    public void ShouldReturnDateOfCachedType_WhenQueried() throws ParseException {
        when(sharedPreferencesMock.getLong(INFORMATION_TYPE.name() + DeviceInformationCache.DATE_SUFFIX, 0)).thenReturn(TEST_DATE.getTime());

        Date date = deviceInformationCache.getDate(INFORMATION_TYPE);

        assertThat(date).isEqualTo(TEST_DATE);
    }

    @Test
    public void ShouldPersistValueAndDate_WhenSaveIsCalled() {
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);

        deviceInformationCache.save(INFORMATION_TYPE, TEST_MESSAGE);

        verify(editorMock).putString(INFORMATION_TYPE.name(), TEST_MESSAGE);
        verify(editorMock).putLong(eq(INFORMATION_TYPE.name() + DeviceInformationCache.DATE_SUFFIX), longThat(new ArgumentMatcher<Long>() {
            @Override
            public boolean matches(final Object argument) {
                boolean res = false;

                if (argument instanceof Long) {
                    long millis = ((Long) argument).longValue();
                    res = TEST_DATE.getTime() < millis;
                    res &= millis < new Date().getTime();
                }

                return res;
            }
        }));

        verify(editorMock).commit();
    }
}
