/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.progressindicators;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LinearProgressIndicators {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Context activityContext;
    private Resources resources;
    private int GroupBlue45 = R.color.uid_group_blue_level_45;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_progressbar);
        resources = getInstrumentation().getContext().getResources();
        activityContext = activity;
    }

    //*********************************Layout TestScenarios**************************//
    @Test
    public void verifyHeightOfProgressBar() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.linearprogressbar_height);
        getProgressBar().check(matches(FunctionDrawableMatchers.isMinHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight, progressID())));
    }

    @Test
    public void verifyMinWidthOfProgressBar() {
        int expectedMinWidth = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.linearprogressbar_minwidth);
        getProgressBar().check(matches(FunctionDrawableMatchers.isMinWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedMinWidth, progressID())));
    }

    @Test
    public void verifyLeftMarginOfProgressBar() {
        int expectedLeftMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.linearprogressbar_leftmargin);
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyRightMarginOfProgressBar() {
        int expectedRightMargin = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.linearprogressbar_leftmargin);
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedRightMargin)));
    }

    //*********************************Theming Scenarios**************************//
    @Test
    public void verifyProgressBarProgressColor() {
        final int expectedProgressBarProgressColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTrackDetailNormalOnBackgroundColor);//ContextCompat.getColor(activityContext, GroupBlue45);
        getProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, progressID(), true)));
    }

    @Test
    public void verifyProgressBarBackgroundColor() {
        final int expectedProgressBarBackgroundColor = getExpectedProgressBarBackgroundColor();
        getProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, progressBackgroundID(), true)));
    }

    @Test
    public void verifySecondaryProgressBarProgressColor() {
        final int expectedSecProgressBarProgressColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTrackDetailNormalOnBackgroundColor);
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedSecProgressBarProgressColor, progressID(), true)));
    }

    @Test
    public void verifySecondaryProgressBarSecondaryProgressColor() {
        final int expectedSecProgressBarSecondaryColor = UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTrackDetailBufferBackgroundColor);
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedSecProgressBarSecondaryColor, progressSecondaryID(), true)));
    }

    @Test
    public void verifySecondaryProgressBarSecondaryBackgroundColor() {

        final int expectedProgressBarBackgroundColor = getExpectedProgressBarBackgroundColor();
        getProgressBarSecondary().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarBackgroundColor, progressBackgroundID(), true)));
    }

    private int getExpectedProgressBarBackgroundColor() {
        return UIDTestUtils.getAttributeColor(activityContext, R.attr.uidTrackDetailNormalOffBackgroundColor);
    }

    private ViewInteraction getProgressBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.progress_bar));
    }

    private ViewInteraction getProgressBarSecondary() {
        return onView(withId(com.philips.platform.uid.test.R.id.progress_bar_secondary));
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