/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.inapppurchase;

import androidx.fragment.app.FragmentManager;

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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class IAPStateTest extends TestCase {
    private IAPRetailerFlowState iapRetailerFlowState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;
    private ActivityController<TestActivity> activityController;
    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        launchActivity=null;
        iapRetailerFlowState=null;
        fragmentLauncher=null;
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        iapRetailerFlowState = new IAPRetailerFlowState();
        getApplicationContext().setIapState(iapRetailerFlowState);
        iapRetailerFlowState.init(RuntimeEnvironment.application);
        UIStateData iapStateData = new UIStateData();
        iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        iapRetailerFlowState.setUiStateData(iapStateData);

        activityController=Robolectric.buildActivity(TestActivity.class);
        launchActivity=activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }

    public TestAppFrameworkApplication getApplicationContext(){
        return (TestAppFrameworkApplication) RuntimeEnvironment.application;
    }

    @Test
    public void launchIAP(){
        iapRetailerFlowState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = launchActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }
}
