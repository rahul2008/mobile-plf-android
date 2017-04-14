/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.notificationbadge;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;

import org.junit.Before;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class NotificationBadgeTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Resources testResources;
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);

        activity = mActivityTestRule.launchActivity(launchIntent);
        activity.switchTo(R.layout.uid_notification_bg_white);

        testResources = getInstrumentation().getContext().getResources();
    }


}
