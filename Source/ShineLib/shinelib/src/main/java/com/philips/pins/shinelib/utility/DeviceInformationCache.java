package com.philips.pins.shinelib.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceInformationCache {

    public static final String DEVICE_INFORMATION_CACHE = "DeviceInformationCache";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SUFFIX = "date";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public DeviceInformationCache(@NonNull final Context context) {
        sharedPreferences = context.getSharedPreferences(ShinePreferenceWrapper.SHINELIB_PREFERENCES_FILE_KEY + DEVICE_INFORMATION_CACHE, Context.MODE_PRIVATE);
    }

    public void save(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType, @NonNull final String value) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(informationType.name(), value);
        edit.putString(informationType.name() + DATE_SUFFIX, getSimpleDateFormat().format(new Date()));
        edit.commit();
    }

    @Nullable
    public Date getDate(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        String dateString = sharedPreferences.getString(informationType.name() + DATE_SUFFIX, null);
        Date date = null;

        if (dateString != null) {
            try {
                date = getSimpleDateFormat().parse(dateString);
            } catch (ParseException e) {
            }
        }

        return date;
    }

    @Nullable
    public String getValue(@NonNull final SHNCapabilityDeviceInformation.SHNDeviceInformationType informationType) {
        return sharedPreferences.getString(informationType.name(), null);
    }

    @NonNull
    SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DATE_PATTERN);
    }
}
