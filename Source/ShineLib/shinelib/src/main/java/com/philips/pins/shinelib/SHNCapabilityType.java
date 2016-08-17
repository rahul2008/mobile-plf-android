/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.capabilities.CapabilityGenericCharacteristic;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

/**
 * Capability types supported by BlueLib.
 * Use the value to access a capability from a device via {@link com.philips.pins.shinelib.SHNDevice#getCapabilityForType(SHNCapabilityType)}
 */
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

    /**
     * Data streaming provides an interface to enable data streaming for various {@link com.philips.pins.shinelib.datatypes.SHNDataType}.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityDataStreaming
     */
    DATA_STREAMING,

    /**
     * Device information provides information such as FirmwareRevision, HardwareRevision and SerialNumber.
     *
     * @see SHNCapabilityDeviceInformation
     */
    DEVICE_INFORMATION,

    /**
     * Firmware update provides an interface to update device to a different version.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate
     */
    FIRMWARE_UPDATE,

    /**
     * Log synchronization provides an interface to retrieve stored measurements from a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization
     */
    LOG_SYNCHRONIZATION,

    /**
     * Notifications provides an interface to show notification on a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityNotifications
     */
    NOTIFICATIONS,

    /**
     * Wearing position provides an interface to set and retrieve wearing position from a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityConfigWearingPosition
     */
    WEARING_POSITION,

    /**
     * Battery provides an interface to retrieve battery level from a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityBattery
     */
    BATTERY,

    /**
     * Plugin specific value.
     */
    USER_INFORMATION_LIFE_SENSE,

    /**
     * Sedentary provides an interface to retrieve a sedentary period and subscribe for notifications from a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary
     */
    CONFIG_SEDENTARY,

    /**
     * Device diagnostic provides interface to retrieve device diagnostic from a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics
     */
    DEVICE_DIAGNOSTIC,

    /**
     * Heart rate provides interface to set and get heart rate zones for a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones
     */
    CONFIG_HEARTRATE_ZONES,

    /**
     * Targets provide interface to set and get heart rate zones for a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityConfigTargets
     */
    CONFIG_TARGETS,

    /**
     * Energy intake provides an interface to set meal configurations for a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake
     */
    CONFIG_ENERGY_INTAKE,

    /**
     * Provides an interface to clear all user data on a peripheral.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityClearUserData
     */
    CLEAR_USER_DATA,

    /**
     * Moonshine plugin specific value.
     *
     * @see com.philips.pins.shinelib.capabilities.SHNCapabilityDataModelDebugging
     */
    DATA_MODEL_DEBUG,

    /**
     * Generic plugin for reading, writing data and notifications
     *
     * @see CapabilityGenericCharacteristic
     */
    GENERIC;

    /**
     * Provides support for backwards compatibility,
     */
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
            case CONFIG_ENERGY_INTAKE:
            case CLEAR_USER_DATA:
            case GENERIC:
            default:
                break;
        }

        return capabilityType;
    }
}
