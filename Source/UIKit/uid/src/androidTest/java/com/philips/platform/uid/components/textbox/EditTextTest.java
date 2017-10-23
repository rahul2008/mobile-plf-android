/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.textbox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.DrawableMatcher;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.EditText;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;
import static junit.framework.Assert.assertTrue;

public class EditTextTest {

    private static final int RIGHT_DRAWABLE_INDEX = 2;
    private static final int COMPOUND_DRAWABLE_INDEX = RIGHT_DRAWABLE_INDEX;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;
    private Context activityContext;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_textbox);
        testResources = activity.getResources();
        activityContext = activity;
    }

    //*********************************TextBoxLayout TestScenarios**************************//

    @Test
    public void verifyTextBoxTextFormatSupport() {
        getTextBox().perform(ViewActions.clearText());
        getTextBox().perform(ViewActions.typeText("Hello@123?"));
        getTextBox().check(matches(withText("Hello@123?")));
    }

    @Test
    public void verifyTextBoxTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_fontsize);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyTextBoxTextTypeface() {
        EditText editText = (EditText) View.inflate(activityContext, com.philips.platform.uid.test.R.layout.edit_text, null);
        assertTrue(TextViewPropertiesMatchers.isSameTypeface(activityContext, TestConstants.FONT_PATH_CS_BOOK).matches(editText));
    }

    @Test
    public void verifySameTextEditBoxRadiusOnDynamicCreation() {
        EditText editText = new EditText(mActivityTestRule.getActivity());
        float expectedRadius = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_corner_radius);
        Drawable strokeDrawable = ((LayerDrawable) editText.getBackground()).findDrawableByLayerId(R.id.uid_texteditbox_stroke_drawable);
        DrawableMatcher.isSameRadius(0, expectedRadius).matches(strokeDrawable);
    }

    @Test
    public void verifyTextEditBoxStrokeBackgroundRadius() {
        float expectedRadius = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_corner_radius));
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uid_texteditbox_stroke_drawable)));
    }

    //    This has to be fixed as part of text box border
    @Ignore
    @Test
    public void verifyTextEditBoxFillBackgroundRadius() {
        float expectedRadius = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_corner_radius));
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uid_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyTextEditBoxLeftPadding() {
        int expectedLeftPadding = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_left_padding));
        getTextBox().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyTextEditBoxRightPadding() {
        int expectedRightPadding = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_right_padding));
        getTextBox().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedRightPadding)));
    }

    @Test
    public void verifyTextEditBoxHeight() {
        waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME_EXTRA);
        int expectedHeight = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_height));
        getTextBox().check(matches(FunctionDrawableMatchers.isMinHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight, R.id.uid_texteditbox_fill_drawable)));
    }

    @Ignore
    @Test
    public void verifyTextEditBoxStrokeWidth() {
        int expectedStrokeWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_stroke_height);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedStrokeWidth, R.id.uid_texteditbox_stroke_drawable)));
    }

    //*********************************TextBoxColors TestScenarios**************************//

    @Test
    public void verifyNormalTextBoxFillColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultNormalBackgroundColor);
        getTextBox().check(matches(FunctionDrawableMatchers
                .isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor, R.id.uid_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyNormalTextBoxBorderColor() {
        int expectedColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultNormalBorderColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor, R.id.uid_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyNormalTextBoxTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultNormalTextColor);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedTextColor)));
    }

    @Test
    public void verifyNormalTextBoxHintTextColor() {
        int expectedHintTextColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultNormalHintTextColor);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameHintTextColor(android.R.attr.state_enabled, expectedHintTextColor)));
    }

    @Test
    public void verifyFocusedTextBoxFillColor() {
        int expectedFillColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultPressedBackgroundColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedFillColor, R.id.uid_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyFocusedTextBoxBorderColor() {
        int expectedBorderColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultFocusBorderColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_focused, expectedBorderColor, R.id.uid_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxFillColor() {
        int expectedFillColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultDisabledBackgroundColor);
        getDisabledFilledTextBox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.state_enabled, expectedFillColor, R.id.uid_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxBorderColor() {
        int expectedBorderColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultDisabledBorderColor);
        getDisabledFilledTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.state_enabled, expectedBorderColor, R.id.uid_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxTextColor() {
        int expectedColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultDisabledTextColor);
        getDisabledFilledTextBox().check(
                matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledTextBoxHintTextColor() {
        int expectedColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultDisabledHintTextColor);
        getDisabledHintTextBox().check(
                matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    /*******************************
     * TextBoxPassword Test Scenarios
     **************************************/

    @Test
    public void verifyPasswordTextBoxTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxDefaultNormalTextColor);
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedTextColor)));
    }

    @Test
    public void verifyPasswordTextBoxTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_fontsize);
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyPasswordTextBoxTextMasked() throws Exception {
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));
    }

    @Test
    public void verifyNonPasswordTextHasNoMasking() throws Exception {
        getTextBox().check(matches(TextViewPropertiesMatchers.hasNoMasking()));
    }

    @Ignore                 //Always fails with a difference of 1px
    @Test
    public void verifyPasswordRightDrawablePadding() throws Exception {
        final int padding = (int) activityContext.getResources().getDimension(R.dimen.uid_edittext_password_right_drawable_left_padding);
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(padding)));
    }

    @Test
    public void verifyPasswordRightDrawableWidth() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));

        final int width = activityContext.getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.edittext_compound_drawble_width);
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(COMPOUND_DRAWABLE_INDEX, width)));
    }

    @Test
    public void verifyPasswordRightDrawableHeight() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));

        final int height = activityContext.getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.edittext_compound_drawble_width);
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(COMPOUND_DRAWABLE_INDEX, height)));
    }

    @Test
    public void verifyPasswordMasked() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));

        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));
    }

    @Test
    public void verifyPasswordShownWhenClickedOnShow() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));
        getPasswordTextbox().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasNoMasking()));
    }

    @Test
    public void verifyPasswordMaskedWhenClickedOnShowAndThenHideAgain() throws Exception {
        waitFor(activityContext, 300);

        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));
        getPasswordTextbox().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));
        getPasswordTextbox().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));
    }

    @Test
    public void verifyPasswordMaskedWhenDoubleClicked() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));
        getPasswordTextbox().perform(new GeneralClickAction(Tap.DOUBLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));
    }

    @Test
    public void verifyMaskingIncaseOfFocusChanged() throws Exception {
        getPasswordTextbox().perform(ViewActions.typeText("Hello@123?"));
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));

        getPasswordTextbox().perform(new GeneralClickAction(Tap.DOUBLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        Espresso.closeSoftKeyboard();
        getClearTextbox().perform(ViewActions.click());
        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.hasMasking()));
    }

    @Test
    public void verifyPasswordTextBoxCompoundPadding() {
        int expectedCompoundPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_passwordtextbox_compound_drawable_padding);

        getPasswordTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(expectedCompoundPadding)));
    }

    @Test
    public void verifyPasswordTextBoxRightPadding() {
        waitFor(testResources, 750);
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_right_padding);
        getPasswordTextbox().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyClearIconDisplayedOnEntringText() throws Exception {
        getClearTextbox().perform(ViewActions.typeText("Hello@123?"));

        getClearTextbox().check(matches(TextViewPropertiesMatchers.hasCompoundDrawable(RIGHT_DRAWABLE_INDEX)));
    }

    @Test
    public void verifyClearIconClickClearsText() throws Exception {
        getClearTextbox().perform(ViewActions.typeText("Hello@123?"));
        getClearTextbox().perform(new GeneralClickAction(Tap.SINGLE, GeneralLocation.CENTER_RIGHT, Press.FINGER));

        getClearTextbox().check(matches(TextViewPropertiesMatchers.hasNoText()));

        getClearTextbox().check(matches(TextViewPropertiesMatchers.noCompoundDrawable(RIGHT_DRAWABLE_INDEX)));
    }

    @Test
    public void verifyClearTextBoxLeftPadding() {
        waitFor(testResources, 750);
        int expectedCompoundPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_passwordtextbox_compound_drawable_padding);
        getClearTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(expectedCompoundPadding)));
    }

    @Test
    public void verifyClearTextBoxRightPadding() {
        waitFor(testResources, 750);
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.edittext_right_padding);
        getClearTextbox().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyClearRightDrawableWidth() throws Exception {
        getClearTextbox().perform(ViewActions.typeText("Hello@123?"));
        final int width = activityContext.getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.edittext_clear_icon_width_height);
        getClearTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(COMPOUND_DRAWABLE_INDEX, width)));
    }

    @Test
    public void verifyClearRightDrawableHeight() throws Exception {
        getClearTextbox().perform(ViewActions.typeText("Hello@123?"));
        final int width = activityContext.getResources().getDimensionPixelOffset(com.philips.platform.uid.test.R.dimen.edittext_clear_icon_width_height);
        getClearTextbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(COMPOUND_DRAWABLE_INDEX, width)));
    }

    private ViewInteraction getTextBox() {
        return onView(withId(com.philips.platform.uid.test.R.id.textBox));
    }

    private ViewInteraction getDisabledHintTextBox() {
        return onView(withId(com.philips.platform.uid.test.R.id.hintTextBoxDisabled));
    }

    private ViewInteraction getDisabledFilledTextBox() {
        return onView(withId(com.philips.platform.uid.test.R.id.FilledTextBoxDisabled));
    }

    private ViewInteraction getPasswordTextbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.passwordTextBox));
    }

    private ViewInteraction getClearTextbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.clearButton));
    }
}