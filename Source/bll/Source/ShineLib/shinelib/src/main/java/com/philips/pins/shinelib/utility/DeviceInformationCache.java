package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.Date;

public class DeviceInformationCache {

    public static final String DATE_SUFFIX = "date";

    @NonNull
    private final PersistentStorage persistentStorage;

    public DeviceInformationCache(@NonNull final PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }

    public void save(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final String value) {
        persistentStorage.put(informationType.name(), value);
        persistentStorage.put(informationType.name() + DATE_SUFFIX, (new Date()).getTime());
    }

    @Nullable
    public Date getDate(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        long dateMillis = persistentStorage.get(informationType.name() + DATE_SUFFIX, 0L);
        Date date = null;

        if (dateMillis > 0) {
            date = new Date(dateMillis);
        }

        return date;
    }

    @Nullable
    public String getValue(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        return persistentStorage.get(informationType.name());
    }
}
