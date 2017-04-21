/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class PrimaryButtonWithTextOnlyTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;
    private Context context;
    private BaseTestActivity activity;

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
        testResources = activity.getResources();
        context = activity;
    }

    /*****************************************
     * Layout Scenarios
     *********************************************/

    @Test
    public void verifyButtonHeight() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME_EXTRA);
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_height);

        getPrimaryButton()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    @Test
    public void verifyButtonLeftPadding() {
        int expectedLeftPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_left_padding);
        getPrimaryButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyButtonRightPadding() {
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_right_padding);
        getPrimaryButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyButtonCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_cornerradius));
        getPrimaryButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    @Test
    public void verifyButtonFontSize() {
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_font_size);
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifyPrimaryTextOnlyButtonControlColorULTone() {
        final int exoectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonPrimaryNormalBackgroundColor);
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, exoectedColor)));
    }

    @Test
    public void verifyPrimaryTextOnlyDisabledButtonFillColor() {
        final int exoectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonPrimaryDisabledBackgroundColor);
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, -android.R.attr.state_enabled, exoectedColor)));
    }

    @Test
    public void verifyPrimaryTextOnlyPressedButtonControlColorULTone() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonPrimaryPressedBackgroundColor);

        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 0, expectedColor)));
    }

    @Test
    public void verifyPrimaryTextOnlyButtonFontColor() {
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, Color.WHITE)));
    }

    @Test
    public void verifyPrimaryTextOnlyPressedButtonFontColor() {
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(1, Color.WHITE)));
    }

    @Test
    public void verifyPrimaryTextOnlyDisabledButtonControlColorULTone() {
        final int disabledColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonPrimaryDisabledBackgroundColor);
        disableAllViews();
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, -android.R.attr.enabled, disabledColor)));
    }

    @Test
    public void verifyPrimaryTextOnlyDisabledButtonFontColor() {
        disableAllViews();
        final int disabledTextColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonPrimaryDisabledTextColor);
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.enabled, disabledTextColor)));
    }

    private ViewInteraction getPrimaryButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.primary_button));
    }

    private void disableAllViews() {
        onView(withId(com.philips.platform.uid.test.R.id.disable_switch)).perform(ViewActions.click());
    }
}

