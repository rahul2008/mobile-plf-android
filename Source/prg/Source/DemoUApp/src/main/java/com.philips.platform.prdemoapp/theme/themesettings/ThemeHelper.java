/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.prdemoapp.theme.themesettings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.annotation.StyleRes;

import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.UIDHelper;

public class ThemeHelper {
    final SharedPreferences sharedPreferences;

    public ThemeHelper(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
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

    public AccentRange initAccentRange() {
        String accentRangeString = sharedPreferences.getString(UIDHelper.ACCENT_RANGE, AccentRange.ORANGE.name());
        final AccentRange accentRange = AccentRange.valueOf(accentRangeString);
        return accentRange;
    }

    @StyleRes
    int getColorResourceId(final Resources resources, final String colorRange, final String tonalRange, final String packageName) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange), toCamelCase(tonalRange));

        return resources.getIdentifier(themeName, "style", packageName);
    }

    String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    public
    @StyleRes
    int getThemeResourceId(Resources resources, final String packageName) {
        final ColorRange colorRange = initColorRange();
        final ContentColor contentColor = initContentTonalRange();
        int colorResourceId = getColorResourceId(resources, colorRange.name(), contentColor.name(), packageName);
        return colorResourceId;
    }
}
