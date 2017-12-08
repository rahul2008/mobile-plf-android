/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.app.Application;
import com.philips.platform.uid.thememanager.*;

public class CatalogApplication extends Application {

    //Demo to apply app level theme. Make this boolean true to see the app level thememing .
    //All the activities must extend UIDActivity to have app level thememing.
    private boolean supportAppLevelThemeing = true;

    public ColorRange colorRange = ColorRange.GROUP_BLUE;
    public ContentColor contentColor = ContentColor.ULTRA_LIGHT;
    public AccentRange accentRange = AccentRange.ORANGE;
    public NavigationColor navigationColor = NavigationColor.ULTRA_LIGHT;

    ThemeConfiguration themeConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        UIDHelper.injectCalligraphyFonts();

        if (supportAppLevelThemeing) {
            themeConfiguration = new ThemeConfiguration(this, colorRange, contentColor, navigationColor, accentRange);
            UIDHelper.init(themeConfiguration);
        }
    }

    public void injectNewTheme(ColorRange colorRange, ContentColor contentColor, NavigationColor navigationColor, AccentRange accentRange) {
        if (supportAppLevelThemeing) {
            if (colorRange != null) {
                this.colorRange = colorRange;
            }
            if (contentColor != null) {
                this.contentColor = contentColor;
            }
            if (navigationColor != null) {
                this.navigationColor = navigationColor;
            }
            if (accentRange != null) {
                this.accentRange = accentRange;
            }
            String themeName = String.format("Theme.DLS.%s.%s", this.colorRange.getThemeName(), this.contentColor.getThemeName());
            getTheme().applyStyle(getResources().getIdentifier(themeName, "style", getPackageName()), true);
            themeConfiguration = new ThemeConfiguration(this, this.colorRange, this.contentColor, this.navigationColor, this.accentRange);
            UIDHelper.init(themeConfiguration);
        }
    }

    //All the below function to demo app level theme setting.
    public boolean shouldApplyAppLevelTheme() {
        return supportAppLevelThemeing;
    }

    public ColorRange getThemeColorRange() {
        return colorRange;
    }

    public ContentColor getThemeContentColor() {
        return contentColor;
    }

    public NavigationColor getNavigationColor() {
        return navigationColor;
    }

    public AccentRange getThemeAccentRange() {
        return accentRange;
    }
}
