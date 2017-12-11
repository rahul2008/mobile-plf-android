/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.settingscreen;

import android.support.v4.app.Fragment;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNull;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class, sdk=25)
public class SettingsFragmentTest {
    private HamburgerActivity hamburgerActivity = null;
    private SettingsFragment settingsFragment;
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        hamburgerActivity=null;
        activityController=null;
    }
    @Before
    public void setUp() throws Exception{
        activityController= Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity=activityController.create().start().get();
        settingsFragment = new SettingsFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(settingsFragment,"SettingsTestTag").commit();

    }

//    @Test
//    public void testSettingsScreenFragment(){
//
//        Fragment fragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("SettingsTestTag");
//        assertNotNull(fragment);
//    }

    @Test
    public void testSettingsScreenFragmentNotExists(){

        Fragment fragment = hamburgerActivity.getSupportFragmentManager().findFragmentByTag("Test");
        assertNull(fragment);
    }
}
