package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.prdemoapp.theme.themesettings.ThemeHelper;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

/**
 * Created by philips on 9/14/17.
 */

public abstract class DemoBaseState extends BaseState {

    public DemoBaseState(String stateID) {
        super(stateID);
    }

    protected ContentColor contentColor;
    protected ColorRange colorRange;
    protected NavigationColor navigationColor;
    protected SharedPreferences defaultSharedPreferences;
    protected AccentRange accentColorRange;
    protected ThemeConfiguration getDLSThemeConfiguration(Context context) {

        defaultSharedPreferences = new SharedPreferenceUtility(context).getMyPreferences();
        final ThemeHelper themeHelper = new ThemeHelper(defaultSharedPreferences);
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        accentColorRange = themeHelper.initAccentRange();
        return new ThemeConfiguration(context, colorRange, navigationColor, contentColor, accentColorRange);
      //  return new ThemeConfiguration(context, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
    }
}
