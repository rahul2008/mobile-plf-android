package com.philips.platform.uid.components.inlinevalidation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class InputValidationLayoutTest {

    Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_inline_validation_input);
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

    private ViewInteraction getIcon() {
        return onView(withId(R.id.uid_inline_validation_icon));
    }

    private ViewInteraction getErrorText() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_gridview_title));
    }

    @Test
    public void verifyIconHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(R.dimen.uid_inline_validation_icon_size);
        getIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyIconWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(R.dimen.uid_inline_validation_icon_size);
        getIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedWidth)));
    }

    @Test
    public void verifyIconMargin(){
        waitFor(testResources, 750);
        int expectedEndMargin = testResources.getDimensionPixelSize(R.dimen.uid_inline_validation_label_padding_start);
        getIcon().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin)));
    }

    @Test
    public void verifyErrorTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(R.dimen.uid_inline_label_text_size);
        getErrorText().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }
}
