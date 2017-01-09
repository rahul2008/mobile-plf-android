package com.philips.platform.uid.view.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.BaseTypeSafteyMatcher;
import com.philips.platform.uid.matcher.DrawableMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class IndeterminateLinearProgressBarTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_indeterminate_progress_bar);
        testResources = getInstrumentation().getContext().getResources();
    }

    @Test
    public void verifyProgressBarHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.linearprogressbar_height);
        onView(withId(com.philips.platform.uid.test.R.id.progressBar))
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyTransitionLeadingDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    @Test
    public void verifyTransitionLeadingDrawableCenterColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    @Test
    public void verifyTransitionLeadingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(2, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableCenterColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(2, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Test
    public void verifyLeadingAnimDuration() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        long duration = progressBar.leadingAnim.getAnimator().getDuration();
        assertEquals(1800, duration);
    }

    @Test
    public void verifyTrailingAnimDuration() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        long duration = progressBar.trailingAnim.getAnimator().getDuration();
        assertEquals(1800, duration);
    }
}
