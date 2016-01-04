/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

public enum SHNCapabilityType {

    /**
     * Use {@link #DATA_STREAMING} instead.
     */
    @Deprecated
    DataStreaming,

    /**
     * Use {@link #DEVICE_INFORMATION} instead.
     */
    @Deprecated
    DeviceInformation,

    /**
     * Use {@link #FIRMWARE_UPDATE} instead.
     */
    @Deprecated
    FirmwareUpdate,

    /**
     * Use {@link #LOG_SYNCHRONIZATION} instead.
     */
    @Deprecated
    LogSynchronization,

    /**
     * Use {@link #NOTIFICATIONS} instead.
     */
    @Deprecated
    Notifications,

    /**
     * This value is unused and will be removed.
     */
    @Deprecated
    Sedentary,

    /**
     * This value is unused and will be removed.
     */
    @Deprecated
    TargetHeartrateZone,

    /**
     * This value is unused and will be removed.
     */
    @Deprecated
    Targets,

    /**
     * This value is unused and will be removed.
     */
    @Deprecated
    UserConfiguration,

    /**
     * This value is unused and will be removed.
     */
    @Deprecated
    WearingPosition,

    /**
     * Use {@link #BATTERY} instead.
     */
    @Deprecated
    Battery,

    /**
     * Use {@link #USER_INFORMATION_LIFE_SENSE} instead.
     */
    @Deprecated
    UserInformationLifeSense,

    DATA_STREAMING,

    /**
     * Device information provides information such as FirmwareRevision, HardwareRevision and SerialNumber.
     *
     * @see SHNCapabilityDeviceInformation
     */
    DEVICE_INFORMATION,
    FIRMWARE_UPDATE,
    LOG_SYNCHRONIZATION,
    NOTIFICATIONS,
    WEARING_POSITION,
    BATTERY,
    USER_INFORMATION_LIFE_SENSE,
    DEVICE_DIAGNOSTIC;

    public static SHNCapabilityType fixDeprecation(SHNCapabilityType capabilityType) {
        switch (capabilityType) {
            case DataStreaming:
                capabilityType = DATA_STREAMING;
                break;
            case DeviceInformation:
                capabilityType = DEVICE_INFORMATION;
                break;
            case FirmwareUpdate:
                capabilityType = FIRMWARE_UPDATE;
                break;
            case LogSynchronization:
                capabilityType = LOG_SYNCHRONIZATION;
                break;
            case Notifications:
                capabilityType = NOTIFICATIONS;
                break;
            case Sedentary:
            case TargetHeartrateZone:
            case Targets:
            case UserConfiguration:
            case WearingPosition:
                break;
            case Battery:
                capabilityType = BATTERY;
                break;
            case UserInformationLifeSense:
                capabilityType = USER_INFORMATION_LIFE_SENSE;
                break;
        }

        return capabilityType;
    }

    public static SHNCapabilityType getDeprecated(SHNCapabilityType capabilityType) {
        switch (capabilityType) {
            case DATA_STREAMING:
                capabilityType = DataStreaming;
                break;
            case DEVICE_INFORMATION:
                capabilityType = DeviceInformation;
                break;
            case FIRMWARE_UPDATE:
                capabilityType = FirmwareUpdate;
                break;
            case LOG_SYNCHRONIZATION:
                capabilityType = LogSynchronization;
                break;
            case NOTIFICATIONS:
                capabilityType = Notifications;
                break;
            case BATTERY:
                capabilityType = Battery;
                break;
            case USER_INFORMATION_LIFE_SENSE:
                capabilityType = UserInformationLifeSense;
                break;
        }

        return capabilityType;
    }

    public static boolean isDeprecated(SHNCapabilityType capabilityType) {
        switch (capabilityType) {
            case DataStreaming:
            case DeviceInformation:
            case FirmwareUpdate:
            case LogSynchronization:
            case Notifications:
            case Sedentary:
            case TargetHeartrateZone:
            case Targets:
            case UserConfiguration:
            case WearingPosition:
            case Battery:
            case UserInformationLifeSense:
                return true;
            default:
                return false;
        }
    }
}
