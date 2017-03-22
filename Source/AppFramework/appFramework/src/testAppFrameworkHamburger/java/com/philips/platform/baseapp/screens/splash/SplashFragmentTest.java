package com.philips.platform.baseapp.screens.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.GradleRunner;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.homescreen.TestAppFrameworkApplication;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(GradleRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 21)
public class SplashFragmentTest {
    private LaunchActivity launchActivity;
    private SplashFragment splashFragment;

    @Before
    public void setUp(){
        splashFragment =  new SplashFragment();
        SupportFragmentTestUtil.startVisibleFragment(splashFragment);
    }

    @Test
    public void testSplashFragmentLaunch(){
        assertNotNull(splashFragment);
    }


}
