/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.checkbox;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.buttons.ButtonWithProgressIndicatorsTest;
import com.philips.platform.uid.components.buttons.ButtonsTestFragment;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.ProgressIndicatorButton;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;
import static com.philips.platform.uid.test.R.color.Gray75;
import static com.philips.platform.uid.test.R.color.GroupBlue35;
import static com.philips.platform.uid.test.R.color.GroupBlue45;
import static com.philips.platform.uid.test.R.color.GroupBlue75;
import static com.philips.platform.uid.test.R.color.White;
import static com.philips.platform.uid.utils.UIDTestUtils.modulateColorAlpha;
import static org.hamcrest.CoreMatchers.allOf;

public class checkbox {

    private Context context;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private BaseTestActivity activity;
    private Resources testResources;

    @Before
    public void setUpDefaultTheme() {
        final Intent intent = getIntent(0);
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);

        activity.switchFragment(new ButtonsTestFragment());
        testResources = getInstrumentation().getContext().getResources();
        context = getInstrumentation().getContext();
    }

    /********************************
     * Layout scenarios
     ******************************/
    @Test
    public void verifyCheckboxHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_height);
        getCheckbox()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    @Test
    public void verifyCheckboxCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_cornerradius));
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));

    }

    @Test
    public void verifyCheckboxTargetArea() {

    }

    @Test
    public void verifyCheckboxLeftMargin() {
        int expectedLeftMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_leftrightmargin);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyCheckboxRightMargin() {
        int expectedRightMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_leftrightmargin);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedRightMargin)));
    }

    @Test
    public void verifyCheckboxLabelFontSize() {
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyCheckboxTickMarkHeight() {
        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_tickmarkheightwidth);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyCheckboxTickMarkWidth() {
        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_tickmarkheightwidth);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconWidth)));
    }

    /****************************Theming scenarios***********************/
    /***************************CheckBox On Test scenarios**************/

    @Test
    public void verifyCheckBoxOnFillColor() {
        setUpDefaultTheme();
        onView(withId(com.philips.platform.uid.test.R.id.checkbox_1)).perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedOnFillColor)));
    }

    @Test
    public void verifyCheckBoxOnFillColorBrightTonalRange() {
        setupBrightTheme();
        onView(withId(com.philips.platform.uid.test.R.id.checkbox_1)).perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(instrumentationContext, GroupBlue75);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedOnFillColor)));
    }

    @Test
    public void verifyCheckBoxOnFillColorVeryDarkTonalRange() {
        setupVeryDarkTheme();
        onView(withId(com.philips.platform.uid.test.R.id.checkbox_1)).perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedOnFillColor)));
    }

    @Test
    public void verifyCheckBoxOnIconColor() {

    }

    @Test
    public void verifyCheckBoxOnIconColorBrightTonalRange() {

    }

    @Test
    public void verifyCheckBoxTextColor() {
        setUpDefaultTheme();
        final int expectedColor = ContextCompat.getColor(instrumentationContext, Gray75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCheckBoxTextColorVeryLightTheme() {
        setupVeryLightTheme();
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCheckBoxTextColorBrightTheme() {
        setupBrightTheme();
        final int expectedColor = ContextCompat.getColor(instrumentationContext, White);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    /***************************CheckBox Off Test scenarios**************/
    @Test
    public void verifyCheckBoxOffFillColor() {
        setUpDefaultTheme();
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.60f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxOffFillColorBright() {
        setupBrightTheme();
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue75), 0.60f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxOffFillColorVeryDark() {
        setupVeryDarkTheme();
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.60f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedCheckBoxOffFillColor)));
    }

    /***************************
     * Pressed state of CheckBox Test Scenarios
     *******************************/
    //// TODO: 1/16/2017
    @Test
    public void verifyCheckBoxPressedBorderColor() {


    }

    //// TODO: 1/16/2017
    @Test
    public void verifyCheckBoxPressedBorderColorBright() {

    }

    //// TODO: 1/16/2017
    @Test
    public void verifyCheckBoxPressedBorderColorVeryDark() {

    }

    /***************************
     * Disabled CheckBox Test Scenarios
     *******************************/

    @Test
    public void verifyCheckBoxDisabledFillColor() {
        setUpDefaultTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxDisabledFillColorBright() {
        setupBrightTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue75), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxDisabledFillColorVeryDark() {
        setupVeryDarkTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColor() {
        setUpDefaultTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedColor = ContextCompat.getColor(instrumentationContext, Gray75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorVeryLightTheme() {
        setupVeryLightTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorBrightTheme() {
        setupBrightTheme();
        getCheckbox().perform(new SetViewDisabledViewAction());
        final int expectedColor = ContextCompat.getColor(instrumentationContext, White);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    //// TODO: 1/16/2017
    @Test
    public void verifyCheckBoxDisabledIconColor() {

    }

    private void setupVeryLightTheme() {
        final Intent intent = getIntent(1);

        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ButtonsTestFragment());
    }

    private void setupBrightTheme() {
        final Intent intent = getIntent(2);

        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ButtonsTestFragment());
    }

    private void setupVeryDarkTheme() {
        final Intent intent = getIntent(4);

        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ButtonsTestFragment());
    }

    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    static class SetViewDisabledViewAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return allOf();
        }

        @Override
        public String getDescription() {
            return "set checkbox enabled";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setEnabled(false);
            }
        }
    }

    private ViewInteraction getCheckbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.checkbox_1));
    }

}