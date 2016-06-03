/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNStringResultListener;

import java.util.Date;

/**
 * Device information provides information such as described by {@link SHNDeviceInformationType}.
 * This information can be queried even if the device is offline, if it has had a successful connection at least once.
 */
public interface SHNCapabilityDeviceInformation extends SHNCapability {

    /**
     * Callback interface for {@link #readDeviceInformation(SHNDeviceInformationType, Listener)}.
     */
    interface Listener {

        /**
         * The requested data has been retrieved successfully.
         *
         * @param deviceInformationType type of information retrieved
         * @param value                 value of the information
         * @param dateWhenAcquired      date when the value was acquired
         */
        void onDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final String value, @NonNull final Date dateWhenAcquired);

        /**
         * An error occurred while retrieving the required information.
         *
         * @param deviceInformationType type of information retrieved
         * @param error                 that occurred
         */
        void onError(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final SHNResult error);
    }

    /**
     * Supported device information types.
     */
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

    /**
     * Use {@link #readDeviceInformation(SHNDeviceInformationType, Listener)} instead.
     */
    @Deprecated
    void readDeviceInformation(@NonNull final SHNDeviceInformationType shnDeviceInformationType, @NonNull final SHNStringResultListener shnStringResultListener);

    /**
     * Read out the device information.
     *
     * @param deviceInformationType type of information to read
     * @param listener              to receive callbacks on
     */
    void readDeviceInformation(@NonNull final SHNDeviceInformationType deviceInformationType, @NonNull final Listener listener);
}
