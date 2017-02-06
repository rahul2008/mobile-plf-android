/*
 * (C) Koninklijke Philips N.V., 20NAVIGATION_COLOR_ULTRALIGHT6.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.navigation;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.LandscapeModeActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NavigationBarLandscapeTest extends BaseTest {
    private static final int GRAY_75 = R.color.uid_gray_level_75;
    private static final int WHITE = R.color.uidColorWhite;
    private static final int NAVIGATION_COLOR_ULTRALIGHT = NavigationColor.ULTRA_LIGHT.ordinal();
    @Rule
    public final ActivityTestRule<LandscapeModeActivity> landscapeModeActivityRule = new ActivityTestRule<>(LandscapeModeActivity.class, false, false);

    private BaseTestActivity baseTestActivity;

    private Resources resources;

    private void setupLandscapeModeActivity() {
        final LandscapeModeActivity landscapeModeActivity = landscapeModeActivityRule.launchActivity(getLaunchIntent(NAVIGATION_COLOR_ULTRALIGHT));
        landscapeModeActivity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        resources = landscapeModeActivity.getResources();

        landscapeModeActivity.switchFragment(new NavigationbarFragment());
        registerIdlingResources(landscapeModeActivity);
    }

    private int getNavigationTextExpectedFromThemeColor() {
        return UIDTestUtils.getAttributeColor(baseTestActivity, R.attr.uidNavigationTextColor);
    }

    @Test
    public void verifyTitleLineHeightInLandscape() throws Exception {
        setupLandscapeModeActivity();

        float lineheight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.navigation_title_text_height);

        UIDTestUtils.waitFor(resources, 750);
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

        UIDTestUtils.waitFor(resources, 750);
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
