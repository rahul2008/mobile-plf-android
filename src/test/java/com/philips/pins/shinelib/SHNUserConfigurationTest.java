package com.philips.pins.shinelib;

import android.content.SharedPreferences;

import com.philips.pins.shinelib.utility.SHNServiceRegistry;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by 310188215 on 21/10/15.
 */
public class SHNUserConfigurationTest {

    private SHNUserConfiguration shnUserConfiguration;
    private ShinePreferenceWrapper mockedShinePreferenceWrapper;
    private SharedPreferences.Editor mockedEditor;

    @Before
    public void setUp() throws Exception {
        mockedShinePreferenceWrapper = mock(ShinePreferenceWrapper.class);
        mockedEditor = mock(SharedPreferences.Editor.class);

        when(mockedShinePreferenceWrapper.getInt(anyString())).thenReturn(-1);
        when(mockedShinePreferenceWrapper.getLong(anyString())).thenReturn(-1L);
        when(mockedShinePreferenceWrapper.getBoolean(anyString())).thenReturn(null);
        when(mockedShinePreferenceWrapper.getFloat(anyString())).thenReturn(Float.NaN);
        when(mockedShinePreferenceWrapper.getString(anyString())).thenReturn(null);
        when(mockedShinePreferenceWrapper.edit()).thenReturn(mockedEditor);

        SHNServiceRegistry.getInstance().add(mockedShinePreferenceWrapper, ShinePreferenceWrapper.class);
        assertEquals(mockedShinePreferenceWrapper, SHNServiceRegistry.getInstance().get(ShinePreferenceWrapper.class));
        shnUserConfiguration = new SHNUserConfiguration();
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
        verify(mockedShinePreferenceWrapper).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Male.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Female);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Female.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Unspecified);
        verify(mockedShinePreferenceWrapper, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Unspecified.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(null);
        verify(mockedShinePreferenceWrapper, times(4)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_SEX);
        assertEquals(4, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenHandednessIsSetTheWrapperIsCalledWithThePropperValue() {
        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.LeftHanded);
        verify(mockedShinePreferenceWrapper).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.LeftHanded.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.RightHanded);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.RightHanded.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.MixedHanded);
        verify(mockedShinePreferenceWrapper, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.MixedHanded.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.Unknown);
        verify(mockedShinePreferenceWrapper, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.Unknown.name());
        assertEquals(4, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(null);
        verify(mockedShinePreferenceWrapper, times(5)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_HANDEDNESS);
        assertEquals(5, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenMaxHeartRateIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setMaxHeartRate(220);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfiguration.USER_CONFIG_MAX_HEART_RATE, (Integer) 220);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setMaxHeartRate(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_MAX_HEART_RATE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

    }

    @Test
    public void whenRestingHeartRateIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setRestingHeartRate(60);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfiguration.USER_CONFIG_RESTING_HEART_RATE, (Integer) 60);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setRestingHeartRate(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_RESTING_HEART_RATE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

    }

    @Test
    public void whenHeightInCmIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setHeightInCm(160);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfiguration.USER_CONFIG_HEIGHT_IN_CM, (Integer) 160);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHeightInCm(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_HEIGHT_IN_CM);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenWeightInKgIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setWeightInKg(80.5);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putFloat(SHNUserConfiguration.USER_CONFIG_WEIGHT_IN_KG, 80.5f);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setWeightInKg(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_WEIGHT_IN_KG);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenBirthDateIsSetTheWrapperIsCalledWithTheProperValue() {
        Date now = new Date();
        shnUserConfiguration.setDateOfBirth(now);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putLong(SHNUserConfiguration.USER_CONFIG_DATE_OF_BIRTH, now.getTime());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDateOfBirth(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_DATE_OF_BIRTH);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenLanguageCodeIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setIsoLanguageCode("nl");
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_ISO_LANGUAGE_CODE, "nl");
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setIsoLanguageCode(null);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_ISO_LANGUAGE_CODE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenUseMetricSystemIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setUseMetricSystem(true);
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putBoolean(SHNUserConfiguration.USER_CONFIG_USE_METRIC_SYSTEM, Boolean.TRUE);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setUseMetricSystem(false);
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).putBoolean(SHNUserConfiguration.USER_CONFIG_USE_METRIC_SYSTEM, Boolean.FALSE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setUseMetricSystem(null);
        verify(mockedShinePreferenceWrapper, times(3)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_USE_METRIC_SYSTEM);
        assertEquals(3, shnUserConfiguration.getChangeIncrement());
    }


    @Test
    public void whenDecimalSeparatorIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setDecimalSeparator('.');
        verify(mockedShinePreferenceWrapper, times(1)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_DECIMAL_SEPARATOR, ".");
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDecimalSeparator(',');
        verify(mockedShinePreferenceWrapper, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfiguration.USER_CONFIG_DECIMAL_SEPARATOR, ",");
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDecimalSeparator(null);
        verify(mockedShinePreferenceWrapper, times(3)).edit();
        verify(mockedEditor).remove(SHNUserConfiguration.USER_CONFIG_DECIMAL_SEPARATOR);
        assertEquals(3, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void theObjectIsProperlyInitializedFromPermanentStorage() {
        Date now = new Date();
        when(mockedShinePreferenceWrapper.getString(SHNUserConfiguration.USER_CONFIG_SEX)).thenReturn(SHNUserConfiguration.Sex.Male.name());
        when(mockedShinePreferenceWrapper.getInt(SHNUserConfiguration.USER_CONFIG_MAX_HEART_RATE)).thenReturn(220);
        when(mockedShinePreferenceWrapper.getInt(SHNUserConfiguration.USER_CONFIG_RESTING_HEART_RATE)).thenReturn(60);
        when(mockedShinePreferenceWrapper.getInt(SHNUserConfiguration.USER_CONFIG_HEIGHT_IN_CM)).thenReturn(160);
        when(mockedShinePreferenceWrapper.getFloat(SHNUserConfiguration.USER_CONFIG_WEIGHT_IN_KG)).thenReturn(80.5f);
        when(mockedShinePreferenceWrapper.getLong(SHNUserConfiguration.USER_CONFIG_DATE_OF_BIRTH)).thenReturn(now.getTime());
        when(mockedShinePreferenceWrapper.getString(SHNUserConfiguration.USER_CONFIG_HANDEDNESS)).thenReturn(SHNUserConfiguration.Handedness.LeftHanded.name());
        when(mockedShinePreferenceWrapper.getString(SHNUserConfiguration.USER_CONFIG_ISO_LANGUAGE_CODE)).thenReturn("nl");
        when(mockedShinePreferenceWrapper.getBoolean(SHNUserConfiguration.USER_CONFIG_USE_METRIC_SYSTEM)).thenReturn(true);
        when(mockedShinePreferenceWrapper.getString(SHNUserConfiguration.USER_CONFIG_DECIMAL_SEPARATOR)).thenReturn(",");
        when(mockedShinePreferenceWrapper.getInt(SHNUserConfiguration.USER_CONFIG_INCREMENT)).thenReturn(3456);

        shnUserConfiguration = new SHNUserConfiguration();
        assertEquals(SHNUserConfiguration.Sex.Male, shnUserConfiguration.getSex());
        assertEquals((Integer)220, shnUserConfiguration.getMaxHeartRate());
        assertEquals((Integer)60, shnUserConfiguration.getRestingHeartRate());
        assertEquals((Integer)160, shnUserConfiguration.getHeightInCm());
        assertEquals((Double)80.5, shnUserConfiguration.getWeightInKg());
        assertEquals(now.getTime(), shnUserConfiguration.getDateOfBirth().getTime());
        assertEquals(SHNUserConfiguration.Handedness.LeftHanded, shnUserConfiguration.getHandedness());
        assertEquals("nl", shnUserConfiguration.getIsoLanguageCode());
        assertTrue(shnUserConfiguration.getUseMetricSystem());
        assertEquals((Character)',', shnUserConfiguration.getDecimalSeparator());
        assertEquals(3456, shnUserConfiguration.getChangeIncrement());
    }
}