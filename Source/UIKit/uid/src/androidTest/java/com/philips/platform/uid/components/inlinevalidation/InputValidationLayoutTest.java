package com.philips.platform.uid.components.inlinevalidation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.InputValidationMatcher;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class InputValidationLayoutTest {

    Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_validation_text);
        testResources = activity.getResources();
    }

    @NonNull
    private Intent getLaunchIntent(final int navigationColor, final int contentColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyErrorTextColor() {
        int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidTextBoxValidationErrorColor);
        getValidationLayout().check(matches(InputValidationMatcher.isSameErrorColor(color)));
    }

    @Test
    public void verifyIconMargin() {
        int expectedEndMargin = testResources.getDimensionPixelSize(R.dimen.uid_inline_validation_icon_margin_end);
        getValidationLayout().check(matches(InputValidationMatcher.isSameIconMargin(expectedEndMargin)));
    }

    @Test
    public void verifyErrorTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(R.dimen.uid_inline_label_text_size);
        getValidationLayout().check(matches(InputValidationMatcher.isSameErrorFontSize((int) expectedFontSize)));
    }

    private ViewInteraction getValidationLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.validation_layout));
    }
}
