package com.philips.cdp.di.iap.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class IapSharedPreference {

    private Context mContext;
    private final transient SharedPreferences mDemoSharedPreference;
    private transient SharedPreferences.Editor mEditor;


    public IapSharedPreference(final Context context) {
        mContext = context;
        mDemoSharedPreference = mContext.getSharedPreferences("IapPref", Context.MODE_PRIVATE);
    }

    public String getString(String key){
        return mDemoSharedPreference.getString(key, null);
    }

    public void setString(String key, String value){
        mEditor = mDemoSharedPreference.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void clearPreference(){
        mEditor = mDemoSharedPreference.edit();
        mEditor.clear().commit();
    }

}
