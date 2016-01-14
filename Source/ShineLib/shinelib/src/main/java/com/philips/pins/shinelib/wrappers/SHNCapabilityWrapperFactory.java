/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigWearingPosition;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataStreaming;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.capabilities.SHNCapabilityNotifications;
import com.philips.pins.shinelib.capabilities.SHNCapabilityUserInformationLifeSense;

public class SHNCapabilityWrapperFactory {
    public static SHNCapability createCapabilityWrapper(SHNCapability shnCapability, SHNCapabilityType shnCapabilityType, Handler internalHandler, Handler userHandler) {
        SHNCapability shnCapabilityWrapper;
        switch (shnCapabilityType) {
            case Notifications:
            case NOTIFICATIONS:
                shnCapabilityWrapper = new SHNCapabilityNotificationsWrapper(
                        (SHNCapabilityNotifications) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case DeviceInformation:
            case DEVICE_INFORMATION:
                shnCapabilityWrapper = new SHNCapabilityDeviceInformationWrapper(
                        (SHNCapabilityDeviceInformation) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case DataStreaming:
            case DATA_STREAMING:
                shnCapabilityWrapper = new SHNCapabilityDataStreamingWrapper(
                        (SHNCapabilityDataStreaming) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case LogSynchronization:
            case LOG_SYNCHRONIZATION:
                shnCapabilityWrapper = new SHNCapabilityLogSynchronizationWrapper(
                        (SHNCapabilityLogSynchronization) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case Battery:
            case BATTERY:
                shnCapabilityWrapper = new SHNCapabilityBatteryWrapper(
                        (SHNCapabilityBattery) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case UserInformationLifeSense:
            case USER_INFORMATION_LIFE_SENSE:
                shnCapabilityWrapper = new SHNCapabilityUserInformationLifeSenseWrapper(
                        (SHNCapabilityUserInformationLifeSense) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case WearingPosition:
            case WEARING_POSITION:
                shnCapabilityWrapper = new SHNCapabilityConfigWearingPositionWrapper(
                        (SHNCapabilityConfigWearingPosition) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case FirmwareUpdate:
            case FIRMWARE_UPDATE:
                shnCapabilityWrapper = new SHNCapabilityFirmwareUpdateWrapper(
                        (SHNCapabilityFirmwareUpdate) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case DEVICE_DIAGNOSTIC:
                shnCapabilityWrapper = new SHNCapabilityDeviceDiagnosticsWrapper(
                        (SHNCapabilityDeviceDiagnostics) shnCapability,
                        internalHandler,
                        userHandler);
                break;
            case Sedentary:
                shnCapabilityWrapper = new SHNCapabilityConfigSedentaryWrapper(
                        (SHNCapabilityConfigSedentary) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            default:
                throw new IllegalStateException("No wrapper for capability: " + shnCapabilityType);
        }
        return shnCapabilityWrapper;
    }
}
