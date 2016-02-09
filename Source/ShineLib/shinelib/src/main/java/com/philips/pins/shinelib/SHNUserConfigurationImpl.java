/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.SHNPersistentStorage;
import com.philips.pins.shinelib.utility.SharedPreferencesHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

public class  SHNUserConfigurationImpl extends Observable implements SHNUserConfiguration {
    private static final String TAG = SHNUserConfigurationImpl.class.getSimpleName();


    public static final String CLOCK_FORMAT_KEY = "CLOCK_FORMAT_KEY";
    public static final String ISO_LANGUAGE_CODE_KEY = "ISO_LANGUAGE_CODE_KEY";
    public static final String ISO_COUNTRY_CODE_KEY = "ISO_COUNTRY_CODE_KEY";
    public static final String USE_METRIC_SYSTEM_KEY = "USE_METRIC_SYSTEM_KEY";


    /* package */ static final String USER_CONFIG_DATE_OF_BIRTH = "USER_CONFIG_DATE_OF_BIRTH";
    /* package */ static final String USER_CONFIG_MAX_HEART_RATE = "USER_CONFIG_MAX_HEART_RATE";
    /* package */ static final String USER_CONFIG_RESTING_HEART_RATE = "USER_CONFIG_RESTING_HEART_RATE";
    /* package */ static final String USER_CONFIG_WEIGHT_IN_KG = "USER_CONFIG_WEIGHT_IN_KG";
    /* package */ static final String USER_CONFIG_HEIGHT_IN_CM = "USER_CONFIG_HEIGHT_IN_CM";
    /* package */ static final String USER_CONFIG_SEX = "USER_CONFIG_SEX";
    /* package */ static final String USER_CONFIG_HANDEDNESS = "USER_CONFIG_HANDEDNESS";
    /* package */ static final String USER_CONFIG_ISO_LANGUAGE_CODE = "USER_CONFIG_ISO_LANGUAGE_CODE";
    /* package */ static final String USER_CONFIG_USE_METRIC_SYSTEM = "USER_CONFIG_USE_METRIC_SYSTEM";
    /* package */ static final String USER_CONFIG_DECIMAL_SEPARATOR = "USER_CONFIG_DECIMAL_SEPARATOR";
    /* package */ static final String USER_CONFIG_INCREMENT = "USER_CONFIG_INCREMENT";

    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final int DEFAULT_INT_VALUE = -1;
    private static final Boolean DEFAULT_USE_METRIC_SYSTEM = Boolean.TRUE;
    private static final float DEFAULT_FLOAT_VALUE = Float.NaN;
    private static final String DEFAULT_STRING_VALUE = null;
    private static final Boolean DEFAULT_BOOLEAN_VALUE = null;
    private static final Character DEFAULT_CHAR_VALUE = null;
    private static final Sex DEFAULT_SEX_VALUE = Sex.Unspecified;
    private static final Handedness DEFAULT_HANDEDNESS_VALUE = Handedness.Unknown;
    public static final char DEFAULT_DECIMAL_SEPARATOR = '.';

    @NonNull
    private final SharedPreferencesHelper sharedPreferences;

    @NonNull
    private final Handler internalHandler;

    private Sex sex = Sex.Unspecified;
    private Integer maxHeartRate;
    private Integer restingHeartRate;
    private Integer heightInCm;
    private Double weightInKg;
    private Date dateOfBirth;
    private Handedness handedness = Handedness.Unknown;
    private Character decimalSeparator;

    private int changeIncrement;

    public SHNUserConfigurationImpl(@NonNull final SHNPersistentStorage shnPersistentStorage, @NonNull final Handler internalHandler) {
        this.sharedPreferences = new SharedPreferencesHelper(shnPersistentStorage.getSharedPreferences());
        this.internalHandler = internalHandler;
        retrieveFromPreferences();
    }

    @Override
    public synchronized Sex getSex() {
        return sex;
    }

