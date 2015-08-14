package com.philips.pins.shinelib.datatypes;

import android.content.SharedPreferences;
import android.util.Log;

import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 310188215 on 02/06/15.
 */
public class SHNUserConfiguration {
    private static final String TAG = SHNUserConfiguration.class.getSimpleName();

    private static final String USER_CONFIG_DATE_OF_BIRTH = "USER_CONFIG_DATE_OF_BIRTH";
    private static final String USER_CONFIG_MAX_HEART_RATE = "USER_CONFIG_MAX_HEART_RATE";
    private static final String USER_CONFIG_RESTING_HEART_RATE = "USER_CONFIG_RESTING_HEART_RATE";
    private static final String USER_CONFIG_WEIGHT_IN_KG = "USER_CONFIG_WEIGHT_IN_KG";
    private static final String USER_CONFIG_HEIGHT_IN_CM = "USER_CONFIG_HEIGHT_IN_CM";
    private static final String USER_CONFIG_SEX = "USER_CONFIG_SEX";
    private static final String USER_CONFIG_INCREMENT = "USER_CONFIG_INCREMENT";

    public enum Sex {
        Female, Male, Unspecified
    }

    private final ShinePreferenceWrapper shinePreferenceWrapper;
    private Sex sex;
    private Integer maxHeartRate;
    private Integer restingHeartRate;
    private Integer heightInCm;
    private Double weightInKg;
    private Date dateOfBirth;

    private int incrementIndex;

    public static SHNUserConfiguration getNewDefaultInstance(ShinePreferenceWrapper shinePreferenceWrapper) {
        SHNUserConfiguration shnUserConfiguration = new SHNUserConfiguration(shinePreferenceWrapper);
        shnUserConfiguration.retrieveFromPreferences();
        return shnUserConfiguration;
    }

    private SHNUserConfiguration(ShinePreferenceWrapper shinePreferenceWrapper) {
        this.shinePreferenceWrapper = shinePreferenceWrapper;
    }

    private void incrementIndex() {
        incrementIndex++;
        saveToPreferences();
    }

    public int getIncrementIndex() {
        return incrementIndex;
    }

    private void setIncrementIndex(int incrementIndex) {
        this.incrementIndex = incrementIndex;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
        incrementIndex();
    }

    public Integer getMaxHeartRate() {
        Integer age = getAge();
        if (maxHeartRate == null && age != null) {
            return (int) (207 - (0.7 * age));
        }
        return maxHeartRate;
    }

    public void setMaxHeartRate(Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
        incrementIndex();
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
        incrementIndex();
    }

    public Integer getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(Integer heightInCm) {
        this.heightInCm = heightInCm;
        incrementIndex();
    }

    public Double getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
        incrementIndex();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        incrementIndex();
    }

    /* return null when birthdate not set */
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }

        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();

        dob.setTime(dateOfBirth);

        if (dob.after(now)) {
            Log.e(TAG, "Can't be born in the future");
            return null;
        }

        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public Integer getBaseMetabolicRate() {
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

        if (getDateOfBirth() != null) {
            edit.putLong(USER_CONFIG_DATE_OF_BIRTH, getDateOfBirth().getTime());
        } else {
            edit.remove(USER_CONFIG_DATE_OF_BIRTH);
        }

        if (getHeightInCm() != null) {
            edit.putInt(USER_CONFIG_HEIGHT_IN_CM, getHeightInCm());
        } else {
            edit.remove(USER_CONFIG_HEIGHT_IN_CM);
        }

        if (getMaxHeartRate() != null) {
            edit.putInt(USER_CONFIG_MAX_HEART_RATE, getMaxHeartRate());
        } else {
            edit.remove(USER_CONFIG_MAX_HEART_RATE);
        }

        if (getRestingHeartRate() != null) {
            edit.putInt(USER_CONFIG_RESTING_HEART_RATE, getRestingHeartRate());
        } else {
            edit.remove(USER_CONFIG_RESTING_HEART_RATE);
        }

        if (getWeightInKg() != null) {
            edit.putFloat(USER_CONFIG_WEIGHT_IN_KG, (float) (double) getWeightInKg());
        } else {
            edit.remove(USER_CONFIG_WEIGHT_IN_KG);
        }

        if (getSex() != null) {
            edit.putString(USER_CONFIG_SEX, getSex().name());
        } else {
            edit.remove(USER_CONFIG_SEX);
        }

        edit.putInt(USER_CONFIG_INCREMENT, getIncrementIndex());
        edit.commit();
    }

    private synchronized void retrieveFromPreferences() {
        long longValue = shinePreferenceWrapper.getLong(USER_CONFIG_DATE_OF_BIRTH);
        dateOfBirth = null;
        if (longValue != -1l) {
            dateOfBirth = new Date(longValue);
        }

        int intValue = shinePreferenceWrapper.getInt(USER_CONFIG_HEIGHT_IN_CM);
        heightInCm = null;
        if (intValue != -1) {
            heightInCm = intValue;
        }

        intValue = shinePreferenceWrapper.getInt(USER_CONFIG_MAX_HEART_RATE);
        maxHeartRate = null;
        if (intValue != -1) {
            maxHeartRate = intValue;
        }

        intValue = shinePreferenceWrapper.getInt(USER_CONFIG_RESTING_HEART_RATE);
        restingHeartRate = null;
        if (intValue != -1) {
            restingHeartRate = intValue;
        }

        float floatValue = shinePreferenceWrapper.getFloat(USER_CONFIG_WEIGHT_IN_KG);
        weightInKg = null;
        if (floatValue != Float.NaN) {
            weightInKg = (double) floatValue;
        }

        String stringValue = shinePreferenceWrapper.getString(USER_CONFIG_SEX);
        sex = null;
        if (stringValue != null) {
            sex = Sex.valueOf(stringValue);
        }

        int index = shinePreferenceWrapper.getInt(USER_CONFIG_INCREMENT);
        setIncrementIndex(index);
    }
}
