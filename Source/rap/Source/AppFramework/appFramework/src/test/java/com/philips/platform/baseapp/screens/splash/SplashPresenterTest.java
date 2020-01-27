/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import androidx.fragment.app.FragmentActivity;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.introscreen.LaunchView;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {

    private SplashPresenter splashPresenter;

    @Mock
    private LaunchView launchView;

    @Mock
    private FragmentActivity fragmentActivity;

    @Mock
    private AppFrameworkApplication application;

    @Mock
    private BaseFlowManager baseFlowManager;

    @Mock
    private SplashState splashState;

    @Mock
    private WelcomeState welcomeState;


    private String APP_START = "onSplashTimeOut";

    @Before
    public void setUp() {
        when(launchView.getFragmentActivity()).thenReturn(fragmentActivity);
        when(fragmentActivity.getApplicationContext()).thenReturn(application);
        when(application.getTargetFlowManager()).thenReturn(baseFlowManager);
        when(baseFlowManager.getState(AppStates.SPLASH)).thenReturn(splashState);
        when(baseFlowManager.getNextState(splashState,APP_START)).thenReturn(welcomeState);
        when(welcomeState.getStateID()).thenReturn(AppStates.HOME_FRAGMENT);
        splashPresenter = new SplashPresenter(launchView);
    }

    @Test
    public void testAppStartAfterSplash(){
        splashPresenter.onEvent(1);
        verify(welcomeState, times(1)).navigate(any(FragmentLauncher.class));
    }

    @After
    public void tearDown() {
        splashPresenter = null;
        launchView = null;
        fragmentActivity = null;
        application = null;
        baseFlowManager = null;
        splashState = null;
        welcomeState = null;
    }

}
