/*
 * (C) Koninklijke Philips N.V., 20NAVIGATION_COLOR_ULTRALIGHT6.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.LandscapeModeActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class NavigationBarTest {
    private static final int GRAY_75 = R.color.uid_gray_level_75;
    private static final int WHITE = R.color.uidColorWhite;
    private static final int NAVIGATION_COLOR_ULTRALIGHT = NavigationColor.ULTRA_LIGHT.ordinal();
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    public final ActivityTestRule<LandscapeModeActivity> landscapeModeActivityRule = new ActivityTestRule<>(LandscapeModeActivity.class, false, false);

    private Context applicationContext;
    private BaseTestActivity baseTestActivity;

    @Before
    public void setUp() throws Exception {
    }

    private void setupActivity(final int navigationColor) {
        final Intent intent = getLaunchIntent(navigationColor);
        baseTestActivity = mActivityTestRule.launchActivity(intent);
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        baseTestActivity.switchFragment(new NavigationbarFragment());
        applicationContext = baseTestActivity.getApplicationContext();
    }

    @NonNull
    private Intent getLaunchIntent(final int navigationColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt("NavigationColor", navigationColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyTitleMargin() throws Exception {
        setupUltralightTonalRangeActivity();
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_margin);

        getTitle().check(matches(ViewPropertiesMatchers.isSameLeftMargin(titleMargin)));
        getTitle().check(matches(ViewPropertiesMatchers.isSameRightMargin(titleMargin)));
    }

    @Test
    public void verifyTitleTextColorInUltraLight() throws Exception {
        setupUltralightTonalRangeActivity();

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.navigationTextColor);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTitleTextColorInBright() throws Exception {
        setupActivity(NavigationColor.BRIGHT.ordinal());

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.White);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTitleTextColorInVeryLight() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.GroupBlue75);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Ignore
    @Test
    public void verifyTitleLineSpacing() throws Exception {
        setupUltralightTonalRangeActivity();

        int linespacing = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_text_spacing);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(linespacing)));
    }

    @Test
    public void verifyTitleTextSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int fontSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_text_size);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    //Toolbar tests cases
    @Test
    public void verifyToolbarHeightOnLandscapeMode() throws Exception {

        final LandscapeModeActivity landscapeModeActivity = landscapeModeActivityRule.launchActivity(getLaunchIntent(NAVIGATION_COLOR_ULTRALIGHT));
        applicationContext = landscapeModeActivity.getApplicationContext();
        landscapeModeActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        landscapeModeActivity.switchFragment(new NavigationbarFragment());

        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_height_land);
        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void verifyToolbarHeight() throws Exception {
        setupUltralightTonalRangeActivity();

        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_height);

        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    //Menu icon test cases
    @Test
    public void verifyMenuIconSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameHeight("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.ic_hamburger_menu)));
        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameWidth("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.ic_hamburger_menu)));
    }

    @Test
    public void verifyMenuTextColorDefaultColor() throws Exception {
        setupUltralightTonalRangeActivity();

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.navigationTextColor);

        getNavigationMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyMenuTextPadding() throws Exception {
        setupUltralightTonalRangeActivity();

        int marginleft = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_menu_text_padding_left_right);

        getNavigationMenuText().check(matches(ViewPropertiesMatchers.isSameStartPadding(marginleft)));
    }

    @Test
    public void verifyMenuTextColorForUltraLight() throws Exception {
        setupActivity(NavigationColor.ULTRA_LIGHT.ordinal());

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.Gray75);

        getNavigationMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyMenuTextColorForVeryLight() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.GroupBlue75);

        getNavigationMenuText().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyNavigationMenuTextSize() throws Exception {
        setupActivity(NavigationColor.VERY_LIGHT.ordinal());

        int fontSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_menu_text_size);

        getNavigationMenuText().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    @Ignore
    @Test
    public void verifyMenuIconMargin() throws Exception {
        setupUltralightTonalRangeActivity();

        int padding = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_padding);

        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameLeftPadding(padding)));
        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameRightPadding(padding)));
    }

    @Test
    public void verifyNavigationBarIconSize() throws Exception {
        setupUltralightTonalRangeActivity();

        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameHeight(iconSize)));
        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameWidth(iconSize)));
    }

    @Test
    public void verifyNavigationBarIconTargetArea() throws Exception {
        setupUltralightTonalRangeActivity();

        int navigationbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_button_touchable_area);

        getNavigationMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(navigationbarHeight)));
        getNavigationMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(navigationbarHeight)));
    }

    @Test
    public void verifyNavigationBarIconTargetAreaInLandscape() throws Exception {

        final LandscapeModeActivity landscapeModeActivity = landscapeModeActivityRule.launchActivity(getLaunchIntent(NAVIGATION_COLOR_ULTRALIGHT));
        applicationContext = landscapeModeActivity.getApplicationContext();
        landscapeModeActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        landscapeModeActivity.switchFragment(new NavigationbarFragment());

        int navigationbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_button_touchable_area);

        getNavigationMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(navigationbarHeight)));
        getNavigationMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(navigationbarHeight)));
    }

    private void setupUltralightTonalRangeActivity() {
        setupActivity(NAVIGATION_COLOR_ULTRALIGHT);
    }

    private ViewInteraction getNavigationBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar));
    }

    private ViewInteraction getNavigationIcon() {
        return onView(withContentDescription(applicationContext.getResources().getString(com.philips.platform.uid.test.R.string.navigation_content_desc)));
    }

    private ViewInteraction getTitleView() {
        return onView(withText(applicationContext.getResources().getString(com.philips.platform.uid.test.R.string.catalog_app_name)));
    }

    private ViewInteraction getNavigationMenuIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.theme_settings));
    }

    private ViewInteraction getNavigationMenuText() {
        return onView(withId(com.philips.platform.uid.test.R.id.set_theme_settings));
    }

    private ViewInteraction getTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar_title));
    }
}
