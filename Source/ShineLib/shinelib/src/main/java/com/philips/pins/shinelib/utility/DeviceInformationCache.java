package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.util.Date;

public class DeviceInformationCache {

    public static final String DEVICE_INFORMATION_CACHE = "DeviceInformationCache";
    public static final String DATE_SUFFIX = "date";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public DeviceInformationCache(@NonNull final Context context) {
        sharedPreferences = context.getSharedPreferences(ShinePreferenceWrapper.SHINELIB_PREFERENCES_FILE_KEY + DEVICE_INFORMATION_CACHE, Context.MODE_PRIVATE);
    }

    public void save(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final String value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(informationType.name(), value);
        edit.putLong(informationType.name() + DATE_SUFFIX, (new Date()).getTime());
        edit.commit();
    }

    @Nullable
    public Date getDate(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        long dateMillis = sharedPreferences.getLong(informationType.name() + DATE_SUFFIX, 0);
        Date date = null;

        if (dateMillis > 0) {
            date = new Date(dateMillis);
        }

        return date;
    }

    @Nullable
    public String getValue(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        return sharedPreferences.getString(informationType.name(), null);
    }
}
