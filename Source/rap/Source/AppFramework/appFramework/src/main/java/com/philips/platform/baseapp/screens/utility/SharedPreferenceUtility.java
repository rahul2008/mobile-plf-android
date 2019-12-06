/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SharedPreferenceUtility {

    private Context context;
    @NonNull private SharedPreferences mMyPreferences;

    public SharedPreferenceUtility(@NonNull Context ctxt){
        context = ctxt;
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    public SharedPreferences getMyPreferences() {
        return mMyPreferences;
    }

    public void writePreferenceString(@NonNull String key, @Nullable String value) {
        mMyPreferences.edit().putString(key, value).apply();
    }

    public void writePreferenceBoolean(@NonNull String key, boolean value) {
        mMyPreferences.edit().putBoolean(key, value).apply();
    }

    public void writePreferenceInt(@NonNull String key, int value) {
        mMyPreferences.edit().putInt(key, value).apply();
    }

    public String getPreferenceString(@NonNull String key){
        return mMyPreferences.getString(key,"");

    }

    public boolean getPreferenceBoolean(@NonNull String key) {
        return mMyPreferences.getBoolean(key, false);
    }

    public int getPreferenceInt(@NonNull String key) {
        return mMyPreferences.getInt(key,0);
    }

    public boolean contains(@NonNull String key){
        return mMyPreferences.contains(key);
    }
}
