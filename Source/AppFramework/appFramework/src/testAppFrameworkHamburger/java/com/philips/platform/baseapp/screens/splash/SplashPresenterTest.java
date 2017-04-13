/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.introscreen.LaunchView;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.Before;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SplashPresenterTest extends TestCase{
    private SplashPresenter splashPresenter;
    private LaunchView launchView;
    private FragmentActivity fragmentActivity;
    private String APP_START = "onSplashTimeOut";

    @Before
    protected void setUp() throws Exception{
        super.setUp();
        launchView = mock(LaunchView.class);
        fragmentActivity = mock(FragmentActivity.class);
        when(launchView.getFragmentActivity()).thenReturn(fragmentActivity);
        splashPresenter = new SplashPresenter(launchView);
    }

    public void testAppStartAfterSplash(){
        final WelcomeState welcomeStateMock = mock(WelcomeState.class);
        final UIStateData uiStateData = mock(UIStateData.class);
        final SplashState splashState = mock(SplashState.class);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        final BaseFlowManager baseFlowManager = mock(BaseFlowManager.class);
        final FlowManager uiFlowManagerMock = mock(FlowManager.class);
        when(fragmentActivity.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        splashPresenter = new SplashPresenter(launchView) {
            @Override
            public void setState(final String stateID) {
                super.setState(AppStates.SPLASH);
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }

            @NonNull
            @Override
            protected UIStateData setStateData(final String componentID) {
                return uiStateData;
            }

            @Override
            protected BaseFlowManager getTargetFlowManager() {
                return uiFlowManagerMock;
            }

            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplicationMock;
            }
        };
        when(uiFlowManagerMock.getState(AppStates.SPLASH)).thenReturn(splashState);
        when(uiFlowManagerMock.getNextState(splashState,APP_START)).thenReturn(welcomeStateMock);
        splashPresenter.onEvent(1);
        verify(welcomeStateMock, atLeastOnce()).navigate(fragmentLauncherMock);

    }

}
