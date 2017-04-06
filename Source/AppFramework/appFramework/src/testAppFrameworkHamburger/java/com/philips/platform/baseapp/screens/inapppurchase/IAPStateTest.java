package com.philips.platform.baseapp.screens.inapppurchase;


import android.support.v4.app.FragmentManager;

import com.philips.platform.GradleRunner;
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
import org.robolectric.annotation.Config;

@RunWith(GradleRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class IAPStateTest extends TestCase {
    private IAPRetailerFlowState iapRetailerFlowState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        iapRetailerFlowState = new IAPRetailerFlowState();
        UIStateData iapStateData = new UIStateData();
        iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        iapRetailerFlowState.setUiStateData(iapStateData);

        launchActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }

    @Test
    public void launchIAP(){
        iapRetailerFlowState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = launchActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }
}
