package com.philips.platform.uit.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.platform.uit.R;
import com.philips.platform.uit.view.widget.Switch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class ToggleSwitchTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Switch toggleSwitch;
    private Drawable trackDrawable;
    private Drawable thumbDrawable;
    private Resources testResources;

    @Before
    public void setup() {
        toggleSwitch = new Switch(activityTestRule.getActivity());
        trackDrawable = toggleSwitch.getTrackDrawable();
        thumbDrawable = toggleSwitch.getThumbDrawable();
        testResources = getInstrumentation().getContext().getResources();
    }

    @Test
    public void verifyToggleSwitchTrackWidth() {
        toggleSwitch = new Switch(activityTestRule.getActivity());
        int expectedWidth = (int) Math.ceil(testResources.getDimension(R.dimen.uit_switch_track_height));
        Assert.assertEquals(expectedWidth, trackDrawable.getIntrinsicWidth());
    }

    @Test
    public void verifyToggleSwitchTrackHeight() {
        toggleSwitch = new Switch(activityTestRule.getActivity());
        int expectedHeight = (int) Math.ceil(testResources.getDimension(R.dimen.uit_switch_track_width)-testResources.getDimension(R.dimen.uit_switch_track_transparent_height));
        Assert.assertEquals(expectedHeight, trackDrawable.getIntrinsicHeight());
    }

    @Test
    public void verifyToggleSwitchThumbRadius() {
        toggleSwitch = new Switch(activityTestRule.getActivity());
        int expectedHeight = (int) Math.ceil(testResources.getDimension(R.dimen.uit_switch_thumb_radius));
        Assert.assertEquals(expectedHeight, thumbDrawable.getIntrinsicWidth());
    }

    //@Test
    //public void verifyToggleSwitchStrokeWidth() {
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
    //    Assert.assertEquals((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_strokewidth), stateColors.getStrokeWidth());
    //}
    //
    //@Test
    //public void verifyCornerRadius() {
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(trackDrawable);
    //    float radius = (float) Math.ceil(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.switch_togglebutton_cornerradius));
    //    Assert.assertEquals(radius, stateColors.getCornerRadius()[0]);
    //}
    //
    //@Test
    //public void verifySwitchThumbSolidColor() {
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
    //    Assert.assertEquals(Color.WHITE, stateColors.getGradientSolidColor());
    //}
    //
    //@Test
    //public void verifyThumbStrokeColor() {
    //    int baseColor = UITTestUtils.getAttributeColor(activityTestRule.getActivity(), R.attr.uit_baseColor);
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(thumbDrawable);
    //    Assert.assertEquals(baseColor, stateColors.getStrokeSolidColor());
    //}
    //
    //@Test
    //public void verifyToggleSwitchCheckedColorTest() {
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
    //    int baseColor = UITTestUtils.getAttributeColor(activityTestRule.getActivity(), R.attr.uit_baseColor);
    //    Assert.assertEquals(baseColor, stateColors.getStateColor(android.R.attr.state_checked));
    //}
    //
    //@Test
    //public void verifyToggleSwitchOffColorTest() {
    //    GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(toggleSwitch.getTrackDrawable());
    //    int baseColor = UITTestUtils.getAttributeColor(activityTestRule.getActivity(), R.attr.uit_baseColor);
    //    int offColor = UITTestUtils.modulateColorAlpha(baseColor, 0.1f);
    //    Assert.assertEquals(offColor, stateColors.getStateColor(android.R.attr.state_enabled));
    //}
    //
    //@Test
    //public void verifyToggleSwitchOrientationChange() {
    //    toggleSwitch.setChecked(true);
    //    activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    //    Assert.assertTrue(toggleSwitch.isChecked());
    //    toggleSwitch.setChecked(false);
    //    activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    //    Assert.assertFalse(toggleSwitch.isChecked());
    //}
}
