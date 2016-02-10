package com.philips.pins.shinelib;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.pins.shinelib.helper.MockedHandler;
import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.Locale;
import java.util.Observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SHNUserConfigurationImplTest {

    public static final String TEST_KEY = "TEST_KEY";
    public static final String TEST_VALUE = "TEST_VALUE";
    @Mock
    private PersistentStorageFactory persistentStorageFactoryMock;

    @Mock
    private SharedPreferences sharedPreferencesMock;

    @Mock
    private SharedPreferences.Editor mockedEditor;

    private MockedHandler mockedHandler = new MockedHandler();

    @Mock
    private Observer mockedObserver;

    private SHNUserConfigurationImpl shnUserConfiguration;
    private Locale defaultLocale;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        when(sharedPreferencesMock.getInt(anyString(), anyInt())).thenReturn(-1);
        when(sharedPreferencesMock.getLong(anyString(), anyLong())).thenReturn(-1L);
        when(sharedPreferencesMock.getBoolean(anyString(), anyBoolean())).thenReturn(false);
        when(sharedPreferencesMock.getFloat(anyString(), anyFloat())).thenReturn(Float.NaN);
        when(sharedPreferencesMock.getString(anyString(), anyString())).thenReturn(null);
        when(sharedPreferencesMock.edit()).thenReturn(mockedEditor);
        PersistentStorage persistentStorage = new PersistentStorage(sharedPreferencesMock);

        when(persistentStorageFactoryMock.getPersistentStorageForUser()).thenReturn(persistentStorage);

        shnUserConfiguration = new SHNUserConfigurationImpl(persistentStorageFactoryMock, mockedHandler.getMock());
        shnUserConfiguration.addObserver(mockedObserver);
    }

    @After
    public void tearDown() throws Exception {
        Locale.setDefault(defaultLocale);
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

        assertEquals(0, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenSexIsSetTheWrapperIsCalledWithThePropperValue() {
        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Male);
        verify(sharedPreferencesMock).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Male.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Unspecified);
        verify(sharedPreferencesMock, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Unspecified.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Female);
        verify(sharedPreferencesMock, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_SEX, SHNUserConfiguration.Sex.Female.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);

        shnUserConfiguration.setSex(null);
        verify(sharedPreferencesMock, times(4)).edit();
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
        verify(sharedPreferencesMock).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.LeftHanded.name());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.RightHanded);
        verify(sharedPreferencesMock, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.RightHanded.name());
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.Unknown);
        verify(sharedPreferencesMock, times(3)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.Unknown.name());
        assertEquals(3, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.MixedHanded);
        verify(sharedPreferencesMock, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, SHNUserConfiguration.Handedness.MixedHanded.name());
        assertEquals(4, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);

        shnUserConfiguration.setHandedness(null);
        verify(sharedPreferencesMock, times(5)).edit();
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
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE, (Integer) 220);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setMaxHeartRate(null);
        verify(sharedPreferencesMock, times(2)).edit();
        verify(mockedEditor).remove(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE);
        assertEquals(2, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void whenRestingHeartRateIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setRestingHeartRate(60);
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_RESTING_HEART_RATE, (Integer) 60);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setRestingHeartRate(null);
        verify(sharedPreferencesMock, times(2)).edit();
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
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putInt(SHNUserConfigurationImpl.USER_CONFIG_HEIGHT_IN_CM, (Integer) 160);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setHeightInCm(null);
        verify(sharedPreferencesMock, times(2)).edit();
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
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putFloat(SHNUserConfigurationImpl.USER_CONFIG_WEIGHT_IN_KG, 80.5f);
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setWeightInKg(null);
        verify(sharedPreferencesMock, times(2)).edit();
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
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putLong(SHNUserConfigurationImpl.USER_CONFIG_DATE_OF_BIRTH, now.getTime());
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDateOfBirth(null);
        verify(sharedPreferencesMock, times(2)).edit();
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
    public void whenDecimalSeparatorIsSetTheWrapperIsCalledWithTheProperValue() {
        shnUserConfiguration.setDecimalSeparator(',');
        verify(sharedPreferencesMock, times(1)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ",");
        assertEquals(1, shnUserConfiguration.getChangeIncrement());

        shnUserConfiguration.setDecimalSeparator('.');
        verify(sharedPreferencesMock, times(2)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ".");
        assertEquals(2, shnUserConfiguration.getChangeIncrement());

        reset(mockedEditor);
        shnUserConfiguration.setDecimalSeparator(',');

        shnUserConfiguration.setDecimalSeparator(null);
        verify(sharedPreferencesMock, times(4)).edit();
        verify(mockedEditor).putString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, ".");
        assertEquals(4, shnUserConfiguration.getChangeIncrement());
    }

    @Test
    public void theObjectIsProperlyInitializedFromPermanentStorage() {
        Date now = new Date();
        when(sharedPreferencesMock.getString(SHNUserConfigurationImpl.USER_CONFIG_SEX, null)).thenReturn(SHNUserConfiguration.Sex.Male.name());
        when(sharedPreferencesMock.getInt(SHNUserConfigurationImpl.USER_CONFIG_MAX_HEART_RATE, -1)).thenReturn(220);
        when(sharedPreferencesMock.getInt(SHNUserConfigurationImpl.USER_CONFIG_RESTING_HEART_RATE, -1)).thenReturn(60);
        when(sharedPreferencesMock.getInt(SHNUserConfigurationImpl.USER_CONFIG_HEIGHT_IN_CM, -1)).thenReturn(160);
        when(sharedPreferencesMock.getFloat(SHNUserConfigurationImpl.USER_CONFIG_WEIGHT_IN_KG, Float.NaN)).thenReturn(80.5f);
        when(sharedPreferencesMock.getLong(SHNUserConfigurationImpl.USER_CONFIG_DATE_OF_BIRTH, -1L)).thenReturn(now.getTime());
        when(sharedPreferencesMock.getString(SHNUserConfigurationImpl.USER_CONFIG_HANDEDNESS, null)).thenReturn(SHNUserConfiguration.Handedness.LeftHanded.name());
        when(sharedPreferencesMock.getString(SHNUserConfigurationImpl.USER_CONFIG_ISO_LANGUAGE_CODE, null)).thenReturn("nl");
        when(sharedPreferencesMock.contains(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM)).thenReturn(true);
        when(sharedPreferencesMock.getBoolean(SHNUserConfigurationImpl.USER_CONFIG_USE_METRIC_SYSTEM, false)).thenReturn(true);
        when(sharedPreferencesMock.getString(SHNUserConfigurationImpl.USER_CONFIG_DECIMAL_SEPARATOR, null)).thenReturn(",");
        when(sharedPreferencesMock.getInt(SHNUserConfigurationImpl.USER_CONFIG_INCREMENT, -1)).thenReturn(3456);

        shnUserConfiguration = new SHNUserConfigurationImpl(persistentStorageFactoryMock, mockedHandler.getMock());
        assertEquals(SHNUserConfiguration.Sex.Male, shnUserConfiguration.getSex());
        assertEquals((Integer) 220, shnUserConfiguration.getMaxHeartRate());
        assertEquals((Integer) 60, shnUserConfiguration.getRestingHeartRate());
        assertEquals((Integer) 160, shnUserConfiguration.getHeightInCm());
        assertEquals((Double) 80.5, shnUserConfiguration.getWeightInKg());
        assertEquals(now.getTime(), shnUserConfiguration.getDateOfBirth().getTime());
        assertEquals(SHNUserConfiguration.Handedness.LeftHanded, shnUserConfiguration.getHandedness());
        assertEquals((Character) ',', shnUserConfiguration.getDecimalSeparator());
        assertEquals(3456, shnUserConfiguration.getChangeIncrement());
    }

    // ------------------

    private void setupNewPreferenceFormat() {
        SharedPreferences sharedPreferences = RuntimeEnvironment.application.getSharedPreferences("testPreferences", Context.MODE_PRIVATE);
        PersistentStorage persistentStorage = new PersistentStorage(sharedPreferences);
        when(persistentStorageFactoryMock.getPersistentStorageForUser()).thenReturn(persistentStorage);
        shnUserConfiguration = new SHNUserConfigurationImpl(persistentStorageFactoryMock, mockedHandler.getMock());
        shnUserConfiguration.addObserver(mockedObserver);
    }

    @Test
    public void whenLanguageIsSet_ThenListenerIsNotified() {
        setupNewPreferenceFormat();

        shnUserConfiguration.setIsoLanguageCode("TEST");

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIsoLanguageCodeHasNotBeenSet_ThenGetReturnDefaultIsoLanguageCode() {
        setupNewPreferenceFormat();

        String expectedIsoLanguageCode = Locale.getDefault().getLanguage();
        String actualIsoLanguageCode = shnUserConfiguration.getIsoLanguageCode();

        assertThat(actualIsoLanguageCode).isEqualTo(expectedIsoLanguageCode);
    }

    @Test
    public void whenIsoLanguageCodeHasBeenSet_ThenGetReturnsThatIsoLanguageCode() {
        setupNewPreferenceFormat();

        String expectedIsoLanguageCode = "TEST";
        shnUserConfiguration.setIsoLanguageCode(expectedIsoLanguageCode);

        String actualIsoLanguageCode = shnUserConfiguration.getIsoLanguageCode();

        assertThat(actualIsoLanguageCode).isEqualTo(expectedIsoLanguageCode);
    }

    @Test
    public void whenCountryIsSet_ThenListenerIsNotified() {
        setupNewPreferenceFormat();

        shnUserConfiguration.setIsoCountryCode("TEST");

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIsoCountryCodeHasNotBeenSet_ThenGetReturnDefaultIsoCountryCode() {
        setupNewPreferenceFormat();

        String expectedIsoCountryCode = Locale.getDefault().getCountry();
        String actualIsoCountryCode = shnUserConfiguration.getIsoCountryCode();

        assertThat(actualIsoCountryCode).isEqualTo(expectedIsoCountryCode);
    }

    @Test
    public void whenIsoCountryCodeHasBeenSet_ThenGetReturnsThatIsoCountryCode() {
        setupNewPreferenceFormat();

        String expectedIsoCountryCode = "TEST";
        shnUserConfiguration.setIsoCountryCode(expectedIsoCountryCode);

        String actualIsoCountryCode = shnUserConfiguration.getIsoCountryCode();

        assertThat(actualIsoCountryCode).isEqualTo(expectedIsoCountryCode);
    }

    @Test
    public void whenClockFormatIsSet_ThenListenerIsNotified() {
        setupNewPreferenceFormat();

        shnUserConfiguration.setClockFormat(SHNUserConfiguration.ClockFormat._12H);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenClockFormatHasNotBeenSet_ThenItIsNull() {
        setupNewPreferenceFormat();

        SHNUserConfiguration.ClockFormat actualClockFormat = shnUserConfiguration.getClockFormat();

        assertThat(actualClockFormat).isNull();
    }

    @Test
    public void whenClockFormatHasBeenSet_ThenGetReturnsThatClockFormat() {
        setupNewPreferenceFormat();

        SHNUserConfiguration.ClockFormat expectedClockFormat = SHNUserConfiguration.ClockFormat._12H;
        shnUserConfiguration.setClockFormat(expectedClockFormat);

        SHNUserConfiguration.ClockFormat actualClockFormat = shnUserConfiguration.getClockFormat();

        assertThat(actualClockFormat).isEqualTo(expectedClockFormat);
    }

    @Test
    public void whenUseMetricSystemIsSet_ThenListenerIsNotified() {
        setupNewPreferenceFormat();

        shnUserConfiguration.setUseMetricSystem(false);

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenUseMetricSystemHasNotBeenSet_ThenItReturnsTrue() {
        setupNewPreferenceFormat();

        boolean useMetricSystem = shnUserConfiguration.getUseMetricSystem();

        assertThat(useMetricSystem).isTrue();
    }

    @Test
    public void whenUseMetricSystemHasBeenSetToFalse_ThenItReturnsFalse() {
        setupNewPreferenceFormat();

        shnUserConfiguration.setUseMetricSystem(false);
        boolean useMetricSystem = shnUserConfiguration.getUseMetricSystem();

        assertThat(useMetricSystem).isFalse();
    }

    @Test
    public void whenClearIsCalled_ThenDataIsCleared() {
        setupNewPreferenceFormat();
        persistentStorageFactoryMock.getPersistentStorageForUser().put(TEST_KEY, TEST_VALUE);
        assertThat(persistentStorageFactoryMock.getPersistentStorageForUser().contains(TEST_KEY)).isTrue();

        shnUserConfiguration.clear();

        assertThat(persistentStorageFactoryMock.getPersistentStorageForUser().contains(TEST_KEY)).isFalse();

        verify(mockedObserver).update(shnUserConfiguration, null);
    }

    @Test
    public void whenClearIsCalled_ThenListenerIsNotified() {
        setupNewPreferenceFormat();
        persistentStorageFactoryMock.getPersistentStorageForUser().put(TEST_KEY, TEST_VALUE);

        shnUserConfiguration.clear();

        verify(mockedObserver).update(shnUserConfiguration, null);
    }
}