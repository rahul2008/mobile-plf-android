package com.philips.hor_productselection_android.util;/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import androidx.annotation.StyleRes;

import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.Random;

public class ThemeHelper {
    SharedPreferences sharedPreferences;
    final Context mContext;
    public ThemeHelper(final Activity context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.mContext = context;
    }

    public NavigationColor initNavigationRange() {
        String navigation = sharedPreferences.getString(UIDHelper.NAVIGATION_RANGE, NavigationColor.BRIGHT.name());
        final NavigationColor navigationColor = NavigationColor.valueOf(navigation);
        return navigationColor;
    }

    public ColorRange initColorRange() {
        String color = sharedPreferences.getString(UIDHelper.COLOR_RANGE, ColorRange.GROUP_BLUE.name());
        final ColorRange colorRange = ColorRange.valueOf(color);
        return colorRange;
    }

    public ContentColor initContentTonalRange() {
        String tonalRange = sharedPreferences.getString(UIDHelper.CONTENT_TONAL_RANGE, ContentColor.ULTRA_LIGHT.name());
        final ContentColor contentColor = ContentColor.valueOf(tonalRange);
        return contentColor;
    }

    public ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration( mContext, this.initContentTonalRange(), this.initNavigationRange());
    }


    @StyleRes
    public int getThemeResourceId() {
        int colorResourceId = getColorResourceId(mContext.getResources(), initColorRange().name(), initContentTonalRange().name(), mContext.getPackageName());
        return colorResourceId;
    }

    @StyleRes
    private int getColorResourceId(final Resources resources, final String colorRange, final String tonalRange, final String packageName) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange), toCamelCase(tonalRange));
        return resources.getIdentifier(themeName, "style", packageName);
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    public void changeTheme() {
        saveThemeSettings();
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public void saveThemeSettings() {
        saveThemeValues(UIDHelper.COLOR_RANGE, randomEnum(ColorRange.class).name());
        saveThemeValues(UIDHelper.NAVIGATION_RANGE, NavigationColor.BRIGHT.name());
        saveThemeValues(UIDHelper.CONTENT_TONAL_RANGE,ContentColor.VERY_LIGHT.name());
    }

    @SuppressLint("CommitPrefEdits")
    private void saveThemeValues(final String key, final String name) {
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, name);
        edit.commit();
    }
}
