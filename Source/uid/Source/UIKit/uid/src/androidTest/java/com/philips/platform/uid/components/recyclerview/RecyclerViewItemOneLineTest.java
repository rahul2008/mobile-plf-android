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

public class RecyclerViewItemOneLineTest {
    Resources testResources;
    private static final int ULTRA_LIGHT = 0;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_recyclerview_item_one_line_with_icon);
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
    public void verifyTitleTextFontSize() {
        float expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_one_line_title_text_size);
        getOneLineItem().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyTitleTextColor() {
        final int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidListItemDefaultNormalOffTextColor);
        getOneLineItem().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedTextColor)));
    }

    @Test
    public void verifyHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_item_one_line_height);
        getOneLineItem().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyLeftMargin() {
        int expectedLeftMargin = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_one_line_item_margin_left_right));
        getOneLineItem().check(matches(ViewPropertiesMatchers.isSameLeftMargin(expectedLeftMargin)));
    }

    @Test
    public void verifyRightMargin() {
        int expectedRightMargin = (int) Math.ceil(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_one_line_item_margin_left_right));
        getOneLineItem().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedRightMargin)));
    }

    private ViewInteraction getOneLineItem() {
        return onView(withId(R.id.uid_recyclerview_item_one_line));
    }
}
