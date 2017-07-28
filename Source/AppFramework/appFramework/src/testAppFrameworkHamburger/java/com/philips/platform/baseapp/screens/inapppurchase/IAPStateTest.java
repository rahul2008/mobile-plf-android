/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.inapppurchase;

import android.support.v4.app.FragmentManager;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class IAPStateTest extends TestCase {
    private IAPRetailerFlowState iapRetailerFlowState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        iapRetailerFlowState = new IAPRetailerFlowState();
        getApplicationContext().setIapState(iapRetailerFlowState);
        iapRetailerFlowState.init(RuntimeEnvironment.application);
        UIStateData iapStateData = new UIStateData();
        iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        iapRetailerFlowState.setUiStateData(iapStateData);

        launchActivity = Robolectric.buildActivity(TestActivity.class).create().start().get();
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
