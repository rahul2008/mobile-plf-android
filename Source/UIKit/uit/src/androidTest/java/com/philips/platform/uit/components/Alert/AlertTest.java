/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Alert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.DialogTestFragment;
import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;

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
import static com.philips.platform.uit.test.R.color.White;
import static com.philips.platform.uit.utils.UITTestUtils.waitFor;

public class AlertTest {

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
    public void verifyAlertMinWidth() {

    }

    @Test
    public void verifyAlertMaxWidth() {

    }

    @Test
    public void verifyAlertCornerRadius() {
        float expectedCornerRadius = (float) Math.floor(testResources.getDimension(com.philips.platform.uit.test.R.dimen.alert_corner_radius));
        getAlert().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedCornerRadius)));
    }

    /******************************
     * Alert title layout scenarios
     ******************************/

    @Test
    public void verifyAlertTitleFontSize() {
        int expectedFontSize = (int) (testResources.getDimension(com.philips.platform.uit.test.R.dimen.alert_title_font_size));
        getAlertTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertTitleIconHeight() {
        waitFor(testResources, 750);
        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedIconHeight)));
    }

    @Test
    public void verifyAlertTitleIconWidth() {
        waitFor(testResources, 750);
        int expectedIconWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alerttitle_iconsize);
        getAlertTitleIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedIconWidth)));
    }

    @Test
    public void verifyAlertTitleIconRightPadding() {
        waitFor(testResources, 750);
        int expectedRightPadding = (int) (testResources.getDimension(com.philips.platform.uit.test.R.dimen.alerttitle_icon_rightpadding));
        getAlertTitleIcon().check(matches((ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding))));
    }

    /******************************
     * Alert content layout scenarios
     ******************************/

    @Test
    public void verifyAlertContentFontSize() {
        int expectedFontSize = (int) (testResources.getDimension(com.philips.platform.uit.test.R.dimen.alert_font_size));
        getAlertContent().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAlertContentTextLeading() {

    }

    @Test
    public void verifyAlertContentTopPadding() {
        int expectedTopMargin = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertcontent_top_padding);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedTopMargin)));
    }

    @Test
    public void verifyAlertContentLeftPadding() {
        UITTestUtils.waitFor(testResources, 750);

        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftPadding)));
    }

    @Test
    public void verifyAlertContentRightPadding() {
        int expectedRightPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedRightPadding)));
    }

    @Test
    public void verifyAlertContentBottomPadding() {
        int expectedBottomPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertcontent_leftrightbottom_margin);
        getAlertContent().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expectedBottomPadding)));
    }

    /******************************
     * Alert action button layout scenarios
     ******************************/

    @Test
    public void verifyPaddingBetweenActionButtons() {
        int expectedButtonsPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertaction_buttons_padding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedButtonsPadding)));
    }

    @Test
    public void verifyRightPaddingOfActionButtonView() {
        int expectedButtonRightPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertaction_button_rightpadding);
        getAlertConfirmativeButton().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedButtonRightPadding)));
    }

    @Test
    public void verifyActionAreaHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedActionareaHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.alertactionarea_height);
        getAlertActionArea()
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedActionareaHeight)));
    }

    // TODO: 08/11/16 This tests needs to be moved to app instead of library
    @Test
    public void verifyFunctionalityOfConfirmativeButton() {
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert_positive_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert)).check(doesNotExist());
    }

    // TODO: 08/11/16 This tests needs to be moved to app instead of library
    @Test
    public void verifyFunctionalityOfDismissiveButton() {
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert_negative_button)).perform(ViewActions.click());
        onView(withId(com.philips.platform.uit.test.R.id.uid_alert)).check(doesNotExist());
    }

    /*******************************************************
     * Theming Scenarios for Alert
     ******************************************************/

    @Test
    public void verifyFillColorofAlert() {
        final int expectedFillColor = ContextCompat.getColor(instrumentationContext, White);
    }

    @Test
    public void verifyShadowColorofAlert() {
        final int shadowColor = UITTestUtils.modulateColorAlpha(Color.parseColor("#000000"), 0.20f);
    }

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

    @Test
    public void verifyIconColorofAlertTitle() {
        //Cannot be automated
    }

    @Test
    public void verifyColorofDimLayer() {
        final int expecteddimLayerColor = UITTestUtils.modulateColorAlpha(Color.parseColor("#0A0C1E"), 0.80f);
    }

    private ViewInteraction getAlert() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert));
    }

    private ViewInteraction getAlertTitle() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_title));
    }

    private ViewInteraction getAlertTitleIcon() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_icon));
    }

    private ViewInteraction getAlertContent() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_message));
    }

    private ViewInteraction getAlertActionArea() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_control_area));
    }

    private ViewInteraction getAlertConfirmativeButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_positive_button));
    }

    private ViewInteraction getAlertDismissiveButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.uid_alert_negative_button));
    }
}

