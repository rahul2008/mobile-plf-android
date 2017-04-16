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

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.InputValidationMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.Gray65;
import static com.philips.platform.uid.test.R.color.White;

public class NotificationBadgeTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = mActivityTestRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_notification_badge);
        testResources = activity.getResources();
    }
    @Test
    public void verifyIconTextSize() {
        int expectedTextSize = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_default_square_round_textSize);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedTextSize)));
    }
    @Test
    public void verifyIconTextColor() {
        final int expectedColor = ContextCompat.getColor(activity, White);
        getNotificationBadgeTextLayout().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyViewTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_default_view_margin_top);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyViewRightMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(R.dimen.uid_notification_badge_default_view_margin_right);
        getNotificationBadgeViewLayout().check(matches(TextViewPropertiesMatchers.isSameRightMargin(expectedTopMargin)));
    }

    private ViewInteraction getNotificationBadgeTextLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_text_default));
    }
    private ViewInteraction getNotificationBadgeViewLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_button));
    }

}
