package com.philips.cdp.ui.catalog.themeutils;

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

    private final int DEFAULT_THEME = 0;
    public static final String DELIMITER = "|";
    private TreeMap<String, int[]> themesMap = new TreeMap<>();
    private final String CURRENT_THEME_STATE = "current_theme_state";
    private final String DEFAULT_THEME_STATE = "blue|false|solid|0";
    private String COLOR_STRING = ColorType.BLUE.getDescription();
    private SharedPreferences sharedPreferences;

    public ThemeUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        appendThemes();
    }

    public void setColorString(String COLOR_STRING) {
        this.COLOR_STRING = COLOR_STRING;
    }

    public String getColorString() {
        return COLOR_STRING;
    }

    private void appendThemes(){
                R.style.Theme_Philips_GradientBlue_WhiteBackground,
                R.style.Theme_Philips_GradientBlue};
                R.style.Theme_Philips_GradientOrange_WhiteBackground,
                R.style.Theme_Philips_GradientOrange};
        int[] aqua_themes = {R.style.Theme_Philips_VeryLightAqua_WhiteBackground, R.style.Theme_Philips_VeryLightAqua,
                R.style.Theme_Philips_GradientAqua_WhiteBackground,
                R.style.Theme_Philips_GradientAqua};
        themesMap.put(ColorType.BLUE.getDescription(), blue_themes);
        themesMap.put(ColorType.ORANGE.getDescription(), orange_themes);
        themesMap.put(ColorType.AQUA.getDescription(), aqua_themes);
    }

    private int[] themes = {
            R.style.Theme_Philips_VeryLightBlue_WhiteBackground,
            R.style.Theme_Philips_VeryLightOrange_WhiteBackground,
			R.style.Theme_Philips_VeryLightAqua_WhiteBackground
    };

    public void setThemePreferences(boolean previous) {
        int theme = getThemeIndex(previous);
        String data = ColorType.fromId(theme).getDescription() + "|false|solid|0";
        sharedPreferences.edit().putString(CURRENT_THEME_STATE, data).apply();
    }

    public ArrayList<String> getThemeTokens(String prefData) {
        ArrayList<String> themeTokens = new ArrayList<String>();
        StringTokenizer tokenParser = new StringTokenizer(prefData, DELIMITER);
        while (tokenParser.hasMoreTokens()) {
            themeTokens.add(tokenParser.nextToken());
        }
        return themeTokens;
    }

    public int getTheme() {
        String index = sharedPreferences.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        ArrayList<String> themeTokens = getThemeTokens(index);
        setColorString(themeTokens.get(0));
        return getThemeValue(themeTokens);
    }

    private int getThemeValue(ArrayList<String> themeTokens) {
        String key = themeTokens.get(0);
        return getThemeByKey(key, Integer.parseInt(themeTokens.get(3)));
    }

    private int getThemeByKey(String key, int index) {
        int[] data = themesMap.get(key);
        return data[index];
    }

    private int getThemeIndex(final boolean previous) {
        String data = sharedPreferences.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        ArrayList<String> tokens = getThemeTokens(data);
        int index = ColorType.fromValue(tokens.get(0)).getId();
        if (!previous)
            return getNextTheme(index);
        else
            return getPreviousTheme(index);
    }

    private int getPreviousTheme(final int index) {
        if (index <= 0) {
            return (themes.length - 1);
        }
        return (index - 1);
    }

    private int getNextTheme(final int index) {
        if (index == (themes.length - 1))
            return DEFAULT_THEME;
        else
            return (index + 1);
    }

    public String getThemePreferences() {
        String theme_state = sharedPreferences.getString(CURRENT_THEME_STATE, DEFAULT_THEME_STATE);
        return theme_state;
    }

    public void setThemePreferences(String prefData) {
        sharedPreferences.edit().putString(CURRENT_THEME_STATE, prefData).apply();
    }
}
