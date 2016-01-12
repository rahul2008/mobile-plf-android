package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.Date;

public class DeviceInformationCache {

    public static final String DATE_SUFFIX = "date";

    @NonNull
    private final SHNDevicePersistentStorage shnDevicePersistentStorage;

    public DeviceInformationCache(@NonNull final SHNDevicePersistentStorage preferenceWrapper) {
        this.shnDevicePersistentStorage = preferenceWrapper;
    }

    public void save(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final String value) {
        shnDevicePersistentStorage.putString(informationType.name(), value);
        shnDevicePersistentStorage.putLong(informationType.name() + DATE_SUFFIX, (new Date()).getTime());
    }

    @Nullable
    public Date getDate(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        long dateMillis = shnDevicePersistentStorage.getLong(informationType.name() + DATE_SUFFIX);
        Date date = null;

        if (dateMillis > 0) {
            date = new Date(dateMillis);
        }

        return date;
    }

    @Nullable
    public String getValue(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        return shnDevicePersistentStorage.getString(informationType.name());
    }
}
