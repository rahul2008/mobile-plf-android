/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.alert;


import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.DialogTestFragment;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.LayoutPropertiesMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DialogTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;
    private Resources testResources;

    @Before
    public void setUp() {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(0));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new DialogTestFragment());
        testResources = activity.getResources();
    }

    /******************************
     * Dialog header layout scenarios
     ******************************/

    @Test
    public void verifyDialogTitleFontSize() {

        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_title_font_size);
        getDialogTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyDialogTitleTopMargin() {

        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_content_top_margin_when_no_title);
        getDialogTitle().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyDialogTitleIconHeight() {

        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_iconsize);
        getDialogTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedIconHeight)));
    }

    @Test
    public void verifyDialogTitleIconWidth() {

        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_iconsize);
        getDialogTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expectedIconWidth)));
    }

    @Test
    public void verifyDialogTitleIconEndMargin() {

        int expectedEndMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_icon_rightpadding);
        getDialogTitleIcon().check(matches((ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin))));
    }

    @Test
    public void verifyDialogTitleIconTopMargin() {

        int expectedEndMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_content_top_padding);
        getDialogTitleIcon().check(matches((ViewPropertiesMatchers.isSameTopMargin(expectedEndMargin))));
    }

    @Test
    public void verifyDialogHeaderEndPadding() {

        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_leftrighttop_margin);
        getDialogHeader().check(matches(ViewPropertiesMatchers.isSameEndPadding(expectedRightPadding)));
    }

    @Test
    public void verifyDialogHeaderStartPadding() {

        int expectedLeftPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_leftrighttop_margin);
        getDialogHeader().check(matches(ViewPropertiesMatchers.isSameStartPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyDialogHeaderBottomPadding() {

        int expectedBottomPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_content_top_padding);
        getDialogHeader().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expectedBottomPadding)));
    }

    /******************************
     * Dialog divider scenarios
     ******************************/

    @Test
    public void verifyDialogTopDividerDoesNotExist() {
        getDialogTopDivider().check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void verifyDialogBottomDividerDoesNotExist() {
        getDialogBottomDivider().check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /******************************
     * Dialog action button layout scenarios
     ******************************/

    @Test
    public void verifyDialogActionAreaTopPadding() {

        int expectedPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_button_rightpadding);
        getDialogActionArea().check(matches(ViewPropertiesMatchers.isSameTopPadding(expectedPadding)));
    }

    @Test
    public void verifyDialogActionAreaBottomPadding() {

        int expectedPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_button_rightpadding);
        getDialogActionArea().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expectedPadding)));
    }

    @Test
    public void verifyDialogActionAreaStartPadding() {

        int expectedPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_button_rightpadding);
        getDialogActionArea().check(matches(ViewPropertiesMatchers.isSameStartPadding(expectedPadding)));
    }

    @Test
    public void verifyDialogActionAreaEndPadding() {

        int expectedPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_button_rightpadding);
        getDialogActionArea().check(matches(ViewPropertiesMatchers.isSameEndPadding(expectedPadding)));
    }

    @Test
    public void verifyDialogPositiveButtonLeftMargin() {

        int expectedMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_buttons_padding);
        getPositiveButton().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedMargin)));
    }

    @Test
    public void verifyAlternateButtonDoesNotExist() {
        getAlternateButton().check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void verifyDialogActionAreaIsHorizontal() {

        int expectedOrientation = LinearLayout.HORIZONTAL;
        getDialogActionArea().check(matches(LayoutPropertiesMatcher.isSameOrientation(expectedOrientation)));
    }

    /*******************************************************
     * Theming Scenarios for Dialog
     ******************************************************/
    @Test
    public void verifyTextColorOfAlertTitle() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemPrimaryNormalTextColor);
        getDialogTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    private ViewInteraction getDialogTopDivider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_top_divider));
    }

    private ViewInteraction getDialogTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_title));
    }

    private ViewInteraction getDialogHeader() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_header));
    }

    private ViewInteraction getDialogTitleIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_icon));
    }

    private ViewInteraction getDialogBottomDivider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_bottom_divider));
    }

    private ViewInteraction getDialogActionArea() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_control_area));
    }

    private ViewInteraction getAlternateButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_alternate_button));
    }

    private ViewInteraction getPositiveButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_positive_button));
    }
}
