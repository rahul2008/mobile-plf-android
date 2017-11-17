/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.checkbox;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.CheckBox;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

public class CheckboxTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(NavigationColor.BRIGHT.ordinal(), ContentColor.ULTRA_LIGHT.ordinal(),ColorRange.GRAY.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_checkbox);
        resources = activity.getResources();
    }

    @Test
    public void verifyCheckboxHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_height);
        getCheckbox().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyCheckboxLabelFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyCheckBoxTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularNormalTextColor);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCheckBoxDisabledTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularDisabledTextColor);
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    /***************************
     * Disabled CheckBox Test Scenarios
     *******************************/
    @Test
    public void verifyDisabledCheckBoxTextColorULTheme() {
        getCheckbox().perform(new SetCheckboxCheckedViewAction());
        final int expectedDisabledCheckBoxTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularDisabledTextColor);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedDisabledCheckBoxTextColor)));
    }

    @Test
    public void verifyLineSpacing() {
        int lineSpacing = getInstrumentation().getContext().getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.checkbox_line_spacing);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(lineSpacing)));
    }

    @Test
    public void verifyLineMultiplier() {
        int lineMultiplier = getInstrumentation().getContext().getResources().getInteger(com.philips.platform.uid.test.R.integer.checkbox_line_spacing_multiplier);
        getCheckbox().check(matches(TextViewPropertiesMatchers.isSameLineSpacingMultiplier(lineMultiplier)));
    }

    private ViewInteraction getCheckbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_checkbox));
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
}