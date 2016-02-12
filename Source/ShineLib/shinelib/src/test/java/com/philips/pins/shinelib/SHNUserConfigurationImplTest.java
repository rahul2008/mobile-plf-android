package com.philips.pins.shinelib;

import com.philips.pins.shinelib.helper.MockedHandler;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SHNUserConfigurationImplTest {

    public static final String TEST_KEY = "TEST_KEY";
    public static final String TEST_VALUE = "TEST_VALUE";


    @Mock
    private SHNUserConfigurationCalculations calculationsMock;

    @Mock
    private Observer observerMock;

    private PersistentStorageFactory persistentStorageFactory;
    private SHNUserConfigurationImpl shnUserConfiguration;
    private Locale defaultLocale;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        persistentStorageFactory = new PersistentStorageFactory(RuntimeEnvironment.application);

        shnUserConfiguration = new SHNUserConfigurationImpl(persistentStorageFactory, new MockedHandler().getMock(), calculationsMock);
        shnUserConfiguration.addObserver(observerMock);
    }

    @After
    public void tearDown() throws Exception {
        Locale.setDefault(defaultLocale);
    }

    // ------------------

    @Test
    public void whenClearIsCalled_ThenDataIsCleared() {
        persistentStorageFactory.getPersistentStorageForUser().put(TEST_KEY, TEST_VALUE);
        assertThat(persistentStorageFactory.getPersistentStorageForUser().contains(TEST_KEY)).isTrue();

        shnUserConfiguration.clear();

        assertThat(persistentStorageFactory.getPersistentStorageForUser().contains(TEST_KEY)).isFalse();

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenClearIsCalled_ThenListenerIsNotified() {
        persistentStorageFactory.getPersistentStorageForUser().put(TEST_KEY, TEST_VALUE);

        shnUserConfiguration.clear();

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenLanguageIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setIsoLanguageCode("TEST");

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIsoLanguageCodeHasNotBeenSet_ThenGetReturnDefaultIsoLanguageCode() {
        String expectedIsoLanguageCode = Locale.getDefault().getLanguage();
        String actualIsoLanguageCode = shnUserConfiguration.getIsoLanguageCode();

        assertThat(actualIsoLanguageCode).isEqualTo(expectedIsoLanguageCode);
    }

    @Test
    public void whenIsoLanguageCodeHasBeenSet_ThenGetReturnsThatIsoLanguageCode() {
        String expectedIsoLanguageCode = "TEST";
        shnUserConfiguration.setIsoLanguageCode(expectedIsoLanguageCode);

        String actualIsoLanguageCode = shnUserConfiguration.getIsoLanguageCode();

        assertThat(actualIsoLanguageCode).isEqualTo(expectedIsoLanguageCode);
    }

    @Test
    public void whenCountryIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setIsoCountryCode("TEST");

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIsoCountryCodeHasNotBeenSet_ThenGetReturnDefaultIsoCountryCode() {
        String expectedIsoCountryCode = Locale.getDefault().getCountry();
        String actualIsoCountryCode = shnUserConfiguration.getIsoCountryCode();

        assertThat(actualIsoCountryCode).isEqualTo(expectedIsoCountryCode);
    }

    @Test
    public void whenIsoCountryCodeHasBeenSet_ThenGetReturnsThatIsoCountryCode() {
        String expectedIsoCountryCode = "TEST";
        shnUserConfiguration.setIsoCountryCode(expectedIsoCountryCode);

        String actualIsoCountryCode = shnUserConfiguration.getIsoCountryCode();

        assertThat(actualIsoCountryCode).isEqualTo(expectedIsoCountryCode);
    }

    @Test
    public void whenClockFormatIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setClockFormat(SHNUserConfiguration.ClockFormat._12H);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenClockFormatHasNotBeenSet_ThenItIsNull() {
        SHNUserConfiguration.ClockFormat actualClockFormat = shnUserConfiguration.getClockFormat();

        assertThat(actualClockFormat).isNull();
    }

    @Test
    public void whenClockFormatHasBeenSet_ThenGetReturnsThatClockFormat() {
        SHNUserConfiguration.ClockFormat expectedClockFormat = SHNUserConfiguration.ClockFormat._12H;
        shnUserConfiguration.setClockFormat(expectedClockFormat);

        SHNUserConfiguration.ClockFormat actualClockFormat = shnUserConfiguration.getClockFormat();

        assertThat(actualClockFormat).isEqualTo(expectedClockFormat);
    }

    @Test
    public void whenUseMetricSystemIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setUseMetricSystem(false);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenUseMetricSystemHasNotBeenSet_ThenItReturnsTrue() {
        boolean useMetricSystem = shnUserConfiguration.getUseMetricSystem();

        assertThat(useMetricSystem).isTrue();
    }

    @Test
    public void whenUseMetricSystemHasBeenSetToFalse_ThenItReturnsFalse() {
        shnUserConfiguration.setUseMetricSystem(false);
        boolean useMetricSystem = shnUserConfiguration.getUseMetricSystem();

        assertThat(useMetricSystem).isFalse();
    }

    @Test
    public void whenSexHasNotBeenSet_ThenItIsNull() {
        SHNUserConfiguration.Sex actualSex = shnUserConfiguration.getSex();

        assertThat(actualSex).isEqualTo(SHNUserConfiguration.Sex.Unspecified);
    }

    @Test
    public void whenSexHasBeenSet_ThenGetReturnsThatSex() {
        SHNUserConfiguration.Sex expectedSex = SHNUserConfiguration.Sex.Male;
        shnUserConfiguration.setSex(expectedSex);

        SHNUserConfiguration.Sex actualSex = shnUserConfiguration.getSex();

        assertThat(actualSex).isEqualTo(expectedSex);
    }

    @Test
    public void whenSexIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setSex(SHNUserConfiguration.Sex.Female);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenRestingHeartRateHasNotBeenSet_ThenItIsMinus1() {
        Integer restingHeartRate = shnUserConfiguration.getRestingHeartRate();

        assertThat(restingHeartRate).isEqualTo(-1);
    }

    @Test
    public void whenRestingHeartRateHasBeenSet_ThenGetReturnsThatRestingHeartRate() {
        int expected = 111;
        shnUserConfiguration.setRestingHeartRate(expected);

        int actual = shnUserConfiguration.getRestingHeartRate();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenRestingHeartRateIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setRestingHeartRate(111);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenHeightInCmHasNotBeenSet_ThenItIsNull() {
        Integer heightInCm = shnUserConfiguration.getHeightInCm();

        assertThat(heightInCm).isNull();
    }

    @Test
    public void whenHeightInCmHasBeenSet_ThenGetReturnsThatHeightInCm() {
        int expected = 111;
        shnUserConfiguration.setHeightInCm(expected);

        int actual = shnUserConfiguration.getHeightInCm();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenHeightInCmIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setHeightInCm(111);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenWeightInKgHasNotBeenSet_ThenItIsNull() {
        Double weightInKg = shnUserConfiguration.getWeightInKg();

        assertThat(weightInKg).isNull();
    }

    @Test
    public void whenWeightInKgHasBeenSet_ThenGetReturnsThatWeightInKg() {
        double expected = 111;
        shnUserConfiguration.setWeightInKg(expected);

        Double actual = shnUserConfiguration.getWeightInKg();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenWeightInKgIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setWeightInKg(111.0);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenDecimalSeparatorHasNotBeenSet_ThenItIsDefault() {
        Character decimalSeparator = shnUserConfiguration.getDecimalSeparator();

        assertThat(decimalSeparator).isEqualTo(SHNUserConfigurationImpl.DEFAULT_DECIMAL_SEPARATOR);
    }

    @Test
    public void whenDecimalSeparatorHasBeenSet_ThenGetReturnsThatDecimalSeparator() {
        Character expected = 'T';
        shnUserConfiguration.setDecimalSeparator(expected);

        Character actual = shnUserConfiguration.getDecimalSeparator();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenDecimalSeparatorIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setDecimalSeparator('T');

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenHandednessHasNotBeenSet_ThenItIsUnknown() {
        SHNUserConfiguration.Handedness actualHandedness = shnUserConfiguration.getHandedness();

        assertThat(actualHandedness).isEqualTo(SHNUserConfiguration.Handedness.Unknown);
    }

    @Test
    public void whenHandednessHasBeenSet_ThenGetReturnsThatHandedness() {
        SHNUserConfiguration.Handedness expectedHandedness = SHNUserConfiguration.Handedness.RightHanded;
        shnUserConfiguration.setHandedness(expectedHandedness);

        SHNUserConfiguration.Handedness actualHandedness = shnUserConfiguration.getHandedness();

        assertThat(actualHandedness).isEqualTo(expectedHandedness);
    }

    @Test
    public void whenHandednessIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setHandedness(SHNUserConfiguration.Handedness.LeftHanded);

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenDateOfBirthHasNotBeenSet_ThenItIsNull() {
        Date actualDateOfBirth = shnUserConfiguration.getDateOfBirth();

        assertThat(actualDateOfBirth).isNull();
    }

    @Test
    public void whenDateOfBirthHasBeenSet_ThenGetReturnsThatDateOfBirth() {
        Date expected = new Date();
        shnUserConfiguration.setDateOfBirth(expected);

        Date actualDateOfBirth = shnUserConfiguration.getDateOfBirth();

        assertThat(actualDateOfBirth).isEqualTo(expected);
    }

    @Test
    public void whenDateOfBirthIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setDateOfBirth(new Date());

        verify(observerMock).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIncomingValueIsTheSameAsPersistedValue_ThenListenerIsNotInformed() {
        shnUserConfiguration.setClockFormat(SHNUserConfiguration.ClockFormat._12H);
        reset(observerMock);

        shnUserConfiguration.setClockFormat(SHNUserConfiguration.ClockFormat._12H);
        verify(observerMock, never()).update(shnUserConfiguration, null);
    }

    @Test
    public void whenIncomingValueIsNullAndPersistedValueIsNull_ThenListenerIsNotInformed() {
        shnUserConfiguration.setClockFormat(null);
        verify(observerMock, never()).update(shnUserConfiguration, null);
    }

    @Test
    public void whenMaxHeartRateAndAgeHasNotBeenSet_ThenMaxHeartRateIsNull() {
        when(calculationsMock.getMaxHeartRate(null, null)).thenReturn(null);
        Integer maxHeartRate = shnUserConfiguration.getMaxHeartRate();

        assertThat(maxHeartRate).isNull();
    }

    @Test
    public void whenMaxHeartRateAndAgeHasBeenSet_ThenItIsCalculated() {
        Date dateOfBirth = new Date();
        int expectedAge = 111;
        int expectedMaxHeartRate = 222;

        shnUserConfiguration.setDateOfBirth(dateOfBirth);
        when(calculationsMock.getAge(dateOfBirth)).thenReturn(expectedAge);
        when(calculationsMock.getMaxHeartRate(null, expectedAge)).thenReturn(expectedMaxHeartRate);

        Integer maxHeartRate = shnUserConfiguration.getMaxHeartRate();

        assertThat(maxHeartRate).isEqualTo(expectedMaxHeartRate);
    }

    @Test
    public void whenMaxHeartRateHasBeenSet_ThenGetReturnsThatMaxHeartRate() {
        int expected = 111;
        shnUserConfiguration.setMaxHeartRate(expected);

        when(calculationsMock.getMaxHeartRate(expected, null)).thenReturn(expected);
        int actual = shnUserConfiguration.getMaxHeartRate();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenMaxHeartRateAndAgeHasBeenSet_ThenGetReturnsThatMaxHeartRate() {
        int expectedMaxHeartRate = 222;
        shnUserConfiguration.setMaxHeartRate(expectedMaxHeartRate);
        Date dateOfBirth = new Date();
        int expectedAge = 111;

        shnUserConfiguration.setDateOfBirth(dateOfBirth);
        when(calculationsMock.getAge(dateOfBirth)).thenReturn(expectedAge);
        when(calculationsMock.getMaxHeartRate(expectedMaxHeartRate, expectedAge)).thenReturn(expectedMaxHeartRate);
        int actual = shnUserConfiguration.getMaxHeartRate();

        assertThat(actual).isEqualTo(expectedMaxHeartRate);
    }

    @Test
    public void whenBaseMetabolicRateIsAsked_ThenCalculationIsDispatched() {

        double weightInKg = 111.0;
        int age = 333;
        int heightInCm = 222;
        SHNUserConfiguration.Sex sex = SHNUserConfiguration.Sex.Female;
        Date dateOfBirth = new Date();
        int expectedBaseMetabolicRate = 444;

        shnUserConfiguration.setWeightInKg(weightInKg);
        shnUserConfiguration.setHeightInCm(heightInCm);
        shnUserConfiguration.setSex(sex);
        shnUserConfiguration.setDateOfBirth(dateOfBirth);
        when(calculationsMock.getAge(dateOfBirth)).thenReturn(age);
        when(calculationsMock.getBaseMetabolicRate(weightInKg, heightInCm, age, sex)).thenReturn(expectedBaseMetabolicRate);

        int actualBaseMetabolicRate = shnUserConfiguration.getBaseMetabolicRate();

        assertThat(actualBaseMetabolicRate).isEqualTo(expectedBaseMetabolicRate);
    }

    @Test
    public void whenMaxHeartRateIsSet_ThenListenerIsNotified() {
        shnUserConfiguration.setMaxHeartRate(111);

        verify(observerMock).update(shnUserConfiguration, null);
    }
}