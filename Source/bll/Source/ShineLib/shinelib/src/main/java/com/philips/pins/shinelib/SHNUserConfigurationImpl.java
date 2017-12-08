/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;

import java.util.Date;
import java.util.Locale;
import java.util.Observable;

public class SHNUserConfigurationImpl extends Observable implements SHNUserConfiguration {

    public static final String CLOCK_FORMAT_KEY = "CLOCK_FORMAT_KEY";
    public static final String DATE_OF_BIRTH_KEY = "DATE_OF_BIRTH_KEY";
    public static final String DECIMAL_SEPARATOR_KEY = "DECIMAL_SEPARATOR_KEY";
    public static final String HANDEDNESS_KEY = "HANDEDNESS_KEY";
    public static final String HEIGHT_IN_CM_KEY = "HEIGHT_IN_CM_KEY";
    public static final String ISO_LANGUAGE_CODE_KEY = "ISO_LANGUAGE_CODE_KEY";
    public static final String ISO_COUNTRY_CODE_KEY = "ISO_COUNTRY_CODE_KEY";
    public static final String MAX_HEART_RATE_KEY = "MAX_HEART_RATE_KEY";
    public static final String RESTING_HEART_RATE_KEY = "RESTING_HEART_RATE_KEY";
    public static final String SEX_KEY = "SEX_KEY";
    public static final String USE_METRIC_SYSTEM_KEY = "USE_METRIC_SYSTEM_KEY";
    public static final String WEIGHT_IN_KG_KEY = "WEIGHT_IN_KG_KEY";

    public static final String CHANGE_INCREMENT_KEY = "CHANGE_INCREMENT_KEY";

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

    @NonNull
    private final Handler internalHandler;
    private final SHNUserConfigurationCalculations userConfigurationCalculations;
    private ClockFormat clockFormat;
    private String isoLanguageCode;
    private String isoCountryCode;
    private Boolean useMetricSystem;
    private Sex sex;
    private Integer restingHeartRate;
    private Integer heightInCm;
    private Double weightInKg;
    private Handedness handedness;
    private Character decimalSeparator;
    private Date dateOfBirth;
    private Integer maxHeartRate;
    private int changeIncrement;

    public SHNUserConfigurationImpl(@NonNull final PersistentStorageFactory persistentStorageFactory, @NonNull final Handler internalHandler, final SHNUserConfigurationCalculations userConfigurationCalculations) {
        this.persistentStorage = persistentStorageFactory.getPersistentStorageForUser();
        this.internalHandler = internalHandler;
        this.userConfigurationCalculations = userConfigurationCalculations;

        initFromStorage();
    }

    private void initFromStorage() {
        clockFormat = readClockFormat();
        isoLanguageCode = readIsoLanguageCode();
        isoCountryCode = readIsoCountryCode();
        useMetricSystem = readUseMetricSystem();
        sex = readSex();
        restingHeartRate = readRestingHeartRate();
        heightInCm = readHeightInCm();
        weightInKg = readWeightInKg();
        handedness = readHandedness();
        decimalSeparator = readDecimalSeparator();
        dateOfBirth = readDateOfBirth();
        maxHeartRate = readMaxHeartRate();
        changeIncrement = readChangeIncrement();
    }

