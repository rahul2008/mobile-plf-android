/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.os.Bundle;
import android.widget.ImageView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class SplashFragmentTest {
    private LaunchActivityMock launchActivity;
    private SplashFragment splashFragment;
    private ImageView logo;

    @Before
    public void setUp(){
        launchActivity = Robolectric.buildActivity(LaunchActivityMock.class).create().start().get();
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

    public static class LaunchActivityMock extends LaunchActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
            super.onCreate(savedInstanceState);
        }
    }

}
