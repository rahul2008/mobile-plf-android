package com.philips.platform.uid.components.recyclerview;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.Gray65;
import static com.philips.platform.uid.test.R.color.GroupBlue05;
import static com.philips.platform.uid.utils.UIDTestUtils.modulateColorAlpha;

public class RecyclerViewHeaderTest {
    Resources testResources;
    private static final int ULTRA_LIGHT = 0;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_recyclerview);
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
    public void verifyHeaderTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_header_text_size);
        getHeader().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyHeaderTextColor() {
        final int expectedTextColor = modulateColorAlpha(ContextCompat.getColor(getInstrumentation().getContext(), Gray65), 0.70f);
        getHeader().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedTextColor)));
    }

    @Test
    public void verifyHeaderBGColor() {
        final int expectedBGColor = ContextCompat.getColor(getInstrumentation().getContext(), GroupBlue05);
        getHeader().check(matches(TextViewPropertiesMatchers.sameBackgroundColor(expectedBGColor)));
    }

    @Test
    public void verifyHeaderHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_item_one_line_height);
        getHeader().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyHeaderLeftPadding() {
        int expectedLeftPadding = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_header_padding_left_right));
        getHeader().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    private ViewInteraction getHeader() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_recyclerview_header));
    }
}
