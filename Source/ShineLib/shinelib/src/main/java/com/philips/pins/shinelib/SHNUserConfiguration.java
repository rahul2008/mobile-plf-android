/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Date;

/**
 * Interface representing user data. Includes various user data as gender, date of birth, age, height, weight etc. Also contains user related preferences such as language and country code, clock format etc.
 */
public interface SHNUserConfiguration {
    /**
     * Supported ganders.
     */
    enum Sex {
        Female,
        Male,
        Unspecified
    }

    /**
     * Supported handedness.
     */
    enum Handedness {
        Unknown,
        RightHanded,
        LeftHanded,
        MixedHanded
    }

    /**
     * Supported clock formats.
     */
    enum ClockFormat {
        _24H,
        _12H
    }

    /**
     * Gender the user.
     *
     * @return gender of the user. Returns {@code Sex.Unspecified} if not set.
     */
    Sex getSex();

    void setSex(Sex sex);

    /**
     * Maximum heart of the user.
     *
     * @return maximum heart of the user. Returns {@code null} if not set.
     */
    Integer getMaxHeartRate();

    void setMaxHeartRate(Integer maxHeartRate);

    /**
     * Resting heart of the user.
     *
     * @return resting heart of the user. Returns -1 if not set.
     */
    Integer getRestingHeartRate();

    void setRestingHeartRate(Integer restingHeartRate);

    /**
     * Height of the user in cm.
     *
     * @return height of the user in cm. Returns {@code null} if not set.
     */
    Integer getHeightInCm();

    void setHeightInCm(Integer heightInCm);

    /**
     * Weight of the user in kg.
     *
     * @return weight of the user in kg. Returns {@code null} if not set.
     */
    Double getWeightInKg();

    void setWeightInKg(Double weightInKg);

    /**
     * Date of birth of the user.
     *
     * @return date of birth of the user. Returns {@code null} if not set.
     */
    Date getDateOfBirth();

    void setDateOfBirth(Date dateOfBirth);

    /**
     * Calculated age of the user.
     *
     * @return calculated age of the user. Returns {@code null} if the date of the birth is not set.
     */
    Integer getAge();

    /**
     * Handedness of birth of the user.
     *
     * @return handedness of the user. Returns {@code Handedness.Unknown} if not set.
     */
    Handedness getHandedness();

    void setHandedness(Handedness handedness);

    /**
     * Language code according to ISO standard of the user.
     *
     * @return ISO language code of the user. Returns user's local default language code if not set.
     */
    String getIsoLanguageCode();

    void setIsoLanguageCode(String isoLanguageCode);

    /**
     * Country code according to ISO standard of the user.
     *
     * @return ISO country code of the user. Returns user's local default country code if not set.
     */
    String getIsoCountryCode();

    void setIsoCountryCode(String isoCountryCode);

    /**
     * Specifies if metric system is used.
     *
     * @return user preferences for metric system. Returns {@code true} if not set.
     */
    Boolean getUseMetricSystem();

    void setUseMetricSystem(Boolean useMetricSystem);

    /**
     * Specifies decimal separator to be used.
     *
     * @return user preferences for decimal separator. Returns '.' if not set.
     */
    Character getDecimalSeparator();

    void setDecimalSeparator(Character decimalSeparator);

    /**
     * Returns calculated metabolic rate on basis of age, gender, weight and height of the user.
     *
     * @return calculated metabolic rate . Returns {@code null} if not set.
     */
    Integer getBaseMetabolicRate();

    /**
     * Specifies clock format to be used.
     *
     * @return user preferences for clock format. Returns {@code null} code if not set.
     */
    ClockFormat getClockFormat();

    void setClockFormat(ClockFormat clockFormat);

    /**
     * Clear all user data. Also clears corresponding persistent storage.
     */
    void clear();
}
