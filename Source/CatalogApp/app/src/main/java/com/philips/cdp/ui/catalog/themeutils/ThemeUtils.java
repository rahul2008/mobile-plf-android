package com.philips.cdp.ui.catalog.themeutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.ThemesActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ThemeUtils {

    private static final int DEFAULT_THEME = 0;
    private static final String THEME_STATE = "THEME_STATE";
    private static TreeMap<String, int[]> treeMap = new TreeMap<>();
    private static final String BLUE = "blue";
    private static final String ORANGE = "orange";
    private static final String CURRENT_THEME_STATE = "current_theme_state";
    private static final String DEFAULT_THEME_STATE = "blue|false|solid|0";

    static {
        int[] blue_themes = {R.style.Theme_Philips_Default_LightBlue_WhiteBackground, R.style.Theme_Philips_Default_LightBlue,
                R.style.Theme_Philips_Default_GradientBlue_WhiteBackground,
                R.style.Theme_Philips_Default_GradientBlue};
        int[] orange_themes = {R.style.Theme_Philips_Default_Orange_WhiteBackground, R.style.Theme_Philips_Default_Orange,
                R.style.Theme_Philips_Default_GradientOrange_WhiteBackground,
                R.style.Theme_Philips_Default_GradientOrange};
        treeMap.put(BLUE, blue_themes);
        treeMap.put(ORANGE, orange_themes);

    }

    private static int[] themes = {R.style.Theme_Philips_Default_LightBlue,
            R.style.Theme_Philips_Default_LightBlue_WhiteBackground,
            R.style.Theme_Philips_Default_Orange,
            R.style.Theme_Philips_Default_Orange_WhiteBackground,
            R.style.Theme_Philips_Default_GradientBlue,
            R.style.Theme_Philips_Default_GradientBlue_WhiteBackground
    };

    public static void setThemePreferences(Context context, boolean previous) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int theme = getThemeIndex(prefs, previous);
        prefs.edit().putInt(THEME_STATE, theme).apply();
    }

    public static ArrayList<String> getTokens(String prefData){
        ArrayList<String> tokens = new ArrayList<String>();
        StringTokenizer tokenParser = new StringTokenizer(prefData, "|");
        while (tokenParser.hasMoreTokens()) {
            String token = tokenParser.nextToken();
            tokens.add(token);
        }
        return tokens;
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);
        return themes[index];
    }

    public static int getThemeByKey(String key, int index){
        int[] data = treeMap.get(key);
        return data[index];
    }

    private static int getThemeIndex(final SharedPreferences prefs, final boolean previous) {
        int index = prefs.getInt(THEME_STATE, DEFAULT_THEME);
        if (!previous)
            return getNextTheme(index);
        else
            return getPreviousTheme(index);
    }

    private static int getPreviousTheme(final int index) {
        if (index <= 0) {
            return (themes.length - 1);
        }
        return (index - 1);
    }

    private static int getNextTheme(final int index) {
        if (index == (themes.length - 1))
            return DEFAULT_THEME;
        else
            return (index + 1);
    }

    public static String getThemePreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String theme_state = prefs.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        return theme_state;
    }

    public static void setThemePreferences(Context context, String prefData) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        prefs.edit().putString(CURRENT_THEME_STATE, prefData).apply();
    }
}
