package com.philips.platform.uid.components.sidebar;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Kunal on 16/08/17.
 */

public class SidebarTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(ULTRA_LIGHT, ColorRange.GROUP_BLUE.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_sidebar);
        resources = activity.getResources();
    }

    private ViewInteraction getSidebar() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_sidebar));
    }

    @Test
    public void verifySidebarElevation() {
        float expectedElevation = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.sidebar_elevation);
        getSidebar().check(matches(ViewPropertiesMatchers.isSameDrawerElevation(expectedElevation)));
    }

    /*@Test
    public void verifySidebarDimlayerColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidDimLayerSubtleBackgroundColor);
        getSidebar().check(matches(ViewPropertiesMatchers.isSameDrawerScrimColor(expectedColor)));
    }*/

}
