/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertEquals;

public class ThemeHelperTest {
    public static final String CONTENT_COLOR_KEY = "ContentColor";
    public static final String NAVIGATION_COLOR_KEY = "NavigationColor";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);
    ThemeHelper themeHelper;

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor, final int contentColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyThemeNameWithGroupBlueUltraLight() throws Exception {
        final MainActivity mainActivity = activityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal(), ColorRange.GROUP_BLUE.ordinal()));
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        setTheme(mainActivity, ContentColor.ULTRA_LIGHT, ColorRange.GROUP_BLUE);
        themeHelper = new ThemeHelper(defaultSharedPreferences, getContext());
        assertEquals(R.style.Theme_DLS_GroupBlue_UltraLight, themeHelper.getThemeResourceId(mainActivity.getResources(), mainActivity.getPackageName()));
    }

    private void setTheme(final MainActivity mainActivity, final ContentColor ultraLight, final ColorRange groupBlue) {
        mainActivity.setContentColor(ultraLight);
        mainActivity.setColorRange(groupBlue);
        mainActivity.saveThemeSettings();
    }

    @Test
    public void verifyThemeIsBlueUltraLightWhenSaved() throws Exception {
        final MainActivity mainActivity = activityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal(), ColorRange.GROUP_BLUE.ordinal()));
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        setTheme(mainActivity, ContentColor.BRIGHT, ColorRange.BLUE);
        themeHelper = new ThemeHelper(defaultSharedPreferences, getContext());
        assertEquals(R.style.Theme_DLS_Blue_Bright, themeHelper.getThemeResourceId(mainActivity.getResources(), mainActivity.getPackageName()));
    }
}