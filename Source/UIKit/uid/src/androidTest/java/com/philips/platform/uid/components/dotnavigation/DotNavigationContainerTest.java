/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.DotNavigationMatcher;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class DotNavigationContainerTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal()));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(DotNavigationFragment.create());
    }

    protected ViewInteraction getIndicator() {
        return onView(withId(com.philips.platform.uid.test.R.id.pager_indicator));
    }

    @Test
    public void verifyDotNavigationContainerIsGoneWhenAdapterHasZeroItems() throws Exception {
        getIndicator().check(matches(not(ViewMatchers.isDisplayed())));
    }

    @Test
    public void verifyDotsAreDrawnAtCenter() throws Exception {
        getIndicator().check(matches(DotNavigationMatcher.hasGravity(Gravity.CENTER)));
    }
}
