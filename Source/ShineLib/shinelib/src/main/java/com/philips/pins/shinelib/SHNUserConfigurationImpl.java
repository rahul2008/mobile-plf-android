package com.philips.pins.shinelib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

/**
 * Created by 310188215 on 02/06/15.
 */
public class SHNUserConfigurationImpl extends Observable implements SHNUserConfiguration {

    public static final char DEFAULT_DECIMAL_SEPARATOR = '.';
    public static final Boolean DEFAULT_USE_METRIC_SYSTEM = Boolean.FALSE;
    private final Handler internalHandler;

    private static final String TAG = SHNUserConfigurationImpl.class.getSimpleName();
    private static final String SHINELIB_PREFERENCES_FILE_KEY = SHNUserConfigurationImpl.class.getSimpleName() + "_preferences";

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

    private final SharedPreferences sharedPreferences;
    private Sex sex = Sex.Unspecified;
    private Integer maxHeartRate;
    private Integer restingHeartRate;
    private Integer heightInCm;
    private Double weightInKg;
    private Date dateOfBirth;
    private Handedness handedness = Handedness.Unknown;
    private String isoLanguageCode;
    private Boolean useMetricSystem;
    private Character decimalSeparator;

    private int changeIncrement;

    /* package */ SHNUserConfigurationImpl(SharedPreferences sharedPreferences, Handler internalHandler) { // Added for testing only!!
        this.sharedPreferences = sharedPreferences;
        this.internalHandler = internalHandler;
        retrieveFromPreferences();
    }

    /* package */ SHNUserConfigurationImpl(Context context, Handler internalHandler) {
        this(context.getSharedPreferences(SHINELIB_PREFERENCES_FILE_KEY, Context.MODE_PRIVATE), internalHandler);
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
            Log.e(TAG, "Can't be born in the future");
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
    public synchronized String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    @Override
    public synchronized void setIsoLanguageCode(String isoLanguageCode) {
        if (isoLanguageCode == null) {
            isoLanguageCode = getDefaultLanguage();
        }
        if (!isEqualTo(this.isoLanguageCode, isoLanguageCode)) {
            this.isoLanguageCode = isoLanguageCode;
            incrementChangeIncrementAndNotifyModifiedListeners();
        }
    }

    @Override
    public synchronized Boolean getUseMetricSystem() {
        return useMetricSystem;
    }

    @Override
    public synchronized void setUseMetricSystem(Boolean useMetricSystem) {
        if (useMetricSystem == null) {
            useMetricSystem = DEFAULT_USE_METRIC_SYSTEM;
        }
        if (!isEqualTo(this.useMetricSystem, useMetricSystem)) {
            this.useMetricSystem = useMetricSystem;
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
            }
        });
        if (internalHandler != null) {
            internalHandler.post(new Runnable() {
                @Override
                public void run() {
                    setChanged();
                    notifyObservers();
                }
            });
        }
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
        isoLanguageCode = readStringFromPersistentStorage(USER_CONFIG_ISO_LANGUAGE_CODE);
        if (isoLanguageCode == null) {
            isoLanguageCode = getDefaultLanguage();
        }
        useMetricSystem = readBooleanFromPersistentStorage(USER_CONFIG_USE_METRIC_SYSTEM);
        if (useMetricSystem == null) {
            useMetricSystem = DEFAULT_USE_METRIC_SYSTEM;
        }
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
        return null;
    }

    @Nullable
    private Handedness readHandednessFromPersistentStorage(String key) {
        String stringValue = readStringFromPersistentStorage(key);
        if (stringValue != null) {
            return Handedness.valueOf(stringValue);
        }
        return Handedness.Unknown;
    }

    @Nullable
    private Sex readSexFromPersistentStorage(String key) {
        String stringValue = readStringFromPersistentStorage(key);
        if (stringValue != null) {
            return Sex.valueOf(stringValue);
        }
        return Sex.Unspecified;
    }

    @Nullable
    private Date readDateFromPersistentStorage(String key) {
        long value = sharedPreferences.getLong(key, -1L);
        if (value != -1L) {
            return new Date(value);
        }
        return null;
    }

    @Nullable
    private Integer readIntegerFromPersistentStorage(String key) {
        int value = sharedPreferences.getInt(key, -1);
        if (value != -1) {
            return value;
        }
        return null;
    }

    @Nullable
    private Double readDoubleFromPersistentStorage(String key) {
        float value = sharedPreferences.getFloat(key, Float.NaN);
        if (!Float.isNaN(value)) {
            return (double) value;
        }
        return null;
    }

    @Nullable
    private String readStringFromPersistentStorage(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Nullable
    private Boolean readBooleanFromPersistentStorage(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, false);
        }
        return null;
    }
}
