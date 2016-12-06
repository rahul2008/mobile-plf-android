/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.LandscapeModeActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ToolbarMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

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
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    final ActivityTestRule<LandscapeModeActivity> landscapeModeActivityRule = new ActivityTestRule<>(LandscapeModeActivity.class);

    private Context applicationContext;
    private BaseTestActivity baseTestActivity;

    @Before
    public void setUp() throws Exception {
        baseTestActivity = mActivityTestRule.launchActivity(new Intent());
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        baseTestActivity.switchFragment(new NavigationbarFragment());
        applicationContext = baseTestActivity.getApplicationContext();
    }

    //Title test cases
    //Android doesnt provide api to set margin left right
    @Test
    public void verifyTitleMarginLeft() throws Exception {
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_margin);

        getNavigationBar().check(matches(ToolbarMatcher.isSameTitleMarginLeft(titleMargin)));
    }

    @Test
    public void verifyTitleMarginRight() throws Exception {
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_margin);

        getNavigationBar().check(matches(ToolbarMatcher.isSameTitleMarginRight(titleMargin)));
    }


    @Test
    public void verifyTitleTextColor() throws Exception {
        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uid.test.R.color.White);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTitleLineSpacing() throws Exception {
        int linespacing = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_text_spacing);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(linespacing)));
    }

    @Test
    public void verifyTitleTextSize() throws Exception {
        int fontSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_title_text_size);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    //Toolbar tests cases
    @Test
    public void verifyToolbarHeightOnLandscapeMode() throws Exception {
        final LandscapeModeActivity landscapeModeActivity = landscapeModeActivityRule.launchActivity(new Intent());
        landscapeModeActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        landscapeModeActivity.switchFragment(new NavigationbarFragment());

        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_height_land);
        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void verifyToolbarHeight() throws Exception {
        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_height);

        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    //Menu icon test cases
    @Test
    public void verifyMenuIconSize() throws Exception {
        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameHeight("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.uid_switch_thumb)));
        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameWidth("getDrawable", iconSize, com.philips.platform.uid.test.R.drawable.uid_switch_thumb)));
    }

    @Ignore
    @Test
    public void verifyMenuIconMargin() throws Exception {
        int padding = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_padding);

        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameLeftPadding(padding)));
        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameRightPadding(padding)));
    }

    @Test
    public void verifyNavigationBarIconSize() throws Exception {
        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_icon_size);

        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameHeight(iconSize)));
        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameWidth(iconSize)));
    }

    @Test
    public void verifyNavigationBarIconTouchableArea() throws Exception {
        int navigationbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uid.test.R.dimen.navigation_button_touchable_area);

        getNavigationMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(navigationbarHeight)));
    }

    private ViewInteraction getNavigationBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar));
    }

    private ViewInteraction getNavigationIcon() {
        return onView(withContentDescription("navigationIcon"));
    }

    private ViewInteraction getTitleView() {
        return onView(withText(com.philips.platform.uid.test.R.string.catalog_app_name));
    }

    private ViewInteraction getNavigationMenuIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.theme_settings));
    }
}
