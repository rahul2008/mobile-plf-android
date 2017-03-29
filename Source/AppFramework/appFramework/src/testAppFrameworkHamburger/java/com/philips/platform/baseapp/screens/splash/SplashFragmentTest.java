package com.philips.platform.baseapp.screens.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.philips.platform.GradleRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
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
    private LaunchActivityMockTest launchActivity;
    private SplashFragment splashFragment;
    private ImageView logo;

    @Before
    public void setUp(){
        launchActivity = Robolectric.buildActivity(LaunchActivityMockTest.class).create().start().get();
        splashFragment =  new SplashFragment();
        launchActivity.getSupportFragmentManager().beginTransaction().add(splashFragment,null).commit();

    }

    @Test
    public void testSplashFragmentLaunch(){
        assertNotNull(splashFragment);
    }

    @Test
    public void testSplashLogo(){
        logo = (ImageView) splashFragment.getView().findViewById(R.id.splash_logo);
        assertNotNull(logo);
    }

    public static class LaunchActivityMockTest extends LaunchActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
            super.onCreate(savedInstanceState);
        }
    }

}
