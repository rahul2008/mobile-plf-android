/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.buttons.button;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.utils.UITTestUtils.waitFor;

public class PrimaryButtonWithIconOnlyTest {

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

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyButtonHeight() {
        waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_height);
        getPrimaryButton()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    private ViewInteraction getPrimaryButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.demo_image_button));
    }

    @Test
    public void verifyButtonWithIconWidth() {
        waitFor(testResources, 750);
        int expectedWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_width);
        getPrimaryButton()
                .check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedWidth)));
    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconHeight() {

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconWidth() {

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconContainer() {

    }

    @Test
    public void verifyButtonWithIconLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_left_padding);
        getPrimaryButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    // TODO: 9/27/2016
    @Test
    public void verifyButtonWithIconRightPadding() {
    }

    @Test
    public void verifyButtonCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_cornerradius));
        getPrimaryButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    /************************************************
     * Theming
     ************************************************/

    // TODO: 9/27/2016
    @Test
    public void verifyIconButtonDefaultIconColor() {

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconButtonPressedIconColor() {

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconButtonDisabledIconColor() {

    }
}
