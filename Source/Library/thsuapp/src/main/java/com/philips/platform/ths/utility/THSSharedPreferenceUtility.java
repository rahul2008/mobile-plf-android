package com.philips.platform.ths.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class THSSharedPreferenceUtility {

    protected final static String SHARED_PREF_HELPER_FILE = "THS_SHARED_PREF_FILE";

    /**
     * Set a string shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    /**
     * Set a integer shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setInt(Context context, String key, int value){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    /**
     * Set a Boolean shared preference
     * @param key - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    /**
     * Get a string shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getString(Context context, String key, String defValue){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * Get a integer shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getInt(Context context, String key, int defValue){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * Get a boolean shared preference
     * @param key - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getBoolean(Context context, String key, boolean defValue){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_HELPER_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

}
