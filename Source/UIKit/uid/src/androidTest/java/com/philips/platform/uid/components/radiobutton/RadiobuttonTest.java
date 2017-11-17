/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.radiobutton;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.RadioButton;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;

public class RadiobuttonTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(NavigationColor.BRIGHT.ordinal(), ContentColor.ULTRA_LIGHT.ordinal(), ColorRange.GREEN.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_radiobutton);
        resources = activity.getResources();
    }

    @Test
    public void verifyRadiobuttonHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.radiobutton_height);
        getRadiobutton1().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyRadiobuttonWidth() {
        int expectedWidth = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.radiobutton_width);
        getRadiobutton1().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedWidth)));
    }

    @Test
    public void verifyRadiobuttonLabelFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyRadioButtonTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularNormalTextColor);
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    /***************************
     * Disabled RadioButton Test Scenarios
     *******************************/
    @Test
    public void verifyRadioButtonDisabledTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularDisabledTextColor);
        getRadiobutton1().perform(new RadiobuttonTest.SetRadiobuttonDisabledViewAction());
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyRadioButtonTextTypeface() {
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyRadioButtonTextVerticallyAligned() {
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isCenterVerticallyAligned()));
    }

    @Test
    public void verifyRadioButtonSupportsMultiline() {
        getRadiobutton2().check(matches(TextViewPropertiesMatchers.isMultiline()));
    }

    @Test
    public void verifyRadioButtonandLabelStartPadding() {
        int expectedCompoundPadding = getInstrumentation().getContext().getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.radiobutton_margin_left_right);
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameStartPadding(expectedCompoundPadding)));
    }

    @Test
    public void verifyLineSpacing() {
        int lineSpacing = getInstrumentation().getContext().getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.radiobutton_line_spacing);
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(lineSpacing)));
    }

    @Test
    public void verifyLineMultiplier() {
        int lineMultiplier = getInstrumentation().getContext().getResources().getInteger(com.philips.platform.uid.test.R.integer.radiobutton_line_spacing_multiplier);
        getRadiobutton1().check(matches(TextViewPropertiesMatchers.isSameLineSpacingMultiplier(lineMultiplier)));
    }

    @Test
    public void verifyRadioButton1SelectedByDefault() {
        getRadiobutton1().check(matches(isChecked()));
    }

    @Test
    public void verifyOnlyRadioButton1Selected() {
        getRadiobutton1().perform(ViewActions.click());
        getRadiobutton1().check(matches(isChecked()));
        getRadiobutton2().check(matches(isNotChecked()));
    }

    @Test
    public void verifyRadioButton1RemainsSelectedOnTapingAgain() {
        getRadiobutton1().perform(ViewActions.click());
        getRadiobutton1().perform(ViewActions.click());
        getRadiobutton1().check(matches(isChecked()));
        getRadiobutton2().check(matches(isNotChecked()));
    }

    @Test
    public void verifyRippleColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidRadioButtonDefaultPressedBorderColor);
        getRadiobutton1().
                check(matches(FunctionDrawableMatchers.
                        isSameRippleColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor)));
    }

    private ViewInteraction getRadiobutton1() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_radiobutton1));
    }

    private ViewInteraction getRadiobutton2() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_radiobutton2));
    }

    static class SetRadiobuttonDisabledViewAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return allOf();
        }

        @Override
        public String getDescription() {
            return "set RadiobuttonTest enabled";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                radioButton.setEnabled(false);
            }
        }
    }
}