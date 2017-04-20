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

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.notificationbadge;

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
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.view.widget.NotificationBadge;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.White;
import static junit.framework.Assert.assertTrue;

public class SmallNotificationBadgeTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = mActivityTestRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_notification_samll_badge);
        testResources = activity.getResources();
    }
    @Test
    public void verifyIconTextSize() {
        int expectedTextSize = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_small_square_round_textSize);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedTextSize)));
    }
    @Test
    public void verifyIconTextColor() {
        final int expectedColor = ContextCompat.getColor(activity, White);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyViewTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_small_view_margin_top_right);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyViewRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_small_view_margin_top_right);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }
    @Test
    public void verifyTextRightPadding() {
        int expectedStartPadding = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_square_round_padding);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameStartPadding(expectedStartPadding)));
    }
    @Test
    public void verifyTextLeftPadding() {
        int expectedEndPadding = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_square_round_padding);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameEndPadding(expectedEndPadding)));
    }
    @Test
    public void verifyClearText(){
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.hasNoText()));
    }
    @Test
    public void verifyBadgeTextTypeface() {
        ViewGroup parent = (ViewGroup) View.inflate(activity, com.philips.platform.uid.test.R.layout.layout_notification_default_badge, null);
        NotificationBadge badge = (NotificationBadge) parent.findViewById(com.philips.platform.uid.test.R.id.uid_text_default);
        assertTrue(TextViewPropertiesMatchers.isSameTypeface(activity, badge.getTypeface(), TestConstants.FONT_PATH_CS_BOOK).matches(badge));
    }


    private ViewInteraction getNotificationBadgeTextLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_text_small));
    }
    private ViewInteraction getNotificationBadgeViewLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_email));
    }

}
