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

    private static int[] themes = {R.style.Theme_Philips_Default_LightBlue,
            R.style.Theme_Philips_Default_LightBlue_WhiteBackground,
            R.style.Theme_Philips_Default_Orange,
            R.style.Theme_Philips_Default_Orange_WhiteBackground,
            R.style.Theme_Philips_Default_GradientBlue,
            R.style.Theme_Philips_Default_GradientBlue_WhiteBackground
    };

    public static void setThemePreferences(Context context , boolean previous) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int theme = getThemeIndex(prefs, previous);
        prefs.edit().putInt(THEME_STATE, theme).apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);
        return themes[index];
    }

    private static int getThemeIndex(final SharedPreferences prefs, final boolean previous) {
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);
        if(!previous)
            return getNextTheme(index);
        else
            return getPreviousTheme(index);
    }

    private static int getPreviousTheme(final int index) {
        if(index <= 0){
            return (themes.length-1);
        }
        return (index-1);
    }

    private static int getNextTheme(final int index) {
        if (index == (themes.length - 1))
            return DEFAULT_THEME;
        else
            return (index + 1);
    }
}
