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
    CONFIG_SEDENTARY,
    DEVICE_DIAGNOSTIC,
    CONFIG_HEARTRATE_ZONES,
    CONFIG_TARGETS;

    public static SHNCapabilityType getCounterPart(SHNCapabilityType capabilityType) {
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
            case WEARING_POSITION:
                capabilityType = WearingPosition;
                break;
            case USER_INFORMATION_LIFE_SENSE:
                capabilityType = UserInformationLifeSense;
                break;

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
            case WearingPosition:
                capabilityType = WEARING_POSITION;
                break;
            case Battery:
                capabilityType = BATTERY;
                break;
            case UserInformationLifeSense:
                capabilityType = USER_INFORMATION_LIFE_SENSE;
                break;

            case Sedentary:
            case TargetHeartrateZone:
            case Targets:
            case UserConfiguration:
            case CONFIG_TARGETS:
            case CONFIG_HEARTRATE_ZONES:
            default:
                break;
        }

        return capabilityType;
    }
}
