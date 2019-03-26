/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.baseapp.screens.userregistration;

import android.content.Intent;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.aboutscreen.AboutScreenState;
import com.philips.platform.baseapp.screens.webview.WebViewActivity;
import com.philips.platform.baseapp.screens.webview.WebViewState;
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class UserRegistrationStateTest {

    private ActivityController<TestActivity> activityController;

    private HamburgerActivity hamburgerActivity;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private FragmentLauncher fragmentLauncher;

    UserRegistrationStateMock userRegState;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    FlowManager flowManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userRegState = new UserRegistrationStateMock(AppStates.SETTINGS_REGISTRATION);
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        fragmentLauncher = new FragmentLauncher(hamburgerActivity, R.id.frame_container, hamburgerActivity);
        userRegState.navigate(fragmentLauncher);
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(flowManager);
        when(flowManager.getCurrentState()).thenReturn(new AboutScreenState());
        when(flowManager.getNextState(any(BaseState.class), any(String.class))).thenReturn(new WebViewState());
    }


    @Test
    public void getUserObject_NotNull() {
        assertNotNull(userRegState.getUserObject(application));
    }

    @Test
    public void onPrivacyPolicyClickedTest() {
        userRegState.onPrivacyPolicyClick(hamburgerActivity);
        ShadowActivity shadowActivity = shadowOf(hamburgerActivity);
        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(intent);
        assertEquals(shadowIntent.getIntentClass().getSimpleName(), WebViewActivity.class.getSimpleName());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        hamburgerActivity = null;
        fragmentLauncher = null;
        userRegState = null;
        flowManager = null;
        appFrameworkApplication = null;
    }

    class UserRegistrationStateMock extends UserRegistrationState {

        private AppIdentityInterface.AppState configuration;

        /**
         * AppFlowState constructor
         *
         * @param stateID
         */
        public UserRegistrationStateMock(String stateID) {
            super(stateID);

        }

        public void setConfiguration(AppIdentityInterface.AppState configuration) {
            this.configuration = configuration;
        }

        @Override
        public void navigate(UiLauncher uiLauncher) {
            fragmentLauncher = (FragmentLauncher) uiLauncher;
        }

        @Override
        protected AppFrameworkApplication getApplicationContext() {
            return appFrameworkApplication;
        }
    }
}