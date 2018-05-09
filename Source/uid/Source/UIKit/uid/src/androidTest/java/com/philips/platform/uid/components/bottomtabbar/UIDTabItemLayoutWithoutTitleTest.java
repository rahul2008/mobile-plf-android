/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.bottomtabbar;

import android.app.Activity;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class UIDTabItemLayoutWithoutTitleTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Activity activity;

    @Before
    public void setUp() {
        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(R.layout.uid_tabbar_icon_only_layout);
        resources = getInstrumentation().getContext().getResources();
        Espresso.registerIdlingResources(baseTestActivity.getIdlingResource());
        activity = baseTestActivity;
    }

    private ViewInteraction getUIDTabItemIconOnlyLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_tab_frame_layout));
    }

    private ViewInteraction getUIDTabItemIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_tab_icon));
    }

    private ViewInteraction getUIDTabItemNotificationBadge() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_tab_notification_badge));
    }

    @Test
    public void verifyTabItemWidth() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_min_width);
        getUIDTabItemIconOnlyLayout().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expected)));
    }

    @Test
    public void verifyTabItemHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_layout_height_without_title);
        getUIDTabItemIconOnlyLayout().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expected)));
    }

    @Test
    public void verifyTabItemMarginTop() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_item_margin_top_bottom);
        getUIDTabItemIconOnlyLayout().check(matches(ViewPropertiesMatchers.isSameTopMargin(expected)));
    }

    @Test
    public void verifyTabItemMarginBottom() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_item_margin_top_bottom);
        getUIDTabItemIconOnlyLayout().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyTabIconWidth() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_icon_height_width);
        getUIDTabItemIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(expected)));
    }

    @Test
    public void verifyTabIconHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_icon_height_width);
        getUIDTabItemIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }

    @Test
    public void verifyTabIconGravity() {
        getUIDTabItemIcon().check(matches(ViewPropertiesMatchers.hasSameFrameLayoutGravity(Gravity.CENTER)));
    }

    @Test
    public void verifyTabNotificationBadgeGravity() {
        getUIDTabItemNotificationBadge().check(matches(ViewPropertiesMatchers.hasSameFrameLayoutGravity(Gravity.CENTER)));
    }

    @Test
    public void verifyTabNotificationBadgeMarginStart() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_notification_margin_start);
        getUIDTabItemNotificationBadge().check(matches(ViewPropertiesMatchers.isSameStartMargin(expected)));
    }

    @Test
    public void verifyTabNotificationBadgeMarginBottom() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_notification_margin_bottom);
        getUIDTabItemNotificationBadge().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyTabIconTintColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTabsDefaultNormalOnIconColor);
        getUIDTabItemIcon().check(matches(ViewPropertiesMatchers.hasSameImageTintListStateColor(new int[]{android.R.attr.state_selected},expectedColor)));
    }
}
