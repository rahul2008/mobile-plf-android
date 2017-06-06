/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.homefragment;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class HomeFragmentTest extends TestCase{
    private HamburgerActivity hamburgerActivity;
    private HomeFragment homeFragment;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        homeFragment = new HomeFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(homeFragment,"HomeFragmentTest").commit();

    }


    @Test
    public void testWelcomeFragment(){

        assertEquals(1,hamburgerActivity.getSupportFragmentManager().getBackStackEntryCount());

    }

    @Test
    public void testActionBarTitle(){
        assertEquals(hamburgerActivity.getResources().getString(R.string.RA_HomeScreen_Title),homeFragment.getActionbarTitle().toString());
    }

}
