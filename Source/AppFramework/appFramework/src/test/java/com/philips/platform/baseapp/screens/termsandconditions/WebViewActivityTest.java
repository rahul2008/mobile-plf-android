/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by philips on 28/07/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class WebViewActivityTest {
    WebViewActivity webViewActivity;

    private Resources resource = null;
    private ActivityController<WebViewActivityMock> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
    }
    @Before
    public void setup() {
        activityController=Robolectric.buildActivity(WebViewActivityMock.class);
        webViewActivity=activityController.create().start().get();
        webViewActivity.updateActionBar(0,false);
        webViewActivity.updateActionBarIcon(false);
        webViewActivity.updateActionBar("",false);
        //webViewActivity.initDLS();
        resource=webViewActivity.getResources();
    }

    @Test
    public void titleShouldShouldNotBeEmpty() throws Exception {
        String title = resource.getString(R.string.app_name);
        webViewActivity.setTitle(title);
        assertNotNull(title);
    }

    @Test
    public void getContainerIdTest(){
        assertEquals(0,webViewActivity.getContainerId());
    }

    @Test
    public void onBackPressedTest(){
        webViewActivity.onBackPressed();
        assertTrue(webViewActivity.isFinishing());
    }

    @Test
    public void onCreateOptionsMenuTest(){
        Menu menu= new RoboMenu();
        assertTrue(webViewActivity.onCreateOptionsMenu(menu));
    }

    @Test
    public void onOptionsItemSelectedTest(){
        MenuItem menuitem= new RoboMenuItem(R.id.menu_close);
        webViewActivity.onOptionsItemSelected(menuitem);
        assertTrue(webViewActivity.isFinishing());
    }

    public static class WebViewActivityMock extends WebViewActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
            super.onCreate(savedInstanceState);
        }

    }
}