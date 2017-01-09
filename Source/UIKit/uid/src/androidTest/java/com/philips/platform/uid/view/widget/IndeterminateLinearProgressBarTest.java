package com.philips.platform.uid.view.widget;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyTransitionDrawableColor() {

    }
    private ViewInteraction getProgressBar() {
        return onView(withId(com.philips.platform.uid.test.R.id.progressBar));
    }
}
