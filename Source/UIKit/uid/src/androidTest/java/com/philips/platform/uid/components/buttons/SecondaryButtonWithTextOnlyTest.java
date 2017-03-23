/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.GroupBlue15;
import static com.philips.platform.uid.test.R.color.GroupBlue75;
import static com.philips.platform.uid.utils.UIDTestUtils.modulateColorAlpha;

public class SecondaryButtonWithTextOnlyTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Context instrumentationContext;
    private BaseTestActivity activity;

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
        instrumentationContext = getInstrumentation().getContext();
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifySecTextOnlyButtonControlColorULTone() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue15);
        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    // TODO: 9/20/2016 Fix this failing test case.
    @Test
    public void verifySecTextOnlyPressedButtonControlColorULTone() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidControlSecondaryPressed);
        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 0, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyButtonFontColor() {
        final int expectedFontColor = ContextCompat.getColor(getInstrumentation().getContext(), GroupBlue75);
        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedFontColor)));
    }

    @Ignore
    @Test
    public void verifySecTextOnlyPressedButtonFontColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidControlSecondary);

        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(0, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyDisabledButtonControlColorULTone() {
        final int disabledColor = modulateColorAlpha(Color.parseColor("#BFE2EB"), 0.25f);
        disableAllViews();
        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, -android.R.attr.enabled, disabledColor)));
    }

    @Test
    public void verifySecTextOnlyDisabledButtonFontColor() {
        disableAllViews();
        final int expectedFontColor = ContextCompat.getColor(getInstrumentation().getContext(), GroupBlue75);
        final int disabledTextColor = UIDTestUtils.modulateColorAlpha(expectedFontColor, 0.25f);
        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.enabled, disabledTextColor)));
    }

    private ViewInteraction getSecondaryButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.seconday_button));
    }

    private void disableAllViews() {
        onView(withId(com.philips.platform.uid.test.R.id.disable_switch)).perform(ViewActions.click());
    }
}

