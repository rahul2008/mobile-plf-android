package com.philips.platform.baseapp.screens.homefragment;


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
public class HomeFragmentStateTest extends TestCase {
    private HomeFragmentState homeFragmentState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity hamburgerActivity;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        homeFragmentState = new HomeFragmentState();
        UIStateData iapStateData = new UIStateData();
        iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        homeFragmentState.setUiStateData(iapStateData);

        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
    }

    @Test
    public void launchSplashState(){
        homeFragmentState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }
}
