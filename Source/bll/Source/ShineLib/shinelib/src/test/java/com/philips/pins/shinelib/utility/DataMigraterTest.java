/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.RobolectricTest;
import com.philips.pins.shinelib.SHNDeviceAssociationHelper;
import com.philips.pins.shinelib.SHNUserConfiguration;
import com.philips.pins.shinelib.SHNUserConfigurationCalculations;
import com.philips.pins.shinelib.SHNUserConfigurationImpl;
import com.philips.pins.shinelib.SharedPreferencesProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataMigraterTest extends RobolectricTest {

    public static final String TEST_DEVICE_1 = "TEST_DEVICE_1";
    public static final String TEST_DEVICE_2 = "TEST_DEVICE_2";
    public static final String TEST_DEVICE_3 = "TEST_DEVICE_3";
    public static final String KEY_1 = "KEY_1";
    public static final String KEY_2 = "KEY_2";
    public static final String VALUE_1_STRING = "VALUE_1_STRING";
    public static final int VALUE_2_INT = 222;

    public static final SHNUserConfiguration.ClockFormat CLOCK_FORMAT = SHNUserConfiguration.ClockFormat._12H;
    public static final Date DATE_OF_BIRTH = new Date();
    public static final char DECIMAL_SEPARATOR = 'd';
    public static final SHNUserConfiguration.Handedness HANDEDNESS = SHNUserConfiguration.Handedness.MixedHanded;
    public static final int HEIGHT_IN_CM = 23;
    public static final String ISO_COUNTRY_CODE = "cou";
    public static final String ISO_LANGUAGE_CODE = "lan";
    public static final int RESTING_HEART_RATE = 222;
    public static final SHNUserConfiguration.Sex SEX = SHNUserConfiguration.Sex.Male;
    public static final double WEIGHT_IN_KG = 111.0;
    public static final boolean USE_METRIC_SYSTEM = true;
    public static final int MAX_HEART_RATE = 555;

    private DataMigrater dataMigrater;

    @Mock
    private Handler handlerMock;
    public static final String ANOTHER_KEY = "ANOTHER_KEY";
    private Application context;
    private PersistentStorageFactory storageFactory;

    @Before
    public void setUp() {
        initMocks(this);

        context = RuntimeEnvironment.application;
        storageFactory = new PersistentStorageFactory(new SharedPreferencesProvider() {
            @Override
            public SharedPreferences getSharedPreferences(String key, int mode) {
                return context.getSharedPreferences(key, Context.MODE_PRIVATE);
            }

            @NonNull
            @Override
            public String getSharedPreferencesPrefix() {
                return "TEST";
            }
        });

        dataMigrater = new DataMigrater();
    }

    @Test
    public void ShouldMoveShineData_WhenNoDeviceDataPresent() {
        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferencesNames.get(1), Context.MODE_PRIVATE));
        insertTestData(oldRootStorage);

        dataMigrater.execute(context, storageFactory);

        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();
        verifyTestData(newRootStorage);
    }

    @Test
    public void ShouldMoveNoData_WhenNoShineOrDeviceDataPresent() {
        dataMigrater.execute(context, storageFactory);

        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();
        assertThat(newRootStorage.getAll()).hasSize(1);
        assertThat(newRootStorage.getAll().get(DataMigrater.MIGRATION_ID_KEY)).isEqualTo(DataMigrater.MIGRATION_ID);
    }

    @Test
    public void ShouldMoveShineAndDeviceData_WhenPresent() {
        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferencesNames.get(0), Context.MODE_PRIVATE));
        PersistentStorage oldDevice1Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_1 + DataMigrater.oldDevicePreferencesSuffixes.get(0), Context.MODE_PRIVATE));
        PersistentStorage oldDevice2Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_2 + DataMigrater.oldDevicePreferencesSuffixes.get(1), Context.MODE_PRIVATE));
        PersistentStorage oldDevice3Storage = new PersistentStorage(context.getSharedPreferences(TEST_DEVICE_3 + DataMigrater.oldDevicePreferencesSuffixes.get(0), Context.MODE_PRIVATE));

        insertTestData(oldRootStorage);
        insertTestData(oldDevice1Storage);
        insertTestData(oldDevice2Storage);
        insertTestData(oldDevice3Storage);

        Set<String> deviceAddresses = new HashSet<>();
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_1);
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_2);
        deviceAddresses.add(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES + TEST_DEVICE_3);

        oldRootStorage.put(SHNDeviceAssociationHelper.ASSOCIATED_DEVICES, deviceAddresses);

        dataMigrater.execute(context, storageFactory);

        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();
        PersistentStorage newDevice1Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_1);
        PersistentStorage newDevice2Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_2);
        PersistentStorage newDevice3Storage = storageFactory.getPersistentStorageForDevice(TEST_DEVICE_3);

        verifyTestData(newRootStorage);
        verifyTestData(newDevice1Storage);
        verifyTestData(newDevice2Storage);
        verifyTestData(newDevice3Storage);

        assertThat(oldRootStorage.getAll()).isEmpty();
        assertThat(oldDevice1Storage.getAll()).isEmpty();
        assertThat(oldDevice2Storage.getAll()).isEmpty();
        assertThat(oldDevice3Storage.getAll()).isEmpty();
    }

    @Test
    public void ShouldMoveUserData_WhenOldUserDataIsPresent() {
        PersistentStorage oldUserStorage0 = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldUserPreferencesNames.get(0), Context.MODE_PRIVATE));
        PersistentStorage oldUserStorage1 = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldUserPreferencesNames.get(1), Context.MODE_PRIVATE));

        insertTestData(oldUserStorage0);
        oldUserStorage1.put(ANOTHER_KEY, true);

        dataMigrater.execute(context, storageFactory);

        PersistentStorage newUserStorage = storageFactory.getPersistentStorageForUser();
        verifyTestData(newUserStorage);
        assertThat(newUserStorage.get(ANOTHER_KEY)).isEqualTo(true);
    }

    @Test
    public void ShouldMoveOldUserKeysFromRootToUserStorage_WhenOldUserDataIsPresent() {
        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferencesNames.get(0), Context.MODE_PRIVATE));
        OldSHNUserConfigurationImpl oldUserConfiguration = new OldSHNUserConfigurationImpl(oldRootStorage, new SHNUserConfigurationCalculations());

        insertUserData(oldUserConfiguration);

        dataMigrater.execute(context, storageFactory);

        SHNUserConfigurationImpl newUserConfiguration = new SHNUserConfigurationImpl(storageFactory, handlerMock, new SHNUserConfigurationCalculations());
        verifyUserData(newUserConfiguration);
        for (final String key : DataMigrater.userKeyMapping.keySet()) {
            assertThat(oldRootStorage.get(key)).isNull();
        }
    }

    @Test
    public void ShouldMoveOldUserKeysFromUserToUserStorage_WhenOldUserDataIsPresent() {
        PersistentStorage oldUserStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldUserPreferencesNames.get(0), Context.MODE_PRIVATE));
        OldSHNUserConfigurationImpl oldUserConfiguration = new OldSHNUserConfigurationImpl(oldUserStorage, new SHNUserConfigurationCalculations());

        insertUserData(oldUserConfiguration);

        dataMigrater.execute(context, storageFactory);

        SHNUserConfigurationImpl newUserConfiguration = new SHNUserConfigurationImpl(storageFactory, handlerMock, new SHNUserConfigurationCalculations());
        verifyUserData(newUserConfiguration);
        for (final String key : DataMigrater.userKeyMapping.keySet()) {
            assertThat(oldUserStorage.get(key)).isNull();
        }
    }

    @Test
    public void ShouldOnlyMigrateOnce_WhenCalledMoreThanOnce() {
        PersistentStorage oldRootStorage = new PersistentStorage(context.getSharedPreferences(DataMigrater.oldShinePreferencesNames.get(1), Context.MODE_PRIVATE));
        insertTestData(oldRootStorage);

        dataMigrater.execute(context, storageFactory);
        PersistentStorage newRootStorage = storageFactory.getPersistentStorage();

        verifyTestData(newRootStorage);

        oldRootStorage.put(ANOTHER_KEY, true);
        dataMigrater.execute(context, storageFactory);
        assertThat(newRootStorage.contains(ANOTHER_KEY)).isFalse();
    }

    @Test
    public void ShouldConvert_DecimalSeparator_FromOldToNewValue_WhenMigrating() {
        PersistentStorage userStorage = storageFactory.getPersistentStorageForUser();
        userStorage.put(SHNUserConfigurationImpl.DECIMAL_SEPARATOR_KEY, "A");

        dataMigrater.execute(context, storageFactory);

        char actualSeparator = (char) (int) userStorage.get(SHNUserConfigurationImpl.DECIMAL_SEPARATOR_KEY);
        assertThat(actualSeparator).isEqualTo('A');
    }

    @Test
    public void ShouldConvert_Sex_FromOldToNewValue_WhenMigrating() {
        PersistentStorage userStorage = storageFactory.getPersistentStorageForUser();
        SHNUserConfiguration.Sex expectedSex = SHNUserConfiguration.Sex.Female;
        userStorage.put(SHNUserConfigurationImpl.SEX_KEY, expectedSex.name());

        dataMigrater.execute(context, storageFactory);

        SHNUserConfiguration.Sex actualSex = userStorage.get(SHNUserConfigurationImpl.SEX_KEY);
        assertThat(actualSex).isEqualTo(expectedSex);
    }

    @Test
    public void ShouldConvert_Handedness_FromOldToNewValue_WhenMigrating() {
        PersistentStorage userStorage = storageFactory.getPersistentStorageForUser();
        SHNUserConfiguration.Handedness expectedHandedness = SHNUserConfiguration.Handedness.MixedHanded;
        userStorage.put(SHNUserConfigurationImpl.HANDEDNESS_KEY, expectedHandedness.name());

        dataMigrater.execute(context, storageFactory);

        SHNUserConfiguration.Handedness actualHandedness = userStorage.get(SHNUserConfigurationImpl.HANDEDNESS_KEY);
        assertThat(actualHandedness).isEqualTo(expectedHandedness);
    }

    @Test
    public void ShouldConvert_Weight_FromOldToNewValue_WhenMigrating() {
        PersistentStorage userStorage = storageFactory.getPersistentStorageForUser();
        float expectedWeight = 7.12f;
        userStorage.put(SHNUserConfigurationImpl.WEIGHT_IN_KG_KEY, expectedWeight);

        dataMigrater.execute(context, storageFactory);

        double actualWeight = userStorage.get(SHNUserConfigurationImpl.WEIGHT_IN_KG_KEY);
        assertThat(actualWeight).isEqualTo((double) expectedWeight);
    }

    // -----

    private void verifyTestData(final PersistentStorage storage) {
        assertThat(storage.get(KEY_1)).isEqualTo(VALUE_1_STRING);
        assertThat(storage.get(KEY_2)).isEqualTo(VALUE_2_INT);
    }

    private void insertTestData(final PersistentStorage storage) {
        storage.put(KEY_1, VALUE_1_STRING);
        storage.put(KEY_2, VALUE_2_INT);
    }

    private void verifyUserData(final SHNUserConfigurationImpl newUserConfiguration) {
        assertThat(newUserConfiguration.getClockFormat()).isEqualTo(SHNUserConfiguration.ClockFormat._12H);
        assertThat(newUserConfiguration.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);
        assertThat(newUserConfiguration.getDecimalSeparator()).isEqualTo(DECIMAL_SEPARATOR);
        assertThat(newUserConfiguration.getHandedness()).isEqualTo(HANDEDNESS);
        assertThat(newUserConfiguration.getHeightInCm()).isEqualTo(HEIGHT_IN_CM);
        assertThat(newUserConfiguration.getIsoCountryCode()).isEqualTo(ISO_COUNTRY_CODE);
        assertThat(newUserConfiguration.getIsoLanguageCode()).isEqualTo(ISO_LANGUAGE_CODE);
        assertThat(newUserConfiguration.getMaxHeartRate()).isEqualTo(MAX_HEART_RATE);
        assertThat(newUserConfiguration.getRestingHeartRate()).isEqualTo(RESTING_HEART_RATE);
        assertThat(newUserConfiguration.getSex()).isEqualTo(SEX);
        assertThat(newUserConfiguration.getWeightInKg()).isEqualTo(WEIGHT_IN_KG);
        assertThat(newUserConfiguration.getUseMetricSystem()).isEqualTo(USE_METRIC_SYSTEM);
    }

    private void insertUserData(final OldSHNUserConfigurationImpl oldUserConfiguration) {
        oldUserConfiguration.setClockFormat(CLOCK_FORMAT);
        oldUserConfiguration.setDateOfBirth(DATE_OF_BIRTH);
        oldUserConfiguration.setDecimalSeparator(DECIMAL_SEPARATOR);
        oldUserConfiguration.setHandedness(HANDEDNESS);
        oldUserConfiguration.setHeightInCm(HEIGHT_IN_CM);
        oldUserConfiguration.setIsoCountryCode(ISO_COUNTRY_CODE);
        oldUserConfiguration.setIsoLanguageCode(ISO_LANGUAGE_CODE);
        oldUserConfiguration.setRestingHeartRate(RESTING_HEART_RATE);
        oldUserConfiguration.setSex(SEX);
        oldUserConfiguration.setWeightInKg(WEIGHT_IN_KG);
        oldUserConfiguration.setUseMetricSystem(USE_METRIC_SYSTEM);
        oldUserConfiguration.setMaxHeartRate(MAX_HEART_RATE);
    }

    private static class OldSHNUserConfigurationImpl implements SHNUserConfiguration {

        public static final String CLOCK_FORMAT_KEY = "ClockFormat";
        public static final String DATE_OF_BIRTH_KEY = "USER_CONFIG_DATE_OF_BIRTH";
        public static final String DECIMAL_SEPARATOR_KEY = "USER_CONFIG_DECIMAL_SEPARATOR";
        public static final String HANDEDNESS_KEY = "USER_CONFIG_HANDEDNESS";
        public static final String HEIGHT_IN_CM_KEY = "USER_CONFIG_HEIGHT_IN_CM";
        public static final String ISO_LANGUAGE_CODE_KEY = "USER_CONFIG_ISO_LANGUAGE_CODE";
        public static final String ISO_COUNTRY_CODE_KEY = "ISO_COUNTRY_CODE_KEY";
        public static final String MAX_HEART_RATE_KEY = "USER_CONFIG_MAX_HEART_RATE";
        public static final String RESTING_HEART_RATE_KEY = "USER_CONFIG_RESTING_HEART_RATE";
        public static final String SEX_KEY = "USER_CONFIG_SEX";
        public static final String USE_METRIC_SYSTEM_KEY = "USER_CONFIG_USE_METRIC_SYSTEM";
        public static final String WEIGHT_IN_KG_KEY = "USER_CONFIG_WEIGHT_IN_KG";

        public static final String CHANGE_INCREMENT_KEY = "USER_CONFIG_INCREMENT";

        public static final char DEFAULT_DECIMAL_SEPARATOR = '.';
        public static final Handedness DEFAULT_HANDEDNESS = Handedness.Unknown;
        public static final Double DEFAULT_WEIGHT_IN_KG = null;
        public static final Integer DEFAULT_HEIGHT_IN_CM = null;
        public static final int DEFAULT_RESTING_HEART_RATE = -1;
        public static final Sex DEFAULT_SEX = Sex.Unspecified;
        public static final boolean DEFAULT_USE_METRIC_SYSTEM = true;
        public static final ClockFormat DEFAULT_CLOCK_FORMAT = null;
        public static final Date DEFAULT_DATE_OF_BIRTH = null;
        public static final Integer DEFAULT_MAX_HEART_RATE = null;

        @NonNull
        private final PersistentStorage persistentStorage;
        private SHNUserConfigurationCalculations userConfigurationCalculations;

        public OldSHNUserConfigurationImpl(PersistentStorage persistentStorage, final SHNUserConfigurationCalculations userConfigurationCalculations) {
            this.persistentStorage = persistentStorage;
            this.userConfigurationCalculations = userConfigurationCalculations;
        }

        @Override
        public ClockFormat getClockFormat() {
            return persistentStorage.get(CLOCK_FORMAT_KEY, DEFAULT_CLOCK_FORMAT);
        }

        @Override
        public void setClockFormat(@NonNull final ClockFormat clockFormat) {
            putValueIfChanged(CLOCK_FORMAT_KEY, clockFormat);
        }

        @Override
        public synchronized String getIsoLanguageCode() {
            return persistentStorage.get(ISO_LANGUAGE_CODE_KEY, Locale.getDefault().getLanguage());
        }

        @Override
        public synchronized void setIsoLanguageCode(String isoLanguageCode) {
            putValueIfChanged(ISO_LANGUAGE_CODE_KEY, isoLanguageCode);
        }

        @Override
        public String getIsoCountryCode() {
            return persistentStorage.get(ISO_COUNTRY_CODE_KEY, Locale.getDefault().getCountry());
        }

        @Override
        public void setIsoCountryCode(final String isoCountryCode) {
            putValueIfChanged(ISO_COUNTRY_CODE_KEY, isoCountryCode);
        }

        @Override
        public synchronized Boolean getUseMetricSystem() {
            return persistentStorage.get(USE_METRIC_SYSTEM_KEY, DEFAULT_USE_METRIC_SYSTEM);
        }

        @Override
        public synchronized void setUseMetricSystem(Boolean useMetricSystem) {
            putValueIfChanged(USE_METRIC_SYSTEM_KEY, useMetricSystem);
        }

        @Override
        public synchronized Sex getSex() {
            return persistentStorage.get(SEX_KEY, DEFAULT_SEX);
        }

        @Override
        public synchronized void setSex(Sex sex) {
            putValueIfChanged(SEX_KEY, sex);
        }

        @Override
        public synchronized Integer getRestingHeartRate() {
            return persistentStorage.get(RESTING_HEART_RATE_KEY, DEFAULT_RESTING_HEART_RATE);
        }

        @Override
        public synchronized void setRestingHeartRate(Integer restingHeartRate) {
            putValueIfChanged(RESTING_HEART_RATE_KEY, restingHeartRate);
        }

        @Override
        public synchronized Integer getHeightInCm() {
            return persistentStorage.get(HEIGHT_IN_CM_KEY, DEFAULT_HEIGHT_IN_CM);
        }

        @Override
        public synchronized void setHeightInCm(Integer heightInCm) {
            putValueIfChanged(HEIGHT_IN_CM_KEY, heightInCm);
        }

        @Override
        public synchronized Double getWeightInKg() {
            return persistentStorage.get(WEIGHT_IN_KG_KEY, DEFAULT_WEIGHT_IN_KG);
        }

        @Override
        public synchronized void setWeightInKg(Double weightInKg) {
            putValueIfChanged(WEIGHT_IN_KG_KEY, weightInKg);
        }

        @Override
        public synchronized Handedness getHandedness() {
            return persistentStorage.get(HANDEDNESS_KEY, Handedness.Unknown);
        }

        @Override
        public synchronized void setHandedness(Handedness handedness) {
            putValueIfChanged(HANDEDNESS_KEY, (handedness == null ? DEFAULT_HANDEDNESS : handedness));
        }

        @Override
        public synchronized Character getDecimalSeparator() {
            int numericValue = persistentStorage.get(DECIMAL_SEPARATOR_KEY, (int) DEFAULT_DECIMAL_SEPARATOR);
            return (char) numericValue;
        }

        @Override
        public synchronized void setDecimalSeparator(Character decimalSeparator) {
            char dc = decimalSeparator == null ? DEFAULT_DECIMAL_SEPARATOR : decimalSeparator;
            putValueIfChanged(DECIMAL_SEPARATOR_KEY, (int) dc);
        }

        @Override
        public synchronized Date getDateOfBirth() {
            long millis = persistentStorage.get(DATE_OF_BIRTH_KEY, 0L);
            return (millis == 0 ? DEFAULT_DATE_OF_BIRTH : new Date(millis));
        }

        @Override
        public synchronized void setDateOfBirth(Date dateOfBirth) {
            putValueIfChanged(DATE_OF_BIRTH_KEY, (dateOfBirth == null ? 0L : dateOfBirth.getTime()));
        }

        @Override
        public synchronized Integer getMaxHeartRate() {
            Integer maxHeartRate = persistentStorage.get(MAX_HEART_RATE_KEY, DEFAULT_MAX_HEART_RATE);
            return userConfigurationCalculations.getMaxHeartRate(maxHeartRate, getAge());
        }

        @Override
        public synchronized void setMaxHeartRate(Integer maxHeartRate) {
            putValueIfChanged(MAX_HEART_RATE_KEY, maxHeartRate);
        }

        private <T> void putValueIfChanged(final String key, final T value) {
            if (!isEqualTo(persistentStorage.get(key), value)) {
                persistentStorage.put(key, value);
                incrementChangeIncrementAndNotifyModifiedListeners();
            }
        }

        private boolean isEqualTo(Object object1, Object object2) {
            if (object1 == null && object2 == null) {
                return true;
            } else if (object1 == null) {
                return false;
            }
            return object1.equals(object2);
        }

        private void incrementChangeIncrementAndNotifyModifiedListeners() {
            int changeIncrement = getChangeIncrement();
            changeIncrement++;
            persistentStorage.put(CHANGE_INCREMENT_KEY, changeIncrement);
        }

        public int getChangeIncrement() {
            return persistentStorage.get(CHANGE_INCREMENT_KEY, 0);
        }

        @Override
        public void clear() {
        }

        @Override
        public synchronized Integer getAge() {
            return null;
        }

        @Override
        public synchronized Integer getBaseMetabolicRate() {
            return null;
        }
    }
}