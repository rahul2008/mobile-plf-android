/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.slider;


import android.app.Activity;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.widget.SeekBar;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.DiscreteSlider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

public class SliderTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Activity activity;
    int slideProgress;

    @Before
    public void setUp() {
        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.layout_slider);
        resources = getInstrumentation().getContext().getResources();
        Espresso.registerIdlingResources(baseTestActivity.getIdlingResource());
        activity = baseTestActivity;
        ((DiscreteSlider)activity.findViewById(com.philips.platform.uid.test.R.id.uid_asymmetric_discrete_slider)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                slideProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private ViewInteraction getHorizontalSlider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_slider));
    }

    private ViewInteraction getSymmetricDiscreteSlider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_symmetric_discrete_slider));
    }

    private ViewInteraction getAsymmetricDiscreteSlider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_asymmetric_discrete_slider));
    }

    @Test
    public void verifySliderRippleColor() {
        getHorizontalSlider().perform(longClick());
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidThumbDefaultPressedBorderColor);
        getHorizontalSlider().
                check(matches(FunctionDrawableMatchers.
                        isSameRippleColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySliderRippleRadius() {
        final int expectedRadius = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_slider_border_ripple_radius);
        getHorizontalSlider().
                check(matches(FunctionDrawableMatchers.
                        isSameRippleRadius(TestConstants.FUNCTION_GET_BACKGROUND, expectedRadius)));
    }

    @Test
    public void verifySliderMinWidth() {
        final int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_slider_min_width);
        getHorizontalSlider().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expected)));
    }

    @Test
    public void verifySliderTrackProgressColor() {
        final int expectedProgressBarProgressColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTrackDefaultNormalOnBackgroundColor);
        getHorizontalSlider().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, android.R.id.progress)));
    }

    @Test
    public void verifySliderTrackBackgroundColor() {
        final int expectedProgressBarBackgroundColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTrackDefaultNormalOffBackgroundColor);
        getHorizontalSlider().check(matches(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, android.R.id.background)));
    }

    @Test
    public void verifyAsymmetricDiscreteSliderProgress() {
        getAsymmetricDiscreteSlider().perform(longClick());
        assertEquals(10, ((DiscreteSlider) activity.findViewById(com.philips.platform.uid.test.R.id.uid_asymmetric_discrete_slider)).getProgress());
    }

    @Test
    public void verifyDiscreteSliderListener(){
        getSymmetricDiscreteSlider().perform(longClick());
        getAsymmetricDiscreteSlider().perform(longClick());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((DiscreteSlider) activity.findViewById(com.philips.platform.uid.test.R.id.uid_asymmetric_discrete_slider)).setDiscretePointDrawable(resources.getDrawable(com.philips.platform.uid.test.R.drawable.uid_ic_cross_icon, activity.getTheme()));
                ((DiscreteSlider) activity.findViewById(com.philips.platform.uid.test.R.id.uid_symmetric_discrete_slider)).setDiscretePointDrawable((com.philips.platform.uid.test.R.drawable.uid_ic_data_validation));
            }
        });
        assertEquals(slideProgress, ((DiscreteSlider) activity.findViewById(com.philips.platform.uid.test.R.id.uid_asymmetric_discrete_slider)).getProgress());
    }
}
