package com.philips.cdp.ui.catalog.themeutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.cdp.ui.catalog.ColorType;
import com.philips.cdp.ui.catalog.R;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ThemeUtils {

    private static final int DEFAULT_THEME = 0;
    public static final String delimiters = "|";
    private static TreeMap<String, int[]> themesMap = new TreeMap<>();
    private static final String CURRENT_THEME_STATE = "current_theme_state";
    private static final String DEFAULT_THEME_STATE = "blue|false|solid|0";
    private static String COLOR_STRING = ColorType.BLUE.name();

    public static void setCOLOR_STRING(String COLOR_STRING) {
        ThemeUtils.COLOR_STRING = COLOR_STRING;
    }

    public static String getCOLOR_STRING() {
        return COLOR_STRING;
    }

    static {
        int[] blue_themes = {R.style.Theme_Philips_Default_LightBlue_WhiteBackground, R.style.Theme_Philips_Default_LightBlue,
                R.style.Theme_Philips_Default_GradientBlue_WhiteBackground,
                R.style.Theme_Philips_Default_GradientBlue};
        int[] orange_themes = {R.style.Theme_Philips_Default_Orange_WhiteBackground, R.style.Theme_Philips_Default_Orange,
                R.style.Theme_Philips_Default_GradientOrange_WhiteBackground,
                R.style.Theme_Philips_Default_GradientOrange};
        themesMap.put(ColorType.BLUE.getDescription(), blue_themes);
        themesMap.put(ColorType.ORANGE.getDescription(), orange_themes);

    }

    private static int[] themes = {
            R.style.Theme_Philips_Default_LightBlue_WhiteBackground,
            R.style.Theme_Philips_Default_Orange_WhiteBackground
    };

    public static void setThemePreferences(Context context, boolean previous) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        int theme = getThemeIndex(prefs, previous);
        String data = ColorType.fromId(theme).getDescription() + "|false|solid|0";
        prefs.edit().putString(CURRENT_THEME_STATE, data).apply();
    }

    public static ArrayList<String> getTokens(String prefData) {
        ArrayList<String> tokens = new ArrayList<String>();
        StringTokenizer tokenParser = new StringTokenizer(prefData, delimiters);
        while (tokenParser.hasMoreTokens()) {
            tokens.add(tokenParser.nextToken());
        }
        return tokens;
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String index = prefs.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        ArrayList<String> tokens = getTokens(index);
        setCOLOR_STRING(tokens.get(0));

        return getThemeValue(tokens);
    }

    private static int getThemeValue(ArrayList<String> tokens) {
        String key = tokens.get(0);
        return getThemeByKey(key, Integer.parseInt(tokens.get(3)));
    }

    public static int getThemeByKey(String key, int index) {
        int[] data = themesMap.get(key);
        return data[index];
    }

    private static int getThemeIndex(final SharedPreferences prefs, final boolean previous) {
        String data = prefs.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        ArrayList<String> tokens = getTokens(data);
        int index = ColorType.fromValue(tokens.get(0)).getId();
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
