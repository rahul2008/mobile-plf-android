/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.dataservices;

import android.app.ActivityManager;
import android.content.Context;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)

public class DemoDataServicesStateTest extends TestCase {
    private DemoDataServicesState demoDataServicesState;
    private ActivityLauncher activityLauncher;

    @After
    public void tearDown() {
        demoDataServicesState = null;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        demoDataServicesState = new DemoDataServicesState();
        activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 0);
    }

    public TestAppFrameworkApplication getApplicationContext() {
        return (TestAppFrameworkApplication) RuntimeEnvironment.application;
    }

    @Test
    public void testLaunchDemoDataServicesState() {
        demoDataServicesState.init(getApplicationContext());
        demoDataServicesState.navigate(activityLauncher);

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        assertEquals(0, activityManager.getRunningAppProcesses().size());
    }

}