/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uit.components.progressindicators;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.ProgressBarMatcher;
import com.philips.platform.uit.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue45;

public class IndeterminateCircularProgressIndicators {

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
        getSmallIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfSmallCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_small_heightwidth);
        getSmallIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfMediumCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMediumIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfMediumCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMediumIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfBigCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfBigCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyDurationOfSmallCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uit.test.R.integer.circularprogressbar_small_duration);
        getSmallIndeterminateCircularProgressBar().check(matches(ProgressBarMatcher.isSameDuration(expectedDuration)));
    }

    @Test
    public void verifyDurationOfMediumCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uit.test.R.integer.circularprogressbar_medium_duration);
        getMediumIndeterminateCircularProgressBar().check(matches(ProgressBarMatcher.isSameDuration(expectedDuration)));
    }

    @Test
    public void verifyDurationOfBigCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uit.test.R.integer.circularprogressbar_large_duration);
        getLargeIndeterminateCircularProgressBar().check(matches(ProgressBarMatcher.isSameDuration(expectedDuration)));
    }

    @Test
    public void verifyIndeterminateSmallCircularPBThicknessRatio() {
        getSmallIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_SMALL_THICKNESS_RATIO)));
    }

    @Test
    public void verifyIndeterminateMediumCircularPBThickness() {
        getMediumIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_MID_THICKNESS_RATIO)));
    }

    @Test
    public void verifyIndeterminateLargeCircularPBThicknessRatio() {
        getLargeIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameRingThicknessRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_LARGE_THICKNESS_RATIO)));
    }

    @Test
    public void verifyIndeterminateSmallCircularPBRadiusRatio() {
        getSmallIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_SMALL_INNER_RADIUS_RATIO)));
    }

    @Test
    public void verifyIndeterminateMediumCircularPBRadiusRatio() {
        getMediumIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_MID_INNER_RADIUS_RATIO)));
    }

    @Test
    public void verifyIndeterminateLargeCircularPBRadiusRatio() {
        getLargeIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameInnerRadiusRatio(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), TestConstants.CIRCULAR_LARGE_INNER_RADIUS_RATIO)));
    }

    //*********************************Theming TestScenarios**************************//

    @Test
    public void verifyIndeterminateSmallCircularPBStartColor() {
        int expectedStartColor = Color.TRANSPARENT;
        getSmallIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedStartColor, 0)));
    }

    @Test
    public void verifyIndeterminateSmallCircularPBEndColor() {
        final int expectedEndColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getSmallIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedEndColor, 1)));
    }

    @Test
    public void verifyIndeterminateMediumCircularPBStartColor() {
        int expectedStartColor = Color.TRANSPARENT;
        getMediumIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedStartColor, 0)));
    }

    @Test
    public void verifyIndeterminateMediumCircularPBEndColor() {
        final int expectedEndColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getMediumIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedEndColor, 1)));
    }

    @Test
    public void verifyIndeterminateLargeCircularPBStartColor() {
        int startColor = Color.TRANSPARENT;
        getLargeIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), startColor, 0)));
    }

    @Test
    public void verifyIndeterminateLargeCircularPBEndColor() {
        final int expectedEndColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getLargeIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedEndColor, 1)));
    }

    private ViewInteraction getSmallIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_indeterminate_circular_small));
    }

    private ViewInteraction getMediumIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_indeterminate_circular_middle));
    }

    private ViewInteraction getLargeIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_indeterminate_circular_big));
    }

    private int circularprogressID() {
        return android.R.id.progress;
    }
}