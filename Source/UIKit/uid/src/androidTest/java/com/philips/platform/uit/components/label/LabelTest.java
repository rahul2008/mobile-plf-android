/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.components.label;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.Gray75;
import static com.philips.platform.uit.utils.UIDTestUtils.modulateColorAlpha;

public class LabelTest {

    private Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_labels);
        testResources = getInstrumentation().getContext().getResources();
    }

    //*********************************Label layout TestScenarios**************************//
    @Test
    public void verifyLabelTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.label_fontsize);
        getTextLabel().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyLabelNumericFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.label_fontsize);
        getNumericLabel().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    //*********************************Label theming TestScenarios**************************//

    @Test
    public void verifyLabelTextColor() {
        final int expectedTextColor = modulateColorAlpha(Color.parseColor("#383838"), 0.60f);
        getTextLabel().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedTextColor)));
    }

    @Test
    public void verifyLabelNumericColor() {
        final int expectedTextColor = ContextCompat.getColor(getInstrumentation().getContext(), Gray75);
        getNumericLabel().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedTextColor)));
    }

    private ViewInteraction getTextLabel() {
        return onView(withId(com.philips.platform.uit.test.R.id.label_text));
    }

    private ViewInteraction getNumericLabel() {
        return onView(withId(com.philips.platform.uit.test.R.id.label_numeric));
    }
}