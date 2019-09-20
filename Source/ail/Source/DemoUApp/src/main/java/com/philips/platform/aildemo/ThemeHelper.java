package com.philips.platform.aildemo;/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.UIDHelper;

public class ThemeHelper {
    SharedPreferences sharedPreferences;
    final Context mContext;
    public ThemeHelper(final Activity context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.mContext = context;
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




    public int getThemeResourceId() {
        int colorResourceId = getColorResourceId(mContext.getResources(), initColorRange().name(), initContentTonalRange().name(), mContext.getPackageName());
        return colorResourceId;
    }

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


}
