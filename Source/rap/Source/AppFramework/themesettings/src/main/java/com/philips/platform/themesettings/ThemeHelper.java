/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.themesettings;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.UIDHelper;


public class ThemeHelper {
    final SharedPreferences sharedPreferences;

    public ThemeHelper(final SharedPreferences sharedPreferences, Context context) {
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
}
