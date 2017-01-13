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
import com.philips.platform.uid.matcher.DividerMatcher;
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
    public void verifyDividerHeight() throws Exception {
        final float height = activity.getResources().getDimensionPixelSize(R.dimen.uid_divider_Height);
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList)).check(matches(DividerMatcher.hasSameHeight(height)));
    }

    @Test
    public void verifyDividerColor() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidSeparatorColor);
        final float alpha = UIDTestUtils.getAttributeAlpha(activity, R.attr.uidSeparatorAlpha);
        final int modulateColorAlpha = UIDTestUtils.modulateColorAlpha(color, alpha);
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.componentList))
                .check(matches(DividerMatcher.isSameColor(modulateColorAlpha)));
    }

    @Test
    public void verifyDividerColorForRecyclerView() throws Exception {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidSeparatorColor);
        final float alpha = UIDTestUtils.getAttributeAlpha(activity, R.attr.uidSeparatorAlpha);
        final int modulateColorAlpha = UIDTestUtils.modulateColorAlpha(color, alpha);
        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(DividerMatcher.isSameColor(modulateColorAlpha)));
    }

    @Test
    public void verifyDividerHeightForRecyclerView() throws Exception {
        final float height = activity.getResources().getDimensionPixelSize(R.dimen.uid_divider_Height);

        onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems))
                .check(matches(DividerMatcher.hasSameHeight(height)));
    }
}
