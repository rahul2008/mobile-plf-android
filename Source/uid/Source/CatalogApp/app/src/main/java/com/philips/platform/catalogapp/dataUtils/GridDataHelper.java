package com.philips.platform.catalogapp.dataUtils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GridDataHelper {
    public static final String IS_ENLARGED_GUTTER_ENABLED = "isEnlargedGutterEnabled";
    public static final String IS_SET_DISABLE_STATE_ENABLED = "isSetDisableStateEnabled";
    public static final String TEMPLATE_SELECTION = "templateSelection";

    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public GridDataHelper(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public boolean isEnlargedGutterEnabled() {
        return sharedPreferences.getBoolean(IS_ENLARGED_GUTTER_ENABLED, false);
    }

    public void setEnlargedGutterEnabled(boolean enabled) {
        editor.putBoolean(IS_ENLARGED_GUTTER_ENABLED, enabled).apply();
    }

    public boolean isSetDisableStateEnabled() {
        return sharedPreferences.getBoolean(IS_SET_DISABLE_STATE_ENABLED, false);
    }

    public void setSetDisableStateEnabled(boolean enabled) {
        editor.putBoolean(IS_SET_DISABLE_STATE_ENABLED, enabled).apply();
    }

    public int getTemplateSelection(){
        return sharedPreferences.getInt(TEMPLATE_SELECTION, 1);
    }

    public void setTemplateSelection(int templateSelection) {
        editor.putInt(TEMPLATE_SELECTION, templateSelection).apply();
    }
}