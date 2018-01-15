/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.alert;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.EmptyDialogFragment;
import com.philips.platform.uid.activity.OrientationChangeActivity;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.matcher.ViewPropertiesMatchers.isVisible;

@Ignore
@RunWith(AndroidJUnit4.class)
public class DialogOrientationChangeTest {

    @Rule
    public final ActivityTestRule<OrientationChangeActivity> activityActivityTestRule = new ActivityTestRule<>(OrientationChangeActivity.class, false, false);
    private OrientationChangeActivity activity;

    @Before
    public void setUp() {
        activity = activityActivityTestRule.launchActivity(null);
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new EmptyDialogFragment());
    }

    @Test
    public void verifyOrientationChange() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_positive_button)).check(matches(isVisible(View.VISIBLE)));
    }
}
