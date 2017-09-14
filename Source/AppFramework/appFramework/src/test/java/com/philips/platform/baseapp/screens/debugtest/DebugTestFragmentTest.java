/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.debugtest;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DebugTestFragmentTest extends TestCase {
    private ActivityController<TestActivity> activityController;
    private HamburgerActivity hamburgerActivity;
    private DebugTestFragment debugFragment;
    private FragmentActivity fragmentActivityMock;
    //    private AppFrameworkApplication appFrameworkApplication;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        super.setUp();
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().visible().get();
        debugFragment = new DebugTestFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(debugFragment, "DebugFragmentTest").commit();
        fragmentActivityMock = mock(FragmentActivity.class);
//        appFrameworkApplication = mock(AppFrameworkApplication.class);
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

//    @Test
//    public void testView() throws Exception {
//        Spinner spinner = (Spinner) debugFragment.getView().findViewById(R.id.spinner);
//        assertEquals(3, spinner.getAdapter().getCount());
//    }
//
//    @Test
//    public void testSettingState()  throws Exception {
//        String configurationType[] =
//                {
//                        AppStateConfiguration.STAGING.getValue(),
//                        AppStateConfiguration.TEST.getValue(),
//                        AppStateConfiguration.DEVELOPMENT.getValue()
//                };
//
//        Spinner spinner = new Spinner(hamburgerActivity);
//        ArrayAdapter<String> configType = debugFragment.getArrayAdapter(configurationType);
//        when(debugFragment.getSpinner()).thenReturn(spinner);
//        debugFragment.setSpinnerAdaptor(configurationType);
//        verify(spinner).setAdapter(configType);
//    }

//    @Test
//    public void getList()  throws Exception {
//        String configurationType[] =
//                {
//                        AppStateConfiguration.STAGING.getValue(),
//                        AppStateConfiguration.TEST.getValue(),
//                        AppStateConfiguration.DEVELOPMENT.getValue()
//                };
//        List<String> list = debugFragment.getList(configurationType);
//        assertTrue(list.contains(AppStateConfiguration.STAGING.getValue()));
//        assertTrue(list.contains(AppStateConfiguration.DEVELOPMENT.getValue()));
//        assertTrue(list.contains(AppStateConfiguration.TEST.getValue()));
//        assertFalse(list.contains("Production"));
//    }
}