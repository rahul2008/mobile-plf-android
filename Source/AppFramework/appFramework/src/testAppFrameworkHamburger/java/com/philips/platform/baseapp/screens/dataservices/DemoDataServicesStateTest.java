/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.dataservices;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
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

    public AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) RuntimeEnvironment.application;
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void testLaunchDemoDataServicesState() {
        demoDataServicesState.init(getApplicationContext());
        demoDataServicesState.navigate(activityLauncher);
        assertNotNull(DataServicesManager.getInstance());
    }
}