/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uit.components.ProgressIndicators;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.R;
import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue10;
import static com.philips.platform.uit.test.R.color.GroupBlue45;
import static com.philips.platform.uit.utils.UITTestUtils.modulateColorAlpha;

public class LinearProgressIndicators {

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
    public void verifyHeightOfProgressBar() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.linearprogressbar_height);
        getProgressBar().check(matches(FunctionDrawableMatchers.isMinHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight, progressID())));
    }

    @Test
    public void verifyMinWidthOfProgressBar() {
        int expectedMinWidth = testResources.getDimensionPixelSize(com.philips.platform.uit.test.R.dimen.linearprogressbar_minwidth);
        getProgressBar().check(matches(FunctionDrawableMatchers.isMinWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedMinWidth, progressID())));
    }

    @Test
    public void verifyLeftMarginOfProgressBar() {

    }

    @Test
    public void verifyRightMarginOfProgressBar() {

    }

    //*********************************Theming Scenarios**************************//
    @Test
    public void verifyProgressBarProgressColor() {
        final int expectedProgressBarProgressColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, progressID(), true)));
    }

    @Test
    public void verifyProgressBarBackgroundColor() {
        final int expectedProgressBarBackgroundColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.15f);
        getProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, progressBackgroundID(), true)));
    }

    @Test
    public void verifySecondaryProgressBarProgressColor() {
        final int expectedSecProgressBarProgressColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedSecProgressBarProgressColor, progressID(), true)));

    }

    @Test
    public void verifySecondaryProgressBarSecondaryProgressColor() {
        final int expectedSecProgressBarSecondaryColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.50f);
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedSecProgressBarSecondaryColor, progressSecondaryID(), true)));

    }

    @Test
    public void verifySecondaryProgressBarSecondaryBackgroundColor() {
        final int expectedSecProgressBarBackgroundColor = modulateColorAlpha(ContextCompat.getColor(instrumentationContext, GroupBlue45), 0.15f);
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedSecProgressBarBackgroundColor, progressBackgroundID(), true)));

    }

    private ViewInteraction getProgressBar() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar));
    }

    private ViewInteraction getProgressBarSecondary() {
        return onView(withId(com.philips.platform.uit.test.R.id.progress_bar_secondary));
    }

    private int progressID() {
        return android.R.id.progress;
    }

    private int progressSecondaryID() {
        return android.R.id.secondaryProgress;
    }

    private int progressBackgroundID() {
        return android.R.id.background;
    }
}