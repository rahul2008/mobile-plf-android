package com.philips.platform.baseapp.screens.debugtest;

import android.support.v4.app.FragmentManager;

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
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class DebugTestFragmentStateTest extends TestCase {
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity hamburgerActivity;
    private DebugTestFragmentState debugTestFragmentStateTest;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        debugTestFragmentStateTest = new DebugTestFragmentState();
        UIStateData debugFragmentStateData = new UIStateData();
        debugFragmentStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
        debugTestFragmentStateTest.setUiStateData(debugFragmentStateData);

        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
    }

    @Test
    public void launchSettingsState(){
        debugTestFragmentStateTest.navigate(fragmentLauncher);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(1,fragmentCount);
    }

}