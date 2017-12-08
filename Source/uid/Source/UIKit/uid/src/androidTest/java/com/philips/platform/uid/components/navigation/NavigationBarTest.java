/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.navigation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.utils.UIDUtils;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NavigationBarTest {
    private static final int NAVIGATION_COLOR_ULTRALIGHT = NavigationColor.ULTRA_LIGHT.ordinal();
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);

    private BaseTestActivity baseTestActivity;

    private Resources resources;
    private IdlingResource mIdlingResource;

    private void setupActivity(final int navigationColor) {
        final Intent intent = getLaunchIntent(navigationColor);

        baseTestActivity = mActivityTestRule.launchActivity(intent);
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        baseTestActivity.switchFragment(new NavigationbarFragment());
        resources = baseTestActivity.getResources();
        registerIdlingResources(baseTestActivity);
    }

    public void registerIdlingResources(final BaseTestActivity baseTestActivity) {
        mIdlingResource = baseTestActivity.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @NonNull
    private Intent getLaunchIntent(final int navigationColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyTitleMargin() throws Exception {
        setupUltralightTonalRangeActivity();
        int titleMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_margin);

        getTitle().check(matches(ViewPropertiesMatchers.isSameLeftMargin(titleMargin)));
        getTitle().check(matches(ViewPropertiesMatchers.isSameEndMargin(titleMargin)));
    }

    private int getNavigationTextExpectedFromThemeColor() {
        return UIDTestUtils.getAttributeColor(baseTestActivity, R.attr.uidNavigationPrimaryTextColor);
    }

    @Test
    public void verifyTitleTextColorInBright() throws Exception {
        setupActivity(NavigationColor.BRIGHT.ordinal());

        final int expectedColor = getNavigationTextExpectedFromThemeColor();

        getTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTitleTextColorInVeryLight() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        final int expectedColor = getNavigationTextExpectedFromThemeColor();

        getTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Ignore
    @Test
    public void verifyTitleTextSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int fontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_text_size);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    @Test
    public void verifyTitleTextColorInUltraLight() throws Exception {
        setupUltralightTonalRangeActivity();

        int expectedColor = getNavigationTextExpectedFromThemeColor();
        getTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTitleLineSpacing() throws Exception {
        setupUltralightTonalRangeActivity();

        float linespacing = resources.getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_text_spacing);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(linespacing)));
    }

    @Ignore
    @Test
    public void verifyToolbarHeight() throws Exception {
        setupUltralightTonalRangeActivity();

        int toolbarHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_height);
        UIDTestUtils.waitFor(resources, UIDTestUtils.UI_LOAD_WAIT_TIME_EXTRA);

        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void verifyNavigationMenuIconSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int iconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameHeight("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.ic_hamburger_menu)));
        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameWidth("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.ic_hamburger_menu)));
    }

    @Test
    public void verifyMenuTextColorDefaultColor() throws Exception {
        setupUltralightTonalRangeActivity();

        final int expectedColor = getNavigationTextExpectedFromThemeColor();

        getOptionsMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyMenuTextColorForUltraLight() throws Exception {
        setupActivity(NavigationColor.ULTRA_LIGHT.ordinal());

        final int expectedColor = getNavigationTextExpectedFromThemeColor();

        getOptionsMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyMenuTextColorForVeryLight() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        final int expectedColor = getNavigationTextExpectedFromThemeColor();

        getOptionsMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyOptionsMenuTextSize() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        int fontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_menu_text_size);

        getOptionsMenuText().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    @Test
    public void verifyOptionsMenuIconSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int iconSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getOptionsMenuIcon().check(matches(FunctionDrawableMatchers.isSameHeight(iconSize)));
        getOptionsMenuIcon().check(matches(FunctionDrawableMatchers.isSameWidth(iconSize)));
    }

    @Test
    public void verifyOptionsMenuIconTargetArea() throws Exception {
        setupUltralightTonalRangeActivity();

        int navigationbarHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_button_touchable_area);

        getOptionsMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(navigationbarHeight)));
        getOptionsMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(navigationbarHeight)));
    }

    private void setupUltralightTonalRangeActivity() {
        setupActivity(NAVIGATION_COLOR_ULTRALIGHT);
    }

    private ViewInteraction getNavigationBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar));
    }

    private ViewInteraction getNavigationIcon() {
        return onView(withContentDescription(resources.getString(com.philips.platform.uid.test.R.string.navigation_content_desc)));
    }

    private ViewInteraction getOptionsMenuIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.theme_settings));
    }

    private ViewInteraction getOptionsMenuText() {
        return onView(withId(com.philips.platform.uid.test.R.id.set_theme_settings));
    }

    private ViewInteraction getTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar_title));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
