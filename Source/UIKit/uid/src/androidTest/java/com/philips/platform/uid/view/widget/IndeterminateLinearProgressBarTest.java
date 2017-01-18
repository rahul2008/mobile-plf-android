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
import com.philips.platform.uid.utils.UIDUtils;

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

    private static final int TRAILING_ANIMATION_WAIT = 1000;
    private static final int PROGRESS_ANIMATION_TIME = 1800;

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
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    // TODO: 1/10/2017 Fix theming test once automation is in place
    @Ignore
    @Test
    public void verifyTransitionLeadingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingDrawable));
    }

    @Test
    public void verifyTransitionLeadingDrawableWidth() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.leadingDrawable));
    }

    // TODO: 1/10/2017 Fix theming test once automation is in place
    @Ignore
    @Test
    public void verifyTransitionLeadingMirrorDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionLeadingMirrorDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionLeadingMirrorDrawableWidth() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.leadingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    // TODO: 1/10/2017 Fix theming test once automation is in place
    @Ignore
    @Test
    public void verifyTransitionTrailingDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingDrawable));
    }

    @Test
    public void verifyTransitionTrailingDrawableWidth() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.trailingDrawable));
    }

    private IndeterminateLinearProgressBar getIndeterminateProgressBar() {
        return (IndeterminateLinearProgressBar) mActivityTestRule.getActivity().findViewById(com.philips.platform.uid.test.R.id.progressBar);
    }

    // TODO: 1/10/2017 Fix theming test once automation is in place
    @Ignore
    @Test
    public void verifyTransitionTrailingMirrorDrawableStartColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(0, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingMirrorDrawableEndColor() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        BaseTypeSafteyMatcher<Drawable> colorMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColors(1, Color.TRANSPARENT);
        assertTrue(colorMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyTransitionTrailingMirrorDrawableWidth() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        int expectedWidth = (int) (progressBar.getWidth() * 0.2f);
        BaseTypeSafteyMatcher<Drawable> dimenMatcher = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameWidth(expectedWidth);
        assertTrue(dimenMatcher.matches(progressBar.trailingMirrorDrawable));
    }

    @Test
    public void verifyLeadingAnimDuration() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        long duration = progressBar.leadingAnim.getAnimator().getDuration();
        assertEquals(PROGRESS_ANIMATION_TIME, duration);
    }

    @Test
    public void verifyScreenOffPausesLeadingAnimation() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
        assertEquals(true, progressBar.leadingAnim.getAnimator().isPaused());
    }

    @Test
    public void verifyScreenOffOnRestartsLeadingAnimation() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        final IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
                progressBar.onScreenStateChanged(View.SCREEN_STATE_ON);
            }
        });
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        assertEquals(true, progressBar.leadingAnim.getAnimator().isRunning());
    }

    @Test
    public void verifyTrailingAnimDuration() {
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        long duration = progressBar.trailingAnim.getAnimator().getDuration();
        assertEquals(PROGRESS_ANIMATION_TIME, duration);
    }

    @Test
    public void verifyScreenOffPausesTrailingAnimation() {
        UIDTestUtils.waitFor(testResources, TRAILING_ANIMATION_WAIT);
        IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
        assertEquals(true, progressBar.trailingAnim.getAnimator().isPaused());
    }

    @Test
    public void verifyScreenOffOnRestartsTrailingAnimation() {
        UIDTestUtils.waitFor(testResources, TRAILING_ANIMATION_WAIT);
        final IndeterminateLinearProgressBar progressBar = getIndeterminateProgressBar();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                progressBar.onScreenStateChanged(View.SCREEN_STATE_OFF);
                progressBar.onScreenStateChanged(View.SCREEN_STATE_ON);
            }
        });
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        assertEquals(true, progressBar.trailingAnim.getAnimator().isRunning());
    }
}