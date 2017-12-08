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

public class RecyclerViewHeaderTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources testResources;
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
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemTertiaryNormalTextColor);
        getHeader().check(matches(TextViewPropertiesMatchers.isSameTextColor(expectedColor)));
    }

    @Test
    public void verifyHeaderBGColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentSecondaryBackgroundColor);
        getHeader().check(matches(TextViewPropertiesMatchers.sameBackgroundColor(expectedColor)));
    }

    @Test
    public void verifyHeaderHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.recyclerview_header_height);
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
