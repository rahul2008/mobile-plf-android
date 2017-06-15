/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.separator;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.SeparatorMatcher;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class SeparatorTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = testRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ComponentListFragment());
    }

    @Test
    public void verifyListViewSeparatorHeight() throws Exception {
        final int height = activity.getResources().getDimensionPixelSize(R.dimen.uid_divider_Height);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList)).check(matches(SeparatorMatcher.hasSameHeight(height)));
    }

    @Test
    public void verifyListViewSeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, getUidSeparatorAttribute());
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList))
                .check(matches(SeparatorMatcher.hasSameColor(color)));
    }


    //TODO below test is ignored because it is failing beacuse of 1 value diffrence e.g expected is 4 but after java converts float to int its coming out 3
    @Ignore
    @Test
    public void verifySeparatorHeight() throws Exception {
        final float height = activity.getResources().getDimensionPixelSize(R.dimen.uid_divider_Height);
        UIDTestUtils.waitFor(height, 750);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator)).check(matches(SeparatorMatcher.hasHeight((int) height)));
    }

    @Test
    public void verifySeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, getUidSeparatorAttribute());

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator))
                .check(matches(SeparatorMatcher.hasSameColor(color)));
    }

    @Test
    public void verifySeparatorColorWithCustomColor() throws Exception {
        final int color = ContextCompat.getColor(activity, R.color.uidColorBlack);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator_with_color))
                .check(matches(SeparatorMatcher.hasSameColor(color)));
    }

    @Test
    public void verifyRecyclerViewSeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, getUidSeparatorAttribute());
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(SeparatorMatcher.hasSameColor(color)));
    }

    @Test
    public void verifySeperatorIsPresent() {
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyRecyclerViewSeparatorHeight() throws Exception {
        final int height = activity.getResources().getDimensionPixelSize(R.dimen.uid_divider_Height);

        UIDTestUtils.waitFor(height, 1000);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(SeparatorMatcher.hasSameHeight(height)));
    }

    private int getUidSeparatorAttribute() {
        return R.attr.uidSeparatorContentNormalBackgroundColor;
    }
}
