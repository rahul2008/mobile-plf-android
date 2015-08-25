package com.philips.cdp.ui.catalog.themeutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ThemeUtils {

    private static final int DEFAULT_THEME = 0;
    private static final String THEME_STATE = "THEME_STATE";

    private static int[] themes = {R.style.PhilipsTheme_Default_Light,
            R.style.PhilipsTheme_Default_Dark,
            R.style.PhilipsTheme_Default_Dark_Blue_Gradient
    };

    public static void setThemePreferences(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);

        int theme = getThemeIndex(prefs);
        prefs.edit().putInt(THEME_STATE, theme).apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);

        return themes[index];
    }

    public static int getThemeIndex(final SharedPreferences prefs) {
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);
        if (index == (themes.length - 1))
            return DEFAULT_THEME;
        else
            return (index + 1);
    }
}
