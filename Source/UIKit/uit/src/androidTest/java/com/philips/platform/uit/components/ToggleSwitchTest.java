/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uit.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;
import com.philips.platform.uit.view.widget.Switch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue45;
import static com.philips.platform.uit.utils.UITTestUtils.modulateColorAlpha;

@RunWith(AndroidJUnit4.class)
public class ToggleSwitchTest {

    private Context activityContext;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Switch toggleSwitch;
    private Resources testResources;

    @Before
    public void setup() {

        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_toggle_switch);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
        activityContext = activity;

//        trackDrawable = toggleSwitch.getTrackDrawable();
//        thumbDrawable = toggleSwitch.getThumbDrawable();
//        testResources = getInstrumentation().getContext().getResources();
    }

    //*********************************Toggle Switch Layout TestScenarios**************************//
    // TODO: 10/28/2016
    @Test
    public void verifyToggleSwitchTrackWidth() {

    }

    @Test
    public void verifyToggleSwitchTrackHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.toggleswitch_track_height);
        getToggleSwitch()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_TRACK_BACKGROUND, expectedHeight)));
    }


    @Test
    public void verifyToggleSwitchThumbHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.toggleswitch_thumb_height);
        getToggleSwitch()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_THUMB_BACKGROUND, expectedHeight)));
    }


    @Test
    public void verifyToggleSwitchCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimension(com.philips.platform.uit.test.R.dimen.toggleswitch_corner_radius));
        getToggleSwitch().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_TRACK_BACKGROUND, 0, radius)));
    }


    //*********************************Toggle Switch Theming TestScenarios**************************//

    @Test
    public void verifySwitchThumbFillColor() {
        getToggleSwitch().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SWITCH_THUMB_TINT_LIST, android.R.attr.state_enabled, Color.WHITE)));
    }


    @Test
    public void verifyToggleSwitchTrackOnColorTest() {
        final int expectedTrackEnabledColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getToggleSwitch().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SWITCH_TRACK_TINT_LIST, android.R.attr.state_enabled, expectedTrackEnabledColor)));
    }

    @Test
    public void verifyToggleSwitchTrackOffColorTest() {
        final int expectedTrackOffEnabledColor = modulateColorAlpha(Color.parseColor("#1474A4"), 0.30f);
        getToggleSwitch().perform(click());
        getToggleSwitch().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SWITCH_TRACK_TINT_LIST, android.R.attr.state_enabled, expectedTrackOffEnabledColor)));

    }

    // TODO: 10/28/2016  
    @Test
    public void verifyDisabledToggleSwitchTrackFillColorTest(){


    }

    // TODO: 10/28/2016  
    @Test
    public void verifyDisabledToggleSwitchThumbFillColorTest(){

    }

    // TODO: 10/28/2016  
    @Test
    public void verifyToggleSwitchOrientationChange() {
//        toggleSwitch.setChecked(true);
//        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Assert.assertTrue(toggleSwitch.isChecked());
//        toggleSwitch.setChecked(false);
//        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Assert.assertFalse(toggleSwitch.isChecked());
    }


    private ViewInteraction getToggleSwitch() {
        return onView(withId(com.philips.platform.uit.test.R.id.toggle_switch));
    }
}
