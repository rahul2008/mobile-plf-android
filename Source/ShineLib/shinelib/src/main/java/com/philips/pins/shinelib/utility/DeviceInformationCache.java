package com.philips.pins.shinelib.utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.Date;

public class DeviceInformationCache {

    public static final String DATE_SUFFIX = "date";

    @NonNull
    private final SHNDevicePreferenceWrapper preferenceWrapper;

    public DeviceInformationCache(@NonNull final SHNDevicePreferenceWrapper preferenceWrapper) {
        this.preferenceWrapper = preferenceWrapper;
    }

    public void save(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final String value) {
        preferenceWrapper.putString(informationType.name(), value);
        preferenceWrapper.putLong(informationType.name() + DATE_SUFFIX, (new Date()).getTime());
    }

    @Nullable
    public Date getDate(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        long dateMillis = preferenceWrapper.getLong(informationType.name() + DATE_SUFFIX);
        Date date = null;

        if (dateMillis > 0) {
            date = new Date(dateMillis);
        }

        return date;
    }

    @Nullable
    public String getValue(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        return preferenceWrapper.getString(informationType.name());
    }
}
