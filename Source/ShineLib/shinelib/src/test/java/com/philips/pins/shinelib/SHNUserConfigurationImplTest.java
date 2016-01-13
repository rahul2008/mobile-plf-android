package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.utility.SHNPersistentStorage;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class SHNUserConfigurationImplTest {

    private SHNUserConfigurationImpl shnUserConfiguration;
    private SHNPersistentStorage mockedShnPersistentStorage;
    private SharedPreferences mockedSharedPreferences;
    private SharedPreferences.Editor mockedEditor;
    private MockedHandler mockedHandler;
    private Observer mockedObserver;

    @Before
    public void setUp() throws Exception {
        mockedShnPersistentStorage = mock(SHNPersistentStorage.class);
        mockedSharedPreferences = mock(SharedPreferences.class);
        mockedEditor = mock(SharedPreferences.Editor.class);
        mockedObserver = mock(Observer.class);
        mockedHandler = new MockedHandler();

        when(mockedSharedPreferences.getInt(anyString(), anyInt())).thenReturn(-1);
        when(mockedSharedPreferences.getLong(anyString(), anyLong())).thenReturn(-1L);
        when(mockedSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false);
        when(mockedSharedPreferences.getFloat(anyString(), anyFloat())).thenReturn(Float.NaN);
        when(mockedSharedPreferences.getString(anyString(), anyString())).thenReturn(null);
        when(mockedSharedPreferences.edit()).thenReturn(mockedEditor);

        when(mockedShnPersistentStorage.getSharedPreferences()).thenReturn(mockedSharedPreferences);

        shnUserConfiguration = new SHNUserConfigurationImpl(mockedShnPersistentStorage, mockedHandler.getMock());
        shnUserConfiguration.addObserver(mockedObserver);
    }

    @Test
    public void canCreateInstance() {
        assertNotNull(shnUserConfiguration);
    }

    @Test
    public void whenCreatedWithoutConfiguringUserDataThenTheDefaultsAreSet() {
        assertEquals(SHNUserConfiguration.Sex.Unspecified, shnUserConfiguration.getSex());
        assertNull(shnUserConfiguration.getMaxHeartRate());
        assertNull(shnUserConfiguration.getRestingHeartRate());
        assertNull(shnUserConfiguration.getHeightInCm());
        assertNull(shnUserConfiguration.getWeightInKg());
        assertNull(shnUserConfiguration.getDateOfBirth());
        assertEquals(SHNUserConfiguration.Handedness.Unknown, shnUserConfiguration.getHandedness());
        assertEquals("en", shnUserConfiguration.getIsoLanguageCode());
        assertEquals((Character) '.', shnUserConfiguration.getDecimalSeparator());
        assertFalse(shnUserConfiguration.getUseMetricSystem());

        assertEquals(0, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenSexIsSetTheWrapperIsCalledWithThePropperValue() {
        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Male);
        verify(mockedSharedPreferences).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Male.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Unspecified);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Unspecified.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Female);
        verify(mockedSharedPreferences, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Female.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);

        shnUserConfiguration.setSex(null);
        verify(mockedSharedPreferences, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Unspecified.name());
        assertEquals(4, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenSexIsSetThenListenerIsNotified() {
        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Male);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenHandednessIsSetTheWrapperIsCalledWithThePropperValue() {
        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.LeftHanded);
        verify(mockedSharedPreferences).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.LeftHanded.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.RightHanded);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.RightHanded.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.Unknown);
        verify(mockedSharedPreferences, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.Unknown.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.MixedHanded);
        verify(mockedSharedPreferences, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.MixedHanded.name());
        assertEquals(4, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);

        shnUserConfiguration.setHandedness(null);
        verify(mockedSharedPreferences, times(5)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.Unknown.name());
        assertEquals(5, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenHandednessIsSetThenListenerIsNotified() {
        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.LeftHanded);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenMaxHeartRateIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setMaxHeartRate(220);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE, (Integer) 220);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setMaxHeartRate(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenRestingHeartRateIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setRestingHeartRate(60);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_RESTING_HEART_RATE, (Integer) 60);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setRestingHeartRate(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_RESTING_HEART_RATE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenRestingHeartRateIsSetThenListenerIsNotified() {
        shnUserConfiguration.setRestingHeartRate(60);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenHeightInCmIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setHeightInCm(160);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_HEIGHT_IN_CM, (Integer) 160);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHeightInCm(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_HEIGHT_IN_CM);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenHeightIsSetThenListenerIsNotified() {
        shnUserConfiguration.setHeightInCm(171);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenWeightInKgIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setWeightInKg(80.5);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putFloat(SHNUserConfigurationImpl.USER_CONFIG_WEIGHT_IN_KG, 80.5f);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setWeightInKg(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_WEIGHT_IN_KG);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenWeightInKgIsSetThenListenerIsNotified() {
        shnUserConfiguration.setWeightInKg(78.7);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenBirthDateIsSetTheWrapperIsCalledWithTheProperValue() {
        Date now = new Date();
        shnUserConfiguration.setDateOfBirth(now);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putLong(SHNUserConfigurationImpl.USER_CONFIG_DATE_OF_BIRTH, now.getTime());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDateOfBirth(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_DATE_OF_BIRTH);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenBirthDateInKgIsSetThenListenerIsNotified() {
        Date now = new Date();
        shnUserConfiguration.setDateOfBirth(now);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenLanguageCodeIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setIsoLanguageCode("nl");
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_ISO_LANGUAGE_CODE, "nl");
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setIsoLanguageCode(null);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_ISO_LANGUAGE_CODE, "en");
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenLanguageIsSetThenListenerIsNotified() {
        shnUserConfiguration.setIsoLanguageCode("nl");

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenUseMetricSystemIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setUseMetricSystem(true);
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putBoolean(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM, Boolean.TRUE);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setUseMetricSystem(false);
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).putBoolean(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM, Boolean.FALSE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setUseMetricSystem(true);
        reset(mockedEditor);

        shnUserConfiguration.setUseMetricSystem(null);
        verify(mockedSharedPreferences, times(4)).edit();
        verify(mockedEditor).putBoolean(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM, Boolean.FALSE);
        assertEquals(4, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenDecimalSeparatorIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setDecimalSeparator(',');
        verify(mockedSharedPreferences, times(1)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ",");
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDecimalSeparator('.');
        verify(mockedSharedPreferences, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ".");
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);
        shnUserConfiguration.setDecimalSeparator(',');

        shnUserConfiguration.setDecimalSeparator(null);
        verify(mockedSharedPreferences, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ".");
        assertEquals(4, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void theObjectIsProperlyInitializedFromPermanentStorage() {
        Date now = new Date();
        when(mockedSharedPreferences.getString(SHNUserConfigurationImpl.USER_CONFIG_SEX, null)).thenReturn(SHNUserConfiguration.Sex.Male.name());
        when(mockedSharedPreferences.getInt(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE, -1)).thenReturn(220);
        when(mockedSharedPreferences.getInt(SHNUserConfigurationImpl.USER_CONFIG_RESTING_HEART_RATE, -1)).thenReturn(60);
        when(mockedSharedPreferences.getInt(SHNUserConfigurationImpl.USER_CONFIG_HEIGHT_IN_CM, -1)).thenReturn(160);
        when(mockedSharedPreferences.getFloat(SHNUserConfigurationImpl.USER_CONFIG_WEIGHT_IN_KG, Float.NaN)).thenReturn(80.5f);
        when(mockedSharedPreferences.getLong(SHNUserConfigurationImpl.USER_CONFIG_DATE_OF_BIRTH, -1L)).thenReturn(now.getTime());
        when(mockedSharedPreferences.getString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, null)).thenReturn(SHNUserConfiguration.Handedness.LeftHanded.name());
        when(mockedSharedPreferences.getString(SHNUserConfigurationImpl.USER_CONFIG_ISO_LANGUAGE_CODE, null)).thenReturn("nl");
        when(mockedSharedPreferences.contains(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM)).thenReturn(true);
        when(mockedSharedPreferences.getBoolean(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM, false)).thenReturn(true);
        when(mockedSharedPreferences.getString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, null)).thenReturn(",");
        when(mockedSharedPreferences.getInt(SHNUserConfigurationImpl.USER_CONFIG_INCREMENT, -1)).thenReturn(3456);

        shnUserConfiguration = new SHNUserConfigurationImpl(mockedShnPersistentStorage, mockedHandler.getMock());
        assertEquals(SHNUserConfiguration.Sex.Male, shnUserConfiguration.getSex());
        assertEquals((Integer) 220, shnUserConfiguration.getMaxHeartRate());
        assertEquals((Integer) 60, shnUserConfiguration.getRestingHeartRate());
        assertEquals((Integer) 160, shnUserConfiguration.getHeightInCm());
        assertEquals((Double) 80.5, shnUserConfiguration.getWeightInKg());
        assertEquals(now.getTime(), shnUserConfiguration.getDateOfBirth().getTime());
        assertEquals(SHNUserConfiguration.Handedness.LeftHanded, shnUserConfiguration.getHandedness());
        assertEquals("nl", shnUserConfiguration.getIsoLanguageCode());
        assertTrue(shnUserConfiguration.getUseMetricSystem());
        assertEquals((Character) ',', shnUserConfiguration.getDecimalSeparator());
        assertEquals(3456, shnUserConfiguration.getChangeIncrement());
    }
}