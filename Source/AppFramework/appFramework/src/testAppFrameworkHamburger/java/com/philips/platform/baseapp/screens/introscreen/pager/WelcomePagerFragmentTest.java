/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
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
    private ActivityController<SplashFragmentTest.LaunchActivityMockAbstract> activityController;
    private Label title, subText;
    @Before
    public void setUp(){
        activityController = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class);
        launchActivity = activityController.create().start().resume().visible().get();
        welcomePagerFragment =  WelcomePagerFragment.newInstance(R.string.RA_DLS_onboarding_screen2_title, R.string.RA_DLS_onboarding_screen2_sub_text, R.drawable.onboarding_screen_2);
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomePagerFragment,null).commit();
        title = (Label) welcomePagerFragment.getView().findViewById(R.id.welcome_slide_large_text);
        subText = (Label) welcomePagerFragment.getView().findViewById(R.id.welcome_slide_small_text);
    }

    @Test
    public void testFragmentContent() {
        assertEquals(launchActivity.getResources().getString(R.string.RA_DLS_onboarding_screen2_title), title.getText().toString());
        assertEquals(launchActivity.getResources().getString(R.string.RA_DLS_onboarding_screen2_sub_text), subText.getText().toString());
        assertNotNull(welcomePagerFragment.getView().findViewById(R.id.welcome_slide_fragment_layout).getBackground());
    }

    @Test
    @Config(sdk = 23)
    public void testWelcomeFragmentSubTextWithLowerSdk(){
        assertEquals(launchActivity.getResources().getString(R.string.RA_DLS_onboarding_screen2_sub_text), subText.getText().toString());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        welcomePagerFragment = null;
        launchActivity = null;
        activityController = null;
    }

}
