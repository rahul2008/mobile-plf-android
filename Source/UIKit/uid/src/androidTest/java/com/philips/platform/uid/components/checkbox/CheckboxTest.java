/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.checkbox;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.view.widget.CheckBox;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;
import static com.philips.platform.uid.test.R.color.Gray75;
import static com.philips.platform.uid.test.R.color.GroupBlue45;
import static com.philips.platform.uid.test.R.color.GroupBlue75;
import static com.philips.platform.uid.test.R.color.White;
import static com.philips.platform.uid.utils.UIDUtils.modulateColorAlpha;
import static org.hamcrest.CoreMatchers.allOf;

public class CheckboxTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    Resources resources;
    private static final int ULTRA_LIGHT = 0;
    private static final int VERY_LIGHT = 1;
    private static final int BRIGHT = 2;
    private static final int LIGHT = 3;
    private static final int VERY_DARK = 4;

    /********************************
     * Layout scenarios
     ******************************/
    @Test
    public void verifyCheckboxHeight() {
        setUpTheme(ULTRA_LIGHT);
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_height);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    @Test
    public void verifyCheckboxCornerRadius() {
        setUpTheme(ULTRA_LIGHT);

        float radius = (float) Math.floor(resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_cornerradius));
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    @Test
    public void verifyCheckboxTargetArea() {

    }

    @Test
    public void verifyCheckboxLeftMargin() {
        setUpTheme(ULTRA_LIGHT);

        int expectedLeftMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_leftrightmargin);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyCheckboxRightMargin() {
        setUpTheme(ULTRA_LIGHT);

        int expectedRightMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_leftrightmargin);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedRightMargin)));
    }

    @Test
    public void verifyCheckboxLabelFontSize() {
        setUpTheme(ULTRA_LIGHT);

        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyCheckboxTickMarkHeight() {
        setUpTheme(ULTRA_LIGHT);

        int expectedIconHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_tickmarkheightwidth);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyCheckboxTickMarkWidth() {
        setUpTheme(ULTRA_LIGHT);

        int expectedIconWidth = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_tickmarkheightwidth);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconWidth)));
    }

    /****************************Theming scenarios***********************/
    /***************************
     * CheckBox On Test scenarios
     **************/

    @Test
    public void verifyCheckBoxOnFillColor() {
        setUpTheme(ULTRA_LIGHT);
        getCheckbox().perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(activity, GroupBlue45);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedOnFillColor)));
    }

    @Test
    public void verifyCheckBoxOnFillColorBrightTonalRange() {
        setUpTheme(BRIGHT);
        getCheckbox().perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(activity, GroupBlue75);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedOnFillColor)));
    }

    @Test
    public void verifyCheckBoxOnFillColorVeryDarkTonalRange() {
        setUpTheme(VERY_DARK);
        getCheckbox().perform(click());
        final int expectedOnFillColor = ContextCompat.getColor(activity, GroupBlue45);
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
        setUpTheme(ULTRA_LIGHT);
        final int expectedColor = ContextCompat.getColor(activity, Gray75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCheckBoxTextColorVeryLightTheme() {
        setUpTheme(VERY_LIGHT);
        final int expectedColor = ContextCompat.getColor(activity, GroupBlue75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCheckBoxTextColorBrightTheme() {
        setUpTheme(BRIGHT);
        final int expectedColor = ContextCompat.getColor(activity, White);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    /***************************
     * CheckBox Off Test scenarios
     **************/
    @Test
    public void verifyCheckBoxOffFillColor() {
        setUpTheme(ULTRA_LIGHT);
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue45), 0.60f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxOffFillColorBright() {
        setUpTheme(BRIGHT);
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue75), 0.60f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.enabled, expectedCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxOffFillColorVeryDark() {
        setUpTheme(VERY_DARK);
        final int expectedCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue45), 0.60f);
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
        setUpTheme(ULTRA_LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue45), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxDisabledFillColorBright() {
        setUpTheme(BRIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue75), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyCheckBoxDisabledFillColorVeryDark() {
        setUpTheme(VERY_DARK);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxOffFillColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue45), 0.35f);
        getCheckbox().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, -android.R.attr.enabled, expectedDisabledCheckBoxOffFillColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColor() {
        setUpTheme(ULTRA_LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedColor = ContextCompat.getColor(activity, Gray75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorVeryLightTheme() {
        setUpTheme(VERY_LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedColor = ContextCompat.getColor(activity, GroupBlue75);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorBrightTheme() {
        setUpTheme(BRIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedColor = ContextCompat.getColor(activity, White);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    //// TODO: 1/16/2017
    @Test
    public void verifyCheckBoxDisabledIconColor() {

    }

    public void setUpTheme(final int theme) {
        final Intent intent = getIntent(theme);
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_checkbox);
        resources = activity.getResources();
    }

    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    static class SetCheckboxCheckedViewAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return allOf();
        }

        @Override
        public String getDescription() {
            return "set CheckboxTest enabled";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setEnabled(true);
            }
        }
    }

    private ViewInteraction getCheckbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_checkbox));
    }
}