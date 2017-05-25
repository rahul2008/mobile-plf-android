/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.content.res.ColorStateList;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.DotNavigationMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeUtils;

import junit.framework.Assert;

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
    DotNavigationFragment dotNavigationFragment;
    private IdlingResource mIdlingResource;

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal()));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        dotNavigationFragment = new DotNavigationFragment();
        activity.switchFragment(dotNavigationFragment);
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

    protected ViewInteraction getViewPager() {
        return onView(withId(com.philips.platform.uid.test.R.id.dot_navigation_pager));
    }

    @Ignore
    @Test
    public void verifyUnselectedCircleColorBasedOnSuppliedTheme() throws Exception {
        initPagerWithSecondItemSelected(1);

        final ColorStateList expectedColor = ThemeUtils.buildColorStateList(activity.getResources(), activity.getTheme(), R.color.uid_dot_navigation_icon_color);
        getIndicator().check(matches(DotNavigationMatcher.sameBackgroundColor(expectedColor)));
    }

    protected void initPagerWithSecondItemSelected(final int currentItem) {
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
                ((ViewPager) view).setCurrentItem(currentItem);
            }
        });
    }

    @Test
    public void verifySelectedCircleColorBasedOnSuppliedTheme() throws Exception {

    }

    @Test
    public void verifyClickOnRightOfSelectedDotGivesCallbackToShowNext() throws Exception {
        getIndicator().check(matches(ViewMatchers.isDisplayed()));
        getIndicator().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));
        final int currentItem = ((ViewPager) activity.findViewById(com.philips.platform.uid.test.R.id.dot_navigation_pager)).getCurrentItem();
        Assert.assertEquals(currentItem, 1);
    }

    @Test
    public void verifyClickOnLeftOfSelectedDotGivesCallbackToShowPrevious() throws Exception {
        initPagerWithSecondItemSelected(1);
        getIndicator().check(matches(ViewMatchers.isDisplayed()));

        getIndicator().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_LEFT, Press.FINGER));
        final int currentItem = ((ViewPager) activity.findViewById(com.philips.platform.uid.test.R.id.dot_navigation_pager)).getCurrentItem();
        Assert.assertEquals(currentItem, 0);
    }

    @Test
    public void verifyRadiusOfDot() throws Exception {
        final int expectedHeight = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_radius);

        getIndicator().check(matches(DotNavigationMatcher.hasSameRadius(expectedHeight)));
    }

    @Test
    public void verifyWidthOfDot() throws Exception {
        int expectedwidth = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_size);
        getIndicator().check(matches(DotNavigationMatcher.hasSameWidth(expectedwidth)));
    }

    @Test
    public void verifyHeightOfDot() throws Exception {
        int expectedwidth = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_size);
        getIndicator().check(matches(DotNavigationMatcher.hasSameHeight(expectedwidth)));
    }

    @Test
    public void verifyDotNavigationContainerHeight() throws Exception {

        final int expectedHeight = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_container_height);
        getIndicator().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyNavigationDotsAreDrawnAtCenter() throws Exception {
        getIndicator().check(matches(DotNavigationMatcher.hasGravity(Gravity.CENTER)));
    }
}