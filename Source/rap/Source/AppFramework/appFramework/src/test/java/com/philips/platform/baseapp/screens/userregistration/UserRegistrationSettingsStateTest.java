/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.userregistration;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class UserRegistrationSettingsStateTest {
    private ActivityController<TestActivity> activityController;

    private HamburgerActivity hamburgerActivity;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FragmentLauncher fragmentLauncher;

    UserRegistrationSettingsStateMock userRegState;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    FlowManager flowManager;

    @Mock
    HomeFragmentState homeFragmentState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userRegState = new UserRegistrationSettingsStateMock();
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
        userRegState.navigate(fragmentLauncher);
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(flowManager);
        when(flowManager.getNextState(any(BaseState.class), any(String.class))).thenReturn(homeFragmentState);
    }

    @Test
    public void onUserLogoutSuccess() throws Exception {
        userRegState.onUserLogoutSuccess();
        verify(homeFragmentState).navigate((UiLauncher)isNull());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        flowManager=null;
        homeFragmentState=null;
        userRegState=null;
        fragmentLauncher=null;
        hamburgerActivity=null;
    }

    class UserRegistrationSettingsStateMock extends UserRegistrationSettingsState {
        @Override
        protected AppFrameworkApplication getApplicationContext() {
            return appFrameworkApplication;
        }

        @Override
        public void navigate(UiLauncher uiLauncher) {
            fragmentLauncher = (FragmentLauncher) uiLauncher;
        }
    }

}