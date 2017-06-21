package com.philips.platform.testmicroappfw.stateimpl;

import android.support.v4.app.FragmentManager;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.testmicroappfw.stateimpl.TestFragmentState;
import com.philips.platform.appframework.testmicroappfw.ui.TestFragment;
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
public class TestFragmentStateTest extends TestCase{
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;
    private TestFragmentState testFragmentState;


    @Before
    public void setUp() throws Exception{
        super.setUp();
        testFragmentState = new TestFragmentState();
        UIStateData testStateData=new UIStateData();
        testStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
        testFragmentState.setUiStateData(testStateData);
        launchActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }

    @Test
    public void testTestFragmentStateLaunch(){
        testFragmentState.navigate(fragmentLauncher);
        FragmentManager fragmentManager = launchActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertTrue(fragmentCount > 0 && fragmentManager.findFragmentByTag(TestFragment.class.getSimpleName()) instanceof TestFragment);

    }
}
