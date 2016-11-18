/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uit.components.navigation;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.utils.UITTestUtils.waitFor;

public class NavigationBarTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Context applicationContext;

    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.main_layout);

        mActivityTestRule.getActivity().switchFragment(new NavigationbarFragment());

        applicationContext = mActivityTestRule.getActivity().getApplicationContext();
    }

    @Test
    public void VerifyToolbarHeight() throws Exception {
        waitFor(applicationContext.getResources(), 750);
        int toolbarHeight = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_height);

        getNavigationActionButton().check(matches(ViewPropertiesMatchers.isSameViewHeight(toolbarHeight)));
    }

    @Test
    public void VerifyToolbarLeftMargin() throws Exception {
        waitFor(applicationContext.getResources(), 750);
        int iconpadding = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_left_margin);

        getNavigationActionButton().check(matches(ViewPropertiesMatchers.isSameLeftMargin(iconpadding)));
    }

    @Test
    public void VerifyToolbarRightMargin() throws Exception {
        waitFor(applicationContext.getResources(), 750);
        int iconpadding = (int) applicationContext.getResources().getDimension(com.philips.platform.uit.test.R.dimen.navigation_right_margin);

        getNavigationActionButton().check(matches(ViewPropertiesMatchers.isSameRightMargin(iconpadding)));
    }

    private ViewInteraction getNavigationActionButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.toolbar));
    }
}
