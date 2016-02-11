/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;

import java.util.Date;
import java.util.Locale;
import java.util.Observable;

public class SHNUserConfigurationImpl extends Observable implements SHNUserConfiguration {

    public static final String CLOCK_FORMAT_KEY = "CLOCK_FORMAT_KEY";
    public static final String ISO_LANGUAGE_CODE_KEY = "ISO_LANGUAGE_CODE_KEY";
    public static final String ISO_COUNTRY_CODE_KEY = "ISO_COUNTRY_CODE_KEY";
    public static final String USE_METRIC_SYSTEM_KEY = "USE_METRIC_SYSTEM_KEY";
    public static final String SEX_KEY = "SEX_KEY";
    public static final String RESTING_HEART_RATE_KEY = "RESTING_HEART_RATE_KEY";
    public static final String HEIGHT_IN_CM_KEY = "HEIGHT_IN_CM_KEY";
    public static final String WEIGHT_IN_KG_KEY = "WEIGHT_IN_KG_KEY";
    public static final String DECIMAL_SEPARATOR_KEY = "DECIMAL_SEPARATOR_KEY";
    public static final String HANDEDNESS_KEY = "HANDEDNESS_KEY";
    public static final String DATE_OF_BIRTH_KEY = "DATE_OF_BIRTH_KEY";
    public static final String MAX_HEART_RATE_KEY = "MAX_HEART_RATE_KEY";
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

    public SHNUserConfigurationImpl(@NonNull final PersistentStorageFactory persistentStorageFactory, @NonNull final Handler internalHandler, final SHNUserConfigurationCalculations userConfigurationCalculations) {
        this.persistentStorage = persistentStorageFactory.getPersistentStorageForUser();
        this.internalHandler = internalHandler;
        this.userConfigurationCalculations = userConfigurationCalculations;
    }

    @Override
    public void clear() {
        persistentStorage.clear();
        persistentStorage.put(CHANGE_INCREMENT_KEY, 0);
        incrementChangeIncrementAndNotifyModifiedListeners();
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

    @Override
    public synchronized Integer getAge() {
        Date dateOfBirth = getDateOfBirth();
        if (dateOfBirth == null) {
            return null;
        }

        return userConfigurationCalculations.getAge(dateOfBirth);
    }

    @Override
    public synchronized Integer getBaseMetabolicRate() {
        Integer age = getAge();
        Sex sex = getSex();
        Double weightInKg = getWeightInKg();
        Integer heightInCm = getHeightInCm();

        return userConfigurationCalculations.getBaseMetabolicRate(weightInKg, heightInCm, age, sex);
    }

    private void incrementChangeIncrementAndNotifyModifiedListeners() {
        int changeIncrement = persistentStorage.get(CHANGE_INCREMENT_KEY, 0);
        changeIncrement++;
        persistentStorage.put(CHANGE_INCREMENT_KEY, changeIncrement);

        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                setChanged();
                notifyObservers();
            }
        });
    }
}
