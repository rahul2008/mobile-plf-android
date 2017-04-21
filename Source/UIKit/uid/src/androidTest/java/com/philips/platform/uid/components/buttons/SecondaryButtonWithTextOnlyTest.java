/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SecondaryButtonWithTextOnlyTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Context context;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_secondary_buttons);
        context = activity;
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifyTextOnlySecondaryButtonEnabledBackgroundColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryNormalBackgroundColor);

        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyPressedButtonBackgroundColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryPressedBackgroundColor);
        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 0, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyButtonDisabledBackgroundColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryDisabledBackgroundColor);
        getSecondaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 1, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyButtonTextColor() {
        final int expectedFontColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryNormalTextColor);
        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedFontColor)));
    }

    @Test
    public void verifySecTextOnlyDisabledButtonTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryDisabledTextColor);

        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(0, expectedColor)));
    }

    @Test
    public void verifySecTextOnlyNormalButtonTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonSecondaryNormalTextColor);

        getSecondaryButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(1, expectedColor)));
    }

    private ViewInteraction getSecondaryButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.seconday_button));
    }
}

