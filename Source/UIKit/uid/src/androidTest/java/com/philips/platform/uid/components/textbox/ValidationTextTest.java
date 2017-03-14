/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.components.textbox;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.InputValidationMatcher;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ValidationTextTest {

    private static final int RIGHT_DRAWABLE_INDEX = 2;
    private static final int COMPOUND_DRAWABLE_INDEX = RIGHT_DRAWABLE_INDEX;
    private Resources testResources;
    private Context activityContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_validation_text);
        testResources = getInstrumentation().getContext().getResources();
        activityContext = activity;
    }

    @Test
    public void verifyTextBoxTextFormatSupport() {
        int color = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTextBoxValidationErrorColor);
        InputValidationMatcher.isSameErrorColor(color).matches(getValidationLayout());
        getValidationLayout().check(matches(InputValidationMatcher.isSameErrorColor(color)));
    }


    private ViewInteraction getValidationLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.validation_layout));
    }
}