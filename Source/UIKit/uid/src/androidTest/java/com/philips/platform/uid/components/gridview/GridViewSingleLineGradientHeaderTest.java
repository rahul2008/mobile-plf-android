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

public class GridViewSingleLineGradientHeaderTest {

    Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_gridview_item_gradient_icon);
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

    private ViewInteraction getIconHolder() {
        return onView(withId(R.id.uid_gridview_icon_holder));
    }

    @Test
    public void verifyIconHolderHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(R.dimen.uid_grid_single_line_text_height);
        getIconHolder().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyIconHolderWidth() {
        int expectedHeight = testResources.getDimensionPixelSize(R.dimen.uid_grid_icon_holder_width);
        getIconHolder().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    private ViewInteraction getStarIcon() {
        return onView(withId(R.id.uid_gridview_icon));
    }

    @Test
    public void verifyIconHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(R.dimen.uid_grid_icon_size);
        getStarIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyIconWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(R.dimen.uid_grid_icon_size);
        getStarIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedWidth)));
    }

    private ViewInteraction getTitleText() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_gridview_title));
    }

    @Test
    public void verifyTitleTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.color.uidColorWhite);
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
        int expectedStartPadding = testResources.getDimensionPixelSize(R.dimen.uid_grid_title_padding);
        getTitleText().check(matches(ViewPropertiesMatchers.isSameStartPadding(expectedStartPadding)));
    }

    @Test
    public void verifyTitleTextMargin(){
        waitFor(testResources, 750);
        int expectedEndMargin = testResources.getDimensionPixelSize(R.dimen.uid_grid_icon_holder_width);
        getTitleText().check(matches(ViewPropertiesMatchers.isSameRightMargin(expectedEndMargin)));
    }
}
