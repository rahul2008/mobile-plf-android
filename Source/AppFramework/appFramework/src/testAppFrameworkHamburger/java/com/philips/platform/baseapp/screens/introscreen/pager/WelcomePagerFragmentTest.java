package com.philips.platform.baseapp.screens.introscreen.pager;

import android.os.Bundle;
import android.widget.TextView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.splash.SplashFragmentTest;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by 310207283 on 7/28/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class WelcomePagerFragmentTest {

    private WelcomePagerFragment welcomePagerFragment;
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;
    private Label title;
    @Before
    public void setUp(){
        launchActivity = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class).create().start().resume().visible().get();
        welcomePagerFragment =  WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen2_title, R.string.RA_DLS_onboarding_screen2_sub_text, R.mipmap.onboarding_screen_2);
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomePagerFragment,null).commit();
        title = (Label) welcomePagerFragment.getView().findViewById(R.id.welcome_slide_large_text);

    }

    @Test
    public void testWelcomeFragmentLaunch(){
        //assertNotNull(welcomePagerFragment);
        assertEquals(launchActivity.getResources().getString(R.string.RA_DLS_onboarding_screen2_title), title.getText().toString());
    }

    @Test
    @Config(sdk = 23)
    public void testWelcomeFragmentLaunchWithLowerSdk(){
        //assertNotNull(welcomePagerFragment);
        assertEquals(launchActivity.getResources().getString(R.string.RA_DLS_onboarding_screen2_title), title.getText().toString());
    }
}
