package com.philips.platform.uid.view.widget;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.BaseTypeSafteyMatcher;
import com.philips.platform.uid.matcher.DrawableMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Ignore;
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

    @Ignore
    @Test
    public void verifyTransitionLeadingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    @Test
    public void verifyTransitionLeadingDrawableWidth() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.leadingDrawable));
    }

    @Ignore
    @Test
    public void verifyTransitionLeadingMirrorDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionLeadingMirrorDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionLeadingMirrorDrawableWidth() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Ignore
    @Test
    public void verifyTransitionTrailingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableWidth() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.trailingDrawable));
    }

    @Ignore
    @Test
    public void verifyTransitionTrailingMirrorDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingMirrorDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingMirrorDrawableWidth() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyLeadingAnimDuration() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        long duration = progressBar.leadingAnim.getAnimator().getDuration();
        assertEquals(1800, duration);
    }

    @Test
    public void verifyScreenOffPausesLeadingAnimation() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
        assertEquals(true, progressBar.leadingAnim.getAnimator().isPaused());
    }

    @Test
    public void verifyScreenOffOnRestartsLeadingAnimation() {
        UIDTestUtils.waitFor(testResources, 750);
        final IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
                progressBar.onScreenStateChanged(View.SCREEN_STATE_ON);
            }
        });
        UIDTestUtils.waitFor(testResources, 750);
        assertEquals(true, progressBar.leadingAnim.getAnimator().isRunning());
    }

    @Test
    public void verifyTrailingAnimDuration() {
        UIDTestUtils.waitFor(testResources, 750);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        long duration = progressBar.trailingAnim.getAnimator().getDuration();
        assertEquals(1800, duration);
    }

    @Test
    public void verifyScreenOffPausesTrailingAnimation() {
        UIDTestUtils.waitFor(testResources, 1000);
        IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
        assertEquals(true, progressBar.trailingAnim.getAnimator().isPaused());
    }


    @Test
    public void verifyScreenOffOnRestartsTrailingAnimation() {
        UIDTestUtils.waitFor(testResources, 1000);
        final IndeterminateLinearProgressBar progressBar = (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
                progressBar.onScreenStateChanged(View.SCREEN_STATE_ON);
            }
        });
        UIDTestUtils.waitFor(testResources, 750);
        assertEquals(true, progressBar.trailingAnim .getAnimator().isRunning());
    }
}