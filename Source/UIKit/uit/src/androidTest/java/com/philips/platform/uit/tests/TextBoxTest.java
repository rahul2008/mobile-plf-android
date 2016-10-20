/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.tests;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.R;
import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.DrawableMatcher;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;
import com.philips.platform.uit.view.widget.TextEditBox;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TextBoxTest {

    private Resources testResources;
    private Context activityContext;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_textbox);
        testResources = getInstrumentation().getContext().getResources();
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
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.texteditbox_fontsize);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Ignore
    @Test
    public void verifySameTextEditBoxRadiusOnDynamicCreation() {
        TextEditBox textEditBox = new TextEditBox(mActivityTestRule.getActivity());
        float expectedRadius = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.texteditbox_corner_radius);
        Drawable strokeDrawable = ((LayerDrawable) textEditBox.getBackground()).findDrawableByLayerId(R.id.uit_texteditbox_stroke_drawable);
        DrawableMatcher.isSameRadius(0, expectedRadius).matches(strokeDrawable);
    }

    @Test
    public void verifyTextEditBoxStrokeBackgroundRadius() {
        float expectedRadius = (int) Math.ceil(testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_corner_radius));
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uit_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyTextEditBoxFillBackgroundRadius() {
        float expectedRadius = (int) Math.ceil(testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_fill_corner_radius));
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uit_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyTextEditBoxLeftPadding() {
        int expectedLeftPadding = (int) Math.ceil(testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_left_padding));
        getTextBox().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyTextEditBoxRightPadding() {
        int expectedRightPadding = (int) Math.ceil(testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_right_padding));
        getTextBox().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedRightPadding)));
    }

    @Test
    public void verifyTextEditBoxHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedHeight = (int) Math.ceil(testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_height));
        getTextBox().check(matches(FunctionDrawableMatchers.isMinHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight, R.id.uit_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyTextEditBoxStrokeWidth() {
        int expectedStrokeWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_stroke_height);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedStrokeWidth, R.id.uit_texteditbox_stroke_drawable)));
    }

    //*********************************TextBoxColors TestScenarios**************************//

    @Test
    public void verifyNormalTextBoxFillColor() {
        final int expectedColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextFillColor);
        getTextBox().check(matches(FunctionDrawableMatchers
                .isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor, R.id.uit_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyNormalTextBoxBorderColor() {
        int expectedColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextBorderColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor, R.id.uit_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyNormalTextBoxTextColor() {
        int expectedTextColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextTextColor);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled,expectedTextColor)));

    }

    @Test
    public void verifyNormalTextBoxHintTextColor() {
        int expectedHintTextColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextHintTextColor);
        getTextBox().check(matches(TextViewPropertiesMatchers.isSameHintTextColor(android.R.attr.state_enabled,expectedHintTextColor)));
    }

    @Test
    public void verifyFocusedTextBoxFillColor() {
        int expectedFillColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextFillPressedColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedFillColor, R.id.uit_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyFocusedTextBoxBorderColor() {
        int expectedBorderColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextBorderColor);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedBorderColor, R.id.uit_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxFillColor() {
        int expectedFillColor = UITTestUtils.getColorWithAlphaFromAttrs(activityContext,
                R.attr.uitInputTextFillDisabledColor, R.attr.uitInputTextFillDisabledAlpha);
        getDisabledFilledTextBox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.state_enabled, expectedFillColor, R.id.uit_texteditbox_fill_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxBorderColor() {
        int expectedBorderColor = UITTestUtils.getAttributeColor(activityContext, R.attr.uitInputTextBorderDisabledColor);
        getDisabledFilledTextBox().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.state_enabled, expectedBorderColor, R.id.uit_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyDisabledTextBoxTextColor() {
        int expectedColor = UITTestUtils.getColorWithAlphaFromAttrs(activityContext,
                R.attr.uitInputTextTextDisabledColor, R.attr.uitInputTextTextDisabledAlpha);
        getDisabledFilledTextBox().check(
                matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledTextBoxHintTextColor() {
        int expectedColor = UITTestUtils.getColorWithAlphaFromAttrs(activityContext,
                R.attr.uitInputTextHintTextDisabledColor, R.attr.uitInputTextHintTextDisabledAlpha);
        getDisabledHintTextBox().check(
                matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    private ViewInteraction getTextBox() {
        return onView(withId(com.philips.platform.uit.test.R.id.textBox));
    }

    private ViewInteraction getDisabledHintTextBox() {
        return onView(withId(com.philips.platform.uit.test.R.id.hintTextBoxDisabled));
    }

    private ViewInteraction getDisabledFilledTextBox() {
        return onView(withId(com.philips.platform.uit.test.R.id.FilledTextBoxDisabled));
    }
}