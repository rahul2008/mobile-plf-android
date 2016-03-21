package com.philips.pins.shinelib;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Calendar;
import java.util.Date;

public class SHNUserConfigurationCalculations {
    private static final String TAG = "UserConfCalculations";

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
        Integer result = null;
        if (weightInKg != null && heightInCm != null && age != null && sex != null) {
            double heightInMeters = heightInCm / 100.0f;
            double baseMetabolicRate = 0;

            if (age >= 10 && age <= 17) {
                baseMetabolicRate = (sex == SHNUserConfiguration.Sex.Male)
                        ? (16.6 * weightInKg) + (77 * heightInMeters) + 572
                        : (7.4 * weightInKg) + (482 * heightInMeters) + 217;
            } else if (age >= 18 && age <= 30) {
                baseMetabolicRate = (sex == SHNUserConfiguration.Sex.Male)
                        ? (15.4 * weightInKg) - (27 * heightInMeters) + 717
                        : (13.3 * weightInKg) + (334 * heightInMeters) + 35;
            } else if (age >= 31 && age <= 60) {
                baseMetabolicRate = (sex == SHNUserConfiguration.Sex.Male)
                        ? (11.3 * weightInKg) + (16 * heightInMeters) + 901
                        : (8.7 * weightInKg) - (25 * heightInMeters) + 865;
            } else if (age >= 61) {
                baseMetabolicRate = (sex == SHNUserConfiguration.Sex.Male)
                        ? (8.8 * weightInKg) + (1128 * heightInMeters) - 1071
                        : (9.2 * weightInKg) + (637 * heightInMeters) - 302;
            }
            result = (int) baseMetabolicRate;
        }
        return result;
    }
}
