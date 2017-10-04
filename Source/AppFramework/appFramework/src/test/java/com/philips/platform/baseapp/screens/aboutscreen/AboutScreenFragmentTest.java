/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.widget.TextView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

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
public class AboutScreenFragmentTest {

    private ActivityController<TestActivity> activityController;
    private HamburgerActivity testActivity = null;
    private AboutScreenFragment aboutScreenFragment;

    @Before
    public void setUp() {
        aboutScreenFragment = new AboutScreenFragment();
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        testActivity.getSupportFragmentManager().beginTransaction().add(aboutScreenFragment, null).commit();
    }

    @Test
    public void testAboutScreenFragment() {

        assertNotNull(aboutScreenFragment);
    }

    @Test
    public void testVersion() {
        TextView version = (TextView) aboutScreenFragment.getView().findViewById(R.id.about_version);
        assertEquals(version.getText(), aboutScreenFragment.getResources().getString(R.string.RA_About_App_Version) + BuildConfig.VERSION_NAME);
    }

    @Test
    public void testDescription() {
        TextView content = (TextView) aboutScreenFragment.getView().findViewById(R.id.about_content);
        assertEquals(content.getText(), aboutScreenFragment.getResources().getString(R.string.RA_About_Description));
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        aboutScreenFragment = null;
        testActivity = null;
        activityController = null;
    }

}
