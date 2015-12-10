/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;

import java.util.Date;

public interface SHNCapabilityDeviceInformation extends SHNCapability {

    interface Listener {
        void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired);

        void onError(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error);
    }

    enum SHNDeviceInformationType {
        ManufacturerName,
        ModelNumber,
        SerialNumber,
        HardwareRevision,
        FirmwareRevision,
        SoftwareRevision,
        SystemID,
        DeviceCloudComponentVersion,
        DeviceCloudComponentName,
        CTN,
        Unknown
    }

    @Deprecated
    void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener);

    void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener);
}
