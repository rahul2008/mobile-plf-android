package com.philips.platform.uid.components.gridview;


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
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class GridViewItemDoubleLineSolidFooterTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_gridview_item_solid_icon);
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

    private ViewInteraction getTitleText() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_gridview_title));
    }

    @Test
    public void verifyTitleTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidGridViewDefaultNormalTitleTextColor);
        getTitleText().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedTextColor)));
    }

    @Test
    public void verifyTitleTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(R.dimen.uid_grid_title_size);
        getTitleText().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyTitleTextPadding() {
        waitFor(testResources, 750);
        int expectedRightPadding = testResources.getDimensionPixelSize(R.dimen.uid_grid_title_padding);
        getTitleText().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedRightPadding)));
    }

    private ViewInteraction getDescriptionText() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_gridview_description));
    }

    @Test
    public void verifyDescriptionTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidGridViewDefaultNormalSubtitleTextColor);
        getDescriptionText().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedTextColor)));
    }

    @Test
    public void verifyDescriptionTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(R.dimen.uid_grid_descrption_size);
        getDescriptionText().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyDescriptionTextPadding() {
        waitFor(testResources, 750);
        int expectedLeftPadding = testResources.getDimensionPixelSize(R.dimen.uid_grid_title_padding);
        getDescriptionText().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    private ViewInteraction getSolidFillHolder(){
        return onView(withId(R.id.uid_gridview_solid_holder));
    }

    @Test
    public void verifySolidFillColor(){
        waitFor(testResources, 750);
        int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidGridViewPrimaryNormalHeaderBackgroundColor);
        getSolidFillHolder().check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(expectedColor)));
    }
}