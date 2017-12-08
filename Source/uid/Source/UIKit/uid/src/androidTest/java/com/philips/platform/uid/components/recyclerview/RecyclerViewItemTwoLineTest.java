package com.philips.platform.uid.components.recyclerview;

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
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RecyclerViewItemTwoLineTest {
    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_recyclerview_item_two_lines);
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
    public void verifyLayoutHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_item_two_lines_height);
        getLayout().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyTitleTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_two_lines_title_text_size);
        getTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyTitleTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidLabelValueNormalTextColor);

        getTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyLayoutLeftPadding() {
        int expectedLeftMargin = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_two_lines_item_margin_left_right));
        getLayout().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftMargin)));
    }

    @Test
    public void verifyLayoutRightPadding() {
        int expectedRightMargin = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_two_lines_item_margin_left_right));
        getLayout().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightMargin)));
    }

    @Test
    public void verifyDescriptionTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_two_lines_description_text_size);
        getDescription().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyDescriptionTextColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemTertiaryNormalTextColor);

        getDescription().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    private ViewInteraction getTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_recyclerview_two_line_title));
    }

    private ViewInteraction getDescription() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_recyclerview_two_line_description));
    }

    private ViewInteraction getLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_recyclerview_item_two_lines));
    }
}
