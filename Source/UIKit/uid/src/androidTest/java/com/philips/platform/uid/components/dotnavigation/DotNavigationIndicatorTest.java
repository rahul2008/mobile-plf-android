/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.DotNavigationMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class DotNavigationIndicatorTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;
    private IdlingResource mIdlingResource;

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal()));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new DotNavigationFragment());
        mIdlingResource = activity.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    @Test
    public void verifyDotsAreNotDrawnWhenOnlyOneItemIsSuppliedToAdapter() throws Exception {

        final int childCount = DotNavigationFragment.getDrawableArray().length;
        getIndicator().check(matches(DotNavigationMatcher.hasChildrens(childCount)));
    }

    @Test
    public void verifyLetMarginBetweenDots() throws Exception {
        final int expectedMargin = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_padding);
        getIndicator().check(matches(DotNavigationMatcher.isSameLeftMargin(expectedMargin)));
    }

    @Test
    public void verifyRightMarginBetweenDots() throws Exception {
        final int expectedMargin = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_padding);
        getIndicator().check(matches(DotNavigationMatcher.isSameRightMargin(expectedMargin)));
    }

    protected ViewInteraction getIndicator() {
        return onView(withId(com.philips.platform.uid.test.R.id.pager_indicator));
    }

    @Ignore
    @Test
    public void verifyUnselectedCircleColorBasedOnSuppliedTheme() throws Exception {
        getViewPager().perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf();
            }

            @Override
            public String getDescription() {
                return "View pager";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                if (view instanceof ViewPager) {
                    ((ViewPager) view).setCurrentItem(1);
                }
            }
        });
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidDotNavigationDefaultNormalOffBackgroundColor);
        getIndicator().check(matches(TextViewPropertiesMatchers.sameBackgroundColorTintList(expectedColor)));
    }

    protected ViewInteraction getViewPager() {
        return onView(withId(com.philips.platform.uid.test.R.id.dot_navigation_pager));
    }

    @Test
    public void verifySelectedCircleColorBasedOnSuppliedTheme() throws Exception {

    }

    @Test
    public void verifyClickOnRightOfSelectedDotGivesCallbackToShowNext() throws Exception {

    }

    @Test
    public void verifyClickOnLeftOfSelectedDotGivesCallbackToShowPrevious() throws Exception {

    }

    @Test
    public void verifyRadiusOfDot() throws Exception {
        final int expectedHeight = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_radius);

        getIndicator().check(matches(DotNavigationMatcher.hasSameRadius(expectedHeight)));
    }

    @Test
    public void verifyWidthOfDot() throws Exception {

    }

    @Test
    public void verifyHeightOfDot() throws Exception {
    }

    @Test
    public void verifyDotNavigationContainerHeight() throws Exception {

        final int expectedHeight = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_container_height);
        getIndicator().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }
}