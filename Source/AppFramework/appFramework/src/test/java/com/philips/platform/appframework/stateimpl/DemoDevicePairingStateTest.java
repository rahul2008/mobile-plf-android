/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.support.v4.app.FragmentManager;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoDevicePairingStateTest extends TestCase {
    private DemoDevicePairingState demoDevicePairingState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity hamburgerActivity;
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        demoDevicePairingState = null;
        hamburgerActivity = null;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        demoDevicePairingState = new DemoDevicePairingState();

        UIStateData devicePairingStateData = new UIStateData();
        devicePairingStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        demoDevicePairingState.setUiStateData(devicePairingStateData);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void launchDevicePairingState() {
        new DemoDataServicesState().init(RuntimeEnvironment.application);
        demoDevicePairingState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1, fragmentCount);
    }
}
