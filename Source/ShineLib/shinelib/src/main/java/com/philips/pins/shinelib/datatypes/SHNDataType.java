/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

/**
 * All datatypes supported by BlueLib.
 * <p/>
 * Returned by classes inheriting {@link SHNData#getSHNDataType()} to indicate what datatype they are.
 */
public enum SHNDataType {
    Unknown,
    DebugMoonshine,
    HeartRate,
    EnergyExpenditure,
    EnergyIntake,
    Steps,
    TestResultMoonshine,
    HeartRateResting,
    HeartRateRecovery,
    Sleep,
    BodyTemperature,
    ActivityTypeCM3Deprecated,
    ActivityTypeMoonshine,
    ActiveEnergyExpenditure,
    BodyWeight,
    BloodPressure,
    BodyComposition,
    Weight,
    SubjectiveDataMoonshine,
    ResetSourceMoonshine,
    EventMoonshine,
    DebugStringMoonshine,
    HeartBeatsMoonshine,
    ActivityCountPerMinute,
    EnergyExpenditureDebug,
    CardioFitnessIndex,
    Vo2Max,
    VisibleLight,
    UVLight,
    RespirationRate,
    UserConfigChangedEventHeightMoonshine,
    UserConfigChangedEventWeightMoonshine,
    UserConfigChangedEventSexMoonshine,
    UserConfigChangedEventBirthdayMoonshine,
    UserConfigChangedEventBMRMoonshine,
    UserConfigChangedEventHeartRateZonesMoonshine,
    UserConfigChangedEventHandednessMoonshine,
    UserConfigChangedEventWearingPositionMoonshine,
    UserConfigChangedEventLanguageMoonshine,
    UserConfigChangedEventRestingHeartRateMoonshine,
    ActivitySessionMoonshine,
    DayTotalsMoonshine,
    BatteryStateMoonshine,
    UserConfigChangedEventTargetsMoonshine,
    ActivityDayTotalsMoonshine,
    UserConfigChangedEventSedentaryPeriodMoonshine,
    UserConfigChangedEventSedentaryNotificationMoonshine,
    CM3AccelerometerDataMoonshine,
    CM3HeartRateMoonshine,
    UserConfigChangedEventEnergyIntakeMoonshine,
    ChangeTimeMoonshine,
    FatPercentage,
    Raw,
    WeSTDataMoonshine,
    DailyCumulativeTotalEnergyExpenditure,
    DailyCumulativeSteps,
    ActiveMinutes,
    BloodOxygenLevel,
    CM3RestingHeartRateDataMoonshine,
    EnergyIntakeMoonshine,
    ExtendedWeight
}
