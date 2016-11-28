/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uit.components.progressindicators;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue45;
import static com.philips.platform.uit.utils.UIDTestUtils.modulateColorAlpha;

public class DeterminateCircularProgressIndicators {
    private Context activityContext;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_progressbar);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
        activityContext = activity;
    }

    //*********************************Layout TestScenarios**************************//
    @Test
    public void verifyHeightOfSmallCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_small_heightwidth);
        getSmallCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfSmallCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_small_heightwidth);
        getSmallCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfMediumCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMiddleCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfMediumCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMiddleCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfBigCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfBigCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyDeterminateSmallCircularPBThicknessRatio() {
        getSmallCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_SMALL_THICKNESS_RATIO)));
    }

    @Test
    public void verifyDeterminateMediumCircularPBThicknessRatio() {
        getMiddleCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_MID_THICKNESS_RATIO)));
    }

    @Test
    public void verifyDeterminateLargeCircularPBThicknessRatio() {
        getLargeCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_LARGE_THICKNESS_RATIO)));
    }

    @Test
    public void verifyDeterminateSmallCircularPBRadiusRatio() {
        getSmallCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_SMALL_INNER_RADIUS_RATIO)));
    }

    @Test
    public void verifyDeterminateMediumCircularPBRadiusRatio() {
        getMiddleCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_MID_INNER_RADIUS_RATIO)));
    }

    @Test
    public void verifyDeterminateLargeCircularPBRadiusRatio() {
        getLargeCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, circularprogressID(), TestConstants.CIRCULAR_LARGE_INNER_RADIUS_RATIO)));
    }

    //*********************************Theming TestScenarios**************************//

    @Test
    public void verifyCircularProgressBarSmallProgressColor() {
        final int expectedProgressBarProgressColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getSmallCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, circularprogressID(), true)));
    }

    @Test
    public void verifyProgressBarSmallBackgroundColor() {
        final int expectedProgressBarBackgroundColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.15f);
        getSmallCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, circularprogressBackgroundID(), true)));
    }

    @Test
    public void verifyCircularProgressBarMediumProgressColor() {
        final int expectedProgressBarProgressColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getMiddleCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, circularprogressID(), true)));
    }

    @Test
    public void verifyProgressBarMediumBackgroundColor() {
        final int expectedProgressBarBackgroundColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.15f);
        getMiddleCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, circularprogressBackgroundID(), true)));
    }

    @Test
    public void verifyCircularProgressBarLargeProgressColor() {
        final int expectedProgressBarProgressColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getLargeCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, circularprogressID(), true)));
    }

    @Test
    public void verifyProgressBarLargeBackgroundColor() {
        final int expectedProgressBarBackgroundColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.15f);
        getLargeCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, circularprogressBackgroundID(), true)));
    }


    private ViewInteraction getSmallCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_determinate_circular_small));
    }

    private ViewInteraction getMiddleCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_determinate_circular_middle));
    }

    private ViewInteraction getLargeCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_determinate_circular_big));
    }

    private int circularprogressBackgroundID() {
        return android.R.id.background;
    }

    private int circularprogressID() {

        return android.R.id.progress;
    }


}