/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.bottomtabbar;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.philips.platform.uid.activity.OrientationChangeActivity;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.matcher.ViewPropertiesMatchers.isVisible;

@RunWith(AndroidJUnit4.class)
public class TabLayoutOrientationChangeTest {

    @Rule
    public final ActivityTestRule<OrientationChangeActivity> activityActivityTestRule = new ActivityTestRule<>(OrientationChangeActivity.class, false, false);
    private OrientationChangeActivity activity;
    private Resources testResources;

    @Before
    public void setUp() {
        activity = activityActivityTestRule.launchActivity(null);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        testResources = activity.getResources();
    }

    @Test
    public void verifyOrientationChange() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        UIDTestUtils.waitFor(testResources, UIDTestUtils.UI_LOAD_WAIT_TIME);
        onView(withId(com.philips.platform.uid.test.R.id.tab_layout)).check(matches(isVisible(View.VISIBLE)));
    }
}
