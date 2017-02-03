/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.components.alert;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.platform.uid.DialogTestFragment;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.Gray65;
import static com.philips.platform.uid.test.R.color.Gray75;

public class AlertTest {

    private static final String NOTITLE = "NOTITLE";
    private Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private BaseTestActivity activity;

    public void setUpDefault() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new DialogTestFragment());
        testResources = activity.getResources();
    }

    /*****************************************
     * Alert Layout Scenarios
     *********************************************/
    @Test
    public void verifyAlertWidth() {
        setUpDefault();
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_width);
        getAlert().check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    /******************************
     * Alert title layout scenarios
     ******************************/

    @Test
    public void verifyAlertTitleFontSize() {
        setUpDefault();
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_title_font_size);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertTitleIconHeight() {
        setUpDefault();

        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedIconHeight)));
    }

    @Test
    public void verifyAlertTitleIconWidth() {
        setUpDefault();
        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expectedIconWidth)));
    }

    @Test
    public void verifyAlertTitleIconRightPadding() {
        setUpDefault();
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_icon_rightpadding);
        getAlertTitleIcon().check(matches((ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding))));
    }

    @Test
    public void verifyAlertHeaderTopMargin() {
        setUpDefault();
        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_icon_leftrighttop_margin);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyAlertHeaderRightMargin() {
        setUpDefault();
        int expectedRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_leftrighttop_margin);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }

    @Test
    public void verifyAlertHeaderLeftMargin() {
        setUpDefault();
        int expectedLeftMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitle_leftrighttop_margin);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    /******************************
     * Alert content layout scenarios
     ******************************/

    @Test
    public void verifyAlertContentFontSize() {
        setUpDefault();
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_font_size);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertContentTextLeading() {
        setUpDefault();
        int expectedTextLeading = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertcontenttext_linespacing);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(expectedTextLeading)));
    }

    @Test
    public void verifyAlertTitleTextLeading() {
        setUpDefault();
        int expectedTextLeading = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alerttitletext_linespacing);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(expectedTextLeading)));
    }

    @Test
    public void verifyAlertContentTopMargin() {
        setUpDefault();
        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertcontent_top_padding);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedTopMargin)));
    }

    @Test
    public void verifyAlertContentLeftMargin() {
        setUpDefault();
        int expectedLeftMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyAlertContentRightMargin() {
        setUpDefault();
        int expectedRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }

    @Ignore
    @Test
    public void verifyAlertContentBottomMargin() {
        setUpDefault();
        int expectedBottomMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertActionArea().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedBottomMargin)));
    }

    /******************************
     * Alert content layout scenarios without title
     ******************************/
    @Test
    public void verifyAlertIsDisplayedWithNoTitle() {
        setUpNoTitleAlert();

        getAlertTitle().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
    }

    @Test
    public void verifyContentTopMarginWithNoTitle() {
        setUpNoTitleAlert();
        getAlertTitle().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));

        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alert_content_top_margin_when_no_title);
        getAlertContainer().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    private void setUpNoTitleAlert() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        testResources = mActivityTestRule.getActivity().getResources();

        mActivityTestRule.getActivity().switchFragment(DialogTestFragment.create());
    }

    /******************************
     * Alert action button layout scenarios
     ******************************/

    @Test
    public void verifyPaddingBetweenActionButtons() {
        setUpDefault();
        int expectedButtonsMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_buttons_padding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedButtonsMargin)));
    }

    @Test
    public void verifyRightPaddingOfActionButtonView() {
        setUpDefault();
        int expectedButtonRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_button_rightpadding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedButtonRightMargin)));
    }

    @Test
    public void verifyActionAreaHeight() {
        setUpDefault();
        int expectedActionareaHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertactionarea_height);
        getAlertActionArea()
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedActionareaHeight)));
    }

    @Test
    public void verifyFunctionalityOfConfirmativeButton() {
        setUpDefault();

        onView(withId(com.philips.platform.uid.test.R.id.uid_alert_positive_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uid.test.R.id.uid_alert)).check(doesNotExist());
    }

    @Test
    public void verifyFunctionalityOfDismissiveButton() {
        setUpDefault();
        onView(withId(com.philips.platform.uid.test.R.id.uid_alert_negative_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uid.test.R.id.uid_alert)).check(doesNotExist());
    }

    /*******************************************************
     * Theming Scenarios for Alert
     ******************************************************/
    @Test
    public void verifyTextColorofAlertTitle() {
        setUpDefault();
        final int expectedColor = ContextCompat.getColor(activity, Gray75);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTextColorofAlertContent() {
        setUpDefault();
        final int expectedColor = ContextCompat.getColor(activity, Gray65);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    private ViewInteraction getAlert() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert));
    }

    private ViewInteraction getAlertTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_title));
    }

    private ViewInteraction getAlertHeader() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_dialog_header));
    }

    private ViewInteraction getAlertTitleIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_icon));
    }

    private ViewInteraction getAlertContent() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_message));
    }

    private ViewInteraction getAlertContainer() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_message_scroll_container));
    }

    private ViewInteraction getAlertActionArea() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_control_area));
    }

    private ViewInteraction getAlertConfirmativeButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_alert_positive_button));
    }
}

