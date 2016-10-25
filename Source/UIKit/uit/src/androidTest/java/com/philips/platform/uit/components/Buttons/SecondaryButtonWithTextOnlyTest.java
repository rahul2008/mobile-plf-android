/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue15;
import static com.philips.platform.uit.test.R.color.GroupBlue35;
import static com.philips.platform.uit.test.R.color.GroupBlue45;
import static com.philips.platform.uit.utils.UITTestUtils.modulateColorAlpha;

public class SecondaryButtonWithTextOnlyTest {

    private Resources testResources;
    private Context instrumentationContext;
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifySecTextOnlyButtonControlColorULTone() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue15);
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    // TODO: 9/20/2016 Fix this failing test case.
    @Ignore
    @Test
    public void verifySecTextOnlyPressedButtonControlColorULTone() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue35);
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_pressed, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyButtonFontColor() {
        final int expectedFontColor = ContextCompat.getColor(getInstrumentation().getContext(), GroupBlue45);
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedFontColor)));
    }

    @Ignore
    @Test
    public void verifySecTextOnlyPressedButtonFontColor() {
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_pressed, GroupBlue45)));
    }

    @Test
    public void verifySecTextOnlyDisabledButtonControlColorULTone() {
        final int disabledColor = modulateColorAlpha(Color.parseColor("#BFE2EB"), 0.25f);
        disableAllViews();
        getPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, -android.R.attr.enabled, disabledColor)));
    }

    @Test
    public void verifySecTextOnlyDisabledButtonFontColor() {
        disableAllViews();
        final int expectedFontColor = ContextCompat.getColor(getInstrumentation().getContext(), GroupBlue45);
        final int disabledTextColor = UITTestUtils.modulateColorAlpha(expectedFontColor, 0.25f);
        getPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.enabled, disabledTextColor)));
    }

    private ViewInteraction getPrimaryButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.seconday_button));
    }

    private void disableAllViews() {
        onView(withId(com.philips.platform.uit.test.R.id.disable_switch)).perform(ViewActions.click());
    }
}

