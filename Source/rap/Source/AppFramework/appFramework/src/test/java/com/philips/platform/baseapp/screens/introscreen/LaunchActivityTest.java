/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.baseapp.screens.introscreen;


import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.screens.utility.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class LaunchActivityTest {

    private LaunchActivity launchActivity;

    private ActivityController<LaunchActivityMock> activityController;

    private Toolbar toolbar;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static LaunchActivityPresenter launchActivityPresenter;

    @Before
    public void setUp() {
        initMocks(this);
        activityController = Robolectric.buildActivity(LaunchActivityMock.class);
        launchActivity = activityController.create().start().get();
        toolbar = (Toolbar) launchActivity.findViewById(R.id.uid_toolbar);
    }

    @Test
    public void updateActionBarWithTrueValueTest() {
        launchActivity.updateActionBar("Title", true);
        assertEquals("Title" , ((TextView) launchActivity.findViewById(R.id.uid_toolbar_title)).getText());
        assertNotNull(toolbar.getNavigationIcon());
    }

    @Test
    public void updateActionBarWithFalseValueTest() {
        launchActivity.updateActionBar(R.string.RA_Settings_Login, false);
        assertEquals(launchActivity.getString(R.string.RA_Settings_Login) , ((TextView) launchActivity.findViewById(R.id.uid_toolbar_title)).getText());
        assertNull(toolbar.getNavigationIcon());
    }

    @Test
    public void onBackPressedTest() {
        verify(launchActivityPresenter).onEvent(LaunchActivityPresenter.APP_LAUNCH_STATE);
        launchActivity.onBackPressed();
        verify(launchActivityPresenter).onEvent(Constants.BACK_BUTTON_CLICK_CONSTANT);
    }

    @Test
    public void showActionBarTest() {
        launchActivity.showActionBar();
        assertTrue(launchActivity.getSupportActionBar().isShowing());
    }

    @Test
    public void hideActionBarTest() {
        launchActivity.hideActionBar();
        assertFalse(launchActivity.getSupportActionBar().isShowing());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        launchActivity = null;
        toolbar = null;
        launchActivityPresenter = null;
        activityController = null;
    }

    public static class LaunchActivityMock extends LaunchActivity {

        @Override
        protected AbstractUIBasePresenter getLaunchActivityPresenter() {
            return launchActivityPresenter;
        }
    }

}