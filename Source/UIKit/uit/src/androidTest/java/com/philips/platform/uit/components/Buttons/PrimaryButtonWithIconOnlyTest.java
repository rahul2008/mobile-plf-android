/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Buttons;

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

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyIconOnlyButtonHeight() {
        waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_height);
        getIconOnlyButton()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }


    @Test
    public void verifyIconOnlyButtonWidth(){
        waitFor(testResources, 750);
        int expectedWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_width);
        getIconOnlyButton()
                .check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedWidth)));

    }

    // TODO: 9/27/2016
//    @Test
//    public void verifyIconHeight(){
//        waitFor(testResources, 750);
//        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.icon_height);
//        getIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0,expectedIconHeight)));
//
//    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconWidth(){

    }

    @Test
    public void verifyButtonWithIconLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_left_padding);
        getIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
            }

    // TODO: 9/27/2016
    @Test
    public void verifyButtonWithIconRightPadding() {
    }


    @Test
    public void verifyButtonCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_cornerradius));
        getIconOnlyButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
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

    private ViewInteraction getIconOnlyButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.demo_image_button));
    }


}
