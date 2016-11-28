/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.alert;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.platform.uit.DialogTestFragment;
import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.Gray65;
import static com.philips.platform.uit.test.R.color.Gray75;
import static com.philips.platform.uit.utils.UIDTestUtils.waitFor;

public class AlertTest {

    private static final String NOTITLE = "NOTITLE";
    private Resources testResources;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.main_layout);
        mActivityTestRule.getActivity().switchFragment(new DialogTestFragment());
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    /*****************************************
     * Alert Layout Scenarios
     *********************************************/
    @Test
    public void verifyAlertWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alert_width);
        getAlert().check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    /******************************
     * Alert title layout scenarios
     ******************************/

    @Test
    public void verifyAlertTitleFontSize() {
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alert_title_font_size);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertTitleIconHeight() {
        waitFor(testResources, 750);
        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedIconHeight)));
    }

    @Test
    public void verifyAlertTitleIconWidth() {
        waitFor(testResources, 750);
        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expectedIconWidth)));
    }

    @Test
    public void verifyAlertTitleIconRightPadding() {
        waitFor(testResources, 750);
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_icon_rightpadding);
        getAlertTitleIcon().check(matches((ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding))));
    }

    @Test
    public void verifyAlertHeaderTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_icon_leftrighttop_margin);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyAlertHeaderRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_leftrighttop_margin);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }

    @Test
    public void verifyAlertHeaderLeftMargin() {
        int expectedLeftMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitle_leftrighttop_margin);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    /******************************
     * Alert content layout scenarios
     ******************************/

    @Test
    public void verifyAlertContentFontSize() {
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alert_font_size);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertContentTextLeading() {
        int expectedTextLeading = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertcontenttext_linespacing);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(expectedTextLeading)));
    }

    @Test
    public void verifyAlertTitleTextLeading() {
        int expectedTextLeading = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alerttitletext_linespacing);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(expectedTextLeading)));
    }

    @Test
    public void verifyAlertContentTopMargin() {
        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertcontent_top_padding);
        getAlertHeader().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedTopMargin)));
    }

    @Test
    public void verifyAlertContentLeftMargin() {
        UIDTestUtils.waitFor(testResources, 750);
        int expectedLeftMargin = (int) testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyAlertContentRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedRightMargin)));
    }

    @Test
    public void verifyAlertContentBottomMargin() {
        int expectedBottomMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertActionArea().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedBottomMargin)));
    }

    /******************************
     * Alert content layout scenarios without title
     ******************************/
    @Test
    public void VerifyAlertIsDisplayedWithNoTitle() {
        mActivityTestRule.getActivity().switchFragment(DialogTestFragment.create());
        getAlertTitle().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
    }

    @Test
    public void VerifyContentTopMarginWithNoTitle() {
        mActivityTestRule.getActivity().switchFragment(DialogTestFragment.create());
        getAlertTitle().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));

        int expectedTopMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alert_content_top_margin_when_no_title);
        getAlertContainer().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    /******************************
     * Alert action button layout scenarios
     ******************************/

    @Test
    public void verifyPaddingBetweenActionButtons() {
        int expectedButtonsMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertaction_buttons_padding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedButtonsMargin)));
    }

    @Test
    public void verifyRightPaddingOfActionButtonView() {
        int expectedButtonRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertaction_button_rightpadding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedButtonRightMargin)));
    }

    @Test
    public void verifyActionAreaHeight() {
        UIDTestUtils.waitFor(testResources, 750);
        int expectedActionareaHeight = (int) testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.alertactionarea_height);
        getAlertActionArea()
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedActionareaHeight)));
    }

    @Test
    public void verifyFunctionalityOfConfirmativeButton() {
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert_positive_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert)).check(doesNotExist());
    }

    @Test
    public void verifyFunctionalityOfDismissiveButton() {
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert_negative_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert)).check(doesNotExist());
    }

    /*******************************************************
     * Theming Scenarios for Alert
     ******************************************************/
    @Test
    public void verifyTextColorofAlertTitle() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, Gray75);
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyTextColorofAlertContent() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, Gray65);
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    private ViewInteraction getAlert() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert));
    }

    private ViewInteraction getAlertTitle() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_title));
    }

    private ViewInteraction getAlertHeader() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_dialog_header));
    }

    private ViewInteraction getAlertTitleIcon() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_icon));
    }

    private ViewInteraction getAlertContent() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_message));
    }

    private ViewInteraction getAlertContainer() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_message_scroll_container));
    }

    private ViewInteraction getAlertActionArea() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_control_area));
    }

    private ViewInteraction getAlertConfirmativeButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_positive_button));
    }


}

