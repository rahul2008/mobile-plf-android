package com.philips.platform.catalogapp.activity;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.fragments.ButtonFragment;
import com.philips.platform.catalogapp.matcher.FunctionDrawableMatchers;
import com.philips.platform.catalogapp.matcher.ViewPropertiesMatchers;
import com.philips.platform.catalogapp.utils.TestConstants;
import com.philips.platform.catalogapp.utils.UITTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PrimaryButtonWithIconOnlyTest {

    private Resources testResources;
    private Context instrumentationContext;
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchFragment(new ButtonFragment());
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyButtonHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_height);
        getPrimaryButton()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    private ViewInteraction getPrimaryButton() {
        return onView(withId(R.id.demo_image_button));
    }

    @Test
    public void verifyButtonWithIconWidth(){
        UITTestUtils.waitFor(testResources, 750);
        int expectedWidth = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_width);
        getPrimaryButton()
                .check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedWidth)));

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconHeight(){

    }

    // TODO: 9/27/2016
    @Test
    public void verifyIconWidth(){

    }


    @Test
    public void verifyButtonWithIconLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_left_padding);
        getPrimaryButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
            }

    // TODO: 9/27/2016
    @Test
    public void verifyButtonWithIconRightPadding() {
    }


    @Test
    public void verifyButtonCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_cornerradius));
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
