package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by philips on 28/07/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class WebViewActivityTest {
    WebViewActivity webViewActivity;

    private Resources resource = null;

    @Before
    public void setup() {
        webViewActivity = Robolectric.setupActivity(WebViewActivityMock.class);
        webViewActivity.updateActionBar(0,false);
        webViewActivity.updateActionBarIcon(false);
        webViewActivity.updateActionBar("",false);
        webViewActivity.initDLS();
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
        @Override
        public void initDLS(){
            setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
        }
    }
}