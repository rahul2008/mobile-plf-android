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
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.DotNavigationMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

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

 /*   @Test
    public void verifyIndicatorIsNotDisplayedWhenNoViewpagerIsAttachedToIt() throws Exception {
        getEmptyIndicator().check(matches(not(isDisplayed())));
    }*/

    @Test
    public void verifyIndicatorIsNotDisplayedWhenViewpagerWithZeroItemsAttachedToInit() throws Exception {
        getEmptyIndicator().perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf();
            }

            @Override
            public String getDescription() {
                return "EmptyIndicator";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                final ViewPager pager = (ViewPager) activity.findViewById(com.philips.platform.uid.test.R.id.dot_navigation_pager);
                pager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return 0;
                    }

                    @Override
                    public boolean isViewFromObject(final View view, final Object object) {
                        return false;
                    }
                });
                ((DotNavigationIndicator) view).setViewPager(pager);
            }
        });

        getEmptyIndicator().check(matches(not(isDisplayed())));
    }

    @Test
    public void verifyLetMarginBetweenDots() throws Exception {
        final int expectedMargin = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_margin);

        getIndicator().check(matches(DotNavigationMatcher.isSameLeftMargin(expectedMargin)));
    }

    @Test
    public void verifyRightMarginBetweenDots() throws Exception {
        final int expectedMargin = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_icon_margin);

        getIndicator().check(matches(DotNavigationMatcher.isSameRightMargin(expectedMargin)));
    }

    protected ViewInteraction getIndicator() {
        return onView(withId(com.philips.platform.uid.test.R.id.pager_indicator));
    }

    protected ViewInteraction getEmptyIndicator() {
        return onView(withId(com.philips.platform.uid.test.R.id.empty_pager_indicator));
    }

    protected ViewInteraction getViewPager() {
        return onView(withId(com.philips.platform.uid.test.R.id.dot_navigation_pager));
    }

    @Test
    public void verifySelectedCircleColorBasedOnSuppliedTheme() throws Exception {

        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidDotNavigationDefaultNormalOnBackgroundColor);

        final int[] stateArray = {android.R.attr.state_selected, android.R.attr.state_enabled};
        getIndicator().check(matches(DotNavigationMatcher.sameBackgroundColor(stateArray, expectedColor)));
    }

    @Test
    public void verifyUnselectedCircleColorBasedOnSuppliedTheme() throws Exception {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidDotNavigationDefaultNormalOffBackgroundColor);

        getIndicator().check(matches(DotNavigationMatcher.sameBackgroundColor(new int[0], expectedColor)));
    }

/*    @Test
    public void verifyClickOnRightOfSelectedDotGivesCallbackToShowNext() throws Exception {
        getIndicator().check(matches(ViewMatchers.isDisplayed()));
        getIndicator().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        final int currentItem = ((ViewPager) activity.findViewById(com.philips.platform.uid.test.R.id.dot_navigation_pager)).getCurrentItem();
        Assert.assertEquals(currentItem, 1);
    }*/

    @Test
    public void verifyClickOnLeftOfSelectedDotGivesCallbackToShowPrevious() throws Exception {
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
        final int expectedHeight = activity.getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_indicator_min_height);

        getIndicator().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyNavigationDotsAreDrawnAtCenter() throws Exception {
        getIndicator().check(matches(DotNavigationMatcher.hasGravity(Gravity.CENTER)));
    }
}