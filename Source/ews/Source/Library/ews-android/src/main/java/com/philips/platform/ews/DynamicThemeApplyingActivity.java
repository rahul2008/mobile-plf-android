/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.philips.platform.ews.injections.DependencyHelper;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DynamicThemeApplyingActivity extends UIDActivity {

    private ContentColor contentColor;
    private ColorRange colorRange;
    private NavigationColor navigationColor;
    private AccentRange accentColorRange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
    }

    private ColorRange getColorRange() {
        if (colorRange == null) {
            colorRange = ColorRange.GROUP_BLUE;
        }
        return colorRange;
    }

    private ContentColor getContentColor() {
        if (contentColor == null) {
            contentColor = ContentColor.ULTRA_LIGHT;
        }
        return contentColor;
    }

    private NavigationColor getNavigationColor() {
        if (navigationColor == null) {
            navigationColor = NavigationColor.BRIGHT;
        }
        return navigationColor;
    }

    private AccentRange getAccentColorRange() {
        if (accentColorRange == null) {
            accentColorRange = AccentRange.ORANGE;
        }
        return accentColorRange;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void injectNewTheme(ColorRange colorRange, ContentColor contentColor, NavigationColor navigationColor, AccentRange accentRange) {
        String themeName = String.format("Theme.DLS.%s.%s", colorRange.getThemeName(), contentColor.getThemeName());
        getTheme().applyStyle(getResources().getIdentifier(themeName, "style", getPackageName()), true);
        ThemeConfiguration themeConfiguration = new ThemeConfiguration(this, colorRange, contentColor, navigationColor, accentRange);
        UIDHelper.init(themeConfiguration);
    }

    private void initTheme() {
        final ThemeConfiguration themeConfig = DependencyHelper.getThemeConfiguration();
        if (themeConfig != null) {
            for (ThemeConfig config : themeConfig.getConfigurations()) {
                if (config instanceof ColorRange) {
                    colorRange = (ColorRange) config;
                } else if (config instanceof ContentColor) {
                    contentColor = (ContentColor) config;
                } else if (config instanceof AccentRange) {
                    accentColorRange = (AccentRange) config;
                } else if (config instanceof NavigationColor) {
                    navigationColor = (NavigationColor) config;
                }
            }
        }
        injectNewTheme(getColorRange(), getContentColor(), getNavigationColor(), getAccentColorRange());
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    @StyleRes
    int getThemeResourceId(Resources resources, final String packageName, final ColorRange colorRange, final ContentColor contentColor) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange.name()), toCamelCase(contentColor.name()));
        return resources.getIdentifier(themeName, "style", packageName);
    }
}
