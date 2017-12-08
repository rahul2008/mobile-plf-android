/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.buttons;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.ProgressBarButton;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;
import static org.hamcrest.CoreMatchers.allOf;

public class ButtonWithProgressIndicatorsTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    public void setUpDefaultTheme() {
        final Intent intent = getIntent(0);
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);

        activity.switchFragment(new ButtonsTestFragment());
        testResources = activity.getResources();
    }

    ///*******************Layout scenarios*************************
    @Test
    public void verifyProgressIndicatorProgressBarHeight() {
        setUpDefaultTheme();
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);

        getProgressBar().check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedHeight)));
    }

    @Test
    public void verifyProgressIndicatorProgressBarWidth() {
        setUpDefaultTheme();
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.circularprogressbar_small_heightwidth);

        getProgressBar().check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, expectedWidth)));
    }

    @Test
    public void verifyProgressIndicatorMinHeight() {
        setUpDefaultTheme();
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_button_min_height);

        getDeterminateProgressIndicatorButton().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyProgressIndicatorMinWidth() {
        setUpDefaultTheme();
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_button_min_width);

        getDeterminateProgressIndicatorButton().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(expectedWidth)));
    }

    @Test
    public void verifyMarginsBetweenProgressbarAndProgressTextWhenThereIsProgressText() {
        setUpDefaultTheme();

        final int progressIndicatorTextPadding = testResources.getDimensionPixelOffset(R.dimen.uid_progress_indicator_button_progress_text_padding);
        final int progressIndicatorLeftRightMargin = testResources.getDimensionPixelOffset(R.dimen.uid_button_padding_left_right);
        simulateSetProgressText("Hello ");

        //To check progress text is displayed
        getProgressText().check(matches(ViewPropertiesMatchers.isSameEndMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorTextPadding)))
                .check(matches(ViewMatchers.isDisplayed()));
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameEndMargin(0)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifyMarginsBetweenProgressbarAndProgressTextWhenProgressTextIsEmpty() {
        setUpDefaultTheme();

        final int progressIndicatorLeftRightMargin = testResources.getDimensionPixelOffset(R.dimen.uid_button_padding_left_right);
        simulateSetProgressText("");

        //To check progress text is not displayed
        getProgressText().check(matches(new IsNot(isDisplayed())));
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameEndMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Ignore
    @Test
    public void verifyDisabledStateWhenInProgressToShowButton() {
        setUpDefaultTheme();

        getIndeterminateProgressIndicatorButton().perform(new UpdateProgressTextViewAction("Hiasdjadkasjdfkjsj"));

        getIndeterminateProgressIndicatorButton().perform(new SetViewDisabledViewAction());
        getIndeterminateProgressIndicatorButton().check(matches(ViewMatchers.isDisplayed()));
        getIndeterminateProgressText().check(matches(new IsNot<View>(ViewMatchers.isDisplayed())));
    }

    @Test
    public void verifyMarginsBetweenProgressbarAndProgressTextWhenProgressTextIsSetToNotEmptyAndThenSetEmptyTextEmpty() {
        setUpDefaultTheme();

        final int progressIndicatorTextPadding = testResources.getDimensionPixelOffset(R.dimen.uid_progress_indicator_button_progress_text_padding);
        final int progressIndicatorLeftRightMargin = testResources.getDimensionPixelOffset(R.dimen.uid_button_padding_left_right);
        simulateSetProgressText("Hello ");

        //To check progress text is displayed
        getProgressText().check(matches(ViewPropertiesMatchers.isSameEndMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorTextPadding)))
                .check(matches(ViewMatchers.isDisplayed()));
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameEndMargin(0)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewMatchers.isDisplayed()));

        simulateSetProgressText("");

        //To check progress text is not displayed
        getProgressText().check(matches(new IsNot(isDisplayed())));
        getProgressBar().check(matches(ViewPropertiesMatchers.isSameEndMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewPropertiesMatchers.isSameLeftMargin(progressIndicatorLeftRightMargin)))
                .check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifyButtonTextSize() {
        setUpDefaultTheme();

        final float textSize = testResources.getDimension(R.dimen.uid_label_text_size);
        getButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(textSize)));
    }

    //*********************Theming scenarios**************************

    @Test
    public void verifyAttrOfTextColorInProgressState() {
        setUpDefaultTheme();

        simulateSetProgressText("Hello ");
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, getUidControlPrimaryDetailAtrribute());
        //To check progress text is displayed
        getProgressText().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }


    @Test
    public void verifyTextColorInProgressState() {
        setUpDefaultTheme();

        simulateSetProgressText("Hello ");
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, getUidControlPrimaryDetailAtrribute());

        getProgressText().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyButtonTextColorInNormalState() {
        setUpDefaultTheme();

        simulateSetProgressText("Hello ");
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonPrimaryNormalTextColor);

        getButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyProgressTextSize() {
        setUpDefaultTheme();

        simulateSetProgressText("Hello ");
        final float textSize = testResources.getDimension(R.dimen.uid_label_text_size);

        getProgressText().check(matches(TextViewPropertiesMatchers.isSameFontSize(textSize)));
    }

    @Test
    public void verifyButtonFillColor() {
        setUpDefaultTheme();

        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonPrimaryNormalBackgroundColor);

        getButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyProgressButtonFillColor() {
        setUpDefaultTheme();

        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonPrimaryDisabledBackgroundColor);

        getDeterminateProgressIndicatorButton().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedColor)));
    }

    @Test
    public void verifyProgressBarProgressColor() {
        setUpDefaultTheme();

        final int expectedProgressBarProgressColor = UIDTestUtils.getAttributeColor(activity, getUidControlPrimaryDetailAtrribute());

        getProgressBar().check(matches(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_PROGRESS_DRAWABLE, android.R.attr.enabled, expectedProgressBarProgressColor, android.R.id.progress, true)));
    }

    @Test
    public void verifyIndeterminateProgressBarStartColor() {
        setUpDefaultTheme();

        int expectedStartColor = Color.TRANSPARENT;
        getIndeterminateProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, android.R.id.progress, expectedStartColor, 0)));
    }

    @Test
    public void verifyIndeterminateProgressBarEndColor() {
        setUpDefaultTheme();
        final int expectedEndColor = UIDTestUtils.getAttributeColor(activity, getUidControlPrimaryDetailAtrribute());

        getIndeterminateProgressBar()
                .check(matches(FunctionDrawableMatchers.isSameColors(TestConstants.FUNCTION_GET_INDETERMINATE_DRAWABALE, android.R.id.progress, expectedEndColor, 1)));
    }

    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    private void simulateSetProgressText(final String progressText) {
        getDeterminateProgressIndicatorButton().perform(new ButtonWithProgressIndicatorsTest.UpdateProgressTextViewAction(progressText));
    }

    private ViewInteraction getProgressText() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_text),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }

    private ViewInteraction getButton() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_button),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }

    private ViewInteraction getProgressBar() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_progress_bar),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate))))));
    }

    private ViewInteraction getDeterminateProgressIndicatorButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate));
    }

    private ViewInteraction getIndeterminateProgressBar() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_progress_bar),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalIndeterminate))))));
    }

    private ViewInteraction getIndeterminateProgressText() {
        return onView(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_text),
                withParent(allOf(withId(com.philips.platform.uid.test.R.id.uid_progress_indicator_button_layout),
                        withParent(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalIndeterminate))))));
    }

    private ViewInteraction getIndeterminateProgressIndicatorButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.progressButtonsNormalIndeterminate));
    }

    private static class UpdateProgressTextViewAction implements ViewAction {
        private final String progressText;

        public UpdateProgressTextViewAction(final String progressText) {
            this.progressText = progressText;
        }

        @Override
        public Matcher<View> getConstraints() {
            return allOf();
        }

        @Override
        public String getDescription() {
            return "replace text";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            ((ProgressBarButton) view).setProgressText(progressText);
        }
    }

    private int getUidControlPrimaryDetailAtrribute() {
        return R.attr.uidTrackDetailNormalOnBackgroundColor;
    }

    static class SetViewDisabledViewAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return allOf();
        }

        @Override
        public String getDescription() {
            return "set progress bar enabled";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (view instanceof ProgressBarButton) {
                ProgressBarButton progressBar = (ProgressBarButton) view;
                progressBar.setEnabled(false);
            }
        }
    }
}
