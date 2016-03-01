/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Date;

/**
 * Created by 310188215 on 27/10/15.
 */
public interface SHNUserConfiguration {
    enum Sex {
        Female,
        Male,
        Unspecified
    }

    enum Handedness {
        Unknown,
        RightHanded,
        LeftHanded,
        MixedHanded
    }

    enum ClockFormat {
        _24H,
        _12H
    }

    Sex getSex();

    void setSex(Sex sex);

    Integer getMaxHeartRate();

    void setMaxHeartRate(Integer maxHeartRate);

    Integer getRestingHeartRate();

    void setRestingHeartRate(Integer restingHeartRate);

    Integer getHeightInCm();

    void setHeightInCm(Integer heightInCm);

    Double getWeightInKg();

    void setWeightInKg(Double weightInKg);

    Date getDateOfBirth();

    void setDateOfBirth(Date dateOfBirth);

    /* return null when birthdate not set */
    Integer getAge();

    Handedness getHandedness();

    void setHandedness(Handedness handedness);

    String getIsoLanguageCode();

    void setIsoLanguageCode(String isoLanguageCode);

    String getIsoCountryCode();

    void setIsoCountryCode(String isoCountryCode);

    Boolean getUseMetricSystem();

    void setUseMetricSystem(Boolean useMetricSystem);

    Character getDecimalSeparator();

    void setDecimalSeparator(Character decimalSeparator);

    Integer getBaseMetabolicRate();

    ClockFormat getClockFormat();

    void setClockFormat(ClockFormat clockFormat);

    void clear();
}
