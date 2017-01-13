/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.checkbox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.utils.TestConstants;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.GroupBlue45;
import static com.philips.platform.uid.utils.UIDTestUtils.modulateColorAlpha;

public class checkbox {

    private Context activityContext;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_checkbox);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
        activityContext = activity;
    }

    /********************************
     * Layout scenarios
     ******************************/
    @Test
    public void verifyCheckboxHeight() {

    }

    @Test
    public void verifyCheckboxCornerRadius() {

    }

    @Test
    public void verifyCheckboxTargetArea() {

    }

    @Test
    public void verifyCheckboxRightMargin() {

    }

    @Test
    public void verifyCheckboxLabelFontsize() {

    }

    /**********************************
     * Theming scenarios
     *****************************/
    @Test
    public void verifyCheckBoxOnFillColor() {

    }

    @Test
    public void verifyCheckBoxOnFillColorBrightTonalRange() {

    }

    @Test
    public void verifyCheckBoxOnFillColorVeryDarkTonalRange() {

    }

    @Test
    public void verifyCheckBoxOnIconColor() {

    }

    public void verifyCheckBoxOnIconColorBrightTonalRange() {

    }

    public void verifyCheckBoxOffFillColor() {

    }

    public void verifyCheckBoxPressedBorderColor() {

    }

    public void verifyCheckBoxPressedBorderColorBright() {

    }

    public void verifyCheckBoxPressedBorderColorVeryDark() {

    }

    public void verifyCheckBoxDisabledFillColor() {

    }

    public void verifyCheckBoxDisabledIconColor() {

    }

    public void verifyCheckBoxDisabledTextColor() {

    }

    private ViewInteraction getCheckbox() {
        return onView(withId(com.philips.platform.uid.test.R.id.checkbox_1));
    }

}