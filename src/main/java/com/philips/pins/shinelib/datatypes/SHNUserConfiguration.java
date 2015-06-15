package com.philips.pins.shinelib.datatypes;

import android.util.Log;

import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 310188215 on 02/06/15.
 */
public class SHNUserConfiguration {
    private static final String TAG = SHNUserConfiguration.class.getSimpleName();

    public enum Sex {
        Female, Male
    }

    private final ShinePreferenceWrapper shinePreferenceWrapper;
    private Sex sex;
    private Integer maxHeartRate;
    private Integer restingHeartRate;
    private Integer heightInCm;
    private Double weightInKg;
    private Date dateOfBirth;

    public static SHNUserConfiguration getNewDefaultInstance(ShinePreferenceWrapper shinePreferenceWrapper) {
        SHNUserConfiguration shnUserConfiguration = new SHNUserConfiguration(shinePreferenceWrapper);
        shinePreferenceWrapper.readUserConfiguration(shnUserConfiguration);
        return shnUserConfiguration;
    }

    private SHNUserConfiguration(ShinePreferenceWrapper shinePreferenceWrapper) {
        this.shinePreferenceWrapper = shinePreferenceWrapper;
    }

    private void saveToPreferences() {
        shinePreferenceWrapper.storeUserConfiguration(this);
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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
        saveToPreferences();
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
        saveToPreferences();
    }

    public Integer getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(Integer heightInCm) {
        this.heightInCm = heightInCm;
        saveToPreferences();
    }

    public Double getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
        saveToPreferences();
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        saveToPreferences();
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
            result = (int)baseMetabolicRate;
        }
        return result;
    }
}
