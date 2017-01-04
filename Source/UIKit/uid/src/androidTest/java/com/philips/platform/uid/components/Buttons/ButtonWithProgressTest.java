package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.allOf;

public class ButtonWithProgressTest {
    private Resources testResources;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);

        activity.switchFragment(new ButtonsTestFragment());
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    @Test
    public void verifyProgressIndicatorHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);

        getButtonwithDeterminateProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyProgressIndicatorWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);

        getButtonwithDeterminateProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyMarginBetweenProgressbarAndProgressText() {

        getProgressTextFromDeterminateProgressBar().check(matches(ViewPropertiesMatchers.isSameLeftMargin(28)));
    }

    private ViewInteraction getProgressTextFromDeterminateProgressBar() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_text),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }

    private ViewInteraction getDeterminateCircularProgressBar() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_progress_bar),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }

    private ViewInteraction getButtonwithDeterminateProgressBar() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_progress_bar),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }
}
