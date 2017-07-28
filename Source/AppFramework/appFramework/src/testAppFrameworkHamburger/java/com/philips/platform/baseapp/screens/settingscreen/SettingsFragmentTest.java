/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.settingscreen;

import android.support.v4.app.Fragment;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class SettingsFragmentTest {
    private HamburgerActivity hamburgerActivity = null;
    private SettingsFragment settingsFragment;

    @Before
    public void setUp() throws Exception{
        hamburgerActivity = Robolectric.buildActivity(TestActivity.class).create().start().get();
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
