/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.label;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LabelTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_labels);
        resources = activity.getResources();
    }

    //*********************************Label layout TestScenarios**************************//
    @Test
    public void verifyLabelTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getTextLabel().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyLabelTextFontSizeWithOveridenTextSize() {
        float expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize_text_size_overidden);
        getTextLabelWithTextsizeOverriden().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyLabelNumericFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.label_fontsize);
        getNumericLabel().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    //*********************************Label theming TestScenarios**************************//

    @Test
    public void verifyLabelTextColor() {
        final int attributeColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelValueNormalTextColor);
        getTextLabel().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, attributeColor)));
    }

    @Test
    public void verifyLabelNumericColor() {
        final int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelRegularNormalTextColor);

        getNumericLabel().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedTextColor)));
    }

    private ViewInteraction getTextLabel() {
        return onView(withId(com.philips.platform.uid.test.R.id.label_text));
    }

    private ViewInteraction getTextLabelWithTextsizeOverriden() {
        return onView(withId(com.philips.platform.uid.test.R.id.label_text_textsize_overriden));
    }

    private ViewInteraction getNumericLabel() {
        return onView(withId(com.philips.platform.uid.test.R.id.label_numeric));
    }
}