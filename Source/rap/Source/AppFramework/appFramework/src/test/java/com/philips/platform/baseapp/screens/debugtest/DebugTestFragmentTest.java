/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.debugtest;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DebugTestFragmentTest {
    private ActivityController<TestActivity> activityController;
    private HamburgerActivity hamburgerActivity;
    private DebugTestFragment debugFragment;
    private FragmentActivity fragmentActivityMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().visible().get();
        debugFragment = new DebugTestFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(debugFragment, "DebugFragmentTest").commit();
        fragmentActivityMock = mock(FragmentActivity.class);
    }

    @Test
    public void testDebugFragment() throws Exception {
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        assertEquals(0, fragmentCount);
    }

    @Test
    public void handleBackEventTest() {
        assertTrue(debugFragment.handleBackEvent());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        debugFragment = null;
        hamburgerActivity = null;
        activityController = null;
    }
}