/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.capabilities.CapabilityBluetoothDirect;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.capabilities.SHNCapabilityClearUserData;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigSedentary;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigTargets;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigWearingPosition;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataModelDebugging;
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
            case CONFIG_SEDENTARY:
                shnCapabilityWrapper = new SHNCapabilityConfigSedentaryWrapper(
                        (SHNCapabilityConfigSedentary) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case CONFIG_TARGETS:
                shnCapabilityWrapper = new SHNCapabilityConfigTargetsWrapper(
                        (SHNCapabilityConfigTargets) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case CONFIG_HEARTRATE_ZONES:
                shnCapabilityWrapper = new SHNCapabilityConfigHeartRateZonesWrapper(
                        (SHNCapabilityConfigHeartRateZones) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case CONFIG_ENERGY_INTAKE:
                shnCapabilityWrapper = new SHNCapabilityConfigEnergyIntakeWrapper(
                        (SHNCapabilityConfigEnergyIntake) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case CLEAR_USER_DATA:
                shnCapabilityWrapper = new SHNCapabilityClearUserDataWrapper(
                        (SHNCapabilityClearUserData) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case DATA_MODEL_DEBUG:
                shnCapabilityWrapper = new SHNCapabilityDataModelDebuggingWrapper(
                        (SHNCapabilityDataModelDebugging) shnCapability,
                        internalHandler,
                        userHandler
                );
                break;
            case BLUETOOTH_DIRECT:
                shnCapabilityWrapper = new CapabilityBluetoothDirectWrapper(
                    (CapabilityBluetoothDirect) shnCapability,
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