    @Override
    public synchronized void setSex(Sex sex) {
        if (sex == null) {
            sex = Sex.Unspecified;
        }
        if (this.sex != sex) {
            this.sex = sex;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Integer getMaxHeartRate() {
        Integer age = getAge();
        if (maxHeartRate == null && age != null) {
            return (int) (207 - (0.7 * age));
        }
        return maxHeartRate;
    }

    @Override
    public synchronized void setMaxHeartRate(Integer maxHeartRate) {
        if (!isEqualTo(this.maxHeartRate, maxHeartRate)) {
            this.maxHeartRate = maxHeartRate;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    @Override
    public synchronized void setRestingHeartRate(Integer restingHeartRate) {
        if (!isEqualTo(this.restingHeartRate, restingHeartRate)) {
            this.restingHeartRate = restingHeartRate;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Integer getHeightInCm() {
        return heightInCm;
    }

    @Override
    public synchronized void setHeightInCm(Integer heightInCm) {
        if (!isEqualTo(this.heightInCm, heightInCm)) {
            this.heightInCm = heightInCm;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Double getWeightInKg() {
        return weightInKg;
    }

    @Override
    public synchronized void setWeightInKg(Double weightInKg) {
        if (!isEqualTo(this.weightInKg, weightInKg)) {
            this.weightInKg = weightInKg;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public synchronized void setDateOfBirth(Date dateOfBirth) {
        if (!isEqualTo(this.dateOfBirth, dateOfBirth)) {
            this.dateOfBirth = dateOfBirth;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    /* return null when birthdate not set */
    @Override
    public synchronized Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }

        Calendar now = Calendar.getInstance();
        Calendar dateOfBirth = Calendar.getInstance();

        dateOfBirth.setTime(this.dateOfBirth);

        if (dateOfBirth.after(now)) {
            SHNLogger.e(TAG, "Can't be born in the future");
            return null;
        }

        int age = now.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        if (birthdayNotYetPassed(now, dateOfBirth)) {
            age--;
        }

        return age;
    }

    private boolean birthdayNotYetPassed(Calendar now, Calendar dateOfBirth) {
        return now.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    public synchronized Handedness getHandedness() {
        return handedness;
    }

    @Override
    public synchronized void setHandedness(Handedness handedness) {
        if (handedness == null) {
            handedness = Handedness.Unknown;
        }
        if (this.handedness != handedness) {
            this.handedness = handedness;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Character getDecimalSeparator() {
        return decimalSeparator;
    }

    @Override
    public synchronized void setDecimalSeparator(Character decimalSeparator) {
        if (decimalSeparator == null) {
            decimalSeparator = DEFAULT_DECIMAL_SEPARATOR;
        }
        if (!isEqualTo(this.decimalSeparator, decimalSeparator)) {
            this.decimalSeparator = decimalSeparator;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Integer getBaseMetabolicRate() {
        Integer result = null;
        Integer age = getAge();
        if (weightInKg != null && heightInCm != null && age != null && sex != null) {
            double heightInMeters = heightInCm / 100;
            double baseMetabolicRate = 0;

            if (age >= 10 && age < 18) {
                baseMetabolicRate = (sex == Sex.Male) ? (16.6 * weightInKg) + (77 * heightInMeters) + 572 : (7.4 * weightInKg) + (482 * heightInMeters) + 217;
            }
            if (age >= 18 && age < 30) {
                baseMetabolicRate = (sex == Sex.Male) ? (15.4 * weightInKg) - (27 * heightInMeters) + 717 : (13.3 * weightInKg) + (334 * heightInMeters) + 35;
            }
            if (age >= 30 && age < 60) {
                baseMetabolicRate = (sex == Sex.Male) ? (11.3 * weightInKg) + (16 * heightInMeters) + 901 : (8.7 * weightInKg) - (25 * heightInMeters) + 865;
            }
            if (age >= 60) {
                baseMetabolicRate = (sex == Sex.Male) ? (8.8 * weightInKg) + (1128 * heightInMeters) - 1071 : (9.2 * weightInKg) + (673 * heightInMeters) + 302;
            }
            result = (int) baseMetabolicRate;
        }
        return result;
    }

    private void setChangeIncrement(int changeIncrement) {
        this.changeIncrement = changeIncrement;
    }

    public int getChangeIncrement() {
        return changeIncrement;
    }

    private boolean isEqualTo(Object object1, Object object2) {
        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    private void incrementChangeIncrementAndNotifyModifiedListeners() {
        changeIncrement++;
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                saveToPreferences();
                setChanged();
                notifyObservers();
            }
        });
    }

    private synchronized void saveToPreferences() {
        SharedPreferences.Editor edit = sharedPreferences.edit();

        Date dateOfBirth = getDateOfBirth();
        updatePersistentStorage(edit, USER_CONFIG_DATE_OF_BIRTH, ((dateOfBirth == null) ? null : dateOfBirth.getTime()));
        updatePersistentStorage(edit, USER_CONFIG_HEIGHT_IN_CM, getHeightInCm());
        updatePersistentStorage(edit, USER_CONFIG_MAX_HEART_RATE, getMaxHeartRate());
        updatePersistentStorage(edit, USER_CONFIG_RESTING_HEART_RATE, getRestingHeartRate());

        Double weightInKg = getWeightInKg();
        updatePersistentStorage(edit, USER_CONFIG_WEIGHT_IN_KG, (weightInKg == null) ? null : (float) (double) getWeightInKg());

        Sex sex = getSex();
        updatePersistentStorage(edit, USER_CONFIG_SEX, (sex == null) ? null : sex.name());

        Handedness handedness = getHandedness();
        updatePersistentStorage(edit, USER_CONFIG_HANDEDNESS, (handedness == null) ? null : handedness.name());

        updatePersistentStorage(edit, USER_CONFIG_ISO_LANGUAGE_CODE, getIsoLanguageCode());
        updatePersistentStorage(edit, USER_CONFIG_USE_METRIC_SYSTEM, getUseMetricSystem());

        Character decimalSeparator = getDecimalSeparator();
        updatePersistentStorage(edit, USER_CONFIG_DECIMAL_SEPARATOR, (decimalSeparator == null) ? null : decimalSeparator.toString());

        edit.putInt(USER_CONFIG_INCREMENT, getChangeIncrement());

        edit.commit();
    }

    private void updatePersistentStorage(SharedPreferences.Editor edit, String key, String value) {
        if (value != null) {
            edit.putString(key, value);
        } else {
            edit.remove(key);
        }
    }

    private void updatePersistentStorage(SharedPreferences.Editor edit, String key, Integer value) {
        if (value != null) {
            edit.putInt(key, value);
        } else {
            edit.remove(key);
        }
    }

    private void updatePersistentStorage(SharedPreferences.Editor edit, String key, Long value) {
        if (value != null) {
            edit.putLong(key, value);
        } else {
            edit.remove(key);
        }
    }

    private void updatePersistentStorage(SharedPreferences.Editor edit, String key, Boolean value) {
        if (value != null) {
            edit.putBoolean(key, value);
        } else {
            edit.remove(key);
        }
    }

    private void updatePersistentStorage(SharedPreferences.Editor edit, String key, Float value) {
        if (value != null) {
            edit.putFloat(key, value);
        } else {
            edit.remove(key);
        }
    }

    private synchronized void retrieveFromPreferences() {
        dateOfBirth = readDateFromPersistentStorage(USER_CONFIG_DATE_OF_BIRTH);
        heightInCm = readIntegerFromPersistentStorage(USER_CONFIG_HEIGHT_IN_CM);
        maxHeartRate = readIntegerFromPersistentStorage(USER_CONFIG_MAX_HEART_RATE);
        restingHeartRate = readIntegerFromPersistentStorage(USER_CONFIG_RESTING_HEART_RATE);
        weightInKg = readDoubleFromPersistentStorage(USER_CONFIG_WEIGHT_IN_KG);
        sex = readSexFromPersistentStorage(USER_CONFIG_SEX);
        handedness = readHandednessFromPersistentStorage(USER_CONFIG_HANDEDNESS);
        decimalSeparator = readCharacterFromPersistentStorage(USER_CONFIG_DECIMAL_SEPARATOR);
        if (decimalSeparator == null) {
            decimalSeparator = DEFAULT_DECIMAL_SEPARATOR;
        }

        Integer index = readIntegerFromPersistentStorage(USER_CONFIG_INCREMENT);
        if (index == null) {
            index = 0;
        }
        setChangeIncrement(index);
    }

    private String getDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @Nullable
    private Character readCharacterFromPersistentStorage(String key) {
        String value = readStringFromPersistentStorage(key);
        if (value != null && value.length() > 0) {
            return value.charAt(0);
        }
        return DEFAULT_CHAR_VALUE;
    }

    @Nullable
    private Handedness readHandednessFromPersistentStorage(String key) {
        String stringValue = readStringFromPersistentStorage(key);
        if (stringValue != null) {
            return Handedness.valueOf(stringValue);
        }
        return DEFAULT_HANDEDNESS_VALUE;
    }

    @Nullable
    private Sex readSexFromPersistentStorage(String key) {
        String stringValue = readStringFromPersistentStorage(key);
        if (stringValue != null) {
            return Sex.valueOf(stringValue);
        }
        return DEFAULT_SEX_VALUE;
    }

    @Nullable
    private Date readDateFromPersistentStorage(String key) {
        long value = sharedPreferences.getLong(key, DEFAULT_LONG_VALUE);
        if (value != DEFAULT_LONG_VALUE) {
            return new Date(value);
        }
        return null;
    }

    @Nullable
    private Integer readIntegerFromPersistentStorage(String key) {
        int value = sharedPreferences.getInt(key, DEFAULT_INT_VALUE);
        if (value != DEFAULT_INT_VALUE) {
            return value;
        }
        return null;
    }

    @Nullable
    private Double readDoubleFromPersistentStorage(String key) {
        float value = sharedPreferences.getFloat(key, DEFAULT_FLOAT_VALUE);
        if (!Float.isNaN(value)) {
            return (double) value;
        }
        return null;
    }

    @Nullable
    private String readStringFromPersistentStorage(String key) {
        return sharedPreferences.getString(key, DEFAULT_STRING_VALUE);
    }

    @Nullable
    private Boolean readBooleanFromPersistentStorage(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, false);
        }
        return DEFAULT_BOOLEAN_VALUE;
    }

    @Override
    public ClockFormat getClockFormat() {
        return sharedPreferences.get(CLOCK_FORMAT_KEY);
    }

    @Override
    public void setClockFormat(@NonNull final ClockFormat clockFormat) {
        sharedPreferences.put(CLOCK_FORMAT_KEY, clockFormat);
        incrementChangeIncrementAndNotifyModifiedListeners();
    }

    @Override
    public synchronized String getIsoLanguageCode() {
        return sharedPreferences.get(ISO_LANGUAGE_CODE_KEY, Locale.getDefault().getLanguage());
    }

    @Override
    public synchronized void setIsoLanguageCode(String isoLanguageCode) {
        sharedPreferences.put(ISO_LANGUAGE_CODE_KEY, isoLanguageCode);
        incrementChangeIncrementAndNotifyModifiedListeners();
    }

    @Override
    public String getIsoCountryCode() {
        return sharedPreferences.get(ISO_COUNTRY_CODE_KEY, Locale.getDefault().getCountry());
    }

    @Override
    public void setIsoCountryCode(final String isoCountryCode) {
        sharedPreferences.put(ISO_COUNTRY_CODE_KEY, isoCountryCode);
        incrementChangeIncrementAndNotifyModifiedListeners();
    }

    @Override
    public synchronized Boolean getUseMetricSystem() {
        return sharedPreferences.get(USE_METRIC_SYSTEM_KEY, true);
    }

    @Override
    public synchronized void setUseMetricSystem(Boolean useMetricSystem) {
        sharedPreferences.put(USE_METRIC_SYSTEM_KEY, useMetricSystem);
        incrementChangeIncrementAndNotifyModifiedListeners();
    }
}
