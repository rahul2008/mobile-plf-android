package com.pins.philips.shinelib.wrappers;

import android.os.Handler;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNCapabilityType;
import com.pins.philips.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.pins.philips.shinelib.capabilities.SHNCapabilityNotifications;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by 310188215 on 29/04/15.
 */
public class SHNCapabilityWrapperFactory {
    public static SHNCapability createCapabilityWrapper(SHNCapability shnCapability, SHNCapabilityType shnCapabilityType, ScheduledThreadPoolExecutor shnExecutor, Handler userHandler) {
        SHNCapability shnCapabilityWrapper;
        switch (shnCapabilityType) {
            case Notifications:
                shnCapabilityWrapper = new SHNCapabilityNotificationsWrapper(
                        (SHNCapabilityNotifications)shnCapability,
                        shnExecutor,
                        userHandler);
                break;
            case DeviceInformation:
                shnCapabilityWrapper = new SHNCapabilityDeviceInformationWrapper(
                        (SHNCapabilityDeviceInformation)shnCapability,
                        shnExecutor,
                        userHandler);
                break;
            default:
                throw new IllegalStateException("No wrapper for capability: " + shnCapabilityType);
        }
        return shnCapabilityWrapper;
    }
}