    @Override
    public synchronized void clear() {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                persistentStorage.clear();
                changeIncrement = 0;
                putChangeIncrementAndNotifyListeners(changeIncrement);
            }
        });
    }

    @Nullable
    @Override
    public synchronized ClockFormat getClockFormat() {
        return clockFormat;
    }

    @Override
    public synchronized void setClockFormat(@NonNull final ClockFormat clockFormat) {
        if (!isEqualTo(this.clockFormat, clockFormat)) {
            this.clockFormat = clockFormat;
            putChangedValueAndChangeIncrementOnInternalThread(CLOCK_FORMAT_KEY, clockFormat);
        }
    }

    @Nullable
    private ClockFormat readClockFormat() {
        return persistentStorage.get(CLOCK_FORMAT_KEY, DEFAULT_CLOCK_FORMAT);
    }

    @NonNull
    @Override
    public synchronized String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    @Override
    public synchronized void setIsoLanguageCode(String isoLanguageCode) {
        if (!isEqualTo(this.isoLanguageCode, isoLanguageCode)) {
            this.isoLanguageCode = isoLanguageCode;
            putChangedValueAndChangeIncrementOnInternalThread(ISO_LANGUAGE_CODE_KEY, isoLanguageCode);
        }
    }

    @NonNull
    private String readIsoLanguageCode() {
        return persistentStorage.get(ISO_LANGUAGE_CODE_KEY, Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public synchronized String getIsoCountryCode() {
        return isoCountryCode;
    }

    @Override
    public synchronized void setIsoCountryCode(final String isoCountryCode) {
        if (!isEqualTo(this.isoCountryCode, isoCountryCode)) {
            this.isoCountryCode = isoCountryCode;
            putChangedValueAndChangeIncrementOnInternalThread(ISO_COUNTRY_CODE_KEY, isoCountryCode);
        }
    }

    @NonNull
    private String readIsoCountryCode() {
        return persistentStorage.get(ISO_COUNTRY_CODE_KEY, Locale.getDefault().getCountry());
    }

    @NonNull
    @Override
    public synchronized Boolean getUseMetricSystem() {
        return useMetricSystem;
    }

    @Override
    public synchronized void setUseMetricSystem(Boolean useMetricSystem) {
        if (!isEqualTo(this.useMetricSystem, useMetricSystem)) {
            this.useMetricSystem = useMetricSystem;
            putChangedValueAndChangeIncrementOnInternalThread(USE_METRIC_SYSTEM_KEY, useMetricSystem);
        }
    }

    @NonNull
    private Boolean readUseMetricSystem() {
        return persistentStorage.get(USE_METRIC_SYSTEM_KEY, DEFAULT_USE_METRIC_SYSTEM);
    }

    @NonNull
    @Override
    public synchronized Sex getSex() {
        return sex;
    }

    @Override
    public synchronized void setSex(Sex sex) {
        if (!isEqualTo(this.sex, sex)) {
            this.sex = sex;
            putChangedValueAndChangeIncrementOnInternalThread(SEX_KEY, sex);
        }
    }

    @NonNull
    private Sex readSex() {
        return persistentStorage.get(SEX_KEY, DEFAULT_SEX);
    }

    @NonNull
    @Override
    public synchronized Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    @Override
    public synchronized void setRestingHeartRate(Integer restingHeartRate) {
        if (!isEqualTo(this.restingHeartRate, restingHeartRate)) {
            this.restingHeartRate = restingHeartRate;
            putChangedValueAndChangeIncrementOnInternalThread(RESTING_HEART_RATE_KEY, restingHeartRate);
        }
    }

    @NonNull
    private Integer readRestingHeartRate() {
        return persistentStorage.get(RESTING_HEART_RATE_KEY, DEFAULT_RESTING_HEART_RATE);
    }

    @Nullable
    @Override
    public synchronized Integer getHeightInCm() {
        return heightInCm;
    }

    @Override
    public synchronized void setHeightInCm(Integer heightInCm) {
        if (!isEqualTo(this.heightInCm, heightInCm)) {
            this.heightInCm = heightInCm;
            putChangedValueAndChangeIncrementOnInternalThread(HEIGHT_IN_CM_KEY, heightInCm);
        }
    }

    @Nullable
    private Integer readHeightInCm() {
        return persistentStorage.get(HEIGHT_IN_CM_KEY, DEFAULT_HEIGHT_IN_CM);
    }

    @Nullable
    @Override
    public synchronized Double getWeightInKg() {
        return weightInKg;
    }

    @Override
    public synchronized void setWeightInKg(Double weightInKg) {
        if (!isEqualTo(this.weightInKg, weightInKg)) {
            this.weightInKg = weightInKg;
            putChangedValueAndChangeIncrementOnInternalThread(WEIGHT_IN_KG_KEY, weightInKg);
        }
    }

    @Nullable
    private Double readWeightInKg() {
        return persistentStorage.get(WEIGHT_IN_KG_KEY, DEFAULT_WEIGHT_IN_KG);
    }

    @NonNull
    @Override
    public synchronized Handedness getHandedness() {
        return handedness;
    }

    @Override
    public synchronized void setHandedness(Handedness handedness) {
        handedness = (handedness == null ? DEFAULT_HANDEDNESS : handedness);
        if (!isEqualTo(this.handedness, handedness)) {
            this.handedness = handedness;
            putChangedValueAndChangeIncrementOnInternalThread(HANDEDNESS_KEY, handedness);
        }
    }

    @NonNull
    private Handedness readHandedness() {
        return persistentStorage.get(HANDEDNESS_KEY, Handedness.Unknown);
    }

    @NonNull
    @Override
    public synchronized Character getDecimalSeparator() {
        return decimalSeparator;
    }

    @Override
    public synchronized void setDecimalSeparator(Character decimalSeparator) {
        decimalSeparator = decimalSeparator == null ? DEFAULT_DECIMAL_SEPARATOR : decimalSeparator;
        if (!isEqualTo(this.decimalSeparator, decimalSeparator)) {
            this.decimalSeparator = decimalSeparator;
            putChangedValueAndChangeIncrementOnInternalThread(DECIMAL_SEPARATOR_KEY, (int) decimalSeparator);
        }
    }

    @NonNull
    private Character readDecimalSeparator() {
        int numericValue = persistentStorage.get(DECIMAL_SEPARATOR_KEY, (int) DEFAULT_DECIMAL_SEPARATOR);
        return (char) numericValue;
    }

    @Nullable
    @Override
    public synchronized Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public synchronized void setDateOfBirth(Date dateOfBirth) {
        if (!isEqualTo(this.dateOfBirth, dateOfBirth)) {
            this.dateOfBirth = dateOfBirth;
            putChangedValueAndChangeIncrementOnInternalThread(DATE_OF_BIRTH_KEY, (dateOfBirth == null ? 0L : dateOfBirth.getTime()));
            maxHeartRate = userConfigurationCalculations.getMaxHeartRate(maxHeartRate, getAge());
        }
    }

    @Nullable
    private Date readDateOfBirth() {
        long millis = persistentStorage.get(DATE_OF_BIRTH_KEY, 0L);
        return (millis == 0 ? DEFAULT_DATE_OF_BIRTH : new Date(millis));
    }

    @Nullable
    @Override
    public synchronized Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    @Override
    public synchronized void setMaxHeartRate(Integer maxHeartRate) {
        if (!isEqualTo(this.maxHeartRate, maxHeartRate)) {
            this.maxHeartRate = maxHeartRate;
            putChangedValueAndChangeIncrementOnInternalThread(MAX_HEART_RATE_KEY, maxHeartRate);
        }
    }

    @Nullable
    private Integer readMaxHeartRate() {
        Integer maxHeartRate = persistentStorage.get(MAX_HEART_RATE_KEY, DEFAULT_MAX_HEART_RATE);
        maxHeartRate = userConfigurationCalculations.getMaxHeartRate(maxHeartRate, getAge());
        return maxHeartRate;
    }

    private <T> void putChangedValueAndChangeIncrementOnInternalThread(final String key, @NonNull final T value) {
        final int changeIncrement = ++this.changeIncrement;
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                persistentStorage.put(key, value);
                putChangeIncrementAndNotifyListeners(changeIncrement);
            }
        });
    }

    private void putChangeIncrementAndNotifyListeners(int changeIncrement) {
        persistentStorage.put(CHANGE_INCREMENT_KEY, changeIncrement);
        setChanged();
        notifyObservers();
    }

    private boolean isEqualTo(Object object1, Object object2) {
        if (object1 == null && object2 == null) {
            return true;
        } else if (object1 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    @Nullable
    @Override
    public synchronized Integer getAge() {
        Date dateOfBirth = getDateOfBirth();
        if (dateOfBirth == null) {
            return null;
        }

        return userConfigurationCalculations.getAge(dateOfBirth);
    }

    @Nullable
    @Override
    public synchronized Integer getBaseMetabolicRate() {
        Integer age = getAge();
        Sex sex = getSex();
        Double weightInKg = getWeightInKg();
        Integer heightInCm = getHeightInCm();

        if (heightInCm != null && weightInKg != null && age != null && sex != Sex.Unspecified) {
            return userConfigurationCalculations.getBaseMetabolicRate(weightInKg, heightInCm, age, sex);
        } else {
            return null;
        }
    }

    public synchronized int getChangeIncrement() {
        return changeIncrement;
    }

    private int readChangeIncrement() {
        return persistentStorage.get(CHANGE_INCREMENT_KEY, 0);
    }
}