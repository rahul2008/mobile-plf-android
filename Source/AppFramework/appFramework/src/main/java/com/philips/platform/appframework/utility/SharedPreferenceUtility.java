/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.philips.platform.modularui.statecontroller.UIState;

public class SharedPreferenceUtility {
    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPreferenceUtility(Context ctxt){
        context = ctxt;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getMyPreferences() {
        return sharedPreferences;
    }

    public void writePreferenceString(String key, String value){
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public void writePreferenceBoolean(String key, boolean value){
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public void writePreferenceInt(String key,int value){
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(key, value);
        e.commit();
    }

    public String getPreferenceString(String key){
        return sharedPreferences.getString(key,"");

    }

    public boolean getPreferenceBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

 public int getPreferenceInt(String key) {
        return sharedPreferences.getInt(key, UIState.UI_SPLASH_UNREGISTERED_STATE);
    }
    public boolean contains(String key){
        return sharedPreferences.contains(key);
    }
}
