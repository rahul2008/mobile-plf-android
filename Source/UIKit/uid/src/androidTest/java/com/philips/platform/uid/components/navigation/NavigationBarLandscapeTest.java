/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.navigation;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.LandscapeModeActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@Ignore
public class NavigationBarLandscapeTest extends BaseTest {
    private static final int NAVIGATION_COLOR_ULTRALIGHT = NavigationColor.ULTRA_LIGHT.ordinal();
    @Rule
    public final ActivityTestRule<LandscapeModeActivity> landscapeModeActivityRule = new ActivityTestRule<>(LandscapeModeActivity.class, false, false);

    private BaseTestActivity baseTestActivity;

    private Resources resources;

    private void setupLandscapeModeActivity() {
        baseTestActivity = landscapeModeActivityRule.launchActivity(getLaunchIntent(NAVIGATION_COLOR_ULTRALIGHT));
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        resources = baseTestActivity.getResources();

        baseTestActivity.switchFragment(new NavigationbarFragment());
    }

    @Ignore
    @Test
    public void verifyTitleLineHeightInLandscape() throws Exception {
        setupLandscapeModeActivity();

        float lineheight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_text_height);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameLineHeight(lineheight)));
    }

    @Test
    public void verifyTitleLineSpacingInLandscape() throws Exception {
        setupLandscapeModeActivity();

        float linespacing = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_text_spacing);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(linespacing)));
    }

    @Test
    public void verifyTitleTextSizeInLandscape() throws Exception {
        setupLandscapeModeActivity();

        int fontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_text_size);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(fontSize)));
    }

    @Test
    public void verifyToolbarHeightOnLandscapeMode() throws Exception {

        setupLandscapeModeActivity();

        int toolbarHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_height_land);
        getNavigationBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void verifyOptionsMenuIconTargetAreaInLandscape() throws Exception {
        setupLandscapeModeActivity();

        int navigationbarHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_button_touchable_area_landscape);

        getOptionsMenuIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(navigationbarHeight)));
    }

    private ViewInteraction getNavigationBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar));
    }

    private ViewInteraction getOptionsMenuIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.theme_settings));
    }

    private ViewInteraction getTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_toolbar_title));
    }
}
