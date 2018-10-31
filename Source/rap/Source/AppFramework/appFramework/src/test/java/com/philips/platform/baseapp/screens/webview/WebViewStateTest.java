/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.webview;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by philips on 27/07/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class WebViewStateTest {
    private WebViewState webViewState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
    }

    @Before
    public void setUp() throws Exception{
        webViewState = new WebViewState();
        activityController= Robolectric.buildActivity(TestActivity.class);
        launchActivity=activityController.create().start().get();
        webViewState.init(launchActivity);
        webViewState.updateDataModel();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }

    @Test
    public void navigateTest(){
        WebViewStateData webViewStateData =new WebViewStateData();
        webViewState.setUiStateData(webViewStateData);
        webViewState.navigate(fragmentLauncher);
        assertNotNull(shadowOf(launchActivity).getNextStartedActivity());
    }
}