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

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class UIDTabItemLayoutWithTitleTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Activity activity;

    @Before
    public void setUp() {
        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(R.layout.uid_tabbar_with_title_layout);
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

    private ViewInteraction getUIDTabItemLabel() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_tab_label));
    }

    @Test
    public void verifyTabItemMinHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_layout_height_with_title);
        getUIDTabItemIconOnlyLayout().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expected)));
    }

    @Test
    public void verifyTabIconMarginTop() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_icon_margin_top);
        getUIDTabItemIcon().check(matches(ViewPropertiesMatchers.isSameTopMargin(expected)));
    }

    @Test
    public void verifyTabNotificationBadgeMarginBottom() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_notification_margin_start);
        getUIDTabItemNotificationBadge().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyTabLabelMarginBottom() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_label_margin_top_bottom);
        getUIDTabItemLabel().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyTabLabelMarginTop() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_label_margin_top_bottom);
        getUIDTabItemLabel().check(matches(ViewPropertiesMatchers.isSameTopMargin(expected)));
    }

    @Test
    public void verifyTabLabelMarginStart() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_label_margin_start_end);
        getUIDTabItemLabel().check(matches(ViewPropertiesMatchers.isSameStartMargin(expected)));
    }

    @Test
    public void verifyTabLabelMarginEnd() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_label_margin_start_end);
        getUIDTabItemLabel().check(matches(ViewPropertiesMatchers.isSameEndMargin(expected)));
    }

    @Test
    public void verifyTabLabelisClickable() {
        getUIDTabItemLabel().check(matches(isClickable()));
    }

    @Test
    public void verifyTabLabelisSingleLine() {
        getUIDTabItemLabel().check(matches(TextViewPropertiesMatchers.isSingleline()));
    }

    @Test
    public void verifyTabLabelSize() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_tab_label_text_size);
        getUIDTabItemLabel().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyTabLabelFontType() {
        getUIDTabItemLabel().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyTabLabelTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTabsDefaultNormalOnTextColor);
        getUIDTabItemLabel().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_selected, expectedColor)));
    }
}