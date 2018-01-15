/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utilstest;


import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.philips.platform.uid.R;
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
public class OrientationChangeTest {

    @Rule
    public final ActivityTestRule<OrientationChangeActivity> activityActivityTestRule = new ActivityTestRule<>(OrientationChangeActivity.class, false, false);
    private OrientationChangeActivity activity;

    @Before
    public void setUp() {
        activity = activityActivityTestRule.launchActivity(null);
    }

    @Test
    public void verifyOrientationChange() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputValidationLayout layout = (InputValidationLayout) activity.findViewById(com.philips.platform.uid.test.R.id.validation_layout);
                layout.showError();
            }
        });
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getInstrumentation().waitForIdleSync();
        onView(withId(R.id.uid_inline_validation_icon)).check(matches(isVisible(View.VISIBLE)));
    }
}
