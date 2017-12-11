/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.webview;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.ShadowWebView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by philips on 28/07/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class WebViewActivityTest {

    WebViewActivityMock webViewActivity;

    private Resources resource = null;
    private ActivityController<WebViewActivityMock> activityController;

    @Mock
    private static WebViewPresenter webViewPresenter;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private WebView webView;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
    }
    @Before
    public void setup() {
        activityController=Robolectric.buildActivity(WebViewActivityMock.class);
        webViewActivity=activityController.create().start().get();
        webView=webViewActivity.findViewById(R.id.web_view);
        webViewActivity.updateActionBar(0,false);
        webViewActivity.updateActionBarIcon(false);
        webViewActivity.updateActionBar("",false);
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
    public void onUrlLoadSuccessTest(){
        webViewActivity.onUrlLoadSuccess("https://www.usa.philips.com/content/B2C/en_US/apps/77000/deep-sleep/sleep-score/articles/sleep-score/high-sleepscore.html");
        ShadowWebView shadowWebView=shadowOf(webView);
        assertEquals(shadowWebView.getLastLoadedUrl(),"https://www.usa.philips.com/content/B2C/en_US/apps/77000/deep-sleep/sleep-score/articles/sleep-score/high-sleepscore.html");
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

    @Test
    public void showToastTest(){
        webViewActivity.onUrlLoadError("Failed to load url");
        String toastMessage=ShadowToast.getTextOfLatestToast();
        assertEquals(toastMessage,"Failed to load url");
    }

    public static class WebViewActivityMock extends WebViewActivity{


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Philips_BrightBlue_Gradient_NoActionBar);
            super.onCreate(savedInstanceState);
        }

        @Override
        protected WebViewPresenter getWebViewPresenter() {
            return webViewPresenter;
        }
    }
}