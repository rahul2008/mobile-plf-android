package com.philips.platform.catalogapp;

import android.content.SharedPreferences;

import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.UITHelper;

public class ThemeHelper {
    final SharedPreferences sharedPreferences;

    public ThemeHelper(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public NavigationColor initNavigationRange() {
        String navigation = sharedPreferences.getString(UITHelper.NAVIGATION_RANGE, NavigationColor.VERY_LIGHT.name());
        final NavigationColor navigationColor = NavigationColor.valueOf(navigation);
        return navigationColor;
    }

    public ColorRange initColorRange() {
        String color = sharedPreferences.getString(UITHelper.COLOR_RANGE, ColorRange.GROUP_BLUE.name());
        final ColorRange colorRange = ColorRange.valueOf(color);
        return colorRange;
    }

    public ContentTonalRange initTonalRange() {
        String tonalRange = sharedPreferences.getString(UITHelper.CONTENT_TONAL_RANGE, ContentTonalRange.ULTRA_LIGHT.name());
        final ContentTonalRange contentTonalRange = ContentTonalRange.valueOf(tonalRange);
        return contentTonalRange;
    }
}
