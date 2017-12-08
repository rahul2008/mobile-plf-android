/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.progressindicators;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.ProgressBarMatcher;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class IndeterminateCircularProgressIndicators {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Context instrumentationContext;
    private Resources testResources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_progressbar);
        testResources = activity.getResources();
        instrumentationContext = activity;
    }

    //*********************************Layout TestScenarios**************************//
    @Test
    public void verifyHeightOfSmallCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);
        getSmallIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfSmallCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);
        getSmallIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfMediumCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMediumIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfMediumCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_middle_heightwidth);
        getMediumIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyHeightOfBigCircularProgressIndicator() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyWidthOfBigCircularProgressIndicator() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_large_heightwidth);
        getLargeIndeterminateCircularProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyDurationOfSmallCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uid.test.R.integer.circularprogressbar_small_duration);
        getSmallIndeterminateCircularProgressBar().check(matches(ProgressBarMatcher.isSameDuration(expectedDuration)));
    }

    @Test
    public void verifyDurationOfMediumCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uid.test.R.integer.circularprogressbar_medium_duration);
        getMediumIndeterminateCircularProgressBar().check(matches(ProgressBarMatcher.isSameDuration(expectedDuration)));
    }

    @Test
    public void verifyDurationOfBigCircularProgressIndicator() {
        int expectedDuration = testResources.getInteger(com.philips.platform.uid.test.R.integer.circularprogressbar_large_duration);
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
        final int expectedEndColor = UIDTestUtils.getAttributeColor(instrumentationContext, R.attr.uidTrackDetailNormalOnBackgroundColor);
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
        final int expectedEndColor = UIDTestUtils.getAttributeColor(instrumentationContext, R.attr.uidTrackDetailNormalOnBackgroundColor);

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
        final int expectedEndColor = UIDTestUtils.getAttributeColor(instrumentationContext, R.attr.uidTrackDetailNormalOnBackgroundColor);

        getLargeIndeterminateCircularProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, circularprogressID(), expectedEndColor, 1)));
    }

    private ViewInteraction getSmallIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.progress_bar_indeterminate_circular_small));
    }

    private ViewInteraction getMediumIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.progress_bar_indeterminate_circular_middle));
    }

    private ViewInteraction getLargeIndeterminateCircularProgressBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.progress_bar_indeterminate_circular_big));
    }

    private int circularprogressID() {
        return android.R.id.progress;
    }
}