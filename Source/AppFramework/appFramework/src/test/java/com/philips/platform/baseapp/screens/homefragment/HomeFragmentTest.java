/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.homefragment;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.utility.Constants;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class HomeFragmentTest extends TestCase{
    private HamburgerActivity hamburgerActivity;
    private HomeFragment homeFragment;
    private static final String JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED = "JAIL_BROKEN_SCREEN_LOCK";
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        homeFragment=null;
        hamburgerActivity=null;
    }
    @Before
    public void setUp() throws Exception{
        super.setUp();
        activityController=Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity=activityController.create().start().get();
        homeFragment = new HomeFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(homeFragment,"HomeFragmentTest").commit();
    }
    @Test
    public void setUrCompleted() throws Exception {
        homeFragment.setUrCompleted();
        boolean isUrLoginSuccess = homeFragment.getPreferences().getBoolean(Constants.UR_LOGIN_COMPLETED, false);
        assertFalse(isUrLoginSuccess);
    }

    @Test
    public void getDoNotShowValueWhenNotAvailable() throws Exception {
        boolean isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertFalse(isValueAvailable);
    }

    @Test
    public void getDoNotShowValueWhenAvailable() throws Exception {
        boolean isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertFalse(isValueAvailable);

        homeFragment.setDoNotShowValue(Constants.UR_LOGIN_COMPLETED, true);

        isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertTrue(isValueAvailable);
    }

    @Test
    public void initialiseSecurityDialog() throws Exception {
        homeFragment.setDoNotShowValue(Constants.UR_LOGIN_COMPLETED, false);
        homeFragment.initialiseSecurityDialog();

        boolean isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertFalse(isValueAvailable);
    }

    @Test
    public void getSetDoNotShowValueWhenAvailable() throws Exception {
        boolean isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertFalse(isValueAvailable);

        homeFragment.setDoNotShowValue(Constants.UR_LOGIN_COMPLETED, false);

        isValueAvailable = homeFragment.getDoNotShowValue(Constants.UR_LOGIN_COMPLETED);
        assertFalse(isValueAvailable);
    }

    @Test
    public void testWelcomeFragment() throws Exception {

        assertEquals(0, hamburgerActivity.getSupportFragmentManager().getBackStackEntryCount());

    }

    @Test
    public void testActionBarTitle() throws Exception {
        assertEquals(hamburgerActivity.getResources().getString(R.string.RA_HomeScreen_Title),homeFragment.getActionbarTitle().toString());
    }

    @Test
    public void getDoNotShowJailBrokenAndScreenLockDialog() throws Exception {
        boolean isValueAvailable = homeFragment.getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
        assertFalse(isValueAvailable);

        homeFragment.setDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED, true);

        isValueAvailable = homeFragment.getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
        assertTrue(isValueAvailable);

        boolean isDialogCreated = homeFragment.createDialog();
        assertFalse(isDialogCreated);
    }

    @Test
    public void getDoNotShowJailBrokenAndScreenLockIsTrueDialog() throws Exception {
        boolean isValueAvailable = homeFragment.getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
        assertFalse(isValueAvailable);

        boolean isDialogCreated = homeFragment.createDialog();
        assertTrue(isDialogCreated);
    }
}
