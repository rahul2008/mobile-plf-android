/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class EnvironmentPreferences {

    private static final String PREF_FILE = "Environment";
    private static final String ENVIRONMENT_SELECTED = "environment_selected";
    private static final String TST_ENV = "tst.pl.shop.philips.com";
    private static final String ACC_ENV = "acc.occ.shop.philips.com";

    private SharedPreferences mPrefs;

    public EnvironmentPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    int getSelectedEnvironmentIndex() {
        return mPrefs.getInt(ENVIRONMENT_SELECTED, 0);
    }

    void saveEnvironmentPrefrence(int country) {
        mPrefs.edit().putInt(ENVIRONMENT_SELECTED, country).commit();
    }

    void clearEnvironmentPreference() {
        mPrefs.edit().remove(ENVIRONMENT_SELECTED).commit();
    }

    String getSelectedEnvironment(){
        if(mPrefs.getInt(ENVIRONMENT_SELECTED, 0) == 1){
            return TST_ENV;
        }else if(mPrefs.getInt(ENVIRONMENT_SELECTED, 0) == 2){
            return ACC_ENV;
        }else {
            return TST_ENV;
        }
    }
}
