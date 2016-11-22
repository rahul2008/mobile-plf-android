/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uit.components.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.activity.LandscapeModeActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;

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
        baseTestActivity.switchTo(com.philips.platform.uit.test.R.layout.main_layout);
        baseTestActivity.switchFragment(new NavigationbarFragment());
        applicationContext = baseTestActivity.getApplicationContext();
    }

    @Test
    public void VerifyToolbarHeightOnLandscapeMode() throws Exception {
        final LandscapeModeActivity landscapeModeActivity = landscapeModeActivityRule.launchActivity(new Intent());
        landscapeModeActivity.switchTo(com.philips.platform.uit.test.R.layout.main_layout);
        landscapeModeActivity.switchFragment(new NavigationbarFragment());

        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_height_land);
        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void VerifyToolbarHeight() throws Exception {
        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_height);

        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void VerifyTitleLeftMargin() throws Exception {
        int iconpadding = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_margin);

        getTitleView().check(matches(ViewPropertiesMatchers.isSameTittleLeftMargin(iconpadding)));
    }

    @Test
    public void VerifyTitleRightMargin() throws Exception {

        int iconpadding = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_margin);

        getTitleView().check(matches(ViewPropertiesMatchers.isSameTitleRightMargin(iconpadding)));
    }

    @Test
    public void VerifyMenuIconSize() throws Exception {
        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_icon_size);

        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameHeight("getDrawable", iconSize, com.philips.platform.uit.test.R.drawable.uid_switch_thumb)));
        getNavigationIcon().check(matches(FunctionDrawableMatchers.isSameWidth("getDrawable", iconSize, com.philips.platform.uit.test.R.drawable.uid_switch_thumb)));
    }

    @Ignore
    @Test
    public void VerifyMenuIconMargin() throws Exception {
        int padding = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_icon_padding);

        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameLeftPadding(padding)));
        getNavigationIcon().check(matches(ViewPropertiesMatchers.isSameRightPadding(padding)));
    }

    //Android doesnt provide api to set margin left right
    @Ignore
    @Test
    public void VerifyTitleMarginLeft() throws Exception {
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_margin);

        getTitleView().check(matches(ViewPropertiesMatchers.isSameLeftMargin(titleMargin)));
    }

    //Android doesnt provide api to set margin left right
    @Ignore
    @Test
    public void VerifyTitleMarginRight() throws Exception {
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_margin);

        getTitleView().check(matches(ViewPropertiesMatchers.isSameRightMargin(titleMargin)));
    }

    @Test
    public void VerifyTitlePaddingStart() throws Exception {
        int titleMargin = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_margin);

        getTitleView().check(matches(ViewPropertiesMatchers.isSameStartPadding(titleMargin)));
    }
    @Test
    public void VerifyTitleTextColor() throws Exception {
        final int expectedColor = ContextCompat.getColor(applicationContext, com.philips.platform.uit.test.R.color.White);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Ignore
    @Test
    public void VerifyTitleLineSpacing() throws Exception {
        int linespacing = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_text_spacing);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(linespacing)));
    }

    @Test
    public void VerifyTitleTextSize() throws Exception {
        int fontSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_title_text_size);

        getTitleView().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    @Test
    public void VerifyNavigationBarIconSize() throws Exception {
        int iconSize = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_icon_size);

        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameHeight(iconSize)));
        getNavigationMenuIcon().check(matches(FunctionDrawableMatchers.isSameWidth(iconSize)));
    }

    private ViewInteraction getNavigationBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_toolbar));
    }

    private ViewInteraction getNavigationIcon() {
        return onView(withContentDescription("navigationIcon"));
    }

    private ViewInteraction getTitleView() {
        return onView(withText("Tittle"));
    }

    private ViewInteraction getNavigationMenuIcon() {
        return onView(withId(com.philips.platform.uit.test.R.id.theme_settings));
    }
}
