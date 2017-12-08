/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.notificationbadge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.NotificationBadge;
import com.philips.platform.uid.view.widget.RatingBar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.White;
import static junit.framework.Assert.assertTrue;

public class DefaultNotificationBadgeTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = mActivityTestRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_notification_default_badge);
        testResources = activity.getResources();
    }

    @Test
    public void verifyIconDefaultTextSize() {
        int expectedTextSize = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_default_textSize);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedTextSize)));
    }

    @Test
    public void verifyIconDefaultTextColor() {
        final int expectedColor = ContextCompat.getColor(activity, White);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }
    @Test
    public void verifyViewDefaultTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_default_margin);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyViewDefaultRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_default_margin);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }

    @Test
    public void verifyTextDefaultRightPadding() {
        int expectedStartPadding = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_padding);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameStartPadding(expectedStartPadding)));
    }

    @Test
    public void verifyTextDefaultLeftPadding() {
        int expectedEndPadding = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_padding);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameEndPadding(expectedEndPadding)));
    }

    @Test
    public void verifyDefaultClearText() {
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.hasNoText()));
    }

    @Test
    public void verifyDefaultBadgeTextTypeface() {
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyViewDefaultElevation() {
        float expectedElevation = testResources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_notificationbadge_elevation);
        getNotificationBadgeTextLayout().check(matches(ViewPropertiesMatchers.isSameElevation(expectedElevation)));
    }

// below are the small notification Badge test case

    @Test
    public void verifyIconSmallTextSize() {
        int expectedTextSize = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_small_textSize);
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedTextSize)));
    }
    @Test
    public void verifyIconSmallTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidNotificationBadgeDefaultTextColor);
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyViewSmallTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_small_margin);
        getNotificationSmallBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }
    @Test
    public void verifyViewSmallRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_small_margin);
        getNotificationSmallBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }
    @Test
    public void verifyTextSmallRightPadding() {
        int expectedStartPadding = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_padding);
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameStartPadding(expectedStartPadding)));
    }
    @Test
    public void verifyTextSmallLeftPadding() {
        int expectedEndPadding = testResources.getDimensionPixelSize(R.dimen.uid_notificationbadge_padding);
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameEndPadding(expectedEndPadding)));
    }
    @Test
    public void verifySmallClearText(){
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.hasNoText()));
    }
    @Test
    public void verifySmallBadgeTextTypeface() {
        getNotificationSmallBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyViewSmallElevation() {
        float expectedElevation = testResources.getDimensionPixelSize(com.philips.platform.uid.R.dimen.uid_notificationbadge_elevation);
        getNotificationSmallBadgeTextLayout().check(matches(ViewPropertiesMatchers.isSameElevation(expectedElevation)));
    }

    private ViewInteraction getNotificationBadgeTextLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_text_default));
    }

    private ViewInteraction getNotificationBadgeViewLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_email));
    }
    private ViewInteraction getNotificationSmallBadgeTextLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_text_small));
    }
    private ViewInteraction getNotificationSmallBadgeViewLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_imageview));
    }
}
