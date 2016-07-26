package com.philips.platform.appframework.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class SharedPreferenceUtility {
    private static SharedPreferenceUtility mInstance;
    private Context mContext;
    //
    private SharedPreferences mMyPreferences;

    private SharedPreferenceUtility(){ }

    public static SharedPreferenceUtility getInstance(){
        if (mInstance == null) mInstance = new SharedPreferenceUtility();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    public void writePreferenceString(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public void writePreferenceBoolean(String key, boolean value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public void writePreferenceInt(String key,@UIState.UIStateDef int value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putInt(key, value);
        e.commit();
    }

    public String getPreferenceString(String key){
        return mMyPreferences.getString(key,"");

    }

    public boolean getPreferenceBoolean(String key) {
        return mMyPreferences.getBoolean(key, false);
    }

    @UIState.UIStateDef public int getPreferenceInt(String key) {
        return mMyPreferences.getInt(key, UIState.UI_SPLASH_UNREGISTERED_STATE);
    }
    public boolean contains(String key){
        return mMyPreferences.contains(key);
    }
}
