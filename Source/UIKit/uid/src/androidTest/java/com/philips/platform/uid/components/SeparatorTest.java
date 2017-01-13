/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.SeparatorMatcher;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

public class SeparatorTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 4);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ComponentListFragment());
    }

    @NonNull
    private Intent getLaunchIntent(final int navigationColor, final int contentColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyListViewSeparatorHeight() throws Exception {
        final int height = (int) activity.getResources().getDimension(R.dimen.uid_divider_Height);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList)).check(matches(SeparatorMatcher.hasSameHeight(height)));
    }

    @Test
    public void verifyListViewSeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidSeparatorColor);
        final float alpha = UIDTestUtils.getAttributeAlpha(activity, R.attr.uidSeparatorAlpha);
        final int modulateColorAlpha = UIDTestUtils.modulateColorAlpha(color, alpha);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList))
                .check(matches(SeparatorMatcher.hasSameColor(modulateColorAlpha)));
    }

    @Test
    public void verifySeparatorHeight() throws Exception {
        final float height = activity.getResources().getDimension(R.dimen.uid_divider_Height);
        UIDTestUtils.waitFor(height, 750);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator)).check(matches(SeparatorMatcher.hasHeight((int) height)));
    }

    @Test
    public void verifySeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidSeparatorColor);
        final float alpha = UIDTestUtils.getAttributeAlpha(activity, R.attr.uidSeparatorAlpha);
        final int modulateColorAlpha = UIDTestUtils.modulateColorAlpha(color, alpha);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.uid_test_separator))
                .check(matches(SeparatorMatcher.hasSameColor(modulateColorAlpha)));
    }

    @Test
    public void verifyRecyclerViewSeparatorColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidSeparatorColor);
        final float alpha = UIDTestUtils.getAttributeAlpha(activity, R.attr.uidSeparatorAlpha);
        final int modulateColorAlpha = UIDTestUtils.modulateColorAlpha(color, alpha);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(SeparatorMatcher.hasSameColor(modulateColorAlpha)));
    }

    @Test
    public void verifyRecyclerViewSeparatorHeight() throws Exception {
        final int height = (int) activity.getResources().getDimension(R.dimen.uid_divider_Height);

        UIDTestUtils.waitFor(height, 700);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(SeparatorMatcher.hasSameHeight(height)));
    }
}
