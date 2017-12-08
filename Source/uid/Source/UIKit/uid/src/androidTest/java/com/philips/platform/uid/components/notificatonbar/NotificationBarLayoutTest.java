package com.philips.platform.uid.components.notificatonbar;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NotificationBarLayoutTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);

        activity = mActivityTestRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_notification_bg_white);

        testResources = getInstrumentation().getContext().getResources();
    }

    @Test
    public void verifyNotificationTitleColor() {
        int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidNotificationBarWhiteNormalPrimaryTextColor);
        getNotificationTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyNotificationTitleSize() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_title_text_size);
        getNotificationTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedSize)));
    }

    @Test
    public void verifyNotificationTitleTopMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationTitle().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedSize)));
    }

    @Test
    public void verifyNotificationTitleStartMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationTitle().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedSize)));
    }

    @Test
    public void verifyNotificationTitleEndMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationTitle().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedSize)));
    }

    @Test
    public void verifyNotificationContentColor() {
        int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidNotificationBarWhiteNormalSecondaryTextColor);
        getNotificationContent().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyNotificationContentSize() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_content_text_size);
        getNotificationContent().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedSize)));
    }

    @Test
    public void verifyNotificationContentTopMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_content_margin_top);
        getNotificationContent().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedSize)));
    }

    @Test
    public void verifyNotificationContentStartMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationContent().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedSize)));
    }

    @Test
    public void verifyNotificationContentEndMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationContent().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedSize)));
    }

//    @Test
//    public void verifyNotificationActionButton1MinHeight() {
//        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_btn_min_height);
//        getNotificationActionButton1().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedSize)));
//    }

    @Test
    public void verifyNotificationActionButton1StartMargin() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationActionButton1().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedSize)));
    }

//    @Test
//    public void verifyNotificationActionButton2MinHeight() {
//        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_btn_min_height);
//        getNotificationActionButton2().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedSize)));
//    }

    @Test
    public void verifyNotificationActionButton2StartMargin() {
        getNotificationActionButton2().check(matches(ViewPropertiesMatchers.isSameStartMargin(0)));
    }

    @Test
    public void verifyNotificationIconStartPadding() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationCloseButton().check(matches(ViewPropertiesMatchers.isSameStartPadding(expectedSize)));
    }

    @Test
    public void verifyNotificationIconEndPadding() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationCloseButton().check(matches(ViewPropertiesMatchers.isSameEndPadding(expectedSize)));
    }

    @Test
    public void verifyNotificationIconTopPadding() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationCloseButton().check(matches(ViewPropertiesMatchers.isSameTopPadding(expectedSize)));
    }

    @Test
    public void verifyNotificationIconBottomPadding() {
        int expectedSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.notification_bar_general_margin);
        getNotificationCloseButton().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expectedSize)));
    }

    private ViewInteraction getNotificationTitle() {
        return onView(withId(R.id.uid_notification_title));
    }

    private ViewInteraction getNotificationContent() {
        return onView(withId(R.id.uid_notification_content));
    }

    private ViewInteraction getNotificationActionButton1() {
        return onView(withId(R.id.uid_notification_btn_1));
    }

    private ViewInteraction getNotificationActionButton2() {
        return onView(withId(R.id.uid_notification_btn_2));
    }

    private ViewInteraction getNotificationCloseButton() {
        return onView(withId(R.id.uid_notification_icon));
    }
}
