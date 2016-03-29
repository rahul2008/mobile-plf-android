package com.philips.pins.shinelib;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SHNUserConfigurationCalculations {
    private static final String TAG = "UserConfCalculations";

    // -- CHILD

    public static final int CHILD_BASE = 0;
    public static final int CHILD_HEIGHT_M = 0;
    public static final double CHILD_WEIGHT_KG = 0;

    // -- YOUNG

    public static final int YOUNG_MEN_BASE = 717;
    public static final int YOUNG_MEN_HEIGHT_M = -27;
    public static final double YOUNG_MEN_WEIGHT_KG = 15.4;

    public static final int YOUNG_WOMEN_BASE = 35;
    public static final int YOUNG_WOMEN_HEIGHT_M = 334;
    public static final double YOUNG_WOMEN_WEIGHT_KG = 13.3;

    // -- MID

    public static final int MID_MEN_BASE = 901;
    public static final int MID_MEN_HEIGHT_M = 16;
    public static final double MID_MEN_WEIGHT_KG = 11.3;

    public static final int MID_WOMEN_BASE = 865;
    public static final int MID_WOMEN_HEIGHT_M = -25;
    public static final double MID_WOMEN_WEIGHT_KG = 8.7;

    // -- OLD

    public static final int OLD_MEN_BASE = -1071;
    public static final int OLD_MEN_HEIGHT_M = 1128;
    public static final double OLD_MEN_WEIGHT_KG = 8.8;

    public static final int OLD_WOMEN_BASE = -302;
    public static final int OLD_WOMEN_HEIGHT_M = 637;
    public static final double OLD_WOMEN_WEIGHT_KG = 9.2;

    // ---

    private final BmrValues childValues = new BmrValues(CHILD_BASE, CHILD_HEIGHT_M, CHILD_WEIGHT_KG);
    private final Map<AGE_GROUP, BmrValues> maleValuesMap = new HashMap<>();
    private final Map<AGE_GROUP, BmrValues> femaleValuesMap = new HashMap<>();

    public SHNUserConfigurationCalculations() {
        maleValuesMap.put(AGE_GROUP.CHILD, childValues);
        maleValuesMap.put(AGE_GROUP.YOUNG, new BmrValues(YOUNG_MEN_BASE, YOUNG_MEN_HEIGHT_M, YOUNG_MEN_WEIGHT_KG));
        maleValuesMap.put(AGE_GROUP.MID, new BmrValues(MID_MEN_BASE, MID_MEN_HEIGHT_M, MID_MEN_WEIGHT_KG));
        maleValuesMap.put(AGE_GROUP.OLD, new BmrValues(OLD_MEN_BASE, OLD_MEN_HEIGHT_M, OLD_MEN_WEIGHT_KG));

        femaleValuesMap.put(AGE_GROUP.CHILD, childValues);
        femaleValuesMap.put(AGE_GROUP.YOUNG, new BmrValues(YOUNG_WOMEN_BASE, YOUNG_WOMEN_HEIGHT_M, YOUNG_WOMEN_WEIGHT_KG));
        femaleValuesMap.put(AGE_GROUP.MID, new BmrValues(MID_WOMEN_BASE, MID_WOMEN_HEIGHT_M, MID_WOMEN_WEIGHT_KG));
        femaleValuesMap.put(AGE_GROUP.OLD, new BmrValues(OLD_WOMEN_BASE, OLD_WOMEN_HEIGHT_M, OLD_WOMEN_WEIGHT_KG));
    }

    public Integer getMaxHeartRate(final Integer maxHeartRate, final Integer age) {
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

    public Integer getBaseMetabolicRate(final Double weightInKg, final Integer heightInCm, final Integer age, final SHNUserConfiguration.Sex sex) {
        final AGE_GROUP ageGroup = getAgeGroup(age);
        BmrValues bmrValues = getBmrValues(sex, ageGroup);

        return (int) Math.round(weightInKg * bmrValues.weightInKgValue + heightInCm * bmrValues.heightInMValue / 100d + bmrValues.baseValue);
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
    private AGE_GROUP getAgeGroup(final int age) {
        if (age > 60) {
            return AGE_GROUP.OLD;
        } else if (age > 30) {
            return AGE_GROUP.MID;
        } else if (age > 18) {
            return AGE_GROUP.YOUNG;
        } else {
            return AGE_GROUP.CHILD;
        }
    }

    enum AGE_GROUP {
        CHILD, YOUNG, MID, OLD
    }

    private class BmrValues {
        public final int baseValue;
        public final int heightInMValue;
        public final double weightInKgValue;

        public BmrValues(final int baseValue, final int heightInMValue, final double weightInKgValue) {
            this.baseValue = baseValue;
            this.heightInMValue = heightInMValue;
            this.weightInKgValue = weightInKgValue;
        }
    }
}
