/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.os.Bundle;
import android.widget.ImageView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.uid.view.widget.Label;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class SplashFragmentTest {
    private LaunchActivityMockAbstract launchActivity;
    private SplashFragment splashFragment;
    private ImageView logo;
    private ActivityController<LaunchActivityMockAbstract> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        splashFragment=null;
        activityController=null;
        logo=null;
    }
    @Before
    public void setUp(){
        activityController= Robolectric.buildActivity(LaunchActivityMockAbstract.class);
        launchActivity=activityController.create().start().get();
        splashFragment =  new SplashFragment();
        launchActivity.getSupportFragmentManager().beginTransaction().add(splashFragment,null).commit();

    }

    @Test
    public void testSplashFragmentLaunch(){
        assertNotNull(splashFragment);
    }

    @Test
    public void testSplashLogo(){
        logo = (ImageView) splashFragment.getView().findViewById(R.id.uid_splash_screen_icon);
        assertNotNull(logo);
    }

    @Test
    public void testAppTitle() {
        Label title = (Label) splashFragment.getView().findViewById(R.id.uid_splash_screen_app_name);
        assertEquals(title.getText(), launchActivity.getString(R.string.RA_DLS_splash_title));
    }

    public static class LaunchActivityMockAbstract extends LaunchActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
            super.onCreate(savedInstanceState);
        }
        @Override
        public void initDLS(){
            setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
        }
    }

}
