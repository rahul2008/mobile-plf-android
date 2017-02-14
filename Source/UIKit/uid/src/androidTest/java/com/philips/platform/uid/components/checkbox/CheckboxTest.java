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
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.view.widget.CheckBox;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;
import static com.philips.platform.uid.test.R.color.Gray75;
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
    private static final int LIGHT = 2;
    private static final int BRIGHT = 3;
    private static final int VERY_DARK = 4;

    @Test
    public void verifyCheckboxHeight() {
        setUpTheme(ULTRA_LIGHT);
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_height);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }
    @Test
    public void verifyCheckboxLabelFontSize() {
        setUpTheme(ULTRA_LIGHT);
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
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
     * Disabled CheckBox Test Scenarios
     *******************************/
    @Test
    public void verifyDisabledCheckBoxTextColorULTheme() {
        setUpTheme(ULTRA_LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = modulateColorAlpha(ContextCompat.getColor(activity, Gray75), 0.35f);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorVeryLightTheme() {
        setUpTheme(VERY_LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = modulateColorAlpha(ContextCompat.getColor(activity, GroupBlue75), 0.45f);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorLightTheme() {
        setUpTheme(LIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = modulateColorAlpha(ContextCompat.getColor(activity, White), 0.45f);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorBrightTheme() {
        setUpTheme(BRIGHT);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = modulateColorAlpha(ContextCompat.getColor(activity, White), 0.50f);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyDisabledCheckBoxTextColorVeryDarkTheme() {
        setUpTheme(VERY_DARK);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = modulateColorAlpha(ContextCompat.getColor(activity, White), 0.40f);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyLineSpacing() {
        setUpTheme(VERY_DARK);
        int lineSpacing = getInstrumentation().getContext().getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_line_spacing);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(lineSpacing)));
    }

    @Test
    public void verifyLineMultiplier() {
        setUpTheme(VERY_DARK);
        int lineMultiplier = getInstrumentation().getContext().getResources().getInteger(com.philips.platform.uid.test.R.integer.checkbox_line_spacing_multiplier);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameLineSpacingMultiplier(lineMultiplier)));
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
                checkBox.setEnabled(false);
            }
        }
    }

    private ViewInteraction getCheckbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_checkbox));
    }
}