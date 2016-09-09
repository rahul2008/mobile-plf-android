package com.philips.cdpp.catalogapp.activity;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ToggleSwitchTest {
    @Test
    public void dummyTest() {

    }
/*
    @Rule
    public ActivityTestRule<ButtonsActivity> mActivityTestRule = new ActivityTestRule<>(ButtonsActivity.class);
    private Switch toggleSwitch;
    private Drawable trackDrawable;
    private Drawable thumbDrawable;
    private Resources testResources;

    @Before
    public void setUP() {
        toggleSwitch = new Switch(mActivityTestRule.getActivity());
        trackDrawable = toggleSwitch.getTrackDrawable();
        thumbDrawable = toggleSwitch.getThumbDrawable();
        testResources = getInstrumentation().getContext().getResources();
    }

    @Test
    public void verifyToggleSwitchSupport() {
        onView(withId(R.id.change_button_state)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyToggleSwitchWidth() {
        toggleSwitch = new Switch(mActivityTestRule.getActivity());
        int expectedwidth = (int) Math.ceil(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_width));
        Assert.assertEquals(expectedwidth, toggleSwitch.getTrackDrawable().getIntrinsicWidth());
    }

    @Test
    public void verifyToggleSwitchHeight() {
        toggleSwitch = new Switch(mActivityTestRule.getActivity());
        int expectedheight = (int) Math.ceil(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_height));
        Assert.assertEquals(expectedheight, toggleSwitch.getTrackDrawable().getIntrinsicHeight());
    }

    @Test
    public void verifyToggleSwitchStrokeWidth() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
        Assert.assertEquals((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_strokewidth), stateColors.getStrokeWidth());
    }

    @Test
    public void verifyCornerRadius() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(trackDrawable);
        float radius = (float) Math.ceil(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_cornerradius));
        Assert.assertEquals(radius, stateColors.getCornerRadius()[0]);
    }

    @Test
    public void verifySwitchThumbSolidColor() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
        Assert.assertEquals(Color.WHITE, stateColors.getGradientSolidColor());
    }

    @Test
    public void verifyThumbStrokeColor() {
        int baseColor = ThemeColorUtils.getAttributeColor(mActivityTestRule.getActivity(), R.attr.uikit_baseColor);
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
        Assert.assertEquals(baseColor, stateColors.getStrokeSolidColor());
    }

    @Test
    public void verifyToggleSwitchCheckedColorTest() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
        int baseColor = ThemeColorUtils.getAttributeColor(mActivityTestRule.getActivity(), R.attr.uikit_baseColor);
        Assert.assertEquals(baseColor, stateColors.getStateColor(android.R.attr.state_checked));
    }

    @Test
    public void verifyToggleSwitchOffColorTest() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
        int baseColor = ThemeColorUtils.getAttributeColor(mActivityTestRule.getActivity(), R.attr.uikit_baseColor);
        int offColor = ThemeColorUtils.modulateColorAlpha(baseColor, 0.1f);
        Assert.assertEquals(offColor, stateColors.getStateColor(android.R.attr.state_enabled));
    }

    @Test
    public void verifyToggleSwitchOrientationChange() {
        toggleSwitch.setChecked(true);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Assert.assertTrue(toggleSwitch.isChecked());
        toggleSwitch.setChecked(false);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Assert.assertFalse(toggleSwitch.isChecked());
    }*/
}
