/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

public enum SHNCapabilityType {
    /**
     * Use DATA_STREAMING instead.
     */
    @Deprecated
    DataStreaming,

    /**
     * Use DEVICE_INFORMATION instead.
     */
    @Deprecated
    DeviceInformation,

    /**
     * Use FIRMWARE_UPDATE instead.
     */
    @Deprecated
    FirmwareUpdate,

    /**
     * Use LOG_SYNCHRONIZATION instead.
     */
    @Deprecated
    LogSynchronization,

    /**
     * Use NOTIFICATIONS instead.
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
     * Use BATTERY instead.
     */
    @Deprecated
    Battery,

    /**
     * Use USER_INFORMATION_LIFE_SENSE instead.
     */
    @Deprecated
    UserInformationLifeSense,

    DATA_STREAMING,
    DEVICE_INFORMATION,
    FIRMWARE_UPDATE,
    LOG_SYNCHRONIZATION,
    NOTIFICATIONS,
    WEARING_POSITION,
    BATTERY,
    USER_INFORMATION_LIFE_SENSE,
    DEVICE_DIAGNOSTIC
}
