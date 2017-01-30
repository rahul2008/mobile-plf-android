/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RecyclerViewSettingsHelper {
    public static final String IS_SEPERATOR_ENABLED = "isSeparatorEnabled";
    public static final String IS_HEADER_ENABLED = "isHeaderEnabled";
    public static final String IS_ICON_TEMPLATE_SELECTED = "isIconTemplateSelected";

    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public RecyclerViewSettingsHelper(final Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public boolean isSeperatorEnabled() {
        return sharedPreferences.getBoolean(IS_SEPERATOR_ENABLED, false);
    }

    public void setSeperatorEnabled(boolean enabled) {
        editor.putBoolean(IS_SEPERATOR_ENABLED, enabled).apply();
    }

    public boolean isHeaderEnabled() {
        return sharedPreferences.getBoolean(IS_HEADER_ENABLED, false);
    }

    public void setHeaderEnabled(boolean enabled) {
        editor.putBoolean(IS_HEADER_ENABLED, enabled).apply();
    }

    public boolean isIconTemplateSelected() {
        return sharedPreferences.getBoolean(IS_ICON_TEMPLATE_SELECTED, false);
    }

    public void setIconTemplateSelected(boolean selected) {
        editor.putBoolean(IS_ICON_TEMPLATE_SELECTED, selected).apply();
    }
}
