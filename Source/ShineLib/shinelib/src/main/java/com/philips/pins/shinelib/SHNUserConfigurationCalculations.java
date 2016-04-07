package com.philips.pins.shinelib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SHNUserConfigurationCalculations {
    private static final String TAG = "UserConfCalculations";

    // -- CHILD

    public static final int CHILD_BASE_KILO_CALORIES = 0;
    public static final int CHILD_HEIGHT_M_KILO_CALORIES = 0;
    public static final double CHILD_WEIGHT_KG_KILO_CALORIES = 0;

    // -- YOUNG

    public static final int YOUNG_AGE = 17;

    public static final int YOUNG_MEN_BASE_KILO_CALORIES = 717;
    public static final int YOUNG_MEN_HEIGHT_M_KILO_CALORIES = -27;
    public static final double YOUNG_MEN_WEIGHT_KG_KILO_CALORIES = 15.4;

    public static final int YOUNG_WOMEN_BASE_KILO_CALORIES = 35;
    public static final int YOUNG_WOMEN_HEIGHT_M_KILO_CALORIES = 334;
    public static final double YOUNG_WOMEN_WEIGHT_KG_KILO_CALORIES = 13.3;

    // -- ADULT

    public static final int ADULT_AGE = 30;

    public static final int ADULT_MEN_BASE_KILO_CALORIES = 901;
    public static final int ADULT_MEN_HEIGHT_M_KILO_CALORIES = 16;
    public static final double ADULT_MEN_WEIGHT_KG_KILO_CALORIES = 11.3;

    public static final int ADULT_WOMEN_BASE_KILO_CALORIES = 865;
    public static final int ADULT_WOMEN_HEIGHT_M_KILO_CALORIES = -25;
    public static final double ADULT_WOMEN_WEIGHT_KG_KILO_CALORIES = 8.7;

    // -- ELDERLY

    public static final int ELDERLY_AGE = 60;

    public static final int ELDERLY_MEN_BASE_KILO_CALORIES = -1071;
    public static final int ELDERLY_MEN_HEIGHT_M_KILO_CALORIES = 1128;
    public static final double ELDERLY_MEN_WEIGHT_KG_KILO_CALORIES = 8.8;

    public static final int ELDERLY_WOMEN_BASE_KILO_CALORIES = -302;
    public static final int ELDERLY_WOMEN_HEIGHT_M_KILO_CALORIES = 637;
    public static final double ELDERLY_WOMEN_WEIGHT_KG_KILO_CALORIES = 9.2;

    // ---

    private final BmrValues childValues = new BmrValues(CHILD_BASE_KILO_CALORIES, CHILD_HEIGHT_M_KILO_CALORIES, CHILD_WEIGHT_KG_KILO_CALORIES);
    private final Map<AGE_GROUP, BmrValues> maleValuesMap = new HashMap<>();
    private final Map<AGE_GROUP, BmrValues> femaleValuesMap = new HashMap<>();

    public SHNUserConfigurationCalculations() {
        maleValuesMap.put(AGE_GROUP.CHILD, childValues);
        maleValuesMap.put(AGE_GROUP.YOUNG, new BmrValues(YOUNG_MEN_BASE_KILO_CALORIES, YOUNG_MEN_HEIGHT_M_KILO_CALORIES, YOUNG_MEN_WEIGHT_KG_KILO_CALORIES));
        maleValuesMap.put(AGE_GROUP.ADULT, new BmrValues(ADULT_MEN_BASE_KILO_CALORIES, ADULT_MEN_HEIGHT_M_KILO_CALORIES, ADULT_MEN_WEIGHT_KG_KILO_CALORIES));
        maleValuesMap.put(AGE_GROUP.ELDERLY, new BmrValues(ELDERLY_MEN_BASE_KILO_CALORIES, ELDERLY_MEN_HEIGHT_M_KILO_CALORIES, ELDERLY_MEN_WEIGHT_KG_KILO_CALORIES));

        femaleValuesMap.put(AGE_GROUP.CHILD, childValues);
        femaleValuesMap.put(AGE_GROUP.YOUNG, new BmrValues(YOUNG_WOMEN_BASE_KILO_CALORIES, YOUNG_WOMEN_HEIGHT_M_KILO_CALORIES, YOUNG_WOMEN_WEIGHT_KG_KILO_CALORIES));
        femaleValuesMap.put(AGE_GROUP.ADULT, new BmrValues(ADULT_WOMEN_BASE_KILO_CALORIES, ADULT_WOMEN_HEIGHT_M_KILO_CALORIES, ADULT_WOMEN_WEIGHT_KG_KILO_CALORIES));
        femaleValuesMap.put(AGE_GROUP.ELDERLY, new BmrValues(ELDERLY_WOMEN_BASE_KILO_CALORIES, ELDERLY_WOMEN_HEIGHT_M_KILO_CALORIES, ELDERLY_WOMEN_WEIGHT_KG_KILO_CALORIES));
    }

    @Nullable
    public Integer getMaxHeartRate(@Nullable final Integer maxHeartRate, @Nullable final Integer age) {
        if (maxHeartRate == null && age != null) {
            return (int) (207 - (0.7 * age));
        }
        return maxHeartRate;
    }

    public Integer getAge(final Date dateOfBirth) {

        Calendar now = Calendar.getInstance();
        Calendar dateOfBirthCalendar = Calendar.getInstance();

        dateOfBirthCalendar.setTime(dateOfBirth);

        if (dateOfBirthCalendar.after(now)) {
            SHNLogger.e(TAG, "Can't be born in the future");
            return null;
        }

        int age = now.get(Calendar.YEAR) - dateOfBirthCalendar.get(Calendar.YEAR);
        if (birthdayNotYetPassed(now, dateOfBirthCalendar)) {
            age--;
        }

        return age;
    }

    private boolean birthdayNotYetPassed(Calendar now, Calendar dateOfBirth) {
        return now.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR);
    }

    public Integer getBaseMetabolicRate(@NonNull final Double weightInKg, @NonNull final Integer heightInCm, @NonNull final Integer age, @NonNull final SHNUserConfiguration.Sex sex) {
        final AGE_GROUP ageGroup = getAgeGroup(age);
        BmrValues bmrValues = getBmrValues(sex, ageGroup);

        return (int) Math.floor(weightInKg * bmrValues.kCalPerKgWeight + heightInCm * bmrValues.kCalPerMeterHeight / 100d + bmrValues.baseKCal);
    }

    private BmrValues getBmrValues(final SHNUserConfiguration.Sex sex, final AGE_GROUP ageGroup) {
        BmrValues bmrValues;
        if (sex == SHNUserConfiguration.Sex.Male) {
            bmrValues = maleValuesMap.get(ageGroup);
        } else {
            bmrValues = femaleValuesMap.get(ageGroup);
        }
        return bmrValues;
    }

    @NonNull
    private AGE_GROUP getAgeGroup(@NonNull final Integer age) {
        if (age > ELDERLY_AGE) {
            return AGE_GROUP.ELDERLY;
        } else if (age > ADULT_AGE) {
            return AGE_GROUP.ADULT;
        } else if (age > YOUNG_AGE) {
            return AGE_GROUP.YOUNG;
        } else {
            return AGE_GROUP.CHILD;
        }
    }

    enum AGE_GROUP {
        CHILD, YOUNG, ADULT, ELDERLY
    }

    private class BmrValues {
        public final int baseKCal;
        public final int kCalPerMeterHeight;
        public final double kCalPerKgWeight;

        public BmrValues(final int baseKCal, final int kCalPerMeterHeight, final double kCalPerKgWeight) {
            this.baseKCal = baseKCal;
            this.kCalPerMeterHeight = kCalPerMeterHeight;
            this.kCalPerKgWeight = kCalPerKgWeight;
        }
    }
}
