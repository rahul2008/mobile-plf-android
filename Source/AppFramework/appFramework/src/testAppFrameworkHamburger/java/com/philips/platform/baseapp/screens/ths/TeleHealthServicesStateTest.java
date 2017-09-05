/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.ths;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class TeleHealthServicesStateTest extends TestCase {

    private TeleHealthServicesState teleHealthServicesState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;
    private ActivityController<TestActivity> activityController;

    @Mock
    Context context;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        launchActivity=null;
        teleHealthServicesState =null;
        fragmentLauncher=null;
    }
    @Before
    public void setUp() throws Exception{
        super.setUp();
        teleHealthServicesState = new TeleHealthServicesState();
        getApplicationContext().setTeleHealthServicesState(teleHealthServicesState);
        teleHealthServicesState.init(RuntimeEnvironment.application);
        UIStateData telehealthStateData = new UIStateData();
        telehealthStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        teleHealthServicesState.setUiStateData(telehealthStateData);

        activityController= Robolectric.buildActivity(TestActivity.class);
        launchActivity=activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }
    public TestAppFrameworkApplication getApplicationContext(){
        return (TestAppFrameworkApplication) RuntimeEnvironment.application;
    }
    @Test
    public void launchTelehealth(){
        teleHealthServicesState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = launchActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }

    @Test
    public void testinit(){
        teleHealthServicesState.init(context);
    }

    @Test
    public void updateDataModel(){
        teleHealthServicesState.updateDataModel();
    }
}