/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uit.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.drawableutils.GradientDrawableUtils;
import com.philips.platform.uit.view.widget.Switch;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class ToggleSwitchTest {

    private Context activityContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Switch toggleSwitch;
    private Drawable trackDrawable;
    private Drawable thumbDrawable;
    private Resources testResources;

    @Before
    public void setup() {

        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_toggle_switch);
        testResources = getInstrumentation().getContext().getResources();
        activityContext = activity;

        trackDrawable = toggleSwitch.getTrackDrawable();
        thumbDrawable = toggleSwitch.getThumbDrawable();
        testResources = getInstrumentation().getContext().getResources();
    }

    //*********************************Toggle Switch Layout TestScenarios**************************//
    @Test
    public void verifyToggleSwitchTrackWidth() {
//        int expectedWidth = (int) Math.ceil(testResources.getDimension(R.dimen.uit_switch_track_height));
//        Assert.assertEquals(expectedWidth, trackDrawable.getIntrinsicWidth());
    }

    //
    @Test
    public void verifyToggleSwitchTrackHeight() {
//        toggleSwitch = new Switch(activityTestRule.getActivity());
//        int expectedHeight = (int) Math.ceil(testResources.getDimension(R.dimen.uit_switch_track_width)-testResources.getDimension(R.dimen.uit_switch_track_transparent_height));
//        Assert.assertEquals(expectedHeight, trackDrawable.getIntrinsicHeight());
    }

    @Test
    public void verifyToggleSwitchThumbWidth() {
//        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
//        Assert.assertEquals((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_strokewidth), stateColors.getStrokeWidth());
    }

    //
    @Test
    public void verifyCornerRadius() {
//        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(trackDrawable);
//        float radius = (float) Math.ceil(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_cornerradius));
//        Assert.assertEquals(radius, stateColors.getCornerRadius()[0]);
    }


    //*********************************Toggle Switch Theming TestScenarios**************************//


    @Test
    public void verifySwitchThumbFillColor() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
        Assert.assertEquals(Color.WHITE, stateColors.getGradientSolidColor());
    }

    @Test
    public void verifyToggleSwitchTrackOnColorTest() {
//        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
//        int baseColor = UITTestUtils.getAttributeColor(activityTestRule.getActivity(), R.attr.uit_baseColor);
//        Assert.assertEquals(baseColor, stateColors.getStateColor(android.R.attr.state_checked));
    }

    @Test
    public void verifyToggleSwitchTrackOffColorTest() {
//        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
//        int baseColor = UITTestUtils.getAttributeColor(activityTestRule.getActivity(), R.attr.uit_baseColor);
//        int offColor = UITTestUtils.modulateColorAlpha(baseColor, 0.1f);
//        Assert.assertEquals(offColor, stateColors.getStateColor(android.R.attr.state_enabled));
    }

    @Test
    public void verifyDisabledToggleSwitchTrackFillColorTest(){

    }

    @Test
    public void verifyDisabledToggleSwitchThumbFillColorTest(){

    }

    @Test
    public void verifyToggleSwitchOrientationChange() {
//        toggleSwitch.setChecked(true);
//        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Assert.assertTrue(toggleSwitch.isChecked());
//        toggleSwitch.setChecked(false);
//        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Assert.assertFalse(toggleSwitch.isChecked());
    }
}
