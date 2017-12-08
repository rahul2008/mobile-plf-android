package com.philips.cdp.ui.catalog.themeutils;

import android.content.SharedPreferences;

import com.philips.cdp.ui.catalog.ColorType;
import com.philips.cdp.uikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ThemeUtils {

    public static final String DELIMITER = "|";
    private final int DEFAULT_THEME = 0;
    private final String CURRENT_THEME_STATE = "current_theme_state";
    private final String DEFAULT_THEME_STATE = "blue|false|solid|0";
    private TreeMap<String, int[]> themesMap = new TreeMap<>();
    private String COLOR_STRING = ColorType.BLUE.getDescription();
    private SharedPreferences sharedPreferences;
    private int[] themes = {
            R.style.Theme_Philips_DarkBlue_WhiteBackground,
            R.style.Theme_Philips_BrightOrange_WhiteBackground,
            R.style.Theme_Philips_BrightAqua_WhiteBackground,
            R.style.Theme_Philips_BrightGreen_WhiteBackground,
            R.style.Theme_Philips_BrightPink_WhiteBackground,
            R.style.Theme_Philips_DarkPurple_WhiteBackground,
            R.style.Theme_Philips_DarkAqua_WhiteBackground,
            R.style.Theme_Philips_DarkGreen_WhiteBackground,
            R.style.Theme_Philips_DarkOrange_WhiteBackground,
            R.style.Theme_Philips_DarkPink_WhiteBackground,
            R.style.Theme_Philips_BrightBlue_WhiteBackground,
            R.style.Theme_Philips_BrightPurple_WhiteBackground,
            R.style.Theme_Philips_LightBlue_WhiteBackground,
            R.style.Theme_Philips_LightOrange_WhiteBackground,
            R.style.Theme_Philips_LightAqua_WhiteBackground,
            R.style.Theme_Philips_LightGreen_WhiteBackground,
            R.style.Theme_Philips_LightPink_WhiteBackground,
            R.style.Theme_Philips_LightPurple_WhiteBackground
    };

    private HashMap<Integer, Integer> noActionBarMap = new HashMap<Integer,Integer>();
    public ThemeUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        appendThemes();
        initNoActionBarMap();
    }

    public String getColorString() {
        return COLOR_STRING;
    }

    public void setColorString(String COLOR_STRING) {
        this.COLOR_STRING = COLOR_STRING;
    }

    private void appendThemes() {
        int[] blue_themes = {R.style.Theme_Philips_DarkBlue_WhiteBackground, R.style.Theme_Philips_DarkBlue,
                R.style.Theme_Philips_DarkBlue_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkBlue_Gradient};
        int[] orange_themes = {R.style.Theme_Philips_BrightOrange_WhiteBackground, R.style.Theme_Philips_BrightOrange,
                R.style.Theme_Philips_BrightOrange_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightOrange_Gradient};
        int[] aqua_themes = {R.style.Theme_Philips_BrightAqua_WhiteBackground, R.style.Theme_Philips_BrightAqua,
                R.style.Theme_Philips_BrightAqua_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightAqua_Gradient};
        int[] brigh_green_themes = {R.style.Theme_Philips_BrightGreen_WhiteBackground, R.style
                .Theme_Philips_BrightGreen,
                R.style.Theme_Philips_BrightGreen_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightGreen_Gradient};
        int[] brigh_pink_themes = {R.style.Theme_Philips_BrightPink_WhiteBackground, R.style
                .Theme_Philips_BrightPink,
                R.style.Theme_Philips_BrightPink_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightPink_Gradient};

        int[] dark_purple_themes = {R.style.Theme_Philips_DarkPurple_WhiteBackground,
                R.style.Theme_Philips_DarkPurple,
                R.style.Theme_Philips_DarkPurple_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkPurple_Gradient};
        int[] dark_aqua_themes = {R.style.Theme_Philips_DarkAqua_WhiteBackground,
                R.style.Theme_Philips_DarkAqua,
                R.style.Theme_Philips_DarkAqua_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkAqua_Gradient};

        int[] dark_green_themes = {R.style.Theme_Philips_DarkGreen_WhiteBackground,
                R.style.Theme_Philips_DarkGreen,
                R.style.Theme_Philips_DarkGreen_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkGreen_Gradient};

        int[] dark_orange_themes = {R.style.Theme_Philips_DarkOrange_WhiteBackground,
                R.style.Theme_Philips_DarkOrange,
                R.style.Theme_Philips_DarkOrange_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkOrange_Gradient};

        int[] dark_pink_themes = {R.style.Theme_Philips_DarkPink_WhiteBackground,
                R.style.Theme_Philips_DarkPink,
                R.style.Theme_Philips_DarkPink_Gradient_WhiteBackground,
                R.style.Theme_Philips_DarkPink_Gradient};

        int[] bright_blue_themes = {R.style.Theme_Philips_BrightBlue_WhiteBackground, R.style
                .Theme_Philips_BrightBlue,
                R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightBlue_Gradient};

        int[] bright_purple_themes = {R.style.Theme_Philips_BrightPurple_WhiteBackground, R.style
                .Theme_Philips_BrightPurple,
                R.style.Theme_Philips_BrightPurple_Gradient_WhiteBackground,
                R.style.Theme_Philips_BrightPurple_Gradient};

        int[] light_blue_themes = {R.style.Theme_Philips_LightBlue_WhiteBackground, R.style
                .Theme_Philips_LightBlue,
                R.style.Theme_Philips_LightBlue_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightBlue_Gradient};

        int[] light_orange_themes = {R.style.Theme_Philips_LightOrange_WhiteBackground, R.style
                .Theme_Philips_LightOrange,
                R.style.Theme_Philips_LightOrange_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightOrange_Gradient};

        int[] light_aqua_themes = {R.style.Theme_Philips_LightAqua_WhiteBackground, R.style
                .Theme_Philips_LightAqua,
                R.style.Theme_Philips_LightAqua_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightAqua_Gradient};

        int[] light_green_themes = {R.style.Theme_Philips_LightGreen_WhiteBackground, R.style
                .Theme_Philips_LightGreen,
                R.style.Theme_Philips_LightGreen_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightGreen_Gradient};

        int[] light_pink_themes = {R.style.Theme_Philips_LightPink_WhiteBackground, R.style
                .Theme_Philips_LightPink,
                R.style.Theme_Philips_LightPink_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightPink_Gradient};

        int[] light_purple_themes = {R.style.Theme_Philips_LightPurple_WhiteBackground, R.style
                .Theme_Philips_LightPurple,
                R.style.Theme_Philips_LightPurple_Gradient_WhiteBackground,
                R.style.Theme_Philips_LightPurple_Gradient};

        themesMap.put(ColorType.BLUE.getDescription(), blue_themes);
        themesMap.put(ColorType.ORANGE.getDescription(), orange_themes);
        themesMap.put(ColorType.AQUA.getDescription(), aqua_themes);
        themesMap.put(ColorType.BRIGHT_GREEN.getDescription(), brigh_green_themes);
        themesMap.put(ColorType.BRIGHT_PINK.getDescription(), brigh_pink_themes);
        themesMap.put(ColorType.DARK_PURPLE.getDescription(), dark_purple_themes);
        themesMap.put(ColorType.DARK_AQUA.getDescription(), dark_aqua_themes);
        themesMap.put(ColorType.DARK_GREEN.getDescription(), dark_green_themes);
        themesMap.put(ColorType.DARK_ORANGE.getDescription(), dark_orange_themes);
        themesMap.put(ColorType.DARK_PINK.getDescription(), dark_pink_themes);
        themesMap.put(ColorType.BRIGHT_BLUE.getDescription(), bright_blue_themes);
        themesMap.put(ColorType.BRIGHT_PURPLE.getDescription(), bright_purple_themes);
        themesMap.put(ColorType.LIGHT_BLUE.getDescription(), light_blue_themes);
        themesMap.put(ColorType.LIGHT_ORANGE.getDescription(), light_orange_themes);
        themesMap.put(ColorType.LIGHT_AQUA.getDescription(), light_aqua_themes);
        themesMap.put(ColorType.LIGHT_GREEN.getDescription(), light_green_themes);
        themesMap.put(ColorType.LIGHT_PINK.getDescription(), light_pink_themes);
        themesMap.put(ColorType.LIGHT_PURPLE.getDescription(), light_purple_themes);
    }

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

    public int getNoActionBarTheme() {
        return noActionBarMap.get(getTheme());
    }

    private void initNoActionBarMap() {
        noActionBarMap.put(R.style.Theme_Philips_DarkBlue, R.style.Theme_Philips_DarkBlue_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkBlue_WhiteBackground, R.style.Theme_Philips_DarkBlue_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkBlue_Gradient_WhiteBackground, R.style.Theme_Philips_DarkBlue_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkBlue_Gradient, R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);

        //DarkPurple
        noActionBarMap.put(R.style.Theme_Philips_DarkPurple, R.style.Theme_Philips_DarkPurple_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPurple_WhiteBackground, R.style.Theme_Philips_DarkPurple_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPurple_Gradient_WhiteBackground, R.style.Theme_Philips_DarkPurple_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPurple_Gradient, R.style.Theme_Philips_DarkPurple_Gradient_NoActionBar);

        //DarkAqua
        noActionBarMap.put(R.style.Theme_Philips_DarkAqua, R.style.Theme_Philips_DarkAqua_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkAqua_WhiteBackground, R.style.Theme_Philips_DarkAqua_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkAqua_Gradient_WhiteBackground, R.style.Theme_Philips_DarkAqua_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkAqua_Gradient, R.style.Theme_Philips_DarkAqua_Gradient_NoActionBar);

        //DarkGreen
        noActionBarMap.put(R.style.Theme_Philips_DarkGreen, R.style.Theme_Philips_DarkGreen_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkGreen_WhiteBackground, R.style.Theme_Philips_DarkGreen_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkGreen_Gradient_WhiteBackground, R.style.Theme_Philips_DarkGreen_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkGreen_Gradient, R.style.Theme_Philips_DarkGreen_Gradient_NoActionBar);

        //DarkPink
        noActionBarMap.put(R.style.Theme_Philips_DarkPink, R.style.Theme_Philips_DarkPink_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPink_WhiteBackground, R.style.Theme_Philips_DarkPink_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPink_Gradient_WhiteBackground, R.style.Theme_Philips_DarkPink_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkPink_Gradient, R.style.Theme_Philips_DarkPink_Gradient_NoActionBar);

        //DarkOrange
        noActionBarMap.put(R.style.Theme_Philips_DarkOrange, R.style.Theme_Philips_DarkOrange_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkOrange_WhiteBackground, R.style.Theme_Philips_DarkOrange_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkOrange_Gradient_WhiteBackground, R.style.Theme_Philips_DarkOrange_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_DarkOrange_Gradient, R.style.Theme_Philips_DarkOrange_Gradient_NoActionBar);

        //BrightOrange
        noActionBarMap.put(R.style.Theme_Philips_BrightOrange, R.style.Theme_Philips_BrightOrange_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightOrange_WhiteBackground, R.style.Theme_Philips_BrightOrange_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightOrange_Gradient_WhiteBackground, R.style.Theme_Philips_BrightOrange_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightOrange_Gradient, R.style.Theme_Philips_BrightOrange_Gradient_NoActionBar);

        //BrightAqua
        noActionBarMap.put(R.style.Theme_Philips_BrightAqua, R.style.Theme_Philips_BrightAqua_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightAqua_WhiteBackground, R.style.Theme_Philips_BrightAqua_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightAqua_Gradient_WhiteBackground, R.style.Theme_Philips_BrightAqua_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightAqua_Gradient, R.style.Theme_Philips_BrightAqua_Gradient_NoActionBar);

        //BrightGreen
        noActionBarMap.put(R.style.Theme_Philips_BrightGreen, R.style.Theme_Philips_BrightGreen_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightGreen_WhiteBackground, R.style.Theme_Philips_BrightGreen_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightGreen_Gradient_WhiteBackground, R.style.Theme_Philips_BrightGreen_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightGreen_Gradient, R.style.Theme_Philips_BrightGreen_Gradient_NoActionBar);

        //BrightPink
        noActionBarMap.put(R.style.Theme_Philips_BrightPink, R.style.Theme_Philips_BrightPink_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPink_WhiteBackground, R.style.Theme_Philips_BrightPink_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPink_Gradient_WhiteBackground, R.style.Theme_Philips_BrightPink_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPink_Gradient, R.style.Theme_Philips_BrightPink_Gradient_NoActionBar);

        //BrightBlue
        noActionBarMap.put(R.style.Theme_Philips_BrightBlue, R.style.Theme_Philips_BrightBlue_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightBlue_WhiteBackground, R.style.Theme_Philips_BrightBlue_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground, R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightBlue_Gradient, R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);

        //BrightPurple
        noActionBarMap.put(R.style.Theme_Philips_BrightPurple, R.style.Theme_Philips_BrightPurple_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPurple_WhiteBackground, R.style.Theme_Philips_BrightPurple_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPurple_Gradient_WhiteBackground, R.style.Theme_Philips_BrightPurple_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_BrightPurple_Gradient, R.style.Theme_Philips_BrightPurple_Gradient_NoActionBar);

        //LightBlue
        noActionBarMap.put(R.style.Theme_Philips_LightBlue, R.style.Theme_Philips_LightBlue_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightBlue_WhiteBackground, R.style.Theme_Philips_LightBlue_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightBlue_Gradient_WhiteBackground, R.style.Theme_Philips_LightBlue_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightBlue_Gradient, R.style.Theme_Philips_LightBlue_Gradient_NoActionBar);

        //LightOrange
        noActionBarMap.put(R.style.Theme_Philips_LightOrange, R.style.Theme_Philips_LightOrange_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightOrange_WhiteBackground, R.style.Theme_Philips_LightOrange_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightOrange_Gradient_WhiteBackground, R.style.Theme_Philips_LightOrange_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightOrange_Gradient, R.style.Theme_Philips_LightOrange_Gradient_NoActionBar);

        //LightAqua
        noActionBarMap.put(R.style.Theme_Philips_LightAqua, R.style.Theme_Philips_LightAqua_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightAqua_WhiteBackground, R.style.Theme_Philips_LightAqua_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightAqua_Gradient_WhiteBackground, R.style.Theme_Philips_LightAqua_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightAqua_Gradient, R.style.Theme_Philips_LightAqua_Gradient_NoActionBar);

        //LightGreen
        noActionBarMap.put(R.style.Theme_Philips_LightGreen, R.style.Theme_Philips_LightGreen_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightGreen_WhiteBackground, R.style.Theme_Philips_LightGreen_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightGreen_Gradient_WhiteBackground, R.style.Theme_Philips_LightGreen_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightGreen_Gradient, R.style.Theme_Philips_LightGreen_Gradient_NoActionBar);

        //LightPink
        noActionBarMap.put(R.style.Theme_Philips_LightPink, R.style.Theme_Philips_LightPink_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPink_WhiteBackground, R.style.Theme_Philips_LightPink_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPink_Gradient_WhiteBackground, R.style.Theme_Philips_LightPink_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPink_Gradient, R.style.Theme_Philips_LightPink_Gradient_NoActionBar);

        //LightPurple
        noActionBarMap.put(R.style.Theme_Philips_LightPurple, R.style.Theme_Philips_LightPurple_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPurple_WhiteBackground, R.style.Theme_Philips_LightPurple_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPurple_Gradient_WhiteBackground, R.style.Theme_Philips_LightPurple_Gradient_WhiteBackground_NoActionBar);
        noActionBarMap.put(R.style.Theme_Philips_LightPurple_Gradient, R.style.Theme_Philips_LightPurple_Gradient_NoActionBar);

    }
}
