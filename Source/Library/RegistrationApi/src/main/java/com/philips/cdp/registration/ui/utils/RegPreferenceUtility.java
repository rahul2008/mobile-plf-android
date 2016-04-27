package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.registration.settings.RegistrationSettings;


public class RegPreferenceUtility {

    public static void storePreference(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getStoredState(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(
                RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
        return myPrefs.getBoolean(key, false);
    }
}
