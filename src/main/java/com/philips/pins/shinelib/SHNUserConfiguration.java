package com.philips.pins.shinelib;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.pins.shinelib.utility.SHNServiceRegistry;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 310188215 on 02/06/15.
 */
public class SHNUserConfiguration {
    private static final String TAG = SHNUserConfiguration.class.getSimpleName();

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

    public enum Sex
    {
        Female,
        Male,
        Unspecified
    }

    public enum Handedness
    {
        Unknown,
        RightHanded,
        LeftHanded,
        MixedHanded
    }

    private final ShinePreferenceWrapper shinePreferenceWrapper;
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

    /* package */ SHNUserConfiguration() {
        this.shinePreferenceWrapper = SHNServiceRegistry.getInstance().get(ShinePreferenceWrapper.class);
        retrieveFromPreferences();
    }

    private void incrementIndex() {
        changeIncrement++;
        saveToPreferences();
    }

    public synchronized int getChangeIncrement() {
        return changeIncrement;
    }

    private void setChangeIncrement(int changeIncrement) {
        this.changeIncrement = changeIncrement;
    }

    public synchronized Sex getSex() {
        return sex;
    }

    public synchronized void setSex(Sex sex) {
        this.sex = sex;
        incrementIndex();
    }

    public synchronized Integer getMaxHeartRate() {
        Integer age = getAge();
        if (maxHeartRate == null && age != null) {
            return (int) (207 - (0.7 * age));
        }
        return maxHeartRate;
    }

    public synchronized void setMaxHeartRate(Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
        incrementIndex();
    }

    public synchronized Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public synchronized void setRestingHeartRate(Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
        incrementIndex();
    }

    public synchronized Integer getHeightInCm() {
        return heightInCm;
    }

    public synchronized void setHeightInCm(Integer heightInCm) {
        this.heightInCm = heightInCm;
        incrementIndex();
    }

    public synchronized Double getWeightInKg() {
        return weightInKg;
    }

    public synchronized void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
        incrementIndex();
    }

    public synchronized Date getDateOfBirth() {
        return dateOfBirth;
    }

    public synchronized void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        incrementIndex();
    }

    /* return null when birthdate not set */
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

    public synchronized Handedness getHandedness() {
        return handedness;
    }

    public synchronized void setHandedness(Handedness handedness) {
        this.handedness = handedness;
        incrementIndex();
    }

    public synchronized String getIsoLanguageCode() {
        return isoLanguageCode;
    }

    public synchronized void setIsoLanguageCode(String isoLanguageCode) {
        this.isoLanguageCode = isoLanguageCode;
        incrementIndex();
    }

    public synchronized Boolean getUseMetricSystem() {
        return useMetricSystem;
    }

    public synchronized void setUseMetricSystem(Boolean useMetricSystem) {
        this.useMetricSystem = useMetricSystem;
        incrementIndex();
    }

    public synchronized Character getDecimalSeparator() {
        return decimalSeparator;
    }

    public synchronized void setDecimalSeparator(Character decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
        incrementIndex();
    }

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

    private synchronized void saveToPreferences() {
        SharedPreferences.Editor edit = shinePreferenceWrapper.edit();

        Date dateOfBirth = getDateOfBirth();
        updatePersistentStorage(edit, USER_CONFIG_DATE_OF_BIRTH, ((dateOfBirth == null) ? null : dateOfBirth.getTime()));
        updatePersistentStorage(edit, USER_CONFIG_HEIGHT_IN_CM, getHeightInCm());
        updatePersistentStorage(edit, USER_CONFIG_MAX_HEART_RATE, getMaxHeartRate());
        updatePersistentStorage(edit, USER_CONFIG_RESTING_HEART_RATE, getRestingHeartRate());

        Double weightInKg = getWeightInKg();
        updatePersistentStorage(edit, USER_CONFIG_WEIGHT_IN_KG, (weightInKg == null) ? null : (float)(double)getWeightInKg());

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
            isoLanguageCode = Locale.getDefault().getLanguage();
        }
        useMetricSystem = readBooleanFromPersistentStorage(USER_CONFIG_USE_METRIC_SYSTEM);
        if (useMetricSystem == null) {
            useMetricSystem = Boolean.FALSE;
        }
        decimalSeparator = readCharacterFromPersistentStorage(USER_CONFIG_DECIMAL_SEPARATOR);
        if (decimalSeparator == null) {
            decimalSeparator = '.';
        }

        Integer index = readIntegerFromPersistentStorage(USER_CONFIG_INCREMENT);
        if (index == null) {
            index = 0;
        }
        setChangeIncrement(index);
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
        long value = shinePreferenceWrapper.getLong(key);
        if (value != -1L) {
            return new Date(value);
        }
        return null;
    }

    @Nullable
    private Integer readIntegerFromPersistentStorage(String key) {
        int value = shinePreferenceWrapper.getInt(key);
        if (value != -1) {
            return value;
        }
        return null;
    }

    @Nullable
    private Double readDoubleFromPersistentStorage(String key) {
        float value = shinePreferenceWrapper.getFloat(key);
        if (!Float.isNaN(value)) {
            return (double)value;
        }
        return null;
    }

    @Nullable
    private String readStringFromPersistentStorage(String key) {
        return shinePreferenceWrapper.getString(key);
    }

    @Nullable
    private Boolean readBooleanFromPersistentStorage(String key) {
        return shinePreferenceWrapper.getBoolean(key);
    }
}
