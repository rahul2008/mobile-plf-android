/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.stateimpl.HamburgerActivityState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.splash.SplashState;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.Before;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LaunchActivityPresenterTest extends TestCase {

    private LaunchActivityPresenter launchActivityPresenter;
    private LaunchView launchViewMock;
    private FragmentActivity fragmentActivityMock;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        launchViewMock = mock(LaunchView.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        when(launchViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(launchViewMock);
    }

    public void testOnClick() throws Exception {
        final HamburgerActivityState hamburgerStateMock = mock(HamburgerActivityState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        final SplashState splashState = mock(SplashState.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(launchViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(AppStates.HAMBURGER_HOME);
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }

            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplicationMock;
            }

        };
        when(hamburgerStateMock.getStateID()).thenReturn(AppStates.SPLASH);
        FlowManager uiFlowManagerMock = mock(FlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        when(appFrameworkApplicationMock.getTargetFlowManager().getCurrentState()).thenReturn(splashState);
        when(uiFlowManagerMock.getBackState(splashState)).thenReturn(hamburgerStateMock);
        launchActivityPresenter.onEvent(Constants.BACK_BUTTON_CLICK_CONSTANT);
        verify(hamburgerStateMock, atLeastOnce()).navigate(fragmentLauncherMock);
    }

    public void testGetUiState() {
        assertEquals("back", launchActivityPresenter.getEvent(Constants.BACK_BUTTON_CLICK_CONSTANT));
    }

    public void testOnLoad() throws NoEventFoundException {
        final UIStateData uiStateData = mock(UIStateData.class);
        final SplashState splashStateMock = mock(SplashState.class);
        final AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        final FragmentLauncher fragmentLauncherMock = mock(FragmentLauncher.class);
        when(fragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        launchActivityPresenter = new LaunchActivityPresenter(launchViewMock) {
            @Override
            public void setState(final String stateID) {
                super.setState(AppStates.HAMBURGER_HOME);
            }

            @Override
            protected FragmentLauncher getFragmentLauncher() {
                return fragmentLauncherMock;
            }

            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplicationMock;
            }

            @NonNull
            @Override
            protected SplashState getSplashState() {
                return splashStateMock;
            }

            @NonNull
            @Override
            protected UIStateData getUiStateData() {
                return uiStateData;
            }
        };
        FlowManager uiFlowManagerMock = mock(FlowManager.class);
        when(splashStateMock.getStateID()).thenReturn("something");
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(uiFlowManagerMock);
        launchActivityPresenter.onEvent(LaunchActivityPresenter.APP_LAUNCH_STATE);
        verify(splashStateMock).navigate(fragmentLauncherMock);
    }
}
