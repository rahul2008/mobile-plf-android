/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.telehealthservices;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class TeleHealthServicesStateTest extends TestCase {

    @Test(expected = NullPointerException.class)
    public void testLaunchTelehealthServicesState() {
        TeleHealthServicesState teleHealthServicesState  = new TeleHealthServicesState();
        UIStateData uiStateData = new UIStateData();
        uiStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        teleHealthServicesState.setUiStateData(uiStateData);
        ActivityController<TestActivity> activityController  = Robolectric.buildActivity(TestActivity.class);
        HamburgerActivity hamburgerActivity  = activityController.create().start().get();
        FragmentLauncher fragmentLauncher  = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);

        teleHealthServicesState.init(RuntimeEnvironment.application);
        teleHealthServicesState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1, fragmentCount);
    }

    @Test
    public void updateDataModelsTest(){
        TeleHealthServicesState teleHealthServicesState  = new TeleHealthServicesState();
        teleHealthServicesState.updateDataModel();
    }
}
