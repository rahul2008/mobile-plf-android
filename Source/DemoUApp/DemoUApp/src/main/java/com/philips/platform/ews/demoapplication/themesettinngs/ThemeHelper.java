/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.demoapplication.themesettinngs;

import android.content.SharedPreferences;

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
        String navigationColor = sharedPreferences.getString(UIDHelper.NAVIGATION_RANGE, "");
        if (navigationColor.isEmpty()) {
            return null;
        }
        return NavigationColor.valueOf(navigationColor);
    }

    public ColorRange initColorRange() {
        String colorRange = sharedPreferences.getString(UIDHelper.COLOR_RANGE, "");
        if (colorRange.isEmpty()) {
            return null;
        }
        return ColorRange.valueOf(colorRange);
    }

    public ContentColor initContentTonalRange() {
        String tonalRange = sharedPreferences.getString(UIDHelper.CONTENT_TONAL_RANGE, "");
        if (tonalRange.isEmpty()) {
            return null;
        }
        return ContentColor.valueOf(tonalRange);
    }

    public AccentRange initAccentRange() {
        String accentRange = sharedPreferences.getString(UIDHelper.ACCENT_RANGE, "");
        if (accentRange.isEmpty()) {
            return null;
        }
        return AccentRange.valueOf(accentRange);
    }
}
